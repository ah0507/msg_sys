package net.chensee.msg.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @title: BaseInfoPo
 * @date 2019/10/29 17:12
 */
@Data
public class BaseInfoPo {

    @Id
    private String id;

    private Date createTime;

    private String createBy;
}
