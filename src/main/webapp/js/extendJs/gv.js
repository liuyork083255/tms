jQuery.extend({
	
	/*项目路径*/
	projectUrl:"/tms",
	
	/*设置一个JQuery全局变量，用于接收model模型，使得不同页面可以获取该值*/
	globalModel:undefined,
	getGlobalModel:function(){return $.globalModel;},
	setGlobalModel:function(model){$.globalModel = model;},
	
	globalTableId:undefined,
	getGlobalTableId:function(){return $.globalTableId;},
	setGlobalTableId:function(table_id){$.globalTableId = table_id;}
	
})