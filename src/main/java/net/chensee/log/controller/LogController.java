package net.chensee.log.controller;

import io.swagger.annotations.ApiOperation;
import net.chensee.log.service.LogService;
import net.chensee.log.vo.SysLogVo;
import net.chensee.msg.po.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ah
 * @title: LogController
 * @date 2019/11/29 17:37
 */
@RestController
@RequestMapping(value = "/log")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation(value = "新增日志信息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ObjectResponse insertSysLogPo(@RequestBody SysLogVo sysLogVo) {
        return logService.insertSysLogPo(sysLogVo);
    }


}
