var filterDatatableObj;
/**
 * @purpose Show Table View
 * @param $scope,ele,filterObj
 * @author Tejal
 */
function showTableView($scope, ele, filterObj, dataTableCols, tableResponse) {
	$.LoadingOverlay("show", spinner);
	filterDatatableObj = tableResponse.data;
	webix.ui({
		container: ele,
		autoheight: true,
		view: "datatable",
		id: "id_brix_search_table",
		//autoConfig: true,
		css: 'brix_datatable_2 brix_datatableStyle',
		autoWidth: true,
		modal: true,
		gravity: 0.85,
		resizeColumn: true,
		//		pager: "id_brix_table_filter_pager",
		columns: dataTableCols.data,
		on: {
			"onAfterRender": webix.once(function () {
				var obj;
				$.LoadingOverlay("show", spinner);
				obj = JSON.stringify(filterObj);
				if ($('#id_view_container'))
					$('#id_view_container').html('');
				if (tableResponse && tableResponse.returncode == '1' && tableResponse.data && tableResponse.data.searchresult) {
					filterDatatableObj = tableResponse.data;
					if ($$('id_brix_search_table')) {
						$$('id_brix_search_table').parse(tableResponse.data.searchresult);
					}
					/**
					 * @purpose Add checkbox column as per the checkboxrequired value from the received json.
					 */
					var noOfColumns = this.config.columns;
					if (filterDatatableObj && filterDatatableObj.checkboxrequired && filterDatatableObj.checkboxrequired == '1') {
						noOfColumns.unshift({
							id: "id_chbx",
							header: "#",
							width: 10,
							template: "{common.radio()}",
						});
					}
					this.config.columns = noOfColumns;
					this.refreshColumns();
//					$.LoadingOverlay("hide", spinner);
				} else {
//					$.LoadingOverlay("hide", spinner);
					customAlert("Error", resourceObj.brix_lbl_failed_to_load_data);
					//					webix.alert('Faild to load data table data ');
				}
			}),
			onStructureLoad: function () {
				var noOfColumns = this.config.columns;
				var dataArr = this.data.pull;
				var columnTitle = noOfColumns[0].id;
				$.LoadingOverlay("hide", spinner);
				var columns = this.config.columns;
				//				var index=noOfColumns.findIndex(function(ele){
				//					var modeifiedId=ele.id.toLowerCase()
				//					return modeifiedId == 'id';
				//				});
				var index;
				for (var k = 0; k < noOfColumns.length; k++) {
					var modeifiedId = noOfColumns[k].id.toLowerCase()
					if (modeifiedId == 'id') {
						index = k;
					}
				}
				if (index > -1) {
					noOfColumns.splice(index, 1)
				}
				/**
				 * @purpose Add link column as per the islink value from the received json.
				 */
				if (filterDatatableObj && filterDatatableObj.islink && filterDatatableObj.islink == '1' && filterDatatableObj.link) {
					this.getColumnConfig(filterDatatableObj.link).template = function (obj) {

						if (obj[filterDatatableObj.link])
							return "<span class='linkStyle titleLink'>" + obj[filterDatatableObj.link] + "</span>"
						else
							return " "
					}
				}
				
				//DocumentURL   DocumentName
				/*if (filterDatatableObj && this.getColumnConfig("DocumentURL")) {
					this.getColumnConfig("DocumentURL").template = function (obj) {

						if (obj["DocumentURL"])
							return "<span class='linkStyle getDocUrl'>" + obj["DocumentURL"] + "</span>"
						else
							return " "
					}
				}*/
				
				if (filterDatatableObj && this.getColumnConfig("DocumentName")) {
					this.getColumnConfig("DocumentName").template = function (obj) {

						if (obj["DocumentName"])
							return "<span class='linkStyle getDocUrl'>" + obj["DocumentName"] + "</span>"
						else
							return " "
					}
				}
				
				
				if (columns && columns.length) {
					for (var i = 0; i < columns.length; i++) {
						columns[i].width = 0;
						columns[i].fillspace = 1;
						var text = this.config.columns[i].header[0].text;
						var id = this.config.columns[i].id;
						this.config.columns[i].header[0].height = 30;
						var modifiedText, objNameText;
						this.config.columns[i].header[0].text = "<span title='" + text + "'>" + decodeURIComponent(text) + "</span>";
						if (filterDatatableObj && filterDatatableObj.columnconfigurations && filterDatatableObj.columnconfigurations.length > 0) {
							for (var j = 0; j < filterDatatableObj.columnconfigurations.length; j++) {
								if (text) {
									modifiedText = text.toLowerCase();
								}
								if (filterDatatableObj.columnconfigurations[j].key) {
									objNameText = filterDatatableObj.columnconfigurations[j].key.toLowerCase();
								}
								//								console.log('modifiedText'+JSON.stringify(modifiedText))
								//								console.log('objNameText'+JSON.stringify(objNameText))
								if (modifiedText == objNameText && filterDatatableObj.columnconfigurations[j].columnfilter == 'TextBox') {
									this.config.columns[i].header = [resourceObj[text], {
										content: "textFilter"
									}];
								} else if (modifiedText == objNameText && filterDatatableObj.columnconfigurations[j].columnfilter == 'ComboBox') {
									this.config.columns[i].header = [resourceObj[text], {
										content: "selectFilter"
									}];
								}
//								this.config.columns[i].adjust = true;
							}
						}

//						this.adjustColumn();
					}
				}
			},
			onAfterLoad: function () {},
		},
		onClick: {
			//		      titleLink: filterTableTextClickHandeler
			titleLink: launchonTable,
			getDocUrl : getDocUrl,
		},
	})
}
/**
 * @purpose This function is to redirect the page on click of table link to new tab
 * @param event object
 * @author komal.shevane
 */
