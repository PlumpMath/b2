



/**
 *构造自动完成对话框
 *参数@items : 下拉框显示的数组
 *@field : 字段配置
 *	|valueField:值字段名称,必须
 *	|elementId : 组件的HTML元素id，必须
 */
function buildAutoComplete(items,field){
	var elementId ="#"+ field["elementId"];
	$(elementId).autocomplete({
			minLength: 0,
			source: items,
			focus: function( event, ui ) {
				$(elementId).val( ui.item[field["valueField"]] );
				return false;
			},
			select: function( event, ui ) {
				$(elementId).val( ui.item[field["valueField"]] );
				return false;
			},
	})
	.autocomplete( "instance" )._renderItem = function( ul, item ) {
	return $( "<li>" )
	.append( "<a>" + item.label + "<br>" + item.value + "</a>" )
	.appendTo( ul );
	};
}
/**
 * 构建分类自动完成组件的函数
 *
 *参数@items : 下拉框显示的数组
 *@field : 字段配置
 *	|valueField:值字段名称,必须
 *	|elementId : 组件的HTML元素id，必须
 */
function buildCategoryAutoComplete(items,field){
	var elementId ="#"+ field["elementId"];
	$(elementId).catcomplete({
			minLength: 0,
			source: items,
			focus: function( event, ui ) {
				$(elementId).val( ui.item[field["valueField"]] );
				return false;
			},
			select: function( event, ui ) {
				$(elementId).val( ui.item[field["valueField"]] );
				return false;
			},
	})
}

/**
 * 扩展的自动完成类，安装类别分组
 * 使用方法$(elementId).catcomplete
 */
$.widget( "custom.catcomplete", $.ui.autocomplete, {
		_create: function() {
			this._super();
			this.widget().menu( "option", "items", "> :not(.ui-autocomplete-category)" );
		},
		_renderMenu: function( ul, items ) {
			var that = this,
			currentCategory = "";
			$.each( items, function( index, item ) {
				var li;
				if ( item.category != currentCategory ) {
					ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
					currentCategory = item.category;
				}
				li = that._renderItemData( ul, item );
				if ( item.category ) {
					li.attr( "aria-label", item.category + " : " + item.label );
				}
			});
		}
});

function buildDatePicker(elementId){
	var eId = "#"+elementId;
	$( eId ).datepicker( $.datepicker.regional[ "zh-TW" ] );
	$( "#locale" ).change(function() {
		$( eId ).datepicker( "option",
			$.datepicker.regional[ $( this ).val() ] );
	});
}

function buildCategoryDlg(elementId,config){
	var dlgVar = elementId+"Dlg";	
	if(!window[dlgVar]){
		var dialog=$("#"+elementId).dialog({
				autoOpen: false,
				height: 300,
				width: 350,
				modal: true,
				buttons: {
					"Create an account": alert,
					Cancel: function() {
						//dialog.dialog( "close" );
					}
				},
				close: function() {
				//	form[ 0 ].reset();
				//	allFields.removeClass( "ui-state-error" );
		} }
		);
		window[dlgVar]=dialog;
		window[dlgVar].dialog("open");
	}else{
		window[dlgVar].dialog("open");
	}
}

//日期控件的中文语言包
(function( factory ) {
		if ( typeof define === "function" && define.amd ) {
			// AMD. Register as an anonymous module.
			define([ "../widgets/datepicker" ], factory );
		} else {
			// Browser globals
			factory( jQuery.datepicker );
		}
}(function( datepicker ) {
	datepicker.regional['zh-CN'] = {
		closeText: '关闭',
		prevText: '&#x3C;上月',
		nextText: '下月&#x3E;',
		currentText: '今天',
		monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
		monthNamesShort: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
		dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
		dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
		dayNamesMin: ['日','一','二','三','四','五','六'],
		weekHeader: '周',
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: true,
		yearSuffix: '年'};
	datepicker.setDefaults(datepicker.regional['zh-CN']);

	return datepicker.regional['zh-CN'];

}));
