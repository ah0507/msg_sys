package net.chensee.common;

import java.util.UUID;

/**
 * @author ah
 * @title: randomUUID
 * @date 2019/10/30 9:35
 */
public class RandomUUIDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
