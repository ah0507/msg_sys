package net.chensee.log.service;

import net.chensee.log.vo.SysLogVo;
import net.chensee.msg.po.ObjectResponse;

/**
 * @author ah
 * @title: LogService
 * @date 2019/11/29 17:39
 */
public interface LogService {

    ObjectResponse insertSysLogPo(SysLogVo sysLogVo);
}
