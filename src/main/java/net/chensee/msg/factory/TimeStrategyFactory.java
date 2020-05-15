package net.chensee.msg.factory;

import net.chensee.common.StrategyConvertUtil;
import net.chensee.msg.enums.MsgPendingStrategyEnum;
import net.chensee.msg.strategy.time.SetTimeStrategy;
import net.chensee.msg.strategy.time.SomeTimeStrategy;
import net.chensee.msg.strategy.time.TimeStrategy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ah
 * @title: 过期时间策略
 * @date 2019/10/30 15:43
 */
public class TimeStrategyFactory {

    private Map<String, TimeStrategy> timeStrategyFactoryMap = new HashMap<>();

    public TimeStrategyFactory(Date nowDate){
        timeStrategyFactoryMap.put(MsgPendingStrategyEnum.SET_TIME.name(), new SetTimeStrategy());
        timeStrategyFactoryMap.put(MsgPendingStrategyEnum.SOME_TIME.name(), new SomeTimeStrategy(nowDate));
    }

    public TimeStrategy getStrategy(String strategy) {
        return timeStrategyFactoryMap.get(StrategyConvertUtil.getTimeStrategyType(strategy).toUpperCase());
    }
}
