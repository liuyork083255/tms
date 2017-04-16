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

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.websocket.WebSocketModel;

public class MyHandler extends TextWebSocketHandler {
	//在线用户列表
    public static final Map<Integer, WebSocketSession> id_session;
    public static final Map<Integer, String> id_userid;
    public static final Map<Integer, String> id_adminid;
    public static int online = 0;

    static {
    	id_session = new HashMap<Integer, WebSocketSession>();
    	id_userid = new HashMap<Integer,String>();
    	id_adminid = new HashMap<Integer,String>();
    }
	
	//接收到客户端消息时调用
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
    	System.out.println(message.getPayload());
    	WebSocketModel model = null;
    	try{
    	model = JSON.parseObject(message.getPayload(), WebSocketModel.class);
    	}catch(Exception e){
    		System.out.println("model 转换失败");
    	}
    	if(model != null){
    		if(model.getType().equalsIgnoreCase("userlogin")) 
    			setUserIdAndName(model,session);
    		if(model.getType().equalsIgnoreCase("adminlogin")) 
    			setAdminIdAndName(model,session);
    		if(model.getType().equalsIgnoreCase("admintosigle"))
    			sendMessageToUser(Integer.parseInt(model.getTargetUserId()),model.getMessage());
    		if(model.getType().equalsIgnoreCase("admintoall")) 
    			sendMessageToAllUsers(model.getMessage());
    		if(model.getType().equalsIgnoreCase("usertoadmin"))
    			sendMessageToAdmin(model.getMessage(),session);
    		if(model.getType().equalsIgnoreCase("adminnewtabletoall"))
    			sendMessageToAllUsersNewTable(model.getMessage());
    	}
    }
    
    // 与客户端完成连接后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session)throws Exception {
    	online++;
    	id_session.put(Integer.parseInt(session.getId()), session);
    }
    
    // 一个客户端连接断开时关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {
    	online--;
    	String userName1 = id_userid.get(Integer.parseInt(session.getId()));
    	if(userName1 != null){
    		id_userid.remove(Integer.parseInt(session.getId()));
    		sendAdminUserLoginOut(userName1);
    	}
    	
    	String userName2 = id_adminid.get(Integer.parseInt(session.getId()));
    	if(userName2 != null){
    		id_adminid.remove(Integer.parseInt(session.getId()));
    		sendAdminUserLoginOut(userName2);
    	}
    		
    	System.out.println(session.getId() + " 断开连接 ");
    	id_session.remove(Integer.parseInt(session.getId()));
    	
    	
    }
    
    private void sendMessageToAllUsersNewTable(String message){
    	WebSocketModel model = new WebSocketModel();
    	model.setType("adminnewtabletoall");
    	model.setMessage(message);
        Set<Integer> clientIds = id_userid.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSON.toJSONString(model)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(Integer clientId, String message) {
    	WebSocketModel model = new WebSocketModel();
    	model.setType("admintoall");
    	model.setMessage(message);
        if (id_session.get(clientId) == null) return false;
        WebSocketSession session = id_session.get(clientId);

        if (!session.isOpen()) return false;
        try {
            session.sendMessage(new TextMessage(JSON.toJSONString(model)));
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
    public boolean sendMessageToAllUsers(String message) {
    	WebSocketModel model = new WebSocketModel();
    	model.setType("admintoall");
    	model.setMessage(message);
        boolean allSendSuccess = true;
        Set<Integer> clientIds = id_userid.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSON.toJSONString(model)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
        return  allSendSuccess;
    }
    
    
    
    /**
     * 该方法用于存入登陆用户的name
     * @param model
     * @param session
     */
    private void setUserIdAndName(WebSocketModel model,WebSocketSession session){
    	id_userid.put(Integer.parseInt(session.getId()), model.getTargetUserName());
    	System.out.println(model.getTargetUserName() + "登陆");
    	//用户登录后需要通知admin，在线人数
    	senrAdminUserLogin(id_userid.get(Integer.parseInt(session.getId())));
    }
    
    
    private void setAdminIdAndName(WebSocketModel model,WebSocketSession session){
    	System.out.println(model.getTargetUserName() + "登陆");
    	id_adminid.put(Integer.parseInt(session.getId()), model.getTargetUserName());
    	sendAdminInit(Integer.parseInt(session.getId()));
    }
    
    private void sendAdminInit(Integer clientId){
    	WebSocketModel model = new WebSocketModel();
    	model.setType("sendadmininit");
    	model.setOnlineNumber(online);
    	
        if (id_session.get(clientId) == null) return;
        WebSocketSession session = id_session.get(clientId);

        if (!session.isOpen()) return;
        try {
            session.sendMessage(new TextMessage(JSON.toJSONString(model)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void senrAdminUserLogin(String name){
    	WebSocketModel model = new WebSocketModel();
    	model.setType("online");
    	model.setOnlineNumber(online);
    	model.setSourceUserName(name);
    	Set<Integer> clientIds = id_adminid.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSON.toJSONString(model)));
                }
            } catch (IOException e) {
                System.out.println("");
            }
        }
    }
    
    private void sendAdminUserLoginOut(String userName){
    	WebSocketModel model = new WebSocketModel();
    	model.setType("userloginout");
    	model.setOnlineNumber(online);
    	model.setSourceUserName(userName);
    	Set<Integer> clientIds = id_adminid.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSON.toJSONString(model)));
                }
            } catch (IOException e) {
                System.out.println("");
            }
        }
    }
    
    
    private void sendMessageToAdmin(String msg,WebSocketSession se){
    	String userName = id_userid.get(Integer.parseInt(se.getId()));
    	if(userName == null){
    		System.out.println("获取当前当前发送用户的姓名失败");
    		return ;
    	}
    	WebSocketModel model = new WebSocketModel();
    	model.setType("usertoadmin");
    	model.setSourceUserName(userName);
    	model.setMessage(msg);
    	Set<Integer> clientIds = id_adminid.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = id_session.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSON.toJSONString(model)));
                }
            } catch (IOException e) {
                System.out.println("");
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
