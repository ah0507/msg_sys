package net.chensee.msg.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ah
 * @title: 消息接收
 * @date 2019/10/29 17:47
 */
@Data
@Document(value = "MsgReceivingPo")
public class MsgReceivingPo extends BaseInfoPo{

    /**
     * 接收用户ID
     */
    private String userId;

    /**
     * 消息待发送ID
     */
    private String pendingId;

    /**
     * 发送者ID
     */
    private String sender;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 推送状态：1 已推送 0 未推送
     */
    private Integer pushStatus;

    /**
     * 是否已读状态： 1 已读 0 未读
     */
    private Integer readStatus;

    /**
     * 接收消息时间
     */
    private Date receiveTime;

}
