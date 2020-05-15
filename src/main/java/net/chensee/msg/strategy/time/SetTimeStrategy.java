package net.chensee.msg.strategy.time;

import net.chensee.common.DateUtil;

import java.util.Date;

/**
 * @author ah
 * @title: 固定时间的生成策略
 * @date 2019/10/30 9:54
 */
public class SetTimeStrategy implements TimeStrategy {

    @Override
    public Date execute(String strategy) {
        if (strategy.isEmpty()) {
            return null;
        }
        Date executeTime = null;
        String[] split = strategy.split("\\|");
        if (split.length == 2) {
            String staticTimeStr = split[1];
            executeTime = DateUtil.convertStrToDate(staticTimeStr);
        }
        return executeTime;
    }
}
