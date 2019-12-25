var currentState;
/** ********* Need to set or remove following variables ********** */

var idObj = {};
var typeObj = {};
var dateObj = {};
var allDataTypeObj = {};
var mainUIJson;
var objForSearch = {
	"searchname": "Project",
	"searchtype": "simple"
}
var previewdata;
var size = 1000;
var userNameVal;
var nodes = null;
var edges = null;
var network = null;
var previewNetwork = null
var Pgroups = [];
var fid = null;
var selView;
var prevVertex;
webix.i18n.parseFormat = "%d/%m/%Y %H:%i";
webix.i18n.setLocale();
searchApp
	.controller(
		'BrixSearchController', [
			'$scope',
			'$compile',
			'$http',
			function ($scope, $compile, $http) {
				webix
					.protoUI({
							name: "brix_search_view",
							defaults: {
								margin: 0,
								padding: 0,
							},
							$init: function (config) {
								config.rows = [{ //Nav Bar
										view: 'template',
										template: "<div><div style='float:left;width:55%;display:inline-block'><div style='float:left'  class='iconContainer'><i class='webix_icon fa fa-line-chart iconCss'></i></div><div style='float:right'></div><span>Graph Search Portal</span></div><div style='float:right';width:45%;display:inline-block><span id='uName'></span><button type='button' class='htmlButtonStyle' title='Logout' onclick='logout()'><i class='fa fa-power-off'></i></button></div></div>",
										height: 40,
										css: "navCss",
										on: {
											"onAfterRender": webix.once(function () {
												//local storage for setting resource object to measurement tab 
												localStorage.setItem('resourceObj', resource);
												//for getting user name
												var form;
												webix.ajax().get("./search/getuser", function (text, xml, xhr) {
													var response = JSON.parse(text);
													if (response.username === "true") {
														window.location = "login";
													} else {
														var userNameVal = response.username;
														$("#uName").text("UserName : " + userNameVal);
													}
												});
											})
										},
									},
									{
										height: 40,
										css: 'templateStyle',
										cols: [{
											//Combo box(Project)
											view: "richselect",
											id: "filterCombo",
											css: 'brix_combo_1 searchComponetStyle ',
											placeholder: 'Select Search Type',
											width: 250,
											options: [],
											on: {
												"onAfterRender": webix.once(function () {
													$.LoadingOverlay("show", spinner);
													var popup = $$("filterCombo").getPopup();
													webix.html.addCss(popup.$view, "importListStyle");
													webix.ajax().sync().get("getAllSearchNames", function (response) {
														var response = JSON.parse(response);
														console.log(response);

														var list = $$("filterCombo").getPopup().getList();
														list.clearAll();
														list.parse(response.data);
														if (response.data != null) {
															$$("filterCombo").config.value = response.data[0];
															objForSearch = {
																"searchname": response.data[0],
																"searchtype": "simple"
															}
														}
														//															showTableView($scope, 'id_view_container', filterObj, response,tableResponse);
//														$.LoadingOverlay("hide", spinner);
													});
												}),
												"onChange": function (newv, oldv) {
													$.LoadingOverlay("show", spinner);
													if (newv == "Generic") {
//														$.LoadingOverlay("hide", spinner);
														$$("importbtn").hide();
														$$("filterAcc").hide();
														$$("filterButttonContainer").hide();
														$("#id_view_container").empty();
														webix.ui({
															container: "id_view_container",
															height: 700,
															view: "form",
															scroll: false,
															rows: [

																{
																	view: "text",
																	label: "Search"
																},
																{
																	view: "button",
																	label: "Run",
																	inputWidth: 100,
																	align: "center",
																	on: {
																		"onItemClick": function () {
																			generateOerryPreview();
																		}
																	}
																}, {
																	view: "template",
																	template: '<div id="querryPreviewContainer" style="height:800px"><div class="vis-network" tabindex="900" style="position: relative; overflow: hidden;touch-action: pan-y; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"><canvas width="800" style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"></canvas></div></div>',
																	//											    								css: 'graphContainerCss',
																}

															]
														}).show()
													} else {
														$$("importbtn").show()
														$$("filterAcc").show();
														$$("filterButttonContainer").show();
														$("#id_view_container").html();
														objForSearch = {
															"searchname": newv,
															"searchtype": "simple"
														}

														if ($$('id_brix_filter_added_view')) {
															mainView = [];
															$$(
																	'id_brix_filter_continer')
																.removeView(
																	'id_brix_filter_added_view');

														}
														generateFilterView()
															.done(
																function (
																	mainView) {
																	$$(
																			'id_brix_filter_continer')
																		.addView({
																			rows: mainView,
																			id: 'id_brix_filter_added_view'
																		})
																	currentState = window.location.href;
																	currentState = currentState
																		.split('#/')[1];
																	if (currentState &&
																		currentState
																		.indexOf('/')) {
																		currentState = currentState
																			.split('/')[0];
																	}
																});
														var dataForFilter = $$(
																'id_brix_filter_form')
															.getValues();
														$
															.LoadingOverlay(
																"show",
																spinner);
														applyFilter(
															dataForFilter,
															$scope,
															$compile,
															$http);
//														$.LoadingOverlay("hide", spinner);
													}
													$$("id_brix_search_view_selector_Combo").callEvent("onAfterRender",[]);
												}
											}
										}, {
											gravity: 7
										}, {
											css: "marginAdjustStyle",
											rows: [{
												//Import button
												id: "importbtn",
												view: "button",
												css: "launchBtnCss brix_button",
												type: "icon",
												icon: " fa fa-plus",
												label: resourceObj.importLbl,
												inputWidth: 90,
												popup: "importOptionList",
												tooltip: "Import",
											}]
										}]

									},
									{ //Accordion for FilterView
										view: 'accordion',
										id: "filterAcc",
										css: 'brix_accordion_1 brixAccordionStyle brixAccordionBackgroundStyle aa',
										multi: true,
										autoheight: true,
										rows: [{
											header: "<div class=''><span class='fa fa-filter smfViewIconCss'><span class='brix_filter_text_style'>" + resourceObj.Filter_lbl + "</span></div>",
											headerHeight: 30,
											css: "accordianPadding",
											collapsed: true,
											onClick: {
												"openFilters": function () {
													return false;
												}
											},
											body: {
												view: 'form',
												autoheight: true,
												padding: 0,
												id: 'id_brix_filter_form',
												elements: [{
													rows: [{
															rows: [],
															id: 'id_brix_filter_continer',
														},
														{
															view: 'template',
															template: '',
															height: 1,
															on: {
																onAfterRender: webix
																	.once(function () {
																		if ($$('id_brix_filter_added_view')) {
																			mainView = [];
																			$$(
																					'id_brix_filter_continer')
																				.removeView(
																					'id_brix_filter_added_view');

																		}
																		generateFilterView()
																			.done(
																				function (
																					mainView) {
																					$$(
																							'id_brix_filter_continer')
																						.addView({
																							rows: mainView,
																							id: 'id_brix_filter_added_view'
																						})
																					currentState = window.location.href;
																					currentState = currentState
																						.split('#/')[1];
																					if (currentState &&
																						currentState
																						.indexOf('/')) {
																						currentState = currentState
																							.split('/')[0];
																					}
																				});

																	})

															}
														},
														{
															height: 6,
															css: 'spacerStyle'
														},
														{
															css: 'brix_search_last_line brixSeachRowBackgroundStyle lastRowStyle',
															height: 40,
															cols: [{
																	gravity: 4
																},
																{
																	view: "text",
																	id: "id_show_text_box",
																	gravity: 2,
																	labelWidth: 100,
																	label: resourceObj.brix_search_lbl_show_result,
																	type: "text",
																	pattern: {
																		mask: "#####",
																		allow: /[0-9]/g
																	},
																	value: size,
																	css: 'brix_textbox_1 searchComponetStyle noBorderStyle',
																	on: {
																		onTimedKeyPress: function () {
																			var showTextBoxVal = $$(
																					'id_show_text_box')
																				.getValue();
																			if (showTextBoxVal > 10000) {
																				webix
																					.alert('Max 10000 value is allowed in show result field');
																				$$(
																						'id_show_text_box')
																					.setValue(
																						10000);
																			} else if (showTextBoxVal < 1) {

																				$$(
																						'id_show_text_box')
																					.setValue(
																						1);
																			}


																		},

																		onBlur: function () {


																			var showTextBoxVal = $$(
																					'id_show_text_box')
																				.getValue();
																			if (showTextBoxVal > 10000) {
																				webix
																					.alert('Max 10000 value is allowed in show result field');
																				$$(
																						'id_show_text_box')
																					.setValue(
																						1000);
																			} else if (showTextBoxVal < 1) {

																				$$(
																						'id_show_text_box')
																					.setValue(
																						1);
																			}
																			size = $$('id_show_text_box').getValue();
																		}
																	}
																},
																//Combo For Select Filter
																/*{
																	view : "combo",
																	gravity : 3,
																	id : 'id_select_saved_filter',
																	options : [],
																	css:'brix_combo_1 searchComponetStyle',
																	placeholder : resourceObj.brix_search_lbl_select_filter,
																	on : {
																		onItemClick : function() {
																			var currentEleId = $$(this).config.id;
																			var obj = {}
																			// parameterName
																			obj['myfiltername'] = $$(
																			'id_select_saved_filter')
																			.getValue();
																			obj['searchName'] = objForSearch.searchname
																			obj = JSON
																			.stringify(obj);
																			$
																			.get(
																					"getSavedCriteria?sessionKey="
																					+ sessionKey
																					+ "&data="
																					+ obj,
																					function(
																							comboResponse) {
																						comboResponse = JSON
																						.parse(comboResponse)
																						if (comboResponse
																								&& comboResponse.returncode
																								&& comboResponse.returncode == '1'
																								&& comboResponse.data) {
																							$$(
																							'id_select_saved_filter')
																							.getList()
																							.parse(
																									comboResponse.data)
																						}
																					});
																			
																			$.ajax({
																				  type: "POST",
																				  url: "getSavedCriteria",
																				  data: obj,
																				  contentType:"application/json; charset=utf-8",
																				  dataType:"json",
																				  success: function(comboResponse){
																					  comboResponse = comboResponse
																						if (comboResponse
																								&& comboResponse.returncode
																								&& comboResponse.returncode == '1'
																								&& comboResponse.data) {
																							$$(
																							'id_select_saved_filter')
																							.getList()
																							.parse(
																									comboResponse.data)
																						}
																				  },
																				  error: function(XMLHttpRequest, textStatus, errorThrown) {
																					  $.LoadingOverlay("hide", spinner);
																				}
																			})
																		},
																		onChange : function() {
																			var currentEleId = $$(this).config.id;
																			var obj = {}
																			// parameterName
																			obj['myfiltername'] = $$(
																			'id_select_saved_filter')
																			.getValue();
																			obj['searchName'] = objForSearch.searchname
																			obj = JSON
																			.stringify(obj);
																			$
																			.get(
																					"getSavedFilterParameters?sessionKey="
																					+ sessionKey
																					+ "&data="
																					+ obj,
																					function(
																							response) {
																						response = JSON
																						.parse(response)
																						if (response
																								&& response.returncode
																								&& response.returncode == '1'
																								&& response.data
																								&& response.data.length > 0) {
																							var modifiedData = response.data;
																							// Clear
																							// all
																							// fields
																							// related
																							// to
																							// All
																							// date
																							// type
																							// except
																							// date
																							for ( var s in allDataTypeObj) {
																								if (allDataTypeObj[s]) {
																									$$(
																											allDataTypeObj[s])
																											.setValue(
																													'');
																								}
																							}
																							// Clear
																							// all
																							// date
																							// fields
																							for ( var j in dateObj) {
																								if (dateObj[j]) {
																									$$(
																											j)
																											.setValue(
																													'');
																								}
																							}

																							for (var k = 0; k < modifiedData.length; k++) {
																								if (modifiedData[k].displaytype == 'ComboBox'
																									|| modifiedData[k].displaytype == 'MultiComboBox') {
																									var eleId = modifiedData[k].id;
																									var filterDataVal = modifiedData[k].values;
																									var multiComboSelection = '';
																									var arr = [];
																									for (var p = 0; p < filterDataVal.length; p++) {
																										if (multiComboSelection == '') {
																											multiComboSelection = filterDataVal[p];
																										} else {
																											multiComboSelection = multiComboSelection
																											+ ','
																											+ filterDataVal[p]
																										}
																										arr
																										.push(filterDataVal[p]);
																									}
																									if (modifiedData[k].displaytype == 'MultiComboBox') {
																										if (eleId) {
																											$$(
																													eleId)
																													.define(
																															"options",
																															{
																																data : arr,
																															});
																											$$(
																													eleId)
																													.setValue(
																															multiComboSelection);
																											$$(
																													eleId)
																													.refresh();
																										}
																									} else {
																										if (eleId) {
																											$$(
																													eleId)
																													.define(
																															"options",
																															{
																																data : arr,
																															});
																											$$(
																													eleId)
																													.setValue(
																															arr[0]);
																											$$(
																													eleId)
																													.refresh();
																										}
																									}
																								} else if (modifiedData[k].displaytype == 'DateRange') {
																									if (modifiedData[k].fromDate) {
																										var filterDataVal = modifiedData[k].fromDate;
																										var eleId = 'id_datePicker_From_'
																											+ modifiedData[k].id;

																										var format = webix.Date
																										.dateToStr("%d/%m/%Y");
																										var dataVal = format(new Date(
																												filterDataVal));
																										if (eleId) {
																											$$(
																													eleId)
																													.setValue(
																															dataVal)
																										}
																									}
																									if (modifiedData[k].toDate) {
																										var filterDataVal = modifiedData[k].toDate;
																										var eleId = 'id_datePicker_To_'
																											+ modifiedData[k].id;
																										var format = webix.Date
																										.dateToStr("%d/%m/%Y");
																										var dataVal = format(new Date(
																												filterDataVal));
																										if (eleId) {
																											$$(
																													eleId)
																													.setValue(
																															filterDataVal)
																										}
																									}

																								} else if (modifiedData[k].displaytype == 'Date') {
																									var eleId = modifiedData[k].id;
																									if (eleId) {
																										$$(
																												eleId)
																												.setValue(
																														modifiedData[k].values[0])
																									}
																								} else {
																									var eleId = modifiedData[k].id;
																									if (eleId) {
																										$$(
																												eleId)
																												.setValue(
																														modifiedData[k].values[0])
																									}
																								}
																							}
																							var dataForFilter = $$(
																							'id_brix_filter_form')
																							.getValues();
																							applyFilter(
																									dataForFilter,
																									$scope,
																									$compile,
																									$http);
																						}
																					});
																		}
																	}
																},*/
																{
																	view: "button",
																	value: resourceObj.brix_search_lbl_clear_filter,
																	id: "clearFilterBtn",
																	type: "form",
																	css: 'brix_button',
																	autowidth: true,
																	gravity: 0.5,
																	on: {
																		onItemClick: function () {
																			$$(
																					'id_brix_filter_form')
																				.clear();
																			/*$$(
																			'id_select_saved_filter')
																			.setValue(
																			'');*/
																			$$('id_show_text_box').setValue(1000);
																			$$('id_show_text_box').refresh();
																			var dataForFilter = $$(
																					'id_brix_filter_form')
																				.getValues();
																			$
																				.LoadingOverlay(
																					"show",
																					spinner);
																			applyFilter(
																				dataForFilter,
																				$scope,
																				$compile,
																				$http);

																		}
																	},
																},
																{
																	view: "button",
																	value: resourceObj.brix_search_lbl_apply_filter,
																	type: "form",
																	css: 'brix_button',
																	autowidth: true,
																	gravity: 0.5,
																	on: {
																		onItemClick: function () {
																			var dataForFilter = $$(
																					'id_brix_filter_form')
																				.getValues();
																			$
																				.LoadingOverlay(
																					"show",
																					spinner);
																			applyFilter(
																				dataForFilter,
																				$scope,
																				$compile,
																				$http);
																		}
																	},
																}
																//save filter button
																/*,
																{
																	view : "button",
																	value : resourceObj.brix_search_lbl_save_filter,
																	type : "form",
																	css : 'brix_button',
																	autowidth : true,
																	gravity : 0.5,
																	on : {
																		onItemClick : function() {
																			var dataForFilter = $$(
																					'id_brix_filter_form')
																					.getValues();
																			showSaveFilterWindow(
																					dataForFilter,
																					$scope,
																					$compile,
																					$http);
																		}
																	},
																}*/
															]

														}
													]

												}]
											},
										}],
									},
									{
										view: 'accordion',
										css: 'brix_accordion brix_accordion_second brixAccordionStyle accordianPadding ',
										multi: false,
										autoheight: true,
										//css:"accordianPadding",
										rows: [{
											headerHeight: 30,
											body: {
												view: 'form',
												autoheight: true,
												padding: 0,
												css: 'brix_accordion_body',
												elements: [{
													rows: [{
															id: "filterButttonContainer",
															autoheight: true,
															css: 'brixSeachRowBackgroundStyle',
															cols: [{
																	view: "button", //Refresh button
																	css: 'brix_button',
																	type: "icon",
																	icon: "fa fa-refresh",
																	label: resourceObj.brix_search_lbl_refresh,
																	autowidth: true,
																	gravity: 0.5,
																	tooltip: "Refresh",
																	on: {
																		onItemClick: function () {
																			$
																				.LoadingOverlay(
																					"show",
																					spinner);
																			$$(
																					"id_brix_search_table")
																				.eachColumn(
																					function (
																						id,
																						col) {
																						var filter = this
																							.getFilter(id);
																						if (filter) {
																							if (filter.setValue)
																								filter
																								.setValue("")
																							else
																								filter.value = "";
																						}
																					});
																			var dataForFilter = $$('id_brix_filter_form').getValues();

																			applyFilter(
																				dataForFilter,
																				$scope,
																				$compile,
																				$http);
//																			$
//																				.LoadingOverlay(
//																					"hide",
//																					spinner);
																		}
																	},
																},
																{
																	view: 'richselect', //select view
																	id: 'id_brix_search_view_selector_Combo',
																	gravity: 2,
																	width: 150,
																	css: 'brix_combo_1 searchComponetStyle',
//																	value: resourceObj.TableView_lbl,
																	tooltip: false,
																/*	options: [
																		resourceObj.TableView_lbl,
																		resourceObj.TreeView_lbl,
																	],*/
																	on: {
																		onChange: function (
																			newVal) {
																			selView = newVal.value;
																			var dataForFilter = $$(
																					'id_brix_filter_form')
																				.getValues();
																			$
																				.LoadingOverlay(
																					"show",
																					spinner);
																			applyFilter(
																				dataForFilter,
																				$scope,
																				$compile,
																				$http);
																		},
																		onAfterRender: function () {
																		
																			$.ajax({
																				type: "POST",
																				url: "getResultViewList",
																				data: objForSearch.searchname,
																				async: false,
																				contentType: "application/json; charset=utf-8",
																				dataType: "json",
																				success: function (comboResponse) {
																					console.log(comboResponse);
																					comboResponse.data.forEach(function(data){
																						data.value=resourceObj[data.key];
																					});
																					console.log(comboResponse);
																					$$("id_brix_search_view_selector_Combo").define("options",comboResponse.data);
																					$$("id_brix_search_view_selector_Combo").setValue(comboResponse.data[0]);
																				},
																				error: function (XMLHttpRequest, textStatus, errorThrown) {
																					$.LoadingOverlay("hide", spinner);
																				}
																			})
																				var popup = $$("id_brix_search_view_selector_Combo").getPopup();
																			webix.html.addCss(popup.$view, "importListStyle");
																			
																			var sendHeartbeattimer = setInterval(function () {
																				$.ajax({
																					type: "POST",
																					url: "sendHeartBeat",
																					sessionkey: sessionKey,
																					contentType: "application/json; charset=utf-8",
																					dataType: "json",
																					success: function (response) {
																						//																						console.log(response);
																						if (response.returncode == "403" || response.returnCode == "403") {
																							sessionOutPopup();
																							$$("session_Out_Popup").show()
																						}
																					},
																					error: function (XMLHttpRequest, textStatus, errorThrown) {
																						$.LoadingOverlay("hide", spinner);
																						clearInterval(sendHeartbeattimer);
																						webix.modalbox({
																							title: "Connection",
																							buttons: [],
																							text: "Connection server has been lost, Please check server connection",
																							width: 500
																						});

																					}
																				});
																			}, 15000);

																		}
																	}
																},
																{
																	gravity: 2.5
																}
																/*,
																{	//Pager
																	view : "pager",	
																	id : 'id_brix_table_filter_pager',
																	css : "pagerCntrl",
																	align : "right",
																	template : "{common.prev()} {common.pages()} {common.next()}",
																	size : 20,
																	group : 5,
																}*/

															]
														},
														{ //Container for Table & Tree View
															view: "template",
															css: "treeTableViewContainer",
															template: '<div id="id_view_container"></div>',
															on: {
																"onAfterRender": webix
																	.once(function () {
																		var dataForFilter = $$(
																				'id_brix_filter_form')
																			.getValues();
																		$
																			.LoadingOverlay(
																				"show",
																				spinner);
																		applyFilter(
																			dataForFilter,
																			$scope,
																			$compile,
																			$http);
																	})
															}
														}
													]

												}]
											},
										}]

									}
								]

							},
						}, webix.ui.layout,
						webix.EventSystem);
				$scope.name = 'tejal';

				$scope.activateBrixSerchViewContainerUI = function (
					ele) {
					$compile(ele.contents())($scope);
				}
				$scope.activateBrixSerchTreeViewContainerUIList = function (
					ele) {
					$compile(ele.contents())($scope);
				}
				webix.ui({
					view: 'brix_search_view',
					container: 'id_brix_search_controller'
				})
				$scope.activateBrixSerchTreeViewContainerUI = function (
					ele) {
					$compile(ele.contents())($scope);
				}
				/**
				 * @purpose Intentionally added event listener here,
				 *          in order to send send scope and
				 *          compiler.
				 */
				setTimeout(function () {
					if (document.getElementById("id_filter_menu")) {
						document.getElementById("id_filter_menu")
							.addEventListener(
								"click",
								function (e) {
									showFilterList(this,
										$scope, $compile)
								});
					}
				}, 1000);
				webix.ui({
					view: "popup",
					id: "importOptionList",
					css: "brix_list importListStyle",
					width: 200,
					body: {
						id: "importlist",
						view: "list",
						data: [{
								id: "1",
								name: resourceObj.graphmlLbl,
								extention: "graphml"
							},
							{
								id: "2",
								name: resourceObj.UMLXMILbl,
								extention: "xml"
							},
							{
								id: "3",
								name: resourceObj.RDFLbl,
								extention: "rdf"
							},
						],
						template: "#name#",
						autoheight: true,
						select: true,
						on: {
							"onItemClick": function (id) {
								$$("importOptionList").hide();
								$$('importlist').unselectAll();
								importWindow1($$("importlist").getItem(id).extention, $$("importlist").getItem(id).name);
								$$("importWindow1").show();
							}
						}
					}
				});
			}
		]);

