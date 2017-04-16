package cn.edu.sspu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.websocket.WebSocketId_Users;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.websocket.MyHandler;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
	
	@ResponseBody
	@RequestMapping("/getWebSocketAllUsers")
	public List<WebSocketId_Users> getWebSocketAllUsers(){
		List<WebSocketId_Users> userList = new ArrayList<WebSocketId_Users>();
		MyHandler myHandler = new MyHandler();
		Map<Integer, String> id_userid = myHandler.id_userid;
        Set<Integer> clientIds = id_userid.keySet();
        for (Integer clientId : clientIds) {
            String name = id_userid.get(clientId);
            WebSocketId_Users iu = new WebSocketId_Users();
            iu.setId(clientId.toString());
            iu.setName(name);
            userList.add(iu);
        }
		return userList;
	}
}
