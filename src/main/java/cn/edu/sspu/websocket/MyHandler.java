package cn.edu.sspu.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {
	//在线用户列表
    private static final Map<Integer, WebSocketSession> id_session;
    private static final Map<Integer, String> id_userid;
    
    //用户标识
    private static String USER_ID = "";
    static {
    	id_session = new HashMap<Integer, WebSocketSession>();
    	id_userid = new HashMap<Integer,String>();
    }
	
	//接收到客户端消息时调用
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
    	System.out.println("text message: " + session.getId() + "-" + message.getPayload());
    }
    
    // 与客户端完成连接后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session)throws Exception {
    	System.out.println("链接成功 ... ID : " + session.getId());
    }
    
    // 一个客户端连接断开时关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {
    	
    }
    
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(Integer clientId, TextMessage message) {
        if (id_session.get(clientId) == null) return false;
        WebSocketSession session = id_session.get(clientId);
        System.out.println("sendMessage:" + session);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    /**
     * 广播信息
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<Integer> clientIds = id_session.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
        return  allSendSuccess;
    }
    
    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private Integer getClientId(WebSocketSession session) {
        try {
            Integer clientId = (Integer) session.getAttributes().get(USER_ID);
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
    
}
