package net.chensee.msg.strategy.time;

import java.util.Date;

/**
 * @author ah
 * @title: 策略运行环境
 * @date 2019/10/30 9:57
 */
public class TimeEnvirment {

    private TimeStrategy timeStrategy;

    public TimeEnvirment(TimeStrategy timeStrategy) {
        this.timeStrategy = timeStrategy;
    }

    public Date execute(String strategyContent) {
        return timeStrategy.execute(strategyContent);
    }
}