/**
 * @purpose This function shows save filter window
 * @param dataForFilter
 *            used for save filter object and scope
 * @author Tejal
 */
function applyFilter(dataForFilter, $scope, $compile, $http) {
	$.LoadingOverlay("show", spinner);
	var arr = [];
	var filterObj = {};
	filterObj['criteria'] = {}
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
				obj['key'] = k.split('_**')[0];
				obj['date'] = dataVal;
				arr.push(obj);
			} else {
				obj['key'] = k;
				obj['values'] = dataForFilter[k];
				arr.push(obj);
			}
		}
		filterObj['criteria']['parameter'] = arr;
	}
	filterObj['searchlimit'] = $$('id_show_text_box').getValue();
	filterObj['searchName'] = objForSearch.searchname;
	var id = $$('id_brix_search_view_selector_Combo').getValue();
	var option = $$('id_brix_search_view_selector_Combo').getList().getItem(id);
	if(option.type)
	filterObj['responsetype'] = option.type;
	filterObj['key'] = option.key;
/*	if ($$('id_brix_search_view_selector_Combo').getValue() == resourceObj.TableView_lbl) {
		filterObj['responsetype'] = 'Table';
	} else if ($$('id_brix_search_view_selector_Combo').getValue() == resourceObj.BubbleView_lbl) {
		filterObj['responsetype'] = 'Bubble';
	} else {
		filterObj['responsetype'] = 'Tree';
	}*/
	
	if (filterObj['responsetype'] == 'Tree') {
		$('#id_view_container').html('');
		$('.pagerCntrl').hide();
		showtreeView($scope, 'id_view_container', filterObj, $compile, $http,option.key);
		//		$.LoadingOverlay("hide", spinner);
	} else if (filterObj['responsetype'] == 'Table') {
		$('#id_view_container').html('');
		$('.pagerCntrl').show();
		var obj = {}
		obj['searchName'] = filterObj['searchName'];
		obj['key'] = filterObj['key'];
		obj = JSON.stringify(obj);
		$.ajax({
			type: "POST",
			url: "getColumnNamesForResult",
			data: obj,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (res1) {
				$('#id_view_container').html('');
				var response = res1;
				var modifiedObj;
				modifiedObj = JSON.stringify(filterObj);
				$.ajax({
					type: "POST",
					url: "getResultForTableView",
					data: modifiedObj,
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					success: function (tableResponse) {
						if (tableResponse.returncode == "403") {
							sessionOutPopup();
							$$("session_Out_Popup").show()
						}
						var tableResponse = tableResponse;
						if (tableResponse.data.searchresult.length == 0) {
							webix.alert({
								title: "No Result",
								autoheight: true,
								autowidth: true,
								ok: "OK",
								text: resourceObj.brix_lbl_noResultFound,
								callback: function (result) {

									//$$("clearFilterBtn").callEvent("onItemClick", []);
								}
							});

						}
						// $('body').css('overflow', 'visible');
						showTableView($scope, 'id_view_container', filterObj, response, tableResponse);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						$.LoadingOverlay("hide", spinner);
					}
				})
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$.LoadingOverlay("hide", spinner);
			}
		});
//		$.LoadingOverlay("hide", spinner);
	}

}

