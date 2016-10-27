jQuery.extend({
	//初始化方法，如果要引入多个js文件，则可以部署在这里，启动jquery的时候首先调用该初始化方法即可
	init:function(){
		$("html").append("<script type=\"text/javascript\" src=\"myjs/extends.js\"></script>");
		
	}
  
});