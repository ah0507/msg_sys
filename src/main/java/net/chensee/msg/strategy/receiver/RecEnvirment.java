package net.chensee.msg.strategy.receiver;

import java.util.List;

/**
 * @author ah
 * @title: 策略运行环境
 * @date 2019/10/30 9:57
 */
public class RecEnvirment {

    private RecStrategy recStrategy;

    public RecEnvirment(RecStrategy recStrategy) {
        this.recStrategy = recStrategy;
    }

    public void execute(List<String> receiver) throws Exception {
        recStrategy.execute(receiver);
    }
}
