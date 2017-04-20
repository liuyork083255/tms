jQuery.extend({
	
	/*liuyork项目URL*/
	HostPort:"120.25.251.210:8080",
	/*本机项目URL*/
	HostPort:"127.0.0.1:80",
	/*TL项目URL*/
	HostPort:"tms.shalib.cc:80",
	/*项目路径*/
	projectUrl:"/tms",
	
	/*my-email*/
	adminEmail:"18721816191@163.com",
	
	socketModel:undefined,
	getSocketModel:function(){return $.socketModel},
	setSocketModel:function(user){$.socketModel = user;},
	
	/*设置一个JQuery全局变量，用于接收model模型，使得不同页面可以获取该值*/
	globalModel:undefined,
	getGlobalModel:function(){return $.globalModel;},
	setGlobalModel:function(model){$.globalModel = model;},
	
	/*这个全局变量用于暂时存储table的id*/
	globalTableId:undefined,
	getGlobalTableId:function(){return $.globalTableId;},
	setGlobalTableId:function(table_id){$.globalTableId = table_id;},
	
	
	globalUser:undefined,
	getGlobalUser:function(){return $.globalUser;},
	setGlobalUser:function(user){$.globalUser = user;},
	
	globalTableModel:undefined,
	getGlobalTableModel:function(){return $.globalTableModel;},
	setGlobalTableModel:function(model){$.globalTableModel = model;},
	
	/*获取websocket对象*/
	webSocket:undefined,
	getWebSocket:function(){return $.webSocket;},
	setWebSocket:function(socket){$.webSocket = socket;},
	clickWebSocket:function(model){
	    var websocket = null;

	    //判断当前浏览器是否支持WebSocket
	    if('WebSocket' in window){
	        websocket = new WebSocket("ws://"+$.HostPort+"/tms/websocket.do");
	    }
	    else{
	        alert('Not support websocket')
	    }
	    //连接发生错误的回调方法
	    websocket.onerror = function(){
	        reLinkWebsocket();
	    };

	    //连接成功建立的回调方法
	    websocket.onopen = function(event){
	    	console.info("open");
	    	$.webSocket = websocket;
	    	$.webSocket.send(model);
	    }

	    //接收到消息的回调方法
	    websocket.onmessage = function(event){
	    	console.info(event.data);
	    	onWebSocketMessage(event);
	    }

	    //连接关闭的回调方法
	    websocket.onclose = function(){
	    	console.info("close ............");
	    	$.webSocket = undefined;
	    }


	}

	
})


































