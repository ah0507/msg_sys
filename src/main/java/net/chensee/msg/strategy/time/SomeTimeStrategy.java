package net.chensee.msg.strategy.time;

import net.chensee.common.DateUtil;

import java.util.Date;

/**
 * @author ah
 * @title: 增值时间的生成策略
 * @date 2019/10/30 9:55
 */
public class SomeTimeStrategy implements TimeStrategy {

    private Date nowDate;

    public SomeTimeStrategy(Date nowDate) {
        this.nowDate = nowDate;
    }

    @Override
    public Date execute(String strategy) {
        if (strategy.isEmpty() || nowDate == null) {
            return null;
        }
        Date executeTime = null;
        String[] split = strategy.split("\\|");
        if (split.length == 2) {
            String dynamicTimeStr = split[1];
            int dynamicTime = Integer.parseInt(dynamicTimeStr);
            executeTime = DateUtil.setDateMinute(nowDate, dynamicTime);
        }
        return executeTime;
    }

}
