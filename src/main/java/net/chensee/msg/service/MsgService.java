package net.chensee.msg.service;

import net.chensee.msg.enums.MsgPendingStatusEnum;
import net.chensee.msg.po.*;
import net.chensee.msg.vo.MsgVo;

import java.util.List;

/**
 * @author ah
 * @title: MsgService
 * @date 2019/10/29 18:11
 */
public interface MsgService {

    /**
     * 保存消息（包括具体消息和待发送）
     *
     * @param msgVo
     */
    ObjectResponse sendMsg(MsgVo msgVo);

    /**
     * 新建一条消息内容
     * @param msgInfoPo
     * @return
     */
    ObjectResponse addMsgInfo(MsgInfoPo msgInfoPo);

    /**
     * 通过一个用户ID得到所有接收消息
     *
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    ObjectResponse getUserMsgByUserId(String userId, Integer pageNumber, Integer pageSize);


    /**
     * 获得该用户发送的消息
     *
     * @param userId
     * @param msgId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    ObjectResponse getUserSendMsgByUserId(String userId, String msgId, Integer pageNumber, Integer pageSize);

    /**
     * 查看消息详情
     * @param userId
     * @param msgId
     * @return
     */
    ObjectResponse getSenderMsgInfoByMsgId(String userId,String msgId);

    /**
     * 接收者查看消息详情
     *
     * @param userId
     * @param receiveId
     * @return
     */
    ObjectResponse getRecMsgInfoByMsgId(String userId, String receiveId);

    /**
     * 未发送撤销
     *
     *
     * @param userId
     * @param pendingId
     * @return
     */
    ObjectResponse unSentReCall(String userId, String pendingId);

    /**
     * 已发送撤销
     *
     *
     * @param userId
     * @param pendingId
     * @return
     */
    ObjectResponse sentReCall(String userId, String pendingId);

    /**
     * 消息内容删除
     * @param userId
     * @param msgId
     */
    ObjectResponse setMsgInfoStatusNoUsing(String userId,String msgId);

    /**
     * 将接收到的消息未读设为已读
     *
     * @param userId
     * @param receiveId
     */
    ObjectResponse setRecMsgReadStatus(String userId, String receiveId);

    /**
     * 找到未发送的所有待发送消息
     *
     * @param pendingStatus
     * @return
     */
    List<MsgPendingPo> findPendingPosByUnSent(String pendingStatus);

    /**
     * 通过角色得到用户
     *
     * @param roleId
     * @return
     */
    List<String> getUsersByRoleId(String roleId) throws Exception;

    /**
     * 通过用户组得到用户
     *
     * @param userGroupId
     * @return
     */
    List<String> getUsersByUserGroupId(String userGroupId) throws Exception;


    /**
     * 查询是否已有消息(通过待发送id和用户id)
     *
     * @param userId
     * @param pendingId
     * @return
     */
    long getRecMsgIsRepeat(String userId, String pendingId);

    /**
     * 通过多个在线用户ID获取到所有接收到的未推送的消息
     *
     * @param userIds
     * @return
     */
    List<MsgReceivingPo> getUserMsgByUserIds(List<String> userIds);

    /**
     * 批量保存多个用户接收到的消息
     *
     * @param msgReceivingPos
     */
    void saveMsgReceivingPos(List<MsgReceivingPo> msgReceivingPos) throws Exception;

    /**
     * 保存单个用户接收到的消息
     *
     * @param msgReceivingPo
     */
    void saveMsgReceivingPo(MsgReceivingPo msgReceivingPo) throws Exception;


    /**
     * 保存用户访问信息
     * @param recDeviceInfoPo
     * @return
     */
    ObjectResponse saveRecDeviceInfo(RecDeviceInfoPo recDeviceInfoPo);

    /**
     * 将带发送消息状态置为已发送
     *
     * @param id
     * @param oldStatus
     * @param newStatus
     */
    void updateMsgPendingPoSentStatus(String id, MsgPendingStatusEnum oldStatus, MsgPendingStatusEnum newStatus) throws Exception;

    /**
     * 消息推送成功之后将消息状态置为已推送
     *
     * @param recIds
     */
    void setRecMsgPushStatus(List<String> recIds) throws Exception;

    void updateMsgInfoSendNumberById(MsgInfoPo msgInfoPo);

    /**
     * 发送的消息撤回
     * @param userId
     * @param pendingId
     * @return
     */
    ObjectResponse msgReCall(String userId,String pendingId);

    /**
     * 获得该用户的消息详情列表
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    ObjectResponse getUserMsgInfosByUserId(String userId, Integer pageNumber, Integer pageSize);

    /**
     * 修改消息详情
     * @param msgInfoPo
     * @return
     */
    ObjectResponse updateMsgInfo(MsgInfoPo msgInfoPo);

}
