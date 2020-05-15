package net.chensee.log.dao.impl;

import net.chensee.log.dao.SysLogDao;
import net.chensee.log.po.SysLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author ah
 * @title: SysLogDaoImpl
 * @date 2019/12/2 15:48
 */
@Repository
public class SysLogDaoImpl implements SysLogDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertSysLogPo(SysLogPo sysLogPo) {
        mongoTemplate.insert(sysLogPo);
    }
}
