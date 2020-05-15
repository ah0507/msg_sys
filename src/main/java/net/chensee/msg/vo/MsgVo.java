package net.chensee.msg.vo;

import lombok.Data;

/**
 * @author ah
 * @title: MsgVo
 * @date 2019/10/29 18:02
 */
@Data
public class MsgVo {

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者策略：/user/1;/user/2;/role/1
     */
    private String receiver;

    /**
     * 过期时间策略：set_time|11:12:00 或者 some_time|1.5
     */
    private String timeStrategy;

    private String content;

    private String title;

}
