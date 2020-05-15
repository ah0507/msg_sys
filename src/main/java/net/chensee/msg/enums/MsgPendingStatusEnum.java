package net.chensee.msg.enums;

/**
 * @author ah
 * @title: 待发送消息状态枚举
 * @date 2019/10/29 17:23
 */
public enum MsgPendingStatusEnum {

    SENT("已发送"),
    UNSENT("未发送"),
    SENT_RECALL("已发送撤销"),
    UNSENT_RECALL("未发送撤销");

    private String desc;

    MsgPendingStatusEnum(String desc) {
        this.desc = desc;
    }

}
