package net.chensee.msg.enums;

/**
 * @author ah
 * @title: 消息接收者的枚举类
 * @date 2019/10/30 16:41
 */
public enum ReceiverStrategyStatus {

    USER("用户"),
    USER_GROUP("用户组"),
    ROLE("角色");

    private String desc;

    ReceiverStrategyStatus(String desc) {
        this.desc = desc;
    }


}
