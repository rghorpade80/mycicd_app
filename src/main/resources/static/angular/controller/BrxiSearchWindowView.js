var resourceWindowObj = resourceValues;
var idWindowObj={};
var typeWindowObj={};
var dateWindowObj={};
var allDataWindowTypeObj={};
webix.i18n.parseFormat = "%d/%m/%Y %H:%i";
webix.i18n.setLocale();
webix.protoUI({
	name: "brix_search_window_view",
	$init: function (config) {
		config.rows = [
			{
				view: 'accordion',
				css: 'brix_accordion_1 brixAccordionStyle brixAccordionBackgroundStyle',
				multi: true,
				autoheight: true,
				rows: [{
					header: "<div class=''><span class='fa fa-filter smfViewIconCss'><span class='brix_filter_text_style' class='' onClick='showFilterPopup(this)'>"+resourceObj.Filter_lbl+"</span></div>",
					headerHeight: 30,
					onClick: {
//						"openFilters": function () {
//							return false;
//						}
					},
					body: {
						view: 'form',
						autoheight: true,
						padding: 0,
						id: 'id_brix_window_filter_form',
						elements: [{
							rows: [{
									rows: [],
									id: 'id_brix_window_filter_continer',
								},
								{
									view: 'template',
									template: '',
									height: 1,
									on: {
										onAfterRender: webix.once(function () {
											if ($$('id_brix_window_filter_added_view')) {
												mainView = [];
												$$('id_brix_window_filter_continer').removeView('id_brix_window_filter_added_view');

											}
											generateFilterViewForWindow().done(function (mainView) {
												$$('id_brix_window_filter_continer').addView({
													rows: mainView,
													id: 'id_brix_window_filter_added_view'
												})
											});
										})
									}
								},
								{
									height: 6,
									css: 'spacerStyle'
								},
								{

									css: 'brix_search_last_line',
									height: 35,
									cols: [{
											gravity: 4
										},
										{
											view: "text",
											id: "id_brix_window_show_text_box",
											gravity: 2,
											labelWidth: 100,
											label: resourceWindowObj.brix_search_lbl_show_result,
											type: "number",
											value: '10000',
											css: 'brix_textbox',
											on: {
												onTimedKeyPress: function () {
													var showTextBoxVal = $$('id_brix_window_show_text_box').getValue();
													if (showTextBoxVal > 10000) {
														webix.alert('Max 10000 value is allowed in show result field');
														$$('id_brix_window_show_text_box').setValue(10000);
													}
												}
											}
										},
										{

											view: "combo",
											gravity: 3,
											id: 'id_brix_window_select_saved_filter',
											options: [],
											placeholder:resourceWindowObj.brix_search_lbl_select_filter,
											on: {
												onItemClick: function () {
													var currentEleId = $$(this).config.id;
													var obj = {}
													//parameterName
													obj['myfiltername']=$$('id_brix_window_select_saved_filter').getValue();
													obj['searchName']=objForSearch.searchname;
													obj = JSON.stringify(obj);
											    	$.get("getSavedCriteria?sessionKey=" + sessionKey+"&data=" + obj, function(comboResponse) {
											    		comboResponse=JSON.parse(comboResponse)
											    		if(comboResponse && comboResponse.returncode && comboResponse.returncode=='1' && comboResponse.data){
											    		 $$('id_brix_window_select_saved_filter').getList().parse(comboResponse.data)
											    		}
											    	});
												},
												onChange:function(){
													var currentEleId = $$(this).config.id;
													var obj = {}
													//parameterName
													obj['myfiltername']=$$('id_brix_window_select_saved_filter').getValue();
													obj['searchName']=objForSearch.searchname;
													obj = JSON.stringify(obj);
													$.get("getSavedFilterParameters?sessionKey=" + sessionKey+"&data=" + obj, function(response) {
														response=JSON.parse(response)
											    		if(response && response.returncode && response.returncode=='1' && response.data && response.data.length>0){
											    			var modifiedData=response.data;
											    			// Clear all fields related to All date type except date
											    			for(var s in allDataWindowTypeObj){
											    				if(allDataWindowTypeObj[s]){
										    					$$(allDataWindowTypeObj[s]).setValue('');
											    				}
											    			}
											    			// Clear all date fields
											    			for(var j in dateWindowObj){
											    				if(dateWindowObj[j]){
											    					$$(j).setValue('');
											    				}
											    			}
											    				
											    				for(var k=0;k<modifiedData.length;k++){
											    					if(modifiedData[k].displaytype == 'ComboBox' || modifiedData[k].displaytype == 'MultiComboBox'){
													    				var eleId=modifiedData[k].id;
													    				eleId='id_brix_window_'+modifiedData[k].id;
													    				var filterDataVal=modifiedData[k].values;
													    				var arr=[];
													    				var multiComboSelection='';
													    				for(var p=0;p<filterDataVal.length;p++){
													    					if(multiComboSelection == ''){
													    						multiComboSelection=filterDataVal[p];
													    					}else{
													    						multiComboSelection=multiComboSelection+','+filterDataVal[p]
													    					}
													    					arr.push(filterDataVal[p]);
													    				}
													    				if(modifiedData[k].displaytype == 'MultiComboBox'){
													    					if(eleId){
													    					   $$(eleId).define("options", {
																	                 data: arr,
																	            });
													    						$$(eleId).setValue(multiComboSelection);
													    						$$(eleId).refresh();
														    				}
													    				}else{
													    					if(eleId){
														    					  $$(eleId).define("options", {
																		                 data: arr,
																		           });
													    						$$(eleId).setValue(arr[0]);
													    						$$(eleId).refresh();
														    				}
													    				}	
											    					}else if(modifiedData[k].displaytype == 'DateRange'){
											    						if(modifiedData[k].fromDate){
											    							var filterDataVal=modifiedData[k].fromDate;
												    						var eleId='id_window_datePicker_From_' + modifiedData[k].id;
														    				
												    						var format = webix.Date.dateToStr("%d/%m/%Y");
												    						var dataVal = format(new Date(filterDataVal));
														    				if(eleId){
														    					$$(eleId).setValue(dataVal)
														    				}
											    						}
											    						if(modifiedData[k].toDate){
											    							var filterDataVal=modifiedData[k].toDate;
												    						var eleId='id_window_datePicker_To_' + modifiedData[k].id;
												    						var format = webix.Date.dateToStr("%d/%m/%Y");
												    						var dataVal = format(new Date(filterDataVal));
														    				if(eleId){
														    					$$(eleId).setValue(filterDataVal)
														    				}
											    						}
											    					
											    					}else if(modifiedData[k].displaytype == 'Date'){
											    						var eleId=modifiedData[k].id;
													    				eleId='id_brix_window_'+modifiedData[k].id;
													    				if(eleId){
													    					$$(eleId).setValue(modifiedData[k].values[0])
													    				}
											    					}else{
														    				var eleId=modifiedData[k].id;
														    				eleId='id_brix_window_'+modifiedData[k].id;
														    				if(eleId){
														    					$$(eleId).setValue(modifiedData[k].values[0])
														    				}
													    			}
												    			}
											    			var dataForFilter = $$('id_brix_window_filter_form').getValues();
											    			applyWindowFilter(dataForFilter);
											    		}
											    	});
												}
											}
										
										},
										{
											view: "button",
											value: resourceWindowObj.brix_search_lbl_clear_filter,
											type: "form",
											css: 'brix_button',
											autowidth: true,
											gravity: 0.5,
											on: {
												onItemClick: function () {
													$$('id_brix_window_filter_form').clear();
													$$('id_brix_window_select_saved_filter').setValue('');
													
													var dataForFilter = $$('id_brix_window_filter_form').getValues();
													$.LoadingOverlay("show", spinner);
													applyWindowFilter(dataForFilter);

												}
											},
										},
										{
											view: "button",
											value: resourceWindowObj.brix_search_lbl_apply_filter,
											type: "form",
											css: 'brix_button',
											autowidth: true,
											gravity: 0.5,
											on: {
												onItemClick: function () {
													var dataForFilter = $$('id_brix_window_filter_form').getValues();
													$.LoadingOverlay("show", spinner);
													applyWindowFilter(dataForFilter);
												}
											},
										},
										{
											view: "button",
											value: resourceWindowObj.brix_search_lbl_save_filter,
											type: "form",
											css: 'brix_button',
											autowidth: true,
											gravity: 0.5,
											on: {
												onItemClick: function () {
													var dataForFilter = $$('id_brix_window_filter_form').getValues();
													showSaveFilterWindowView(dataForFilter);
												}
											},
										}
									]


								}
							]

						}]
					},
				}]
			}, {

				view: 'accordion',
				css: 'brix_accordion_1 brix_accordion_second brixAccordionStyle',
				multi: false,
				autoheight: true,
				rows: [{
					header: "<div class=''><span class=''><span class='brix_filter_text_style'>" + objForSearch.searchname + "</span></div>",
					headerHeight: 30,
					body: {
						view: 'form',
						autoheight: true,
						padding: 0,
						css: 'brix_accordion_body',
						elements: [{
							rows: [{
								autoheight: true,
								cols: [{
										view: "button",
										css: 'brix_button',
										type: "icon",
										icon: "refresh",
										label: resourceObj.brix_search_lbl_refresh,
										autowidth: true,
										gravity: 0.5,
										on: {
											onItemClick: function () {
												$$("id_brix_window_search_table").eachColumn(function (id, col) {
													var filter = this.getFilter(id);
													if (filter) {
														if (filter.setValue) filter.setValue("")
														else filter.value = "";
													}
												});
												$$("id_brix_window_search_table").filterByAll();
											}
										},
									},
									{
										gravity:1.5
									},
									{
										view: "pager",
										id: 'id_brix_window_table_filter_pager',
										css: "brix_pager pagerCntrl",
										align:"right",
										template: "{common.prev()} {common.pages()} {common.next()}",
										size: 15,
										group: 5,
									}
									
								]
							}, {
								
								autoheight: true,
								view: "datatable",
								id: "id_brix_window_search_table",
								autoConfig: true,
								css: 'brix_datatable_2 brix_datatableStyle',
								autoWidth: true,
								gravity: 0.85,
								resizeColumn: true,
								pager: "id_brix_window_table_filter_pager",
								data: [],
								ready() {
									var obj;
									$.LoadingOverlay("show", spinner);
									var dataForFilter = $$('id_brix_window_filter_form').getValues();
									getFilterObj(dataForFilter).done(function(getFilterObj){
										obj = JSON.stringify(getFilterObj);
										webix.ajax().get("getResultForTableView?sessionKey=" + sessionKey + "&data=" + obj, function (response) {
											response = JSON.parse(response)
											if (response && response.returncode == '1' && response.data && response.data.searchresult) {
												filterDatatableObj = response.data;
												$$('id_brix_window_search_table').parse(response.data.searchresult);
												$.LoadingOverlay("hide", spinner);
											} else {
												$.LoadingOverlay("hide", spinner);
												webix.alert('Faild to load data table data ');
											}
											
										});
									}).fail(function(){
										
									})
									
								},
								on: {

									"onAfterRender": webix.once(function () {
										var noOfColumns = this.config.columns;
										var dataArr = this.data.pull;
										var columnTitle = noOfColumns[0].id;
										/**
										 * @purpose Add checkbox column as per the checkboxrequired value from the received json.
										 */
										if (filterDatatableObj && filterDatatableObj.checkboxrequired && filterDatatableObj.checkboxrequired == '1') {
											noOfColumns.unshift({
												id: "id_chbx_window",
												header: "#",
												template: "{common.checkbox()}",
											});
										}
										
										var index=noOfColumns.findIndex(function(ele){
											var modeifiedId=ele.id.toLowerCase()
											return modeifiedId == 'id';
										});
										if(index>-1){
											noOfColumns.splice(index,1)
										} 
										/**
										 * @purpose Add link column as per the islink value from the received json.
										 */
										
										if (filterDatatableObj && filterDatatableObj.islink && filterDatatableObj.islink == '1' && filterDatatableObj.link) {
											 	this.getColumnConfig(filterDatatableObj.link).template = function(obj){
												  return "<a href='#' onClick='filterTableTextClickHandeler(this)'>"+obj[filterDatatableObj.link]+"</a>"
										      }
										}
										this.config.columns = noOfColumns;
										this.refreshColumns();
									}),
									onStructureLoad: function () {
										$.LoadingOverlay("hide", spinner);
										var columns = this.config.columns;
										columns[0].width = 80;
										if (columns && columns.length) {
											for (var i = 1; i < columns.length; i++) {
												columns[i].width = 0;
												columns[i].fillspace = 1;

												var text = this.config.columns[i].header[0].text;
												var id = this.config.columns[i].id;
												this.config.columns[i].header[0].height = 30;
												var modifiedText, objNameText;
												this.config.columns[i].header[0].text = "<span title='" + text + "'>" + decodeURIComponent(text) + "</span>";
												if (filterDatatableObj.columnconfigurations && filterDatatableObj.columnconfigurations.length > 0) {
													for (var j = 0; j < filterDatatableObj.columnconfigurations.length; j++) {
														if (text) {
															modifiedText = text.toLowerCase();
														}
														if (filterDatatableObj.columnconfigurations[j].key) {
															objNameText = filterDatatableObj.columnconfigurations[j].key.toLowerCase();
														}
														if (modifiedText == objNameText && filterDatatableObj.columnconfigurations[j].columnfilter == 'TextBox') {
															this.config.columns[i].header = [text, {
																content: "textFilter"
															}];
														} else if (modifiedText == objNameText && filterDatatableObj.columnconfigurations[j].columnfilter == 'ComboBox') {
															this.config.columns[i].header = [text, {
																content: "selectFilter"
															}];
														}
														this.config.columns[i].adjust = true;
													}
												}

												this.adjustColumn();
											}
										}
									},

								}
							
							}]

						}]
					},
				}]


			}
		]

	},
}, webix.ui.layout, webix.EventSystem);


