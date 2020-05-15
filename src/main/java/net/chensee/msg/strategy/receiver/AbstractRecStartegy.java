package net.chensee.msg.strategy.receiver;

import net.chensee.common.RandomUUIDUtil;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.po.MsgReceivingPo;
import net.chensee.msg.service.MsgService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ah
 * @title: AbstractRecStartegy
 * @date 2019/10/31 16:21
 */
public abstract class AbstractRecStartegy implements RecStrategy {

    protected MsgService msgService;

    protected MsgPendingPo msgPendingPo;

    protected AbstractRecStartegy(MsgService msgService, MsgPendingPo msgPendingPo) {
        this.msgService = msgService;
        this.msgPendingPo = msgPendingPo;
    }

    protected void handleRecPendingPos(List<String> users) throws Exception {
        //排重
        List<String> newUsers = new ArrayList<>();
        for (String userId : users) {
            long count = msgService.getRecMsgIsRepeat(userId,msgPendingPo.getId());
            if (count == 0) {
                newUsers.add(userId);
            }
        }
        if (newUsers.isEmpty()) {
            return;
        }
        List<MsgReceivingPo> MsgReceivingPos = new ArrayList<>();
        MsgReceivingPo msgReceivingPo;
        for (String userId : newUsers) {
            msgReceivingPo = new MsgReceivingPo();
            msgReceivingPo.setId(RandomUUIDUtil.randomUUID());
            msgReceivingPo.setSender(msgPendingPo.getSender());
            msgReceivingPo.setUserId(userId);
            msgReceivingPo.setPendingId(msgPendingPo.getId());
            msgReceivingPo.setMsgId(msgPendingPo.getMsgId());
            msgReceivingPo.setPushStatus(0);
            msgReceivingPo.setReadStatus(0);
            msgReceivingPo.setReceiveTime(msgPendingPo.getPendingTime());
            msgReceivingPo.setCreateTime(new Date());
            msgReceivingPo.setCreateBy(msgPendingPo.getSender());
            MsgReceivingPos.add(msgReceivingPo);
        }
        msgService.saveMsgReceivingPos(MsgReceivingPos);
    }

}
