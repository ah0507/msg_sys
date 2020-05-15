package net.chensee.msg.enums;

/**
 * @author ah
 * @title: 待发送消息时间策略枚举
 * @date 2019/10/29 17:21
 */
public enum MsgPendingStrategyEnum {

    SET_TIME("固定时间"),
    SOME_TIME("一段时间");

    private String desc;

    MsgPendingStrategyEnum(String desc) {
        this.desc = desc;
    }

}
