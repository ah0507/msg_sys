package net.chensee.msg.controller;

import io.swagger.annotations.ApiOperation;
import net.chensee.msg.po.MsgInfoPo;
import net.chensee.msg.po.ObjectResponse;
import net.chensee.msg.po.RecDeviceInfoPo;
import net.chensee.msg.service.MsgService;
import net.chensee.msg.vo.MsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author ah
 * @title: MsgController
 * @date 2019/10/29 18:07
 */
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private MsgService msgService;

    @ApiOperation(value = "获得该用户接收到的消息列表")
    @RequestMapping(value = "/{userId}/receive", method = RequestMethod.GET)
    public ObjectResponse getUserMsgByUserId(@PathVariable String userId,
                                             @RequestParam Integer pageNumber,
                                             @RequestParam Integer pageSize) {
        return msgService.getUserMsgByUserId(userId,pageNumber,pageSize);
    }

    @ApiOperation(value = "获得该用户的消息详情列表")
    @RequestMapping(value = "/{userId}/msgInfos", method = RequestMethod.GET)
    public ObjectResponse getUserMsgInfosByUserId(@PathVariable String userId,
                                                  @RequestParam Integer pageNumber,
                                                  @RequestParam Integer pageSize) {
        return msgService.getUserMsgInfosByUserId(userId,pageNumber,pageSize);
    }

    @ApiOperation(value = "获得该用户发送的消息列表")
    @RequestMapping(value = "/{userId}/{msgId}/send", method = RequestMethod.GET)
    public ObjectResponse getUserSendMsgByUserId(@PathVariable String userId,
                                                 @PathVariable String msgId,
                                                 @RequestParam Integer pageNumber,
                                                 @RequestParam Integer pageSize) {
        return msgService.getUserSendMsgByUserId(userId,msgId,pageNumber,pageSize);
    }

    @ApiOperation(value = "发送者查看消息详情")
    @RequestMapping(value = "/{userId}/{msgId}/sender/msgInfo", method = RequestMethod.GET)
    public ObjectResponse getSenderMsgInfoByMsgId(@PathVariable String msgId,
                                            @PathVariable String userId) {
        return msgService.getSenderMsgInfoByMsgId(userId,msgId);
    }

    @ApiOperation(value = "接收者查看消息详情")
    @RequestMapping(value = "/{userId}/{receiveId}/receive/msgInfo", method = RequestMethod.GET)
    public ObjectResponse getRecMsgInfoByMsgId(@PathVariable String receiveId,
                                               @PathVariable String userId) {
        return msgService.getRecMsgInfoByMsgId(userId,receiveId);
    }

    @ApiOperation(value = "发送消息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ObjectResponse sendMsg(@RequestBody MsgVo msgVo) {
        return msgService.sendMsg(msgVo);
    }

    @ApiOperation(value = "新建一条消息内容")
    @RequestMapping(value = "/msgInfo", method = RequestMethod.POST)
    public ObjectResponse addMsgInfo(@RequestBody MsgInfoPo msgInfoPo) {
        return msgService.addMsgInfo(msgInfoPo);
    }

    @ApiOperation(value = "保存用户访问信息")
    @RequestMapping(value = "/devInfo", method = RequestMethod.POST)
    public ObjectResponse saveRecDeviceInfo(@RequestBody RecDeviceInfoPo recDeviceInfoPo) {
        return msgService.saveRecDeviceInfo(recDeviceInfoPo);
    }

    @ApiOperation(value = "修改消息详情")
    @RequestMapping(value = "/msgInfo", method = RequestMethod.PUT)
    public ObjectResponse updateMsgInfo(@RequestBody MsgInfoPo msgInfoPo) {
        return msgService.updateMsgInfo(msgInfoPo);
    }

    @ApiOperation(value = "将发送消息撤回")
    @RequestMapping(value = "/{userId}/{pendingId}/msgReCall", method = RequestMethod.PUT)
    public ObjectResponse msgReCall(@PathVariable String pendingId,
                                    @PathVariable String userId) {
        return msgService.msgReCall(userId,pendingId);
    }

    @ApiOperation(value = "将消息内容删除")
    @RequestMapping(value = "/{userId}/{msgId}/msgInfo", method = RequestMethod.PUT)
    public ObjectResponse setMsgInfoNoUsing(@PathVariable String msgId,
                                            @PathVariable String userId) {
        return msgService.setMsgInfoStatusNoUsing(userId,msgId);
    }

}
