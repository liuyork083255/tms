$.extend($.fn.datagrid.defaults.editors, {
	/*扩展datagrid的组件，因为默认里面只有datetime组件，也就是只显示年月日，时间不显示*/
    datetimebox: {
		init: function(container, options){
			var editor = $('<input />').appendTo(container);
			options.editable = false;//控制该框不可以用户输入
			editor.datetimebox(options);//初始化
			return editor;
		},
		destroy: function(target){
			$(target).datetimebox('destroy');
		},
		getValue: function(target){
			return $(target).datetimebox('getValue');
		},
		setValue: function(target, value){
			$(target).datetimebox('setValue',value);
		},
		resize: function(target, width){
			$(target).datetimebox('resize',width);
		}
    }
});