/**
 * @purpose This function shows save filter window
 * @param dataForFilter
 *            used for save filter object
 * @author Tejal
 */
function showSaveFilterWindow(dataForFilter, $scope, $compile) {
	webix
		.ui({
			view: "window",
			move: true,
			css: 'brix_window',
			id: 'id_save_Filter_Window',
			headHeight: 28,
			position: "center",
			head: "<span class='brix_window_header_title'>" + resourceObj.SaveFilter_lbl + "</span><span class='brix_window_close' onclick='hideSaveFilterWindow()'>×</span>",
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
							label: resourceObj.FilterName_lbl + ' :',
							css: 'brix_label',
							gravity: 0.4
						}, {
							view: "text",
							id: 'id_save_Filter_text',
							css: 'brix_textbox_1'
						}]
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
										$$(
												'id_save_Filter_Window')
											.hide();
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
										var comboArr = $$(
												"id_select_saved_filter")
											.getList().data.pull;
										var textBoxValue = $$(
												'id_save_Filter_text')
											.getValue();
										var notificationFlag = false;
										var modifiedComoArr = [];
										for (var i in comboArr) {
											modifiedComoArr
												.push(comboArr[i].value);
											if (comboArr[i].value == textBoxValue) {
												webix
													.alert('Entered template value already exists');
												notificationFlag = true;
												break
											}
										}
										if (!notificationFlag) {
											modifiedComoArr
												.push(textBoxValue);
											var arr = [];
											var filterObj = {};
											filterObj['criteria'] = {}
											for (var k in dataForFilter) {
												var obj = {};
												if (dataForFilter[k]) {
													if (k
														.indexOf('*_*') > -1) {
														var valueKey = k
															.split('*_*')[1];
														var keyVal = k
															.split('*_*')[0];
														var newKeyValue = valueKey +
															'Date';
														var format = webix.Date
															.dateToStr("%d/%m/%Y");
														var dataVal = format(new Date(
															dataForFilter[k]));
														obj['key'] = keyVal;
														obj[newKeyValue] = dataVal;
														arr
															.push(obj);
													} else if (k
														.indexOf('_**') > -1) { // This
														// condition
														// is
														// for
														// date
														var format = webix.Date
															.dateToStr("%d/%m/%Y");
														var dataVal = format(new Date(
															dataForFilter[k]));
														obj['key'] = k
															.split('_**')[0];
														obj['values'] = dataVal;
														arr
															.push(obj);
													} else {
														obj['key'] = k;
														obj['values'] = dataForFilter[k];
														arr
															.push(obj);
													}
												}
												filterObj['criteria']['parameter'] = arr;

											}
											filterObj['searchName'] = objForSearch.searchname;
											filterObj['myfiltername'] = $$(
													'id_save_Filter_text')
												.getValue();
											if ($$(
													'id_brix_search_view_selector_Combo')
												.getValue() == resourceObj.TableView_lbl) {
												filterObj['responsetype'] = 'Table';
											} else {
												filterObj['responsetype'] = 'Tree';
											}

											$$(
													'id_select_saved_filter')
												.getList()
												.parse(
													modifiedComoArr)
											$$(
													'id_save_Filter_Window')
												.hide();
											if (filterObj &&
												filterObj.criteria &&
												filterObj.criteria.parameter &&
												filterObj.criteria.parameter.length > 0) {
												var modifiedObj = filterObj.criteria.parameter;
												for (var s in idObj) {
													for (var l = 0; l < modifiedObj.length; l++) {
														if (modifiedObj[l].key == s) {
															modifiedObj[l].id = idObj[s];
															modifiedObj[l].displaytype = typeObj[s];
														}
													}
												}
											}
											var obj;
											obj = JSON
												.stringify(filterObj);
											/*webix
											.ajax()
											.get(
													"saveUserFilter?sessionKey="
															+ sessionKey
															+ "&data="
															+ obj,
															function(
																	saveFilterResponse) {
																saveFilterResponse = JSON
																.parse(saveFilterResponse)
																if (saveFilterResponse
																		&& saveFilterResponse.returncode == '1') {
																	webix
																	.alert('Filter templated updated successfully');
																} else {
																	webix
																	.alert('Failed to save filter template');
																}
															});*/
											$.ajax({
												type: "POST",
												url: "saveUserFilter",
												data: obj,
												contentType: "application/json; charset=utf-8",
												dataType: "json",
												success: function (saveFilterResponse) {
													saveFilterResponse = saveFilterResponse
													if (saveFilterResponse.returncode == "403") {
														sessionOutPopup();
														$$("session_Out_Popup").show()
													}
													if (saveFilterResponse &&
														saveFilterResponse.returncode == '1') {
														webix
															.alert('Filter templated updated successfully');
													} else {
														webix
															.alert('Failed to save filter template');
													}
												},
												error: function (XMLHttpRequest, textStatus, errorThrown) {
													$.LoadingOverlay("hide", spinner);
												}
											})
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
/**
 * @purpose Closes save filter window
 * @author Tejal
 */
function hideSaveFilterWindow() {
	$$('id_save_Filter_Window').hide();
}
/**
 * @purpose This function is used get width of browser and convert it into
 *          percentage
 * @param percentage
 * @author Tejal
 */
function setWidthInPercentage(percentage) {
	return width = (screen.width / 100) * percentage;
}
/**
 * @purpose This function is used get height of browser and convert it into
 *          percentage
 * @param percentage
 * @author Tejal
 */
function setHeightInPercentage(percentage) {
	return height = (screen.height / 100) * percentage;
}

/**
 * @purpose This function is used show filter options
 * @param event
 * @author Tejal
 */
function showFilterList(e, $scope, $compile) {
	var dataforList;
	/**
	 * @purpose Following condition added to preserve the filter selection
	 */
	if (objForSearch.searchtype == 'Simple') {
		dataforList = [{
				id: "id_simple_filter",
				title: "<span class='fa fa-toggle-on simple_filter_style' style='padding-right: 3px;'></span>" +
					resourceObj.brix_search_lbl_simple_filter + ""
			},
			{
				id: "id_advance_filter",
				title: "<span class='fa fa-toggle-off advance_filter_style' style='padding-right: 3px;'></span>" +
					resourceObj.brix_search_lbl_advance_Filter + ""
			}
		]
	} else {
		dataforList = [{
				id: "id_simple_filter",
				title: "<span class='fa fa-toggle-off simple_filter_style' style='padding-right: 3px;'></span>" +
					resourceObj.brix_search_lbl_simple_filter + ""
			},
			{
				id: "id_advance_filter",
				title: "<span class='fa fa-toggle-on advance_filter_style' style='padding-right: 3px;'></span>" +
					resourceObj.brix_search_lbl_advance_Filter + ""
			}
		]
	}

	webix
		.ui({
			view: "popup",
			id: 'id_filter_list_popup',
			css: '',
			width: 130,
			height: 70,
			padding: 0,
			body: {
				view: "list",
				id: "id_list_of_filter",
				width: 140,
				height: 70,
				template: "#title#",
				select: true,
				css: 'filtersListCss',
				scroll: false,
				data: dataforList,
				on: {
					"onItemClick": function (id) {
						$.LoadingOverlay("show", spinner);
						if (id == 'id_simple_filter') {
							var data = [{
									id: "id_simple_filter",
									title: "<span class='fa fa-toggle-on simple_filter_style' style='padding-right: 3px;'></span>" +
										resourceObj.brix_search_lbl_simple_filter +
										""
								},
								{
									id: "id_advance_filter",
									title: "<span class='fa fa-toggle-off advance_filter_style' style='padding-right: 3px;'></span>" +
										resourceObj.brix_search_lbl_advance_Filter +
										""
								}
							];
							objForSearch.searchtype = 'Simple';
							$$('id_list_of_filter').clearAll();
							$$('id_list_of_filter').parse(data);
							$$('id_filter_list_popup').hide();
						} else if (id == 'id_advance_filter') {
							var data = [{
									id: "id_simple_filter",
									title: "<span class='fa fa-toggle-off simple_filter_style' style='padding-right: 3px;'></span>" +
										resourceObj.brix_search_lbl_simple_filter +
										""
								},
								{
									id: "id_advance_filter",
									title: "<span class='fa fa-toggle-on advance_filter_style' style='padding-right: 3px;'></span>" +
										resourceObj.brix_search_lbl_advance_Filter +
										""
								}
							];
							objForSearch.searchtype = 'Advanced';
							$$('id_list_of_filter').clearAll();
							$$('id_list_of_filter').parse(data);
							$$('id_filter_list_popup').hide();
						}
						/**
						 * @purpose If view exists then remove
						 */
						if ($$('id_brix_filter_added_view')) {
							mainView = [];
							$$('id_brix_filter_continer')
								.removeView(
									'id_brix_filter_added_view');
						}
						/**
						 * @purpose Fetch and add new view
						 */
						generateFilterView()
							.done(
								function (mainView) {
									$$(
											'id_brix_filter_continer')
										.addView({
											rows: mainView,
											id: 'id_brix_filter_added_view'
										})
									var dataForFilter = $$(
											'id_brix_filter_form')
										.getValues();
									applyFilter(
										dataForFilter,
										$scope,
										$compile);

								});

					},
				}
			}
		}).show(e);
}

/**
 * @purpose This function is used o create the view from the object recievd from
 *          getSearchParameter service
 * @param event
 * @author Tejal
 */
var mainUIJson;

function generateFilterView() {
	var jsonObj;
	var mainView = [];
	var $deferred = new $.Deferred(); // Used for promise chaining
	var targetId;
	var objForSearchParam = JSON.stringify(objForSearch);
	$.LoadingOverlay("show", spinner);
	//	$
	//	.get(
	//			"getSearchParameter?sessionKey=" + sessionKey + "&data="
	//			+ objForSearchParam,
	//			function(metadata) {
	$.ajax({
		type: "POST",
		url: "getSearchParameter",
		data: objForSearchParam,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (metadata) {
			//var obj = JSON.parse(metadata);
			var obj = metadata;
			mainUIJson = obj;
			//				console.log('obj::'+JSON.stringify(obj))
			if (metadata.returncode == "403") {
				sessionOutPopup();
				$$("session_Out_Popup").show()
			}
			if (obj && obj.returncode == '1' && obj.data &&
				obj.data.criteria) {
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
						height: 60,
					}
					var blankRow = {
						height: 8,
						css: 'blankRowStyle'
					}
					mainView.push(currentRow);
					mainView.push(blankRow);
					if ((numbeOfRows[i]['column'])) {
						var numberOfCols = jsonObj.layout.row[i].column;
						for (j = 0; j < numberOfCols.length; j++) {
							var viewId = numberOfCols[j]['columnid'];

							//								var eleIndex = rowComponents
							//								.findIndex(function(ele) {
							//									return ele.layoutid == viewId;
							//								});
							var eleIndex;
							for (var k = 0; k < rowComponents.length; k++) {
								if (rowComponents[k].layoutid == viewId) {
									eleIndex = k;
								}
							}

							if (eleIndex > -1) {
								targetId = rowComponents[eleIndex]['layoutid'];

								if (viewId == targetId) {
									var currentObj = rowComponents[eleIndex];
									var showHideProperty;
									var columnWidthArr = jsonObj.layout.columnwidth;

									/**
									 * Gravity and column merging
									 * logic
									 */
									var gravityValue, uiElement;
									if (columnWidthArr[j]) {
										if (numberOfCols[j].mergewithcolumn > 0) {
											var modifiedGravity = 0;
											for (var s = 0; s <= numberOfCols[j].mergewithcolumn; s++) {
												if (columnWidthArr[s]) {
													modifiedGravity = modifiedGravity +
														parseInt(columnWidthArr[s]);
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

									typeObj[currentObj.key] = currentObj.displaytype;
									if (currentObj.displaytype == 'ComboBox') {
										idObj[currentObj.key] = numberOfCols[j]['columnid'];
										allDataTypeObj[currentObj.key] = numberOfCols[j]['columnid'];
										uiElement = {
											view: 'richselect',
											id: numberOfCols[j]['columnid'],
											gravity: gravityValue,
											//labelWidth:150,
											css: 'brix_combo_1 searchComponetStyle',
											placeholder: resourceObj.Combo_msg,
											labelPosition: "top",
											value: currentObj.defaultvalue,
											label: '<span title="' +
												resourceObj[currentObj.key] +
												'">' +
												resourceObj[currentObj.key] +
												'</span>',
											name: currentObj.key,
											hidden: showHideProperty,
											options: {
												on: {
													onBeforeShow: function () {}
												}
											},
											on: {
												onAfterRender: function () {
													var popup = this.getPopup();
													webix.html.addCss(popup.$view, "importListStyle");
												},
												onItemClick: function () {
													var currentEleId = $$(this).config.id;
													var currentKeyName = $$(this).config.name;
													var obj = {
														"key": currentKeyName,
														"searchname": objForSearch.searchname
													}
													obj = JSON
														.stringify(obj);
													//													$
													//													.get(
													//															"getDataForParameter?sessionKey="
													//															+ sessionKey
													//															+ "&data="
													//															+ obj,
													//															function(
													//																	comboResponse) {
													//																comboResponse = JSON
													//																.parse(comboResponse)
													//																if (comboResponse
													//																		&& comboResponse.data) {
													//																	$$(
													//																			currentEleId)
													//																			.getList()
													//																			.parse(
													//																					comboResponse.data)
													//																}
													//															});
													$.ajax({
														type: "POST",
														url: "getDataForParameter",
														data: obj,
														contentType: "application/json; charset=utf-8",
														dataType: "json",
														success: function (comboResponse) {
															if (comboResponse.returncode == "403") {
																sessionOutPopup();
																$$("session_Out_Popup").show()
															}
															if (comboResponse && comboResponse.data) {
																$$(currentEleId).getList().parse(comboResponse.data)
															}
														},
														error: function (XMLHttpRequest, textStatus, errorThrown) {
															$.LoadingOverlay("hide", spinner);
														}
													})

												}
											}

										}
									}
									/**
									 * DateRange Case
									 */
									else if (currentObj.displaytype == 'DateRange') {
										idObj[currentObj.key] = numberOfCols[j]['columnid'];
										dateObj['id_datePicker_From_' +
											numberOfCols[j]['columnid']] = ''
										dateObj['id_datePicker_To_' +
											numberOfCols[j]['columnid']] = ''
										uiElement = {
											gravity: gravityValue,
											hidden: showHideProperty,
											placeholder: 'Select Date',
											//labelWidth:150,
											cols: [{
													view: "datepicker",
													placeholder: "From",
													css: 'brix_datepicker_1 searchComponetStyle',
													id: 'id_datePicker_From_' +
														numberOfCols[j]['columnid'],
													labelPosition: "top",
													name: currentObj.key +
														'*_*' +
														'from',
													format: webix.Date
														.dateToStr("%d/%m/%Y"),
													label: '<span title="' +
														resourceObj[currentObj.key] +
														'">' +
														resourceObj[currentObj.key] +
														'</span>',
													on: {
														onChange: function (
															newv) {
															var currentId = this.config.id;
															var modifiedId = '';
															if (currentId) {
																modifiedId = currentId
																	.split('id_datePicker_From_')[1]
															}
															var newId = 'id_datePicker_To_' +
																modifiedId
															$$(
																	newId)
																.setValue(
																	"");
															$$(
																	newId)
																.getPopup()
																.getBody()
																.define(
																	"minDate",
																	newv);
															$$(
																	newId)
																.refresh();
														}
													}
												},
												{
													view: "datepicker",
													placeholder: "to",
													id: 'id_datePicker_To_' +
														numberOfCols[j]['columnid'],
													css: 'brix_datepicker_1 searchComponetStyle',
													labelPosition: "top",
													format: webix.Date
														.dateToStr("%d/%m/%Y"),
													name: currentObj.key +
														'*_*' +
														'to',
													label: '<span class="brixSearchDateTextStyle">' +
														resourceObj[currentObj.key] +
														'</span>',
												}
											]
										}
									}
									/**
									 * TextBox Case
									 */
									else if (currentObj.displaytype == 'TextBox') {
										allDataTypeObj[currentObj.key] = numberOfCols[j]['columnid'];
										idObj[currentObj.key] = numberOfCols[j]['columnid'];
										uiElement = {
											view: "text",
											id: viewId,
											//labelWidth:150,
											gravity: gravityValue,
											labelPosition: "top",
											//											name: currentObj.key,
											name: currentObj.key,
											placeholder: resourceObj.Text_msg,
											label: '<span title="' +
												resourceObj[currentObj.key] +
												'">' +
												resourceObj[currentObj.key] +
												'</span>',
											css: 'brix_textbox_1 searchComponetStyle brix_textbox',
											hidden: showHideProperty,
											on: {
												onAfterRender: function () {
													//												console.log(resourceObj);
												}
											}
										}
									}
									/**
									 * MultiComboBox Case
									 */
									else if (currentObj.displaytype == 'MultiComboBox') {
										idObj[currentObj.key] = numberOfCols[j]['columnid'];
										allDataTypeObj[currentObj.key] = numberOfCols[j]['columnid'];
										uiElement = {
											view: 'multiselect',
											id: viewId,
											//labelWidth:150,
											gravity: gravityValue,
											labelPosition: "top",
											css: 'brix_multi_combo_1 comboEle',
											placeholder: resourceObj.Combo_msg,
											value: currentObj.defaultvalue,
											label: '<span title="' +
												resourceObj[currentObj.key] +
												'">' +
												resourceObj[currentObj.key] +
												'</span>',
											suggest: [],
											name: currentObj.key,
											hidden: showHideProperty,
											on: {

												onAfterRender: function () {
													//													console.log(resourceObj);
												},
												onItemClick: function () {
													var currentEleId = $$(this).config.id;
													var currentKeyName = $$(this).config.name;
													var obj = {
														"key": currentKeyName,
														"searchname": objForSearch.searchname
													}
													obj = JSON
														.stringify(obj);
													$.ajax({
														type: "POST",
														url: "getDataForParameter",
														data: obj,
														contentType: "application/json; charset=utf-8",
														dataType: "json",
														success: function (comboResponse) {
															if (comboResponse.returncode == "403") {
																sessionOutPopup();
																$$("session_Out_Popup").show()
															}
															if (comboResponse && comboResponse.data) {
																$$(currentEleId).getList().parse(comboResponse.data)
															}
														},
														error: function (XMLHttpRequest, textStatus, errorThrown) {
															$.LoadingOverlay("hide", spinner);
														}
													})
												}
											}
										}
									}
									/**
									 * Date Case
									 */
									else if (currentObj.displaytype == 'Date') {
										idObj[currentObj.key] = numberOfCols[j]['columnid'];
										allDataTypeObj[currentObj.key] = numberOfCols[j]['columnid'];
										uiElement = {
											view: "datepicker",
											value: currentObj.defaultvalue,
											id: viewId,
											labelPosition: "top",
											//	labelWidth:150,
											css: 'brix_datepicker_1 searchComponetStyle',
											name: currentObj.key +
												"_" + "**",
											format: webix.Date
												.dateToStr("%d/%m/%Y"),
											label: '<span title="' +
												resourceObj[currentObj.key] +
												'">' +
												resourceObj[currentObj.key] +
												'</span>',
											gravity: gravityValue,
											hidden: showHideProperty,

										}
									}
									/**
									 * Create columns with data for
									 * row
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
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			$.LoadingOverlay("hide", spinner);
			webix.alert('Faild to generate filter view');
			$deferred.resolve();
		}

	})
	return $deferred.promise();
}

function logout() {
	webix.ajax().get("./search/logout", function (text, xml, xhr) {
		window.location = text;
	});
}
/**
 * @purpose This function shows save import window
 * @param extension of file and name i.e. type of file
 * @author komal.shevane
 */

function importWindow1(extention, name) {
	webix.ui({
		view: "window",
		container: "id_brix_search_controller",
		id: "importWindow1",
		move: false,
		position: "center",
		head: "<span class='create_instance_popup_title_css '>Import - " + name + "</span><span class='closeicon' onclick='$$(\"importWindow1\").close()'>×</span>",
		css: "create_instanceCss brix_popup",
		modal: true,
		resize: false,
		headHeight: 32,
		height: setHeightInPercentage(75),
		width: setWidthInPercentage(70),
		body: {
			css: "importWinCss",
			rows: [{
				//tab bar for graph & its data
				borderless: true,
				view: "tabbar",
				id: 'tabbar',
				value: 'instance_container',
				css: "brix_tabbar tabCss",
				multiview: true,
				options: [{
					value: 'Instance',
					id: 'instance_container'
				}, {
					value: 'Preview',
					id: 'preview_Container'
				}],
			}, {
				css: "importWinCss",
				cells: [{
					id: "instance_container",
					css: "importWinCss",
					gravity: 1,
					rows: [{
						view: "list",
						id: "mylist",
						type: "uploader",
						css: "uploadListCss",
						gravity: 0.35,
						scroll: false,
						onClick: {
							"webix_list": function () {
								$$("uploadAPI").fileDialog();
							}
						},
						on: {
							"data->onStoreUpdated": function () {
								$$("uploadAPI").define("accept", "." + extention);
								$$("uploadAPI").refresh();
								if (!this.count()) {
									webix.extend($$("mylist"), webix.OverlayBox);
									$$("mylist").showOverlay("<div class='uploaddropcss'>Drop file here or <span class='fa fa-cloud-upload'></span><span class='linkcss'>click to upload</span></div>");
								} else
									this.hideOverlay();
							},
						}
					}, {
						view: "datatable",
						scroll: "auto",
						id: "instanceGroupGrid",
						css: "brix_datatable_2 nodetailsGridStyle instanceGroupGridCss preview_rows",
						//						css: "brix-brix_datatable_2 nodetailsGridStyle instanceGroupGridCss preview_rows",
						tooltip: true,
						select: false,
						headerRowHeight: 25,
						autoConfig: true,
						align: "center",
						scroll: true,
						rowHeight: 26,
						columns: [{
							id: "label",
							header: resourceObj.instanceLbl,
							//							css: "parameterStyle",
							fillspace: true,
						}, {
							id: "group",
							header: resourceObj.classLbl,
							//							css: "valueStyle",
							fillspace: true,
							template: function (obj) {
								var data = "-";
								var combo;
								webix.ajax().sync().get("./uploadFile/getGroupElements", function (text, xml, xhr) {
									//									console.log(obj)
									var response = JSON.parse(text);
									var sel = document.createElement("select");
									//							    	 sel.id= obj.label;
									sel.id = obj.id;
									for (i = 0; i < response.data.length; i++) {
										var opt = document.createElement('option');
										opt.value = response.data[i];
										opt.innerHTML = response.data[i];
										sel.appendChild(opt);
									}
									data = sel.innerHTML;
									var item = $$("instanceGroupGrid").getItem(obj.id);
									item["updatedGrp"] = response.data[0];
									//							    	 combo = "<select id='"+ obj.label+"'>" + sel.innerHTML +'</select>';
									combo = "<select id='" + obj.id + "' onchange=onGrpChange(id,this)>" + sel.innerHTML + '</select>';

								});
								if (obj.group == "-") {
									return combo;
								} else {
									data = obj.group;
									return data;
								}

							},
						}],
						data: [],
						on: {
							"onAfterRender": webix.once(function () {
								$$('tabbar').removeOption("preview_Container");
								$$('tabbar').removeOption("instance_container");

								//								webix.html.removeCss($("#next_arrow_name").$view, "enabled");
								//								webix.html.addCss($("#next_arrow_name").$view, "disabled");
							})
						}
					}, {
						gravity: 0.1,
						css: "importbtnContainerCss",
						cols: [{
							view: "button",
							type: "icon",
							label: resourceObj.closeLbl,
							align: "center",
							icon: "fa fa-times",
							css: "brix_button",
							inputWidth: 90,
							on: {
								onItemClick: function () {
									$$("importWindow1").close();
								}
							}

						}, {
							gravity: 0.07,
							view: "button",
							id: "nextBtn",
							label: "Next",
							type: "next",
							height: 30,
							on: {
								onAfterRender: webix.once(function () {
									webix.html.removeCss($$("nextBtn").$view, "enabled");
									webix.html.addCss($$("nextBtn").$view, "disabled");
								}),
								onItemClick: function () {
									$$("tabbar").setValue("preview_Container");
									showPreview()
								}
							}
						}]
					}]
				}, {
					id: "preview_Container",
					css: "importWinCss",
					rows: [{
						gravity: 2,
						autoHeight: true,
						scroll: "y",
						cols: [{
							view: "template",
							gravity: 0.1,
							width: 100,
							template: '<div id="previewlegends"><div class="vis-network" tabindex="900" style="position: relative; overflow: hidden;touch-action: pan-y; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"><canvas  style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"></canvas></div></div>',
							css: 'graphContainerCss',
						}, {

							view: "template",
							template: '<div id="graphView"><div class="vis-network" tabindex="900" style="position: relative; overflow: hidden;touch-action: pan-y; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"><canvas width="800" style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"></canvas></div></div>',
							css: 'graphContainerCss',

						}, {
							view: "accordion",
							css: "detailViewContainerStyle",
							id: "previewDetailAcc",
							//							hidden:true,
							cols: [{
								id: "previewDetailView",
								collapsed: true,
								maxWidth: 270,
								header: "<span id='previveDetailHeader' style='text-align:center'></span>",
								css: "detailsAccStyle",
								body: {
									css: "detailContainerStyle",
									rows: [{
										view: "datatable",
										rowHeight: 30,
										scroll: "y",
										tooltip: true,
										//											autoheight: true,
										css: "brix_datatable_2 nodetailsGridStyle rows",
										id: "previewNodeDetails",
										fixedRowHeight: false,
										select: false,
										header: false,
										columns: [{
											id: "key",
											header: "key",
											sort: "string",
											autoWidth: true,
											//												width: 80,
											fillspace: true,

										}, {
											id: "value",
											header: "value",
											sort: "string",
											fillspace: true,
										}],
										data: []
									}]

								}

							}, {
								width: 0.1
							}],
							on: {
								onAfterExpand: function () {
									
									if (previewNetwork) {
										var grp;
										for (var i in previewdata.nodes) {
											if (i) {
												if (previewNetwork.getSelectedNodes()[0] != undefined) {
													var obj = {};
													obj['key'] = i;
													obj['value'] = previewdata.nodes[i];
													if (obj.value["id"] == previewNetwork.getSelectedNodes()[0])
														grp = obj.value["group"];
												}
											}
										}
										var myEle = document.getElementById("previveDetailHeader");
										if (myEle) {
											if(grp==undefined)
												document.getElementById("previveDetailHeader").innerHTML = prevVertex + " Details";
											else{
												document.getElementById("previveDetailHeader").innerHTML = grp + " Details";
												prevVertex=grp;
											}
										}
										
									}


								},
								onAfterCollapse: function () {
									
									if (document.getElementById("graphView") ) {
										if(previewNetwork.getSelectedNodes()[0] != undefined){
										var nodeId = previewNetwork.getSelectedNodes()[0];
										onSelectPreviewNode(null, nodeId);
										}else{
											document.getElementById("previveDetailHeader").innerHTML = prevVertex + " Details";
										}
									}
								}
							}


						}]
					}, {
						css: "importbtnContainerCss",
						cols: [{
								gravity: 0.1,
								view: "button",
								label: "Previous",
								type: "prev",
								height: 30,
								on: {
									onItemClick: function () {
										$$("tabbar").setValue("instance_container");
										//								showPreview();
									}
								}

							},
							{
								view: "button",
								type: "icon",
								id: "btnImport",
								icon: "fa fa-upload",
								label: resourceObj.importLbl,
								inputWidth: 80,
								height: 37,
								css: "brix_button",
								align: "center",
								on: {
									"onItemClick": function () {
										webix.confirm({
											title: "Import Confirmation",
											text: "Do you want to import ?",
											type: "confirm-error",
											callback: function (result) {
												//												console.log(result);
												if (result == true) {
													$.LoadingOverlay("show", spinner);
													$.ajax({
														type: "POST",
														url: "importPreviewData",
														data: JSON.stringify(importData),
														contentType: "application/json; charset=utf-8",
														dataType: "json",
														success: function (response) {
															//															console.log(response);
															if (response == true) {
																$$("importWindow1").close();
//																$.LoadingOverlay("hide", spinner);
																customAlert("Success", "Data Imported successfully!")
															} else {
																$$("importWindow").close();
//																$.LoadingOverlay("hide", spinner);
																customAlert("Error", "Error occured!!Please try again")
															}
														},
														error: function (XMLHttpRequest, textStatus, errorThrown) {
															$.LoadingOverlay("hide", spinner);
															$$("importWindow1").close();
														}
													});
												}
											}
										});

									}
								}


							}
						]
					}]

				}]
			}]
		}

	});

	webix.ui({
		id: "uploadAPI",
		view: "uploader",
		multiple: false,
		autosend: false,
		//		accept:".rdf, text/xml,.graphml",
		on: {
			"onAfterFileAdd": function (file) {
				var formData = "";
				var formData = new FormData();
				$$("uploadAPI").files.data.each(function (obj, i) {
					formData.append("file", obj.file);
				});
				//				console.log(formData);
				$.ajax({
					type: "POST",
					url: "uploadFile",
					data: formData,
					contentType: false,
					processData: false,
					success: function (response) {
						if (response.returncode == "403") {
							sessionOutPopup();
							$$("session_Out_Popup").show()
						}
						if (response.returncode == 1 || response.returnCode == 1) {
							$$("instanceGroupGrid").clearAll();
							$$("instanceGroupGrid").parse(response.data);
							webix.html.removeCss($$("nextBtn").$view, "disabled");
							webix.html.addCss($$("nextBtn").$view, "enabled")
						} else {
							customAlert("Exception", response.message)
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						$.LoadingOverlay("hide", spinner);
					}
				})

			},
			onBeforeFileAdd: function (item) {
				var type = item.type.toLowerCase();
				if (type != extention) {
					customAlert("Warning", "Import option selected : " + name + "<br>." + extention + " file expected")
					return false;
				}

				//get an array of files with the same name from the list of already added one
				var found = this.files.find(function (obj) {
					return obj.name === item.name
				});

				if (found.length) {
					customAlert("Warning", "You have already uploaded the file with the same name")
					//prevent file add
					return false;
				}
			},
		},

		link: "mylist",
		apiOnly: true
	});
	$$("uploadAPI").files.attachEvent("onBeforeDelete", function (id) {
		$$("instanceGroupGrid").clearAll();
		webix.html.removeCss($$("nextBtn").$view, "enabled");
		webix.html.addCss($$("nextBtn").$view, "disabled");
	})
}

/**
 * @purpose This function will set the new change value of group in import window
 * @param id and HTML component
 * @author komal.shevane
 */
function onGrpChange(id, comp) {
	var item = $$("instanceGroupGrid").getItem(id);
	item["updatedGrp"] = comp.value;
	//	$$("instanceGroupGrid").updateItem(id, item);
}
/**
 * @purpose This function generate the bubble vi
 * @author komal.shevane
 */
function showPreview() {
	var data = [];
	try {
		$$("instanceGroupGrid").eachRow(function (row) {
			var str;
			var paramObj = [];
			var param = $$('instanceGroupGrid').getItem(row).parameter;
			if ($$("instanceGroupGrid").getItem(row).group == "-") {
				var grpVal = $$("instanceGroupGrid").getItem(row).updatedGrp;
				if (!(grpVal in Pgroups))
					str = '{"group": "' + grpVal + '","id":"' + row + '","label":" ' + $$('instanceGroupGrid').getItem(row).label + '","parameter":"' + JSON.stringify(param).replace(/"/g, '\'') + '"}'
			} else {
				str = '{"group": "' + $$('instanceGroupGrid').getItem(row).group + '","id":"' + $$('instanceGroupGrid').getItem(row).id + '","label":" ' + $$('instanceGroupGrid').getItem(row).label + '","parameter":"' + JSON.stringify(param).replace(/"/g, '\'') + '"}'
			}
			data.push(JSON.parse(str));
			importData = data

		});
	} catch (err) {}
	$.ajax({
		type: "POST",
		url: "getDataForPreview",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (response) {
			var previewnodes = response.data[0];
			var previewedges = response.data[1];
			var tempNodes = JSON.parse(previewnodes);
			Pgroups = [];
			for (var i = 0; i < tempNodes.length; i++) {
				Pgroups.push(tempNodes[i].group);
			}
			var PuniqueGroups = Pgroups.filter(function (item, pos) {
				return Pgroups.indexOf(item) == pos;
			});

			var x = 1;
			var y = -260;
			var step = 55;
			var inc = 1;
			var legNodes = [];
			for (i = 0; i < PuniqueGroups.length; i++) {
				legNodes.push({
					id: PuniqueGroups[i],
					x: x,
					y: y + inc * step,
					label: PuniqueGroups[i],
					group: PuniqueGroups[i],
					value: 1,
					fixed: true,
					physics: false
				});
				inc++;
			}
			// create a network for legends
			var PlegendContainer = document.getElementById('previewlegends');
			var PlegendData = {
				nodes: legNodes,
				edges: []
			};
			var PLegendOption = {
				autoResize: false,
				width: '100%',
				height: '100%',
				nodes: {
					shape: 'dot',
					size: 1,
					borderWidth: 0.2,
					font: {
						color: '#070303',
						size: 12
					},
					widthConstraint: {
						maximum: 80
					},
					scaling: {
						min: 8,
						max: 8,

					},
				},
				interaction: {
					zoomView: false,
					dragView: false,
					selectable: false,
				}
			};
			Pnetwork = new vis.Network(PlegendContainer, PlegendData, PLegendOption);

			// create a network
			var previewcontainer = document.getElementById('graphView');
			previewdata = {
				nodes: JSON.parse(previewnodes),
				edges: JSON.parse(previewedges)
			};
			var previewoptions = {
				nodes: {
					borderWidth: 4,
					shape: 'dot',
					size: 16,
					borderWidth: 1,
					color: {

						border: '#222222',
						background: '#666666'
					},
					font: {
						color: '#070303',
						size: 12,
					}
				},
				edges: {
					color: 'lightgray'
				},
			};
			previewNetwork = new vis.Network(previewcontainer, previewdata, previewoptions);
			previewNetwork.on('select', onSelectPreviewNode);
			var nodeId = JSON.parse(previewnodes)[0].id;
			previewNetwork.selectNodes([nodeId], true);
			onSelectPreviewNode(null, nodeId);
			previewNetwork.on("afterDrawing", function (params) {});

		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			$.LoadingOverlay("hide", spinner);
		}
	});
}
/**
 * @purpose This function get the vertex details of selected preview node on import window
 * @param properties of selected node and id (if selection is dynamic)
 * @author komal.shevane
 */
function onSelectPreviewNode(properties, id) {
	var data;
	var name;
	if (properties)
		data = properties.nodes[0];
	else
		data = id;
	var id = data;
	if (data) {
		$.ajax({
			type: "POST",
			url: "getPreviewedVertexDetails",
			data: "" + data,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (response) {
				var data = response.data;
				//				console.log(data);
				var vertexData = [];
				for (var i in data.vertexData) {
					if (i) {
						var obj = {};
						obj['key'] = i;
						obj['value'] = data.vertexData[i];
						vertexData.push(obj)
					}
				}
				var edgeData = [];
				for (var i in data.edges) {
					if (i) {
						var obj = {};
						obj['key'] = i;
						obj['value'] = data.edges[i];
						edgeData.push(obj)
					}
				}
				var edges = [];
				for (var i = 0; i < edgeData.length; i++)
					edges.push(edgeData[i].value)
				//				console.log(vertexData);
				$$("previewNodeDetails").clearAll();
				$$("previewNodeDetails").parse(vertexData);
				$$("previewNodeDetails").adjustRowHeight();
				var grp;
				for (var i in previewdata.nodes) {
					if (i) {
						var obj = {};
						obj['key'] = i;
						obj['value'] = previewdata.nodes[i];
						if (obj.value["id"] == id)
							grp = obj.value["group"]
					}
				}
				document.getElementById("previveDetailHeader").innerHTML = grp + " Details";
				prevVertex=grp;
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$.LoadingOverlay("hide", spinner);
			}
		});
	}
}

function generateOerryPreview() {
	// create a network
	var nodes = new vis.DataSet([{
			id: 1,
			label: 'Node 1'
		},
		{
			id: 2,
			label: 'Node 2'
		},
		{
			id: 3,
			label: 'Node 3'
		},
		{
			id: 4,
			label: 'Node 4'
		},
		{
			id: 5,
			label: 'Node 5'
		}
	]);

	// create an array with edges
	var edges = new vis.DataSet([{
			from: 1,
			to: 3
		},
		{
			from: 1,
			to: 2
		},
		{
			from: 2,
			to: 4
		},
		{
			from: 2,
			to: 5
		},
		{
			from: 3,
			to: 3
		}
	]);
	var container = document.getElementById('querryPreviewContainer');
	var data = {
		nodes: nodes,
		edges: edges
	};
	//option start
	var options = {
		nodes: {
			shape: 'dot',
			size: 30,
			borderWidth: 1,
			borderWidthSelected: 300,
			scaling: {
				min: 10,
				max: 30,
				label: {
					enabled: false,
					min: 8,
					max: 20,
					drawThreshold: 4,
					maxVisible: 7
				}
			},
			font: {
				size: 12,
				face: 'Tahoma'
			}

		},
		edges: {
			smooth: {
				type: 'cubicBezier',
				forceDirection: 'horizontal',
				roundness: 0.4
			},
			font: {
				color: '#070303',
				size: 0
			},
			width: 2,
			selectionWidth: 4,
			hoverWidth: 3,

		},
		layout: {
			improvedLayout: false,
			//randomSeed:2
		},
		physics: {
			stabilization: false,
			/* hierarchicalRepulsion: {
      			    		            centralGravity: 0.0,
      			    		            springLength: 100,
      			    		            springConstant: 0.01,
      			    		            nodeDistance: 120,
      			    		            damping: 0.09
      			    		          },
      			    		        barnesHut: {
      			    		          gravitationalConstant: -20000,
      			    		          springConstant: 0.03,
      			    		          springLength: 300
      			    		        },
      				                	maxVelocity: 50,
      				    		        minVelocity: 0.1,
      				    		        solver: 'barnesHut',
      				    		        timestep: 0.5,
      				    		        adaptiveTimestep: true*/
		},
		interaction: {
			hover: true,
			tooltipDelay: 200,
			hideEdgesOnDrag: true,
			navigationButtons: true,
			keyboard: true,
		}
	};
	//option end
	var network = new vis.Network(container, data, options);
}