package net.chensee.msg.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ah
 * @title: 浏览器设备等信息
 * @date 2019/12/3 10:56
 */
@Data
@Document(value = "RecDeviceInfoPo")
public class RecDeviceInfoPo extends BaseInfoPo{

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * mac地址
     */
    private String mac;

    /**
     * ip地址
     */
    private String ip;

}
