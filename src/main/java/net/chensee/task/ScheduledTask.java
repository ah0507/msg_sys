package net.chensee.task;

import lombok.extern.slf4j.Slf4j;
import net.chensee.common.StrategyConvertUtil;
import net.chensee.msg.enums.MsgPendingStatusEnum;
import net.chensee.msg.factory.RecStrategyFactory;
import net.chensee.msg.po.MsgInfoPo;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.po.MsgReceivingPo;
import net.chensee.msg.service.MsgService;
import net.chensee.msg.strategy.receiver.RecEnvirment;
import net.chensee.msg.strategy.receiver.RecStrategy;
import net.chensee.msg.vo.UserMsgVo;
import net.chensee.socket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ah
 * @title: 定时任务之消息的保存与推送
 * @date 2019/10/30 11:50
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private MsgService msgService;

    /**
     * 接收消息（待发送消息通过接收者策略解析出所有的接收用户，存储每个接收消息用户等信息）
     */
    @Scheduled(fixedRate = 3000)
    private void receiveMsgTask() {
        List<MsgPendingPo> msgPendingPos = msgService.findPendingPosByUnSent(MsgPendingStatusEnum.UNSENT.name());
        if (!msgPendingPos.isEmpty()) {
            try {
                this.handleMsgPendingPos(msgPendingPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消息推送（给所有在线用户实时推送每个用户的消息）
     */
    @Scheduled(fixedRate = 3000)
    private void pushMsgTask() {
        CopyOnWriteArraySet<WebSocketServer> connectSocketSet = WebSocketServer.connectSocketSet;
        Set<String> userIds = new HashSet<>();
        for (WebSocketServer webSocketServer : connectSocketSet) {
            userIds.add(webSocketServer.userId);
        }
        if (userIds.size() == 0) {
//            log.info("暂无在线用户...");
            return;
        }
        //查询到在线用户所有未推送的消息
        List<MsgReceivingPo> msgReceivingPos = msgService.getUserMsgByUserIds(new ArrayList<>(userIds));
        if (msgReceivingPos.size() == 0) {
//            log.info("暂无可推送的消息...");
            return;
        }
        List<UserMsgVo> userMsgVoList = this.getRecMsgVos(msgReceivingPos);
        Map<String,List<UserMsgVo>> userMsgMap = this.handleListToMap(userMsgVoList);
        try {
            WebSocketServer.pushMsg(userMsgMap);
            //将未推送的消息状态置为已推送
            this.setRecMsgPushStatus(msgReceivingPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<UserMsgVo> getRecMsgVos(List<MsgReceivingPo> msgReceivingPos) {
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        UserMsgVo userMsgVo;
        for (MsgReceivingPo msgReceivingPo : msgReceivingPos) {
            userMsgVo = new UserMsgVo();
            BeanUtils.copyProperties(msgReceivingPo,userMsgVo);
            MsgInfoPo msgInfoPo = (MsgInfoPo) msgService.getSenderMsgInfoByMsgId(msgReceivingPo.getSender(),msgReceivingPo.getMsgId()).getData();
            userMsgVo.setTitle(msgInfoPo.getTitle());
            userMsgVo.setContent(msgInfoPo.getContent());
            userMsgVos.add(userMsgVo);
        }
        return userMsgVos;
    }

    /**
     * 清除离线用户
     */
    @Scheduled(fixedRate = 3000)
    private void clearCloseUserTask() {
        CopyOnWriteArraySet<WebSocketServer> closeSocketSet = WebSocketServer.closeSocketSet;
        if (closeSocketSet.isEmpty()) {
            return;
        }
        CopyOnWriteArraySet<WebSocketServer> connectSocketSet = WebSocketServer.connectSocketSet;
        for (WebSocketServer webSocketServer : closeSocketSet) {
            connectSocketSet.remove(webSocketServer);
        }
        closeSocketSet.clear();
    }

    private void setRecMsgPushStatus(List<MsgReceivingPo> userMsgVos) throws Exception {
        List<String> recIds = new ArrayList<>();
        for (MsgReceivingPo msgReceivingPo : userMsgVos) {
            recIds.add(msgReceivingPo.getId());
        }
        msgService.setRecMsgPushStatus(recIds);
    }

    private Map<String, List<UserMsgVo>> handleListToMap(List<UserMsgVo> userMsgVos) {
        Map<String, List<UserMsgVo>> userMsgMap = new HashMap<>();
        for (UserMsgVo userMsgVo : userMsgVos) {
            String userId = userMsgVo.getUserId();
            List<UserMsgVo> userMsgVoList = new ArrayList<>();
            if (userMsgMap.containsKey(userId)) {
                userMsgVoList = userMsgMap.get(userId);
            }
            userMsgVoList.add(userMsgVo);
            userMsgMap.put(userId, userMsgVoList);
        }
        return userMsgMap;
    }

    private void handleMsgPendingPos(List<MsgPendingPo> msgPendingPos) throws Exception {
        for (MsgPendingPo msgPendingPo : msgPendingPos) {
            Date pendingTime = msgPendingPo.getPendingTime();
            if (pendingTime.getTime() <= System.currentTimeMillis()) {
                //保存接收到的消息
                this.saveMsgReceivingPo(msgPendingPo);
                //将待发送消息置为已发送
                this.changeMsgPendingPoSentStatus(msgPendingPo);
                //将消息本体发送次数+1
                this.changeMsgInfo(msgPendingPo.getSender(),msgPendingPo.getMsgId());
            }
        }
    }

    private void changeMsgInfo(String userId,String msgId) {
        MsgInfoPo msgInfoPo = (MsgInfoPo) msgService.getSenderMsgInfoByMsgId(userId,msgId).getData();
        msgInfoPo.setSendNumber(msgInfoPo.getSendNumber() + 1);
        msgService.updateMsgInfoSendNumberById(msgInfoPo);
    }

    private void saveMsgReceivingPo(MsgPendingPo msgPendingPo) throws Exception{
        Map<String, List<String>> recMap = StrategyConvertUtil.getReceiverStrategy(msgPendingPo.getReceiver());
        RecStrategyFactory recStrategyFactory = new RecStrategyFactory(msgService,msgPendingPo);
        for (Map.Entry<String, List<String>> e : recMap.entrySet()) {
            String type = e.getKey();
            List<String> receiver = e.getValue();
            RecStrategy strategy = recStrategyFactory.getStrategy(type);
            RecEnvirment recEnvirment = new RecEnvirment(strategy);
            recEnvirment.execute(receiver);
        }

    }

    private void changeMsgPendingPoSentStatus(MsgPendingPo msgPendingPo) throws Exception {
        msgService.updateMsgPendingPoSentStatus(msgPendingPo.getId(),msgPendingPo.getStatus(),MsgPendingStatusEnum.SENT);
    }
}