function generateFilterViewForWindow() {
	var jsonObj;
	var mainView = [];
	var $deferred = new $.Deferred(); // Used for promise chaining
	var targetId;
	var objForSearchParam = JSON.stringify(objForSearch);
	$.LoadingOverlay("show", spinner);
	$.get("getSearchParameter?sessionKey=" + sessionKey + "&data=" + objForSearchParam, function (metadata) {
		var obj = JSON.parse(metadata);
		mainUIJson=obj;
		if (obj && obj.returncode == '1' && obj.data && obj.data.criteria) {
			jsonObj = obj.data.criteria;
			
			var numbeOfRows = jsonObj.layout.row;
			var rowComponents = jsonObj.parameter;
			for (i = 0; i < numbeOfRows.length; i++) {
				var t = i + 1;
				var rowData = [];
				/**
				 * Create Row
				 */
				var currentRow = {
					cols: numbeOfRows[i]['row'],
					height: 55,
				}
				mainView.push(currentRow);
				if ((numbeOfRows[i]['column'])) {
					var numberOfCols = jsonObj.layout.row[i].column;
					for (j = 0; j < numberOfCols.length; j++) {
						var viewId = numberOfCols[j]['columnid'];
					
						var eleIndex = rowComponents.findIndex(function (ele) {
							return ele.layoutid == viewId;
						});

						if (eleIndex > -1) {
							targetId = rowComponents[eleIndex]['layoutid'];
							
							if (viewId == targetId) {
								var currentObj = rowComponents[eleIndex];
								var showHideProperty;
								var columnWidthArr = jsonObj.layout.columnwidth;

								/**
								 * Gravity and column merging logic
								 */
								var gravityValue, uiElement;
								if (columnWidthArr[j]) {
									if (numberOfCols[j].mergewithcolumn > 0) {
										var modifiedGravity = 0;
										for (var s = 0; s <= numberOfCols[j].mergewithcolumn; s++) {
											if (columnWidthArr[s]) {
												modifiedGravity = modifiedGravity + parseInt(columnWidthArr[s]);
											}
										}
										gravityValue = modifiedGravity / 10;
									} else {
										gravityValue = columnWidthArr[j] / 10;
									}
								}
								/**
								 * Column Visibility logic
								 */
								if (currentObj.isvisible == '1') {
									showHideProperty = false;
								} else {
									showHideProperty = true;
								}
								/**
								 * Combo Box Case
								 */
								typeWindowObj[currentObj.key]=currentObj.displaytype;
								if (currentObj.displaytype == 'ComboBox') {
									idWindowObj[currentObj.key]=numberOfCols[j]['columnid'];
									allDataWindowTypeObj[currentObj.key]=numberOfCols[j]['columnid'];
									uiElement = {
										view: 'combo',
										id: 'id_brix_window_'+numberOfCols[j]['columnid'],
										gravity: gravityValue,
										css: 'brix_combo',
										labelPosition: "top",
										value: currentObj.defaultvalue,
										label: '<span title="' + resourceWindowObj[currentObj.key] + '">' + resourceWindowObj[currentObj.key] + '</span>',
										name: currentObj.key,
										hidden: showHideProperty,
										options: {
											on: {
												onBeforeShow: function () {}
											}
										},
										on: {

											onItemClick:function () {
												var currentEleId = $$(this).config.id;
												var currentKeyName = $$(this).config.name;
												var obj = {
													"key": currentKeyName,
													"searchname": objForSearch.searchname
												}
												obj = JSON.stringify(obj);
												$.get("getDataForParameter?sessionKey=" + sessionKey + "&data=" + obj, function (comboResponse) {
													comboResponse = JSON.parse(comboResponse)
													if (comboResponse && comboResponse.data) {
														$$(currentEleId).getList().parse(comboResponse.data)
													}
												});
											}
										}
									}
								}
								/**
								 * DateRange Case
								 */
								else if (currentObj.displaytype == 'DateRange') {
									idWindowObj[currentObj.key]=numberOfCols[j]['columnid'];
									dateWindowObj['id_window_datePicker_From_' + numberOfCols[j]['columnid']]=''
									dateWindowObj['id_window_datePicker_To_' + numberOfCols[j]['columnid']]=''
									uiElement = {
										gravity: gravityValue,
										hidden: showHideProperty,
										cols: [{
												view: "datepicker",
												placeholder: "From",
												css: 'brix_datepicker',
												id: 'id_window_datePicker_From_' + numberOfCols[j]['columnid'],
												labelPosition: "top",
												name: currentObj.key + '*_*' + 'from',
												format: webix.Date.dateToStr("%d/%m/%Y"),
												label: '<span title="' + resourceWindowObj[currentObj.key] + '">' + resourceWindowObj[currentObj.key] + '</span>',
												on: {
													onChange: function (newv) {
														var currentId = this.config.id;
														var modifiedId = '';
														if (currentId) {
															modifiedId = currentId.split('id_window_datePicker_From_')[1]
														}
														var newId = 'id_window_datePicker_To_' + modifiedId
														$$(newId).setValue("");
														$$(newId).getPopup().getBody().define("minDate", newv);
														$$(newId).refresh();
													}
												}
											},
											{
												view: "datepicker",
												placeholder: "to",
												id: 'id_window_datePicker_To_' + numberOfCols[j]['columnid'],
												css: 'brix_datepicker',
												labelPosition: "top",
												format: webix.Date.dateToStr("%d/%m/%Y"),
												name: currentObj.key + '*_*' + 'to',
												label: '<span class="brixSearchDateTextStyle">' + resourceWindowObj[currentObj.key] + '</span>',
											}
										]
									}
								}
								/**
								 * TextBox Case
								 */
								else if (currentObj.displaytype == 'TextBox') {
									allDataWindowTypeObj[currentObj.key]=numberOfCols[j]['columnid'];
									idWindowObj[currentObj.key]=numberOfCols[j]['columnid'];
									uiElement = {
										view: "text",
										id: 'id_brix_window_'+viewId,
										gravity: gravityValue,
										labelPosition: "top",
										name: currentObj.key,
										label: '<span title="' + resourceWindowObj[currentObj.key] + '">' + resourceWindowObj[currentObj.key] + '</span>',
										css: 'brix_textbox',
										hidden: showHideProperty,
									}
								}
								/**
								 * MultiComboBox Case
								 */
								else if (currentObj.displaytype == 'MultiComboBox') {
									idWindowObj[currentObj.key]=numberOfCols[j]['columnid'];
									allDataWindowTypeObj[currentObj.key]=numberOfCols[j]['columnid'];
									uiElement = {
										view: 'multicombo',
										id: 'id_brix_window_'+viewId,
										gravity: gravityValue,
										labelPosition: "top",
										css: 'brix_multi_combo comboEle',
										value: currentObj.defaultvalue,
										label: '<span title="' + resourceWindowObj[currentObj.key] + '">' + resourceWindowObj[currentObj.key] + '</span>',
										suggest: [],
										name: currentObj.key,
										hidden: showHideProperty,
										on: {
											onItemClick: function () {
												var currentEleId = $$(this).config.id;
												var currentKeyName = $$(this).config.name;
												var obj = {
													"key": currentKeyName,
													"searchname": objForSearch.searchname
												}
												obj = JSON.stringify(obj);
												$.get("getDataForParameter?sessionKey=" + sessionKey + "&data=" + obj, function (comboResponse) {
													comboResponse = JSON.parse(comboResponse)
													if (comboResponse && comboResponse.data) {
														$$(currentEleId).getList().parse(comboResponse.data);
													}
												});
											}
										}
									}
								}
								/**
								 * Date Case
								 */
								else if (currentObj.displaytype == 'Date') {
									idWindowObj[currentObj.key]=numberOfCols[j]['columnid'];
									allDataWindowTypeObj[currentObj.key]=numberOfCols[j]['columnid'];
									uiElement = {
										view: "datepicker",
										value: currentObj.defaultvalue,
										id: 'id_brix_window_'+viewId,
										labelPosition: "top",
										css: 'brix_datepicker',
										name: currentObj.key + "_" + "**",
										format: webix.Date.dateToStr("%d/%m/%Y"),
										label: '<span title="' + resourceWindowObj[currentObj.key] + '">' + resourceWindowObj[currentObj.key] + '</span>',
										gravity: gravityValue,
										hidden: showHideProperty,
									}
								}
								/**
								 * Create columns with data for row
								 */
								rowData.push(uiElement);
								currentRow.cols = rowData;
							}
						}

					}

				}

			}
		}
		$.LoadingOverlay("hide", spinner);
		$deferred.resolve(mainView);
	}).fail(function () {
		$.LoadingOverlay("hide", spinner);
		webix.alert('Faild to generate filter view');
		$deferred.resolve();

	});
	return $deferred.promise();
}

