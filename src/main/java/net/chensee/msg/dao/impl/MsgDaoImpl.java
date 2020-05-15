package net.chensee.msg.dao.impl;

import net.chensee.msg.dao.MsgDao;
import net.chensee.msg.enums.MsgPendingStatusEnum;
import net.chensee.msg.enums.MsgStatusEnum;
import net.chensee.msg.po.MsgInfoPo;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.po.MsgReceivingPo;
import net.chensee.msg.po.RecDeviceInfoPo;
import net.chensee.msg.vo.MsgRecVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ah
 * @title: MsgDaoImpl
 * @date 2019/12/2 10:33
 */
@Repository
public class MsgDaoImpl implements MsgDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveMsgInfo(MsgInfoPo msgInfoPo) {
        mongoTemplate.insert(msgInfoPo);
    }

    @Override
    public void savePendingPo(MsgPendingPo msgPendingPo) {
        mongoTemplate.insert(msgPendingPo);
    }

    @Override
    public void saveMsgReceivingPos(List<MsgReceivingPo> msgReceivingPoList) {
        mongoTemplate.insertAll(msgReceivingPoList);
    }

    @Override
    public void saveMsgReceivingPo(MsgReceivingPo msgReceivingPo) {
        mongoTemplate.insert(msgReceivingPo);
    }

    @Override
    public List<MsgPendingPo> findPendingPosByUnSent(String pendingStatus) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("status").is(pendingStatus);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, MsgPendingPo.class);
    }

    @Override
    public long getRecMsgIsRepeat(String userId, String pendingId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("pendingId").is(pendingId));
        return mongoTemplate.count(query, MsgReceivingPo.class);
    }

    @Override
    public List<MsgRecVo> getUserMsgByUserId(String userId, Integer pageNumber, Integer pageSize) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("pushStatus").is(1))
                .with(new Sort(Sort.Direction.DESC, "receiveTime"));
        query.skip(pageNumber).limit(pageSize);
        return mongoTemplate.find(query, MsgRecVo.class, "MsgReceivingPo");
    }

    @Override
    public List<MsgPendingPo> getUserSendMsgByUserId(String userId, String msgId, Integer pageNumber, Integer pageSize) {
        Query query = new Query(Criteria.where("sender").is(userId)
                .and("msgId").is(msgId))
                .with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip(pageNumber).limit(pageSize);
        return mongoTemplate.find(query, MsgPendingPo.class);
    }

    @Override
    public List<MsgReceivingPo> getUserMsgByUserIds(List<String> userIds) {
        Query query = new Query(Criteria.where("userId").in(userIds)
                .and("pushStatus").is(0)
                .and("readStatus").is(0));
        return mongoTemplate.find(query, MsgReceivingPo.class);
    }

    @Override
    public List<MsgPendingPo> getMsgPendingByMsgId(String userId, String msgId) {
        Query query = new Query(Criteria.where("msgId").is(msgId)
                .and("sender").is(userId));
        return mongoTemplate.find(query, MsgPendingPo.class);
    }

    @Override
    public MsgInfoPo getMsgInfoByMsgId(String userId, String msgId) {
        Query query = new Query(Criteria.where("id").is(msgId)
                .and("createBy").is(userId));
        return mongoTemplate.findOne(query, MsgInfoPo.class);
    }

    @Override
    public void updateMsgPendingPoSentStatus(String pendingId, MsgPendingStatusEnum oldStatus, MsgPendingStatusEnum newStatus) {
        Query query = new Query(Criteria.where("id").is(pendingId)
                .and("status").is(oldStatus));
        Update update = new Update();
        update.set("status", newStatus);
        mongoTemplate.updateFirst(query, update, MsgPendingPo.class);
    }

    @Override
    public void updateSendMsgUnSentReCall(String userId, String pendingId, MsgPendingStatusEnum unSentRecall) {
        Query query = new Query(Criteria.where("id").is(pendingId)
                .and("sender").is(userId));
        Update update = new Update();
        update.set("status", unSentRecall);
        mongoTemplate.updateFirst(query, update, MsgPendingPo.class);
    }

    @Override
    public void updateSendMsgSentReCall(String userId, String pendingId, MsgPendingStatusEnum sentRecall) {
        Query query = new Query(Criteria.where("id").is(pendingId)
                .and("sender").is(userId));
        Update update = new Update();
        update.set("status", sentRecall);
        mongoTemplate.updateFirst(query, update, MsgPendingPo.class);
    }

    @Override
    public void setMsgInfoStatusNoUsing(String userId, String msgId, MsgStatusEnum noUse) {
        Query query = new Query(Criteria.where("id").is(msgId)
                .and("createBy").is(userId));
        Update update = new Update();
        update.set("status", noUse);
        mongoTemplate.updateFirst(query, update, MsgInfoPo.class);
    }

    @Override
    public void setMsgPendingStatusByMsgId(String userId, String id, String msgId, MsgPendingStatusEnum status) {
        Query query = new Query(Criteria.where("id").is(id)
                .and("msgId").is(msgId)
                .and("sender").is(userId));
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query, update, MsgPendingPo.class);
    }

    @Override
    public void setRecMsgPushStatus(List<String> recIds) {
        Query query = new Query(Criteria.where("readStatus").is(0)
                .and("id").in(recIds));
        Update update = new Update();
        update.set("pushStatus", 1);
        mongoTemplate.updateFirst(query, update, MsgReceivingPo.class);
    }

    @Override
    public void setRecMsgReadStatus(String userId, String receiveId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("pushStatus").is(1)
                .and("id").is(receiveId));
        Update update = new Update();
        update.set("readStatus", 1);
        mongoTemplate.updateFirst(query, update, MsgReceivingPo.class);
    }

    @Override
    public void batchDeleteRecMsgByPendingId(String userId, String pendingId) {
        Query query = new Query(Criteria.where("pendingId").is(pendingId)
                .and("sender").is(userId));
        mongoTemplate.remove(query, MsgReceivingPo.class);
    }

    @Override
    public void batchDeleteRecMsgByMsgId(String userId, String msgId) {
        Query query = new Query(Criteria.where("msgId").is(msgId)
                .and("sender").is(userId));
        mongoTemplate.remove(query, MsgReceivingPo.class);
    }

    @Override
    public void saveRecDeviceInfo(RecDeviceInfoPo recDeviceInfoPo) {
        mongoTemplate.insert(recDeviceInfoPo);
    }

    @Override
    public void updateMsgInfoSendNumberById(MsgInfoPo msgInfoPo) {
        Query query = new Query(Criteria.where("id").is(msgInfoPo.getId()));
        Update update = new Update();
        update.set("sendNumber", msgInfoPo.getSendNumber());
        mongoTemplate.updateFirst(query, update, MsgInfoPo.class);
    }

    @Override
    public MsgPendingPo getMsgPendingByPendingId(String userId, String pendingId) {
        Query query = new Query(Criteria.where("id").is(pendingId)
                .and("sender").is(userId));
        return mongoTemplate.findOne(query, MsgPendingPo.class);
    }

    @Override
    public List<MsgInfoPo> getMsgInfosByUserId(String userId, Integer pageNumber, Integer pageSize) {
        Query query = new Query(Criteria.where("createBy").is(userId))
                .with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip(pageNumber).limit(pageSize);
        return mongoTemplate.find(query, MsgInfoPo.class);
    }

    @Override
    public void updateMsgInfo(MsgInfoPo msgInfoPo) {
        Query query = new Query(Criteria.where("id").is(msgInfoPo.getId())
                .and("createBy").is(msgInfoPo.getCreateBy()));
        Update update = new Update();
        update.set("content", msgInfoPo.getContent());
        update.set("title", msgInfoPo.getTitle());
        mongoTemplate.updateFirst(query, update, MsgInfoPo.class);
    }

    @Override
    public Long MsgReceivingPoCount(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("pushStatus").is(1));
        return mongoTemplate.count(query, "MsgReceivingPo");
    }

    @Override
    public Long getUserMsgInfosCount(String userId) {
        Query query = new Query(Criteria.where("createBy").is(userId));
        return mongoTemplate.count(query, MsgInfoPo.class);
    }

    @Override
    public Long getUserSendMsgCount(String userId, String msgId) {
        Query query = new Query(Criteria.where("sender").is(userId)
                .and("msgId").is(msgId));
        return mongoTemplate.count(query, MsgPendingPo.class);
    }

    @Override
    public MsgReceivingPo getMsgReceivingPo(String receiveId) {
        Query query = new Query(Criteria.where("id").is(receiveId));
        return mongoTemplate.findOne(query, MsgReceivingPo.class);
    }
}
