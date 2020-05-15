package net.chensee.socket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.chensee.msg.vo.UserMsgVo;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author ah
 * @title: 消息推送
 * @date 2019/11/27 9:54
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
@Data
public class WebSocketServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    public static CopyOnWriteArraySet<WebSocketServer> connectSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    public static CopyOnWriteArraySet<WebSocketServer> closeSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

//    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 记录当前websocket的连接数（保证线程安全）
     */
    private static LongAdder connectAccount = new LongAdder();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public String userId;

    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        connectSocketSet.add(this);
        connectAccount.increment();
        log.info("有新的连接接入，当前连接数为{}", connectAccount);
    }

    @OnClose
    public void onClose() {
        closeSocketSet.add(this);
//        connectSocketSet.remove(this);
        connectAccount.decrement();
        log.info("有连接关闭，当前连接数为{}", connectAccount);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端发来的消息,message -> {}", message);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public static void pushMsg(Map<String, List<UserMsgVo>> userMsgMap) throws IOException {
        for (WebSocketServer webSocketServer : connectSocketSet) {
            if (!webSocketServer.session.isOpen()) {
                continue;
            }
            String userId = webSocketServer.userId;
            if (!userMsgMap.containsKey(userId)) {
                continue;
            }
            List<UserMsgVo> userMsgVos = userMsgMap.get(userId);
            String s = JSONArray.fromObject(userMsgVos).toString();
            webSocketServer.session.getBasicRemote().sendText(s);
        }
    }

}
