package net.chensee.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ah
 * @title: StrategyConvertUtil
 * @date 2019/10/30 10:14
 */
public class StrategyConvertUtil {

    /**
     * 解析过期时间策略
     * @param strategy
     * @return
     */
    public static String getTimeStrategyType(String strategy) {
        if (strategy.isEmpty()) {
            return null;
        }
        String type = null;
        String[] split = strategy.split("\\|");
        if (split.length == 2) {
            type = split[0];
        }
        return type;
    }

    /**
     * 解析接收方（用户组，角色，用户等）的策略
     * @param strategy
     * @return
     */
    public static Map<String, List<String>> getReceiverStrategy(String strategy) {
        if (strategy.isEmpty()) {
            return null;
        }
        String[] rules = strategy.split(";");
        Map<String, List<String>> recMap = new HashMap<>();
        for (String rule : rules) {
            String[] split = rule.split("/");
            if (split.length == 3) {
                String key = split[1];
                List<String> list = new ArrayList<>();
                if (recMap.containsKey(key)) {
                    list = recMap.get(key);
                }
                list.add(split[2]);
                recMap.put(key, list);
            }
        }
        return recMap;
    }

}
