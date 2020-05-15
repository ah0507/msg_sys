package net.chensee.msg.strategy.receiver;

import java.util.List;

/**
 * @author ah
 * @title: 策略接口
 * @date 2019/10/30 9:52
 */
public interface RecStrategy {

    void execute(List<String> receiver) throws Exception;
}
