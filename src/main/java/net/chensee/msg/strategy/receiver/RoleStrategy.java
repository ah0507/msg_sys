package net.chensee.msg.strategy.receiver;

import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.service.MsgService;

import java.util.List;

/**
 * @author ah
 * @title: 接收方是角色的策略
 * @date 2019/10/30 17:27
 */
public class RoleStrategy extends AbstractRecStartegy {

    public RoleStrategy(MsgService msgService, MsgPendingPo msgPendingPo) {
        super(msgService, msgPendingPo);
    }

    @Override
    public void execute(List<String> receiver) throws Exception {
        if (receiver == null || receiver.size() == 0) {
            return;
        }
        for (String rec : receiver) {
            List<String> userList = msgService.getUsersByRoleId(rec);
            this.handleRecPendingPos(userList);
        }
    }
}
