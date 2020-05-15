package net.chensee.msg.strategy.time;

import java.util.Date;

/**
 * @author ah
 * @title: 策略接口
 * @date 2019/10/30 9:52
 */
public interface TimeStrategy {

    Date execute(String strategy);
}
