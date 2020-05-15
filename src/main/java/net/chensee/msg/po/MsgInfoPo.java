package net.chensee.msg.po;

import lombok.Data;
import net.chensee.msg.enums.MsgStatusEnum;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @title: 消息内容
 * @date 2019/10/29 17:12
 */
@Data
@Document(value = "MsgInfoPo")
public class MsgInfoPo extends BaseInfoPo{

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息状态：USE可用,NO_USE删除
     */
    private MsgStatusEnum status;

    /**
     * 发送次数
     */
    private Integer sendNumber;

}
