jQuery.extend({
	//初始化方法，如果要引入多个js文件，则可以部署在这里，启动jquery的时候首先调用该初始化方法即可
	init:function(){
		$("html").append("<script type='text/javascript' src='myjs/extends.js'></script>");
		/*引入easyui的核心js文件*/
		$("html").append("<script type='text/javascript' src='js/jquery-easyui-1.5/jquery.easyui.min.js' charset='utf-8'></script>");
		
		/*引入easyui的默认主题*/
		$("html").append("<link rel='stylesheet' href='js/jquery-easyui-1.5/themes/default/easyui.css' type='text/css' charset='utf-8'></link>");
		
		/*引入easyui的小图标*/
		$("html").append("<link rel='stylesheet' href='js/jquery-easyui-1.5/themes/icon.css' type='text/css' charset='utf-8'></link>");
	
		/*引入easyui的中文js包*/
		$("html").append("<script type='text/javascript' src='js/jquery-easyui-1.5/locale/easyui-lang-zh_CN.js' charset='utf-8'></script>");
		
		
		
		
		
		
	
	}
  
});