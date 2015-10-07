function buildRemoteTable(elementId,config){
	var mConfig = {
		url:'/users',
		mtype: "GET",
		datatype: "json",
		page: 1,
		userData:{
			"amount" : "233"
		},
		height: 470,
		//width: 800,
		autowidth:true,
		rownumbers: true,
		colModel: [
			{ label: '姓名', name: 'name', width: 90 },
			{ label: '性别', name: 'sex', width: 90 },
								],
								viewrecords: true, 
								loadonce: true,
								// show the current page, data rang and total records on the toolbar
								caption: "Load jqGrid through Javascript Array",
								pager: "#"+elementId+"-Pager",
								footerrow: true, // set a footer row
								userDataOnFooter: true, // the calculated sums and/or strings from server are put at footer row.
	};
	$.extend(mConfig,config);
	
	var grid = jQuery("#"+elementId).jqGrid(mConfig); 
	grid.jqGrid('filterToolbar',{
			//stringResult: true,
			search: true, // show search button on the toolbar
			add: false,
			edit: false,
			del: false,
			refresh: true
	});
	$("#"+elementId).navGrid("#"+elementId+"-Pager", {                
			search: true, // show search button on the toolbar
			add: false,
			edit: false,
			del: false,
			refresh: true
	});
}

function buildLocalTable(elementId,config){
	var mConfig = {
		datatype: "local",
		userData:{
			"amount" : "233"
		},
		data: [
			{ id: "1", invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
			{ id: "2", invdate: "2007-10-02", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
			{ id: "3", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
			{ id: "4", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
			{ id: "5", invdate: "2007-10-05", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
			{ id: "6", invdate: "2007-09-06", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
			{ id: "7", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
			{ id: "8", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
			{ id: "9", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
			{ id: "19", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
			{ id: "29", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
			{ id: "79", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" }
				],
		height: 250,
		width: 780,
		rownumbers: true,
		colModel: [
			{ label: 'Inv No', name: 'id', width: 75, key:true },
			{ label: 'Date', name: 'invdate', width: 90 },
			{ label: 'Client', name: 'name', width: 100 },
			{ label: 'Amount', name: 'amount', width: 80 },
			{ label: 'Tax', name: 'tax', width: 80 },
			{ label: 'Total', name: 'total', width: 80,formatter: 'number',
				summaryTpl : "<b>{0}</b>",
				summaryType: "sum"
			},
			{ label: 'Notes', name: 'note', width: 150 }
								],
								viewrecords: true, 
								// show the current page, data rang and total records on the toolbar
								caption: "Load jqGrid through Javascript Array",
								pager: "#"+elementId+"-Pager",
								footerrow: true, // set a footer row
								userDataOnFooter: true, // the calculated sums and/or strings from server are put at footer row.
	};
	$.extend(mConfig,config);
	
	var grid = jQuery("#"+elementId).jqGrid(mConfig); 
	 grid.jqGrid('filterToolbar');
}
