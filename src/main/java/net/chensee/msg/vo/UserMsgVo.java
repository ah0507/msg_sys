package net.chensee.msg.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author ah
 * @title: RecMsgVo
 * @date 2019/12/3 10:32
 */
@Data
public class UserMsgVo {

    private String userId;

    private String sender;

    private String msgId;

    private String title;

    private String content;

    private Date receiveTime;
}
