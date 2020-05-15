package net.chensee.msg.strategy.receiver;

import net.chensee.common.RandomUUIDUtil;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.po.MsgReceivingPo;
import net.chensee.msg.service.MsgService;

import java.util.Date;
import java.util.List;

/**
 * @author ah
 * @title: 接收方是角色的策略
 * @date 2019/10/30 17:27
 */
public class UserStrategy extends AbstractRecStartegy {

    public UserStrategy(MsgService msgService, MsgPendingPo msgPendingPo) {
        super(msgService, msgPendingPo);
    }

    @Override
    public void execute(List<String> receiver) {
        if (receiver == null || receiver.size() == 0) {
            return;
        }
        for (String rec : receiver) {
            long count = msgService.getRecMsgIsRepeat(rec,msgPendingPo.getId());
            if (count == 0) {
                MsgReceivingPo msgReceivingPo = new MsgReceivingPo();
                msgReceivingPo.setId(RandomUUIDUtil.randomUUID());
                msgReceivingPo.setSender(msgPendingPo.getSender());
                msgReceivingPo.setUserId(rec);
                msgReceivingPo.setPendingId(msgPendingPo.getId());
                msgReceivingPo.setMsgId(msgPendingPo.getMsgId());
                msgReceivingPo.setPushStatus(0);
                msgReceivingPo.setReadStatus(0);
                msgReceivingPo.setReceiveTime(msgPendingPo.getPendingTime());
                msgReceivingPo.setCreateTime(new Date());
                msgReceivingPo.setCreateBy(msgPendingPo.getSender());
                try {
                    msgService.saveMsgReceivingPo(msgReceivingPo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