function launchonTable(e, id, trg) {
	var nodeId = $$("id_brix_search_table").getItem(id).id_DataName;
	var win = window.open("/GraphSearch/measurementview?key=" + nodeId);
}

/**
 * @purpose This function is to redirect the page on click of document url link to new tab
 * @param event object
 */
function getDocUrl(e, id, trg){
	let docId = $$("id_brix_search_table").getItem(id.row).id_Document;
	webix.ajax().get("./search/getDocumentUrl", {
		string: docId
	}, function (text, xml, xhr) {
		//response
		$.LoadingOverlay("hide", spinner);
		var response = JSON.parse(text);
		if(response && response.returncode == '1')
			window.open(response.data,'_blank');	//open url in new window
		else {
			webix.alert({
				title: "Error",
				autoheight: true,
				autowidth: true,
				ok: "OK",
				text: response.message,
			});
		}
			
	});

}
/**
 * @purpose This function is to redirect the page on click of table link
 * @param event object
 * @author Tejal
 */
function filterTableTextClickHandeler(e, id, trg) {
	//console.log($$("id_brix_search_table").getItem(id));
	$.LoadingOverlay("show", spinner);
	var popupId = $$("id_brix_search_table").getItem(id).id_TagNum;
	//	console.log(popupId);
	var treeData = [{
		"id": "1",
		"open": false,
		"value": "measurement1",
		"data": [{
			"id": "1.1",
			"value": "Equivalent Wt 3",
			"actualId": "MeaQuantity:71563",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.2",
			"value": "Equivalent Wt 2",
			"actualId": "MeaQuantity:71562",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.3",
			"value": "Equivalent Wt 1",
			"actualId": "MeaQuantity:71561",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.4",
			"value": "Vlvtrain Mechanism 1",
			"actualId": "MeaQuantity:71560",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.5",
			"value": "Lift Accelaration Neg 3",
			"actualId": "MeaQuantity:71559",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.6",
			"value": "Lift Accelaration Neg 2",
			"actualId": "MeaQuantity:71558",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.7",
			"value": "Lift Accelaration Neg 1",
			"actualId": "MeaQuantity:71557",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.8",
			"value": "Test Title",
			"actualId": "MeaQuantity:71556",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.9",
			"value": "Test Purpose",
			"actualId": "MeaQuantity:71555",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.10",
			"value": "ECU Type",
			"actualId": "MeaQuantity:71554",
			"meaId": "MeaResult:3083",
			"unit": "-"
		}, {
			"id": "1.11",
			"value": "Vlv Spg Lift Load 3",
			"actualId": "MeaQuantity:71568",
			"meaId": "MeaResult:3083",
			"unit": "kgf"
		}, {
			"id": "1.12",
			"value": "Vlv Spg Lift Load 2",
			"actualId": "MeaQuantity:71567",
			"meaId": "MeaResult:3083",
			"unit": "kgf"
		}, {
			"id": "1.13",
			"value": "Vlv Spg Lift Load 1",
			"actualId": "MeaQuantity:71566",
			"meaId": "MeaResult:3083",
			"unit": "kgf"
		}, {
			"id": "1.14",
			"value": "IN Valve Spg Set Load",
			"actualId": "MeaQuantity:71565",
			"meaId": "MeaResult:3083",
			"unit": "kgf"
		}, {
			"id": "1.15",
			"value": "EX Valve Spg Set Load",
			"actualId": "MeaQuantity:71564",
			"meaId": "MeaResult:3083",
			"unit": "kgf"
		}, {
			"id": "1.16",
			"value": "Vlv Open Angle 3",
			"actualId": "MeaQuantity:71571",
			"meaId": "MeaResult:3083",
			"unit": "deg"
		}, {
			"id": "1.17",
			"value": "Vlv Open Angle 2",
			"actualId": "MeaQuantity:71570",
			"meaId": "MeaResult:3083",
			"unit": "deg"
		}, {
			"id": "1.18",
			"value": "Vlv Open Angle 1",
			"actualId": "MeaQuantity:71569",
			"meaId": "MeaResult:3083",
			"unit": "deg"
		}, {
			"id": "1.19",
			"value": "Valve Lift 3",
			"actualId": "MeaQuantity:71576",
			"meaId": "MeaResult:3083",
			"unit": "mm"
		}, {
			"id": "1.20",
			"value": "Valve Lift 2",
			"actualId": "MeaQuantity:71575",
			"meaId": "MeaResult:3083",
			"unit": "mm"
		}, {
			"id": "1.21",
			"value": "Valve Lift 1",
			"actualId": "MeaQuantity:71574",
			"meaId": "MeaResult:3083",
			"unit": "mm"
		}, {
			"id": "1.22",
			"value": "Low Bounce Limit",
			"actualId": "MeaQuantity:71573",
			"meaId": "MeaResult:3083",
			"unit": "mm"
		}, {
			"id": "1.23",
			"value": "High Bounce Limit",
			"actualId": "MeaQuantity:71572",
			"meaId": "MeaResult:3083",
			"unit": "mm"
		}],
		"internalId": "MeaResult:3083"
	}, {
		"id": "2",
		"open": false,
		"value": "A027-1-1-2-3-100001-TestCond-1",
		"data": [{
			"id": "2.1",
			"value": "Point_5",
			"actualId": "MeaQuantity:71581",
			"meaId": "MeaResult:3084",
			"unit": "G"
		}, {
			"id": "2.2",
			"value": "Point_4",
			"actualId": "MeaQuantity:71580",
			"meaId": "MeaResult:3084",
			"unit": "G"
		}, {
			"id": "2.3",
			"value": "Point_3",
			"actualId": "MeaQuantity:71579",
			"meaId": "MeaResult:3084",
			"unit": "G"
		}, {
			"id": "2.4",
			"value": "Point_2",
			"actualId": "MeaQuantity:71578",
			"meaId": "MeaResult:3084",
			"unit": "G"
		}, {
			"id": "2.5",
			"value": "Point_1",
			"actualId": "MeaQuantity:71577",
			"meaId": "MeaResult:3084",
			"unit": "G"
		}],
		"internalId": "MeaResult:3084"
	}, {
		"id": "3",
		"open": false,
		"value": "A027-1-1-2-3-100001-TestCond-1",
		"data": [{
			"id": "3.1",
			"value": "Point_1",
			"actualId": "MeaQuantity:78446",
			"meaId": "MeaResult:3406",
			"unit": "G"
		}, {
			"id": "3.2",
			"value": "Point_5",
			"actualId": "MeaQuantity:78450",
			"meaId": "MeaResult:3406",
			"unit": "G"
		}, {
			"id": "3.3",
			"value": "Point_4",
			"actualId": "MeaQuantity:78449",
			"meaId": "MeaResult:3406",
			"unit": "G"
		}, {
			"id": "3.4",
			"value": "Point_3",
			"actualId": "MeaQuantity:78448",
			"meaId": "MeaResult:3406",
			"unit": "G"
		}, {
			"id": "3.5",
			"value": "Point_2",
			"actualId": "MeaQuantity:78447",
			"meaId": "MeaResult:3406",
			"unit": "G"
		}],
		"internalId": "MeaResult:3406"
	}, {
		"id": "4",
		"open": false,
		"value": "A027-1-1-2-3-100001-TestCond-1",
		"data": [{
			"id": "4.1",
			"value": "Point_5",
			"actualId": "MeaQuantity:78505",
			"meaId": "MeaResult:3416",
			"unit": "G"
		}, {
			"id": "4.2",
			"value": "Point_4",
			"actualId": "MeaQuantity:78504",
			"meaId": "MeaResult:3416",
			"unit": "G"
		}, {
			"id": "4.3",
			"value": "Point_3",
			"actualId": "MeaQuantity:78503",
			"meaId": "MeaResult:3416",
			"unit": "G"
		}, {
			"id": "4.4",
			"value": "Point_2",
			"actualId": "MeaQuantity:78502",
			"meaId": "MeaResult:3416",
			"unit": "G"
		}, {
			"id": "4.5",
			"value": "Point_1",
			"actualId": "MeaQuantity:78501",
			"meaId": "MeaResult:3416",
			"unit": "G"
		}],
		"internalId": "MeaResult:3416"
	}, {
		"id": "5",
		"open": false,
		"value": "A027-1-1-2-3-100001-TestCond-1",
		"data": [{
			"id": "5.1",
			"value": "Point_5",
			"actualId": "MeaQuantity:78515",
			"meaId": "MeaResult:3418",
			"unit": "G"
		}, {
			"id": "5.2",
			"value": "Point_4",
			"actualId": "MeaQuantity:78514",
			"meaId": "MeaResult:3418",
			"unit": "G"
		}, {
			"id": "5.3",
			"value": "Point_3",
			"actualId": "MeaQuantity:78513",
			"meaId": "MeaResult:3418",
			"unit": "G"
		}, {
			"id": "5.4",
			"value": "Point_2",
			"actualId": "MeaQuantity:78512",
			"meaId": "MeaResult:3418",
			"unit": "G"
		}, {
			"id": "5.5",
			"value": "Point_1",
			"actualId": "MeaQuantity:78511",
			"meaId": "MeaResult:3418",
			"unit": "G"
		}],
		"internalId": "MeaResult:3418"
	}]
	webix.ui({
		view: "window",
		height: setHeightInPercentage(70),
		width: setWidthInPercentage(70),
		move: true,
		css: 'brix_window',
		id: 'id_filter_window',
		modal: true,
		position: 'center',
		headHeight: 37, // Adjust headheight
		head: "<span class='brix_window_header_title'>" + resourceObj.MeasurementData_lbl + "</span><span class='brix_window_close' onclick='$$(\"id_filter_window\").close();'>×</span>",

		body: {
			cols: [{
					view: "tree",
					select: true,
					id: 'id_treeView',
					css: 'brix_tree brix_search_tree tree_fix_line',
					width: 300,
					activeTitle: true,
					drag: true,
					data: [],
					tooltip: {
						template: '<span>#value#</span>'
					},
					template: function (obj, common) {
						return "<span>" + common.icon(obj, common) + "<a id='treetag' ui-sref='/" + currentState + "." + obj.link + "' ui-sref-opts='{reload:true}' style='text-decoration: none; color:#333;'>" + common.folder(obj, common) + webix.template.escape(obj.value) + "</a></span>";
					},
					type: {
						folder: function (obj) {
							if (obj.$level == 1)
								return "<i class='fa fa-briefcase briefcaseIconCss treeIconCss'></i>";
							if (obj.$level == 2)
								return "<i class='fi-clipboard-notes clipboardIconCss treeIconCss'></i>";
							if (obj.$level == 3)
								return "<i class='fi-clipboard clipboardIconCss treeIconCss'></i>";
							if (obj.$level == 4)
								return "<i class='fa fa-tag clipboardIconCss'></i>";
						},
					},
					on: {
						onAfterRender: webix.once(function () {
							webix.ajax().get("./search/getMeasurement", {
								string: popupId
							}, function (text, xml, xhr) {
								//response
								$.LoadingOverlay("hide", spinner);
								var response = JSON.parse(text);
								// console.log(text);
								//console.log(response);
								$$('id_treeView').parse(response);
								//  $$("id_treeView").parse(response.data,"json");

							});


						}),
						onBeforeDrag: function (context, ev) {
							/*
							            					var obj=this.getItem(context.start);
							            					if("internalId" in obj){
							          	                  var meaResultId = this.getItem(context.start).internalId;
							          	                $('#meaHeading').html('');
							          	                $('#meaHeading').text("Selected Measurement : "+this.getItem(context.start).value);
							          				         // $.LoadingOverlay("show", spinner);
							          			
							          				        webix.ajax().get("./search/measurement/channels", { meaId : meaResultId }, function(text, xml, xhr){
							          				          //response
							          				          var response=JSON.parse(text);
							          				          drawChart(JSON.parse(text))
							          				      });
							            					}
							          	   					 return false;
							            				
							          	               */
						},
					}
				},
				{
					view: "resizer",
					borderless: false
				},
				{
					rows: [{
						view: "template",
						template: "<div id='id_window_container'></div>",
						on: {
							onAfterRender: webix.once(function () {
								var count = 0;
								var id = "genericCanvas";
								var canvasJsonObj = {
									"canvasperrow": 1,
									"chartoptions": ["Line Chart", "Scatter Chart", "Area Chart"],
									"dataviewoptions": ["Data View", "Data Compare"],
									"exportoptions": ["PDF", "IMAGE"],
									"canvastitle": 1,
									"axistitle": 1,
									"maxnoofcanvases": 10
								}
								createCanvasTemplateView(count, id, 'id_window_container');

							})
						}
					}]
				}
			]
		}
	}).show();

	webix.ui({
		id: "menuPopup1",
		view: "popup",
		padding: 0,
		width: 160,
		css: 'abc',
		right: 200,
		body: {
			view: "list",
			autoheight: true,
			borderless: true,
			select: true,
			data: [{
					id: 1,
					value: "Export CSV"
				},
				{
					id: 2,
					value: "Export Image"
				}
			],
			on: {
				onItemClick: function (id) {
					if (id == 1) {
						webix.alert("CSV");
					}
					if (id == 2) {
						webix.toPNG($$("preview"));
					}

				}
			}
		}
	})

	function closeDetailWindow() {
		$$('id_filter_window').hide();
	}
}

function setWidthInPercentage(percentage) {
	return width = ($(window).width() / 100) * percentage;
}

function setHeightInPercentage(percentage) {
	return height = ($(window).height() / 100) * percentage;
}