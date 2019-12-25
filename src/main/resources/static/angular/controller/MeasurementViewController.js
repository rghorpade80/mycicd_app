var resourceObj;
var authenticationKey;
var baseUrl;
var meaId;
graphApp
	.controller(
		'MeasurementViewController', [
			'$scope',
			'$compile',
			'$http',
			function ($scope, $compile, $http) {
				$scope.newGraphTabConfig = webix.ui({
					minHeight: 670,
					rows: [{
						template: "<div class='iconContainer'><i class='webix_icon fa fa-line-chart iconCss'></i><span style='margin-left:20px;position: absolute;margin-top:10px'>Measurement Viewer</span></div>",
						height: 40,
						css: "graphHeaderStyle",
					}, {
						cols: [{
								view: "tree",
								select: true,
								id: 'id_treeView',
								css: 'brix_tree brix_search_tree tree_fix_line',
								width: 300,
								activeTitle: false,
								drag: "source",
								data: [],
								tooltip: {
									template: '<span>#value#</span>'
								},
								template: function (obj, common) {
									//										return "<span>" + common.icon(obj, common) + "<a id='treetag' ui-sref='/" + currentState + "." + obj.link + "' ui-sref-opts='{reload:true}' style='text-decoration: none; color:#333;'>" + common.folder(obj, common) + webix.template.escape(obj.value) + "</a></span>";
									return "<span>" + common.icon(obj, common) + "<a id='treetag' ui-sref='/" + +"." + obj.link + "' ui-sref-opts='{reload:true}' style='text-decoration: none; color:#333;'>" + common.folder(obj, common) + webix.template.escape(obj.value) + "</a></span>";
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
										webix.ajax().sync().get("./search/getMeasurement", {
											string: nodeId
										}, function (text, xml, xhr) {
											//response
											//				        						  $.LoadingOverlay("hide", spinner);
											var response = JSON.parse(text);
											if (response.returncode == 4)
												webix.alert(response.message)
											else {
												$$('id_treeView').parse(response.Measurement);
												$$('id_treeView').select(response.Measurement[0].id); //First measurement select default
												$$('id_treeView').callEvent("onItemClick", [response.Measurement[0].id])
												if (response.URL) {
													baseUrl = response.URL[0];
													authenticationKey = response.URL[1];
												}
											}
										});
										meaId = nodeId;
										resourceObj = JSON.parse(window.localStorage.getItem("resourceObj"));
										//				            					console.log(resourceObj)

									}),
									onItemClick: function (id) {
										$.LoadingOverlay("hide", spinner);
										var item = $$("id_treeView").getItem(id);
										if (item.nodeType == "MeaResult") { //For measurement
											var meaId = item.nodeId;
											webix.ajax().get("./search/getMeasurementInfo", {
												meaId: meaId
											}, function (text, xml, xhr) {
												//response
												var response = JSON.parse(text);
												console.log(response);
												$$("channelDetails").clearAll();
												$$("channelDetails").parse(response);
												document.getElementById("detailHeading").innerHTML = "Measurement Details"
											});

										} else { //for channel
											var nodeId = $$("id_treeView").getItem(id).nodeId;
											var param = nodeId + "," + baseUrl + "," + authenticationKey;
											//			            			        	  string : nodeId , baseUrl ,authenticationKey 
											webix.ajax().get("./search/getChannelInfo", {
												string: param
											}, function (text, xml, xhr) {
												//response
												$.LoadingOverlay("hide", spinner);
												var response = JSON.parse(text);
												console.log(response);
												var data = [];
												var count = 1;
												for (var i in response) {
													if (i) {
														var object = {};
														object["Sr.no."] = count;
														object['Key'] = i;
														object['Value'] = response[i];
														data.push(object);
														count++;
													}
												}
												console.log(data);
												$$("channelDetails").clearAll();
												$$("channelDetails").parse(data);
												document.getElementById("detailHeading").innerHTML = "Channel  Details"
											});


										}
										console.log(item);
									}
								}
							},
							{
								view: "resizer",
								borderless: false
							},
							{
								rows: [{
									gravity: 1.8,
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
												"maxnoofcanvases": 10,
												"maximize": 0,
												"close": 0
											}
											resourceObj = JSON.parse(window.localStorage.getItem("resourceObj"));
											//  createCanvasTemplateView(count, id, 'id_window_container',canvasJsonObj,resourceObj);
											//							            		 createCanvasTemplateView(count, id, 'id_window_container',canvasJsonObj,resourceObj, 'Basic c3VwZXJ1c2VyOnNlY3JldA==', 'http://172.16.132.41:8087/pods/v1');
											createCanvasTemplateView(count, id, 'id_window_container', canvasJsonObj, resourceObj, authenticationKey, baseUrl);
											$.LoadingOverlay("hide", spinner);
										})
									}
								}, {
									height: 30,
									template: "<span id='detailHeading'></span>",
									css: "meaDetailStyle"
								}, {
									cols: [{
										width: 20
									}, {

										view: "datatable",
										rowHeight: 30,
										scroll: "auto",
										tooltip: true,
										//											autoheight: true,
										minWidth: 300,
										css: "brix_datatable_2 rows",
										id: "channelDetails",
										fixedRowHeight: false,
										select: false,
										//											header: false,
										scroll: true,
										columns: [{
											id: "Sr.no.",
											header: "Sr.No.",
											sort: "string",
											//												autoWidth: true,
											width: 60,
											//												fillspace: true,


										}, {
											id: "Key",
											header: "Attribute",
											sort: "string",
											//												autoWidth: true,
											width: 150,
											//												fillspace: true,

										}, {
											id: "Value",
											header: "Value",
											sort: "string",
											fillspace: true,
											minWidth: 150,
										}],
										data: [],
										on: {
											"onResize": function (id) {
												$$("channelDetails").adjustRowHeight();
											}
										}


									}]
								}]
							}
						]


					}]
				});
			}
		])