function applyWindowFilter(dataForFilter) {
	var arr = [];
	var filterObj = {};
	filterObj['criteria'] = {};

	for (var k in dataForFilter) {
		var obj = {};
		if (dataForFilter[k]) {
			// This condition is for from and to date
			if (k.indexOf('*_*') > -1) {
				var valueKey = k.split('*_*')[1];
				var keyVal = k.split('*_*')[0];
				var newKeyValue = valueKey + 'Date';
				var format = webix.Date.dateToStr("%d/%m/%Y");
				var dataVal = format(new Date(dataForFilter[k]));
				obj['key'] = keyVal;
				obj[newKeyValue] = dataVal;
				arr.push(obj);
			} else if (k.indexOf('_**') > -1) { // This condition is for date
				var format = webix.Date.dateToStr("%d/%m/%Y");
				var dataVal = format(new Date(dataForFilter[k]));
				obj['key'] =  k.split('_**')[0];
				obj['values'] = dataVal;
				arr.push(obj);
			} else {
				obj['key'] = k;
				obj['values'] = dataForFilter[k];
				arr.push(obj);
			}
		}
		filterObj['criteria']['parameter'] = arr;
	}
	filterObj['searchlimit'] = $$('id_brix_window_show_text_box').getValue();
	filterObj['searchName'] = objForSearch.searchname;
	filterObj['responsetype'] = 'Table';
	showTableViewForWindow(filterObj)
	$.LoadingOverlay("hide", spinner);
}

