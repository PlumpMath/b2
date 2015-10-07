



/**
 *构造自动完成对话框
 *参数@items : 下拉框显示的数组
 *@field : 字段配置
 *	|valueField:值字段名称,必须
 *	|elementId : 组件的HTML元素id，必须
 */
function buildAutoComplete(items,fieldName){
	var elementId ="#"+ fieldName;
	$(elementId).autocomplete({
			minLength: 0,
			source: items,
			focus: function( event, ui ) {
				$(elementId).val( ui.item["value"] );
				return false;
			},
			select: function( event, ui ) {
				$(elementId).val( ui.item["value"] );
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
	if(!window[elementId+"DatePicker"]){
	   window[elementId+"DatePicker"]=$( eId ).datepicker( $.datepicker.regional[ "zh-TW" ] );
		 window[elementId+"DatePicker"].datepicker("show");
	}
}
/**
 * 构建弹出式树状选择字段，
 *@fieldName :字段名称
 *@data  : 树状菜单数据
 */
function buildSingleSelectTreeField(fieldName,data){
	var config={
		"data" : data,
		showcheck : false,
		"fieldId" : fieldName 
	}
	buildCategoryDlg(fieldName+"Dlg",config);
}
function buildMultiSelectTreeDlg(elementId,data){
	var config={
		"data" : data,
		showcheck : true
	}
	buildCategoryDlg(elementId,config);
}


function buildCategoryDlg(elementId,config){
	var dfop ={
		showcheck : false
	};
	$.extend(dfop, config);
	var dlgVar = elementId+"Dlg";	
	if(!window[dlgVar]){
		var dialog=$("#"+elementId).dialog({
				autoOpen: false,
				height: 400,
				width: 400,
				modal: true,
				buttons: {
					"选择": function(){
						if(dfop.showcheck){//多选
							var s=$("#"+elementId+"Tree").getCheckedNodes();
							if(s !=null)
								alert(s.join(","));
							else
								alert("NULL");
						}else{
							//单选
							var s=$("#"+elementId+"Tree").getCurrentNode();
							if(s !=null){
								//alert(s.text);
								$("#"+config["fieldId"]).val(s.text);
							}else{
								alert("NULL");}
							dialog.dialog( "close" );
						}

					},
					"关闭": function() {
						dialog.dialog( "close" );
					}
				},
				close: function() {
					//	form[ 0 ].reset();
					//	allFields.removeClass( "ui-state-error" );
		} }
		);
		//生成树
		var userAgent = window.navigator.userAgent.toLowerCase();
		$.browser={};
		$.browser.msie8 = $.browser.msie && /msie 8\.0/i.test(userAgent);
		$.browser.msie7 = $.browser.msie && /msie 7\.0/i.test(userAgent);
		$.browser.msie6 = !$.browser.msie8 && !$.browser.msie7 && $.browser.msie && /msie 6\.0/i.test(userAgent);
		function load() {        
			var o = { showcheck:dfop.showcheck 
				//onnodeclick:function(item){alert(item.text);},        
			};
			o.data = config["data"];                  
			$("#"+elementId+"Tree").treeview(o);            
			$("#showchecked").click(function(e){
				var s=$("#tree").getCheckedNodes();
				if(s !=null)
					alert(s.join(","));
				else
					alert("NULL");
			});
			$("#showcurrent").click(function(e){
				var s=$("#tree").getCurrentNode();
				if(s !=null)
					alert(s.text);
				else
					alert("NULL");
			});
			}   
			if( $.browser.msie6)
			{
				load();
			}
			else{
				$(document).ready(load);
			}
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

function initToolbar(config){
	$('.btn-group').buttonset();
	$('.btn-group .save-btn').button({
			icons: {
				primary: "ui-icon-disk"
	}});
	$('.btn-group .print-btn').button({
			icons: {
				primary: "ui-icon-print"
	}});
	var defaults = {
		entityName : "",
		elementId : "entity-toolbar"
	};
	$("#"+defaults['elementId']+" button").click(function(){
		var formParam = $("#entityForm").serialize();
		$.ajax({  
				type:'post',      
				url:'/users',  
				data:formParam,  
				cache:false,  
				dataType:'json',  
				success:function(data){  
					alert(data);
				}  
		});  
	});

}
