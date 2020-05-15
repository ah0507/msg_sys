package net.chensee.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ah
 * @title: 时间格式转换
 * @date 2019/10/30 10:37
 */
public class DateUtil {

    public static Date convertStrToDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static Date setTime(Date nowDate, double dynamicTime) {
        return new Date((long) (nowDate.getTime() + dynamicTime * 1000));
    }

    public static Date setDateMinute(Date date,int value){
        Calendar calc =Calendar.getInstance();
        calc.setTime(date);
        calc.add(Calendar.MINUTE, value);
        return calc.getTime();
    }
}
