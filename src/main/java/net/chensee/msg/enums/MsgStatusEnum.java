package net.chensee.msg.enums;

/**
 * @author ah
 * @title: 消息状态枚举
 * @date 2019/10/29 17:02
 */
public enum MsgStatusEnum {

    USE("可用"),
    NO_USE("删除");

    private String desc;

    MsgStatusEnum(String desc) {
        this.desc = desc;
    }

}
