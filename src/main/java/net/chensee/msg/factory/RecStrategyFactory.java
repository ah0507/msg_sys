package net.chensee.msg.factory;

import net.chensee.msg.enums.ReceiverStrategyStatus;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.service.MsgService;
import net.chensee.msg.strategy.receiver.RecStrategy;
import net.chensee.msg.strategy.receiver.RoleStrategy;
import net.chensee.msg.strategy.receiver.UserGroupStrategy;
import net.chensee.msg.strategy.receiver.UserStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ah
 * @title: 接收方策略
 * @date 2019/10/30 15:43
 */
public class RecStrategyFactory {

    private Map<String, RecStrategy> recStrategyFactoryMap = new HashMap<>();

    public RecStrategyFactory(MsgService msgService, MsgPendingPo msgPendingPo) {
        recStrategyFactoryMap.put(ReceiverStrategyStatus.USER.name(), new UserStrategy(msgService, msgPendingPo));
        recStrategyFactoryMap.put(ReceiverStrategyStatus.ROLE.name(), new RoleStrategy(msgService, msgPendingPo));
        recStrategyFactoryMap.put(ReceiverStrategyStatus.USER_GROUP.name(), new UserGroupStrategy(msgService, msgPendingPo));
    }

    public RecStrategy getStrategy(String type) {
        return recStrategyFactoryMap.get(type.toUpperCase());
    }
}