function showTableViewForWindow(filterObj){
	var obj;
	obj = JSON.stringify(filterObj);
	webix.ajax().get("getResultForTableView?sessionKey=" + sessionKey + "&data=" + obj, function (response) {
		response = JSON.parse(response)
		if (response && response.returncode == '1' && response.data && response.data.searchresult) {
			filterDatatableObj = response.data;
			$$('id_brix_window_search_table').clearAll();
			$$('id_brix_window_search_table').parse(response.data.searchresult);
			$.LoadingOverlay("hide", spinner);
		} else {
			$.LoadingOverlay("hide", spinner);
			webix.alert('Faild to load data table data ');
		}
	});
}

function showSaveFilterWindowView(dataForFilter) {
	webix.ui({
		view: "window",
		move: true,
		css: 'brix_window',
		id: 'id_brix_window_save_Filter_Window',
		headHeight: 28,
		position: "center",
		head: "<span class='brix_window_header_title'>"+resourceObj.SaveFilter_lbl+"</span><span class='brix_window_close' onclick='hideSaveFilterWindowView()'>×</span>",
		body: {
			view: "form",
			scroll: "auto",
			height: setHeightInPercentage(20),
			width: setWidthInPercentage(30),
			elements: [{
					height: 10
				},
				{
					view: "flexlayout",
					cols: [{
							view: 'label',
							label: resourceObj.FilterName_lbl+' :',
							css: 'brix_label',
							gravity: 0.4
						},
						{
							view: "text",
							id: 'id_brix_window_save_Filter_text',
							css: 'brix_textbox'
						}
					]
				},
				{

				},
				{
					view: "flexlayout",
					cols: [{
							view: "button",
							css: 'brix_button',
							icon: "form",
							label: "Cancel",
							gravity: 0.5,
							on: {
								onItemClick: function () {
									$$('id_brix_window_save_Filter_Window').hide();
								}
							},
						},
						{
							view: "button",
							css: 'brix_button',
							type: "form",
							label: "Save",
							gravity: 0.5,
							on: {
								onItemClick: function () {
									var comboArr = $$("id_brix_window_select_saved_filter").getList().data.pull;
									var textBoxValue = $$('id_brix_window_save_Filter_text').getValue();
									var notificationFlag = false;
									var modifiedComoArr = [];
									for (var i in comboArr) {
										modifiedComoArr.push(comboArr[i].value);
										if (comboArr[i].value == textBoxValue) {
											webix.alert('Entered template value already exists');
											notificationFlag = true;
											break
										}
									}
									if (!notificationFlag) {
										modifiedComoArr.push(textBoxValue);
										var arr = [];
										var filterObj = {};
										filterObj['criteria'] = {}
										for (var k in dataForFilter) {
											var obj = {};
											if (dataForFilter[k]) {
												if (k.indexOf('*_*') > -1) {
													var valueKey = k.split('*_*')[1];
													var keyVal = k.split('*_*')[0];
													var newKeyValue = valueKey + 'Date';
													var format = webix.Date.dateToStr("%d/%m/%Y");
													var dataVal = format(new Date(dataForFilter[k]));
													obj['key'] = keyVal;
													obj[newKeyValue] = dataVal;
													arr.push(obj);
												} else if (k.indexOf('_**') > -1) { // This condition is for date
													var format = webix.Date.dateToStr("%d/%m/%Y");
													var dataVal = format(new Date(dataForFilter[k]));
													obj['key'] =  k.split('_**')[0];
													obj['values'] = dataVal;
													arr.push(obj);
												}  else {
													obj['key'] = k;
													obj['values'] = dataForFilter[k];
													arr.push(obj);
												}
											}
											filterObj['criteria']['parameter'] = arr;

										}
										filterObj['searchName'] = objForSearch.searchname;
										filterObj['myfiltername'] = $$('id_brix_window_save_Filter_text').getValue();
										filterObj['responsetype'] = 'Table';
										
										$$('id_brix_window_select_saved_filter').getList().parse(modifiedComoArr)
										$$('id_brix_window_save_Filter_Window').hide();
										if(filterObj && filterObj.criteria && filterObj.criteria.parameter && filterObj.criteria.parameter.length>0){
											var modifiedObj=filterObj.criteria.parameter;
											for(var s in idWindowObj){
												for(var l=0;l<modifiedObj.length; l++){
													if(modifiedObj[l].key == s){
														modifiedObj[l].id=idWindowObj[s];
														modifiedObj[l].displaytype=typeWindowObj[s];
													}
												}
											}
										}
										var obj;
										obj = JSON.stringify(filterObj);
										webix.ajax().get("saveUserFilter?sessionKey=" + sessionKey + "&data=" + obj, function (saveFilterResponse) {
											saveFilterResponse = JSON.parse(saveFilterResponse)
											if(saveFilterResponse && saveFilterResponse.returncode=='1'){
												webix.alert('Filter templated updated successfully');
											}else{
												webix.alert('Failed to save filter template');
											}
										});
									}
								}
							},
						}
					]
				}
			]
		}
	}).show();
}
function hideSaveFilterWindowView() {
	$$('id_brix_window_save_Filter_Window').hide();
}
function getFilterObj(dataForFilter){
	var $deferred = new $.Deferred();
	var arr = [];
	var filterObj = {};
	filterObj['criteria'] = {};

	for (var k in dataForFilter) {
		var obj = {};
		if (dataForFilter[k]) {
			// This condition is for from and to date
			if (k.indexOf('*_*') > -1) {
				var valueKey = k.split('*_*')[1];
				var keyVal = k.split('*_*')[0];
				var newKeyValue = valueKey + 'Date';
				var format = webix.Date.dateToStr("%d/%m/%Y");
				var dataVal = format(new Date(dataForFilter[k]));
				obj['key'] = keyVal;
				obj[newKeyValue] = dataVal;
				arr.push(obj);
			} else if (k.indexOf('_**') > -1) { // This condition is for date
				var format = webix.Date.dateToStr("%d/%m/%Y");
				var dataVal = format(new Date(dataForFilter[k]));
				obj['key'] =  k.split('_**')[0];
				obj['values'] = dataVal;
				arr.push(obj);
			} else {
				obj['key'] = k;
				obj['values'] = dataForFilter[k];
				arr.push(obj);
			}
		}
		filterObj['criteria']['parameter'] = arr;
	}
	filterObj['searchlimit'] = $$('id_brix_window_show_text_box').getValue();
	filterObj['searchName'] = objForSearch.searchname;
	filterObj['responsetype'] = 'Table';
	$deferred.resolve(filterObj);
return $deferred.promise();
}
/*************************Popup code************************/

//function showFilterPopup(e){
//	webix.ui({
//	    view:"window",
//	    headHeight: 28,
//	    id:'id_brix_search_filter_window',
//        css: 'brix_window brix_filter_window',
//        position: 'center',
//        modal:true,
//        head: "<span class='brix_window_header_title'>Filter</span><span class='brix_window_close' onclick='closeFilterWindow()'>&times;</span>",
//	    body:{
//            view:'scrollview',
//            height: setHeightInPercentage(58),
//            width: setWidthInPercentage(60),
//            body: {
//				view:'brix_search_window_view',
//			}
//	    }
//	}).show();	
//}
//
//function closeFilterWindow(){
//	$$('id_brix_search_filter_window').hide();
//}
