jQuery.extend({
	
	//将datagrid元素转为json对象
	datagridToJson:function(rows,tableName){
		/*
		 * 首先要知道相关的jquery生成json的几个方法
		 * push，是将一个json（[]）对象数组里面加入json（{}）对象，
		 * join，是将一个json数组按指定的分隔符进行拆分，结果是得到多个对象
		 * 给一个json对象添加属性格式  ： jsonObject["属性名称"]="属性值"
		 * 				这种方式其实后面的属性值还可以是一个json对象
		 * 
		 * */
		
		
		var table_id;
		var user_id;
		var name;
		var inputList = [];
		var selectList = [];
		var textareaList = [];
		var model = {};
		
		name = tableName;
		

		for(var i=0;i<rows.length;i++){
			if(rows[i].type == "select"){
				
				selectList.push($.selectToJson(rows[i]));//将text行json转化为后台javabean对应的json对象，并返回该该对象
				
			}else if(rows[i].type == "textarea"){
				
				textareaList.push($.textareaToJson(rows[i]));//将select行json转化为后台javabean对应的json对象，并返回该该对象
				
			}else{
				inputList.push($.inputToJson(rows[i]));//将text行json转化为后台javabean对应的json对象，并返回该该对象
			}
		}
		
		if(inputList.length > 0)model["inputList"]=inputList;
		if(selectList.length > 0)model["selectList"]=selectList;
		if(textareaList.length > 0)model["textareaList"]=textareaList;
		
		model["table_id"]="";
		model["name"]=name;
		
		return model;
	},
	
	
	
	//这下面是辅助上面自定义方法的函数
	inputToJson:function(i){
		var input = {};
		input["name"] = i.name;
		input["required"] = i.required;
		input["type"] = i.type;
		
		
		/*以下这些判断是为后期做扩展功能准备的*/
		if(i.textType == "text"){}
		if(i.textType == "number"){}
		if(i.textType == "file"){}
		if(i.textType == "image"){}
		if(i.textType == "date"){}
		if(i.textType == "datetime"){}
		if(i.textType == "email"){}
		
		return input;
		
	},
	selectToJson:function(s){
		var select = {};
		select["name"] = s.name;
		select["type"] = s.type;
		select["info"] = s.info;
		return select;
	},
	textareaToJson:function(t){
		var textarea = {};
		textarea["name"] = t.name;
		textarea["type"] = t.type;
		textarea["info"] = t.info;
		return textarea;
	}
  
});