package net.chensee.msg.service.impl;

import net.chensee.common.HttpClientHelper;
import net.chensee.common.MD5Builder;
import net.chensee.common.RandomUUIDUtil;
import net.chensee.msg.dao.MsgDao;
import net.chensee.msg.enums.MsgPendingStatusEnum;
import net.chensee.msg.enums.MsgStatusEnum;
import net.chensee.msg.factory.TimeStrategyFactory;
import net.chensee.msg.po.*;
import net.chensee.msg.service.MsgService;
import net.chensee.msg.strategy.time.TimeEnvirment;
import net.chensee.msg.strategy.time.TimeStrategy;
import net.chensee.msg.vo.MsgRecVo;
import net.chensee.msg.vo.MsgVo;
import net.chensee.msg.vo.UserVo;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static net.chensee.msg.enums.MsgPendingStatusEnum.*;

/**
 * @author ah
 * @title: MsgServiceImpl
 * @date 2019/10/29 18:12
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Value("${pageNumber}")
    private Integer pageNumber;

    @Value("${pageSize}")
    private Integer pageSize;

    @Autowired
    private MsgDao msgDao;

    @Value("${erpUrl}")
    private String erpUrl;

    @Override
    public ObjectResponse sendMsg(MsgVo msgVo) {
        try {
            MsgInfoPo msgInfo = msgDao.getMsgInfoByMsgId(msgVo.getSender(),msgVo.getMsgId());
            String msgId;
            if (msgInfo == null) {
                //保存消息主体（消息内容，消息标题等）
                msgId = this.saveMsgInfo(msgVo);
            } else {
                msgId = msgInfo.getId();
            }
            //保存待发送消息
            this.saveMsgPending(msgVo, msgId);
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponse.fail();
        }
        return ObjectResponse.ok();
    }

    @Override
    public ObjectResponse addMsgInfo(MsgInfoPo msgInfoPo) {
        msgInfoPo.setId(RandomUUIDUtil.randomUUID());
        msgInfoPo.setStatus(MsgStatusEnum.USE);
        msgInfoPo.setSendNumber(0);
        msgInfoPo.setCreateTime(new Date());
        msgDao.saveMsgInfo(msgInfoPo);
        return ObjectResponse.ok();
    }

    @Override
    public List<MsgPendingPo> findPendingPosByUnSent(String pendingStatus) {
        return msgDao.findPendingPosByUnSent(pendingStatus);
    }

    @Override
    public List<String> getUsersByRoleId(String roleId) throws Exception {
        String url = erpUrl + "/role/" + roleId + "/users";
        return this.getUserIds(url);
    }

    @Override
    public List<String> getUsersByUserGroupId(String userGroupId) throws Exception {
        String url = erpUrl + "/userGroup/" + userGroupId + "/users";
        return this.getUserIds(url);
    }

    private List<String> getUserIds(String url) throws Exception {
        List<UserVo> userVos = this.getUserVo(url);
        List<String> list = new ArrayList<>();
        if (userVos != null && userVos.size() != 0) {
            for (UserVo userVo : userVos) {
                list.add(userVo.getId().toString());
            }
        }
        return list;
    }

    private List<UserVo> getUserVo(String url) throws Exception {
        Object o = HttpClientHelper.sendGet(url);
        if (o == null) {
            return null;
        }
        return JSONArray.toList((JSONArray) o, new UserVo(), new JsonConfig());
    }

    @Override
    public void saveMsgReceivingPos(List<MsgReceivingPo> msgReceivingPos) {
        if (msgReceivingPos.isEmpty()) {
            return;
        }
        while (true) {
            int startNum = (pageNumber - 1) * pageSize;
            int endNum = pageSize * pageNumber;
            if (startNum < msgReceivingPos.size()) {
                if (endNum > msgReceivingPos.size()) {
                    endNum = msgReceivingPos.size();
                }
                List<MsgReceivingPo> msgReceivingPoList = msgReceivingPos.subList(startNum, endNum);
                pageNumber++;
                msgDao.saveMsgReceivingPos(msgReceivingPoList);
            } else {
                pageNumber = 1;
                break;
            }
        }
    }

    @Override
    public void saveMsgReceivingPo(MsgReceivingPo msgReceivingPo) {
        msgDao.saveMsgReceivingPo(msgReceivingPo);
    }

    @Override
    public ObjectResponse saveRecDeviceInfo(RecDeviceInfoPo recDeviceInfoPo) {
        try {
            String clientId = MD5Builder.getMD5(recDeviceInfoPo.getUserId() + recDeviceInfoPo.getMac());
            recDeviceInfoPo.setClientId(clientId);
            msgDao.saveRecDeviceInfo(recDeviceInfoPo);
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponse.fail();
        }
        return ObjectResponse.ok();
    }

    @Override
    public void updateMsgPendingPoSentStatus(String id, MsgPendingStatusEnum oldStatus, MsgPendingStatusEnum newStatus) {
        msgDao.updateMsgPendingPoSentStatus(id, oldStatus, newStatus);
    }

    @Override
    public long getRecMsgIsRepeat(String userId, String pendingId) {
        return msgDao.getRecMsgIsRepeat(userId, pendingId);
    }

    @Override
    public ObjectResponse getUserMsgByUserId(String userId, Integer pageNumber, Integer pageSize) {
        List<MsgRecVo> msgRecVos = msgDao.getUserMsgByUserId(userId, (pageNumber - 1) * pageSize, pageSize);
        for (MsgRecVo mrv : msgRecVos) {
            MsgInfoPo msgInfoPo = msgDao.getMsgInfoByMsgId(userId,mrv.getMsgId());
            mrv.setTitle(msgInfoPo.getTitle());
        }
        Long count = msgDao.MsgReceivingPoCount(userId);
        Map map = new HashMap();
        map.put("data", msgRecVos);
        map.put("total", count);
        return ObjectResponse.ok(map);
    }

    @Override
    public ObjectResponse getUserSendMsgByUserId(String userId, String msgId, Integer pageNumber, Integer pageSize) {
        List<MsgPendingPo> msgPendingPos = msgDao.getUserSendMsgByUserId(userId, msgId, (pageNumber - 1) * pageSize, pageSize);
        Long count = msgDao.getUserSendMsgCount(userId, msgId);
        Map map = new HashMap();
        map.put("data", msgPendingPos);
        map.put("total", count);
        return ObjectResponse.ok(map);
    }

    @Override
    public List<MsgReceivingPo> getUserMsgByUserIds(List<String> userIds) {
        return msgDao.getUserMsgByUserIds(userIds);
    }

    @Override
    public ObjectResponse unSentReCall(String userId, String pendingId) {
        msgDao.updateSendMsgUnSentReCall(userId,pendingId, UNSENT_RECALL);
        return ObjectResponse.ok();
    }

    @Override
    public ObjectResponse sentReCall(String userId, String pendingId) {
        msgDao.updateSendMsgSentReCall(userId,pendingId, SENT_RECALL);
        msgDao.batchDeleteRecMsgByPendingId(userId,pendingId);
        return ObjectResponse.ok();
    }

    @Override
    public ObjectResponse setMsgInfoStatusNoUsing(String userId,String msgId) {
        try {
            this.setMsgInfoStatus(userId,msgId);
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponse.fail();
        }
        return ObjectResponse.ok();
    }

    private void setMsgInfoStatus(String userId, String msgId) {
        msgDao.setMsgInfoStatusNoUsing(userId,msgId, MsgStatusEnum.NO_USE);
        //删除该消息内容相关联的用户接收消息
        msgDao.batchDeleteRecMsgByMsgId(userId,msgId);
        List<MsgPendingPo> msgPendingPos = msgDao.getMsgPendingByMsgId(userId,msgId);
        for (MsgPendingPo msgPendingPo : msgPendingPos) {
            MsgPendingStatusEnum status = null;
            switch (msgPendingPo.getStatus()) {
                case SENT:
                    status = SENT_RECALL;
                    break;
                case UNSENT:
                    status = UNSENT_RECALL;
                    break;
                default:
                    break;
            }
            //改变该消息内容相关联的待发送消息的状态（已发送撤回，会未发送撤回）
            if (status != null) {
                msgDao.setMsgPendingStatusByMsgId(userId,msgPendingPo.getId(),msgId, status);
            }
        }
    }

    @Override
    public void setRecMsgPushStatus(List<String> recIds) {
        msgDao.setRecMsgPushStatus(recIds);
    }

    @Override
    public ObjectResponse updateMsgInfo(MsgInfoPo msgInfoPo) {
        MsgInfoPo infoPo = msgDao.getMsgInfoByMsgId(msgInfoPo.getCreateBy(), msgInfoPo.getId());
        if (infoPo == null) {
            return ObjectResponse.fail("修改失败！");
        }
        if (infoPo.getSendNumber() == 0) {
            msgDao.updateMsgInfo(msgInfoPo);
            return ObjectResponse.ok();
        } else {
            return ObjectResponse.fail("消息已被发送，不能进行修改！");
        }
    }

    @Override
    public ObjectResponse getUserMsgInfosByUserId(String userId, Integer pageNumber, Integer pageSize) {
        List<MsgInfoPo> msgInfoPos = msgDao.getMsgInfosByUserId(userId, (pageNumber - 1) * pageSize, pageSize);
        Long count = msgDao.getUserMsgInfosCount(userId);
        Map map = new HashMap();
        map.put("data", msgInfoPos);
        map.put("total", count);
        return ObjectResponse.ok(map);
    }

    @Override
    public ObjectResponse msgReCall(String userId,String pendingId) {
        MsgPendingPo msgPendingPo = msgDao.getMsgPendingByPendingId(userId,pendingId);
        switch (msgPendingPo.getStatus()) {
            case UNSENT:
                this.unSentReCall(userId,pendingId);
                break;
            case SENT:
                this.sentReCall(userId,pendingId);
                break;
            default:
                break;
        }
        return ObjectResponse.ok();
    }

    @Override
    public void updateMsgInfoSendNumberById(MsgInfoPo msgInfoPo) {
        msgDao.updateMsgInfoSendNumberById(msgInfoPo);
    }

    @Override
    public ObjectResponse getSenderMsgInfoByMsgId(String userId,String msgId) {
        MsgInfoPo msgInfoPo = msgDao.getMsgInfoByMsgId(userId,msgId);
        return ObjectResponse.ok(msgInfoPo);
    }

    @Override
    public ObjectResponse getRecMsgInfoByMsgId(String userId, String receiveId) {
        MsgReceivingPo msgReceivingPo = msgDao.getMsgReceivingPo(receiveId);
        if (msgReceivingPo == null) {
            return ObjectResponse.fail();
        }
        MsgInfoPo msgInfoPo = msgDao.getMsgInfoByMsgId(userId,msgReceivingPo.getMsgId());
        this.setRecMsgReadStatus(msgReceivingPo.getUserId(),receiveId);
        return ObjectResponse.ok(msgInfoPo);
    }

    @Override
    public ObjectResponse setRecMsgReadStatus(String userId, String receiveId) {
        try {
            msgDao.setRecMsgReadStatus(userId, receiveId);
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponse.fail();
        }
        return ObjectResponse.ok();
    }

    private void saveMsgPending(MsgVo msgVo, String msgId) throws Exception {
        MsgPendingPo msgPendingPo = new MsgPendingPo();
        msgPendingPo.setId(RandomUUIDUtil.randomUUID());
        msgPendingPo.setSender(msgVo.getSender());
        msgPendingPo.setReceiver(msgVo.getReceiver());
        msgPendingPo.setMsgId(msgId);
        msgPendingPo.setStatus(UNSENT);
        Date nowDate = new Date();
        msgPendingPo.setCreateTime(nowDate);
        msgPendingPo.setCreateBy(msgVo.getSender());
        String strategyContent = msgVo.getTimeStrategy();
        msgPendingPo.setStrategy(strategyContent);
        //通过过期时间策略算出消息待发送时间
        TimeStrategyFactory timeStrategyFactory = new TimeStrategyFactory(nowDate);
        TimeStrategy strategy = timeStrategyFactory.getStrategy(strategyContent);
        TimeEnvirment envirment = new TimeEnvirment(strategy);
        Date executeTime = envirment.execute(strategyContent);
        if (executeTime != null) {
            msgPendingPo.setPendingTime(executeTime);
        }
        msgDao.savePendingPo(msgPendingPo);
    }

    private String saveMsgInfo(MsgVo msgVo) throws Exception {
        MsgInfoPo msgInfoPo = new MsgInfoPo();
        msgInfoPo.setId(RandomUUIDUtil.randomUUID());
        msgInfoPo.setContent(msgVo.getContent());
        msgInfoPo.setTitle(msgVo.getTitle());
        msgInfoPo.setStatus(MsgStatusEnum.USE);
        msgInfoPo.setSendNumber(0);
        msgInfoPo.setCreateTime(new Date());
        msgInfoPo.setCreateBy(msgVo.getSender());
        msgDao.saveMsgInfo(msgInfoPo);
        return msgInfoPo.getId();
    }
}
