package net.chensee.log.vo;

import lombok.Data;

/**
 * @author ah
 * @title: SysLogVo
 * @date 2019/12/3 14:09
 */
@Data
public class SysLogVo {

    /**
     * 日志ID
     */
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
    private Long reqTime;

    /**
     * 返回时间
     */
    private Long returnTime;

    /**
     * 发生异常时间
     */
    private Long expTime;

    /**
     * 当前操作者ID
     */
    private Long oprBy;

    /**
     * 当前操作者用户名
     */
    private String oprName;
}
