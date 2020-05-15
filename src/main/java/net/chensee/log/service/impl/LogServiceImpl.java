package net.chensee.log.service.impl;

import net.chensee.log.dao.SysLogDao;
import net.chensee.log.po.SysLogPo;
import net.chensee.log.service.LogService;
import net.chensee.log.vo.SysLogVo;
import net.chensee.msg.po.ObjectResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ah
 * @title: LogServiceImpl
 * @date 2019/11/29 17:40
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public ObjectResponse insertSysLogPo(SysLogVo sysLogVo) {
        try {
            SysLogPo sysLogPo = new SysLogPo();
            BeanUtils.copyProperties(sysLogVo, sysLogPo);
            sysLogPo.setReqTime(new Date(sysLogVo.getReqTime()));
            if (sysLogVo.getExpTime() != 0L) {
                sysLogPo.setExpTime(new Date(sysLogVo.getExpTime()));
            }
            if (sysLogVo.getReturnTime() != 0L) {
                sysLogPo.setReturnTime(new Date(sysLogVo.getReturnTime()));
            }
            sysLogDao.insertSysLogPo(sysLogPo);
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponse.fail();
        }
        return ObjectResponse.ok();
    }
}
