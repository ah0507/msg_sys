package net.chensee.msg.po;

import lombok.Data;
import net.chensee.msg.enums.MsgPendingStatusEnum;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ah
 * @title: 待发送信息
 * @date 2019/10/29 17:19
 */
@Data
@Document(value = "MsgPendingPo")
public class MsgPendingPo extends BaseInfoPo{

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 接收者策略：类似/user/1;/user/2;/role/1
     */
    private String receiver;

    /**
     * 过期时间策略：类似set_time|2020-01-01 11:12:00 或者 some_time|1
     */
    private String strategy;

    /**
     * 待发送消息状态（SENT已发送，UNSENT未发送，SENT_RECALL已发送撤回，UNSENT_RECALL未发送撤回）
     */
    private MsgPendingStatusEnum status;

    /**
     * 待发送时间
     */
    private Date pendingTime;
}
