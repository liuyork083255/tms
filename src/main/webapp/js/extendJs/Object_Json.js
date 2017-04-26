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
		
		var inputList = [];
		var model = {};
		

		for(var i=0;i<rows.length;i++){
			var input = {};
			//input["id"] = "";
			input["name"] = rows[i].name.toLowerCase();
			input["type"] = rows[i].type;
			
			if(rows[i].type == "select" || rows[i].type == "checkbox" || rows[i].type == "radio"){
				input["param"] = rows[i].param;
			}
			
			//input["value"] = "";
			input["info"] = rows[i].info;
			input["required"] = rows[i].required;
			
			input["validatetype"] = rows[i].validatetype;
			input["sort"] = i;
			//input["table_id"] = "";
			//input["user_id"] = "";
			
			//以上有四个属性不设置，为了让后台得到的对应的值显示为 null ，而不是空字符串类型
			
			inputList.push(input);
		}
		
		if(inputList.length > 0)model["inputList"]=inputList;
		
		//model["table_id"]="";
		model["name"]=tableName;
		
		return model;
	}
  
});