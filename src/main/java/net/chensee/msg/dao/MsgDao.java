package net.chensee.msg.dao;

import net.chensee.msg.enums.MsgPendingStatusEnum;
import net.chensee.msg.enums.MsgStatusEnum;
import net.chensee.msg.po.MsgInfoPo;
import net.chensee.msg.po.MsgPendingPo;
import net.chensee.msg.po.MsgReceivingPo;
import net.chensee.msg.po.RecDeviceInfoPo;
import net.chensee.msg.vo.MsgRecVo;

import java.util.List;

/**
 * @author ah
 * @title: SysLogDao
 * @date 2019/12/2 10:32
 */
public interface MsgDao {

    void saveMsgInfo(MsgInfoPo msgInfoPo);

    void savePendingPo(MsgPendingPo msgPendingPo);

    void saveMsgReceivingPos(List<MsgReceivingPo> msgReceivingPoList);

    void saveMsgReceivingPo(MsgReceivingPo msgReceivingPo);

    List<MsgPendingPo> findPendingPosByUnSent(String pendingStatus);

    long getRecMsgIsRepeat(String userId, String pendingId);

    List<MsgRecVo> getUserMsgByUserId(String userId, Integer pageNumber, Integer pageSize);

    List<MsgPendingPo> getUserSendMsgByUserId(String userId, String msgId, Integer pageNumber, Integer pageSize);

    List<MsgReceivingPo> getUserMsgByUserIds(List<String> userIds);

    List<MsgPendingPo> getMsgPendingByMsgId(String userId,String msgId);

    MsgInfoPo getMsgInfoByMsgId(String userId,String msgId);

    void updateMsgPendingPoSentStatus(String pendingId, MsgPendingStatusEnum oldStatus, MsgPendingStatusEnum newStatus);

    void updateSendMsgUnSentReCall(String userId, String pendingId, MsgPendingStatusEnum unSentRecall);

    void updateSendMsgSentReCall(String userId, String pendingId, MsgPendingStatusEnum sentRecall);

    void setMsgInfoStatusNoUsing(String userId,String msgId, MsgStatusEnum noUse);

    void setMsgPendingStatusByMsgId(String userId,String id,String msgId, MsgPendingStatusEnum status);

    void setRecMsgPushStatus(List<String> recIds);

    void setRecMsgReadStatus(String userId, String receiveId);

    void batchDeleteRecMsgByPendingId(String userId, String pendingId);

    void batchDeleteRecMsgByMsgId(String userId,String msgId);

    void saveRecDeviceInfo(RecDeviceInfoPo recDeviceInfoPo);

    void updateMsgInfoSendNumberById(MsgInfoPo msgInfoPo);

    MsgPendingPo getMsgPendingByPendingId(String userId,String pendingId);

    List<MsgInfoPo> getMsgInfosByUserId(String userId, Integer pageNumber, Integer pageSize);

    void updateMsgInfo(MsgInfoPo msgInfoPo);

    Long MsgReceivingPoCount(String userId);

    Long getUserMsgInfosCount(String userId);

    Long getUserSendMsgCount(String userId, String msgId);

    MsgReceivingPo getMsgReceivingPo(String receiveId);
}
