package net.chensee.msg.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author ah
 * @title: MsgRecVo
 * @date 2020/3/27 11:43
 */
@Data
public class MsgRecVo {

    private String id;

    /**
     * 接收用户ID
     */
    private String userId;

    /**
     * 发送者ID
     */
    private String sender;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 是否已读状态： 1 已读 0 未读
     */
    private Integer readStatus;

    /**
     * 接收消息时间
     */
    private Date receiveTime;
}
