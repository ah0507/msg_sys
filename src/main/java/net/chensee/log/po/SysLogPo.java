package net.chensee.log.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ah
 * @title: 日志po
 * @date 2019/11/29 10:38
 */
@Data
@Document(value = "sysLogPo")
public class SysLogPo {

    /**
     * 日志ID
     */
    @Id
    private String id;

    /**
     * 请求路径
     */
    private String reqUrl;

    /**
     * 包名+类名
     */
    private String packageName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 请求参数
     */
    private String args;

    /**
     * 异常内容
     */
    private String expContent;

    /**
     * 返回值
     */
    private String returnVal;

    /**
     * 请求时间
     */
    private Date reqTime;

    /**
     * 返回时间
     */
    private Date returnTime;

    /**
     * 发生异常时间
     */
    private Date expTime;

    /**
     * 当前操作者ID
     */
    private Long oprBy;

    /**
     * 当前操作者用户名
     */
    private String oprName;

}
