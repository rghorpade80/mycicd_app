/**
 * @purpose Show Tree View
 * @param $scope,ele,filterObj
 * @author Tejal
 */
var network;
var groupName;
var legendnetwork;
var legendNode;
var legendEdge;
var pNodeId;
var pX;
var pY;
var x = 1;
var y = -400;
var step = 50;
var inc = 1;
var uniqueGroups = [];
var bhierarchyBtn = false;
var launchUrl;
var prevNode;
var lastNode=null;
//Tee View
function showtreeView($scope, ele, filterObj, $compile, $http,key) {
	$.LoadingOverlay("show", spinner);
	webix.ui({
		container: ele,
		css: "treeContainerStyle",
		rows: [{
			gravity: 0.1,
			view: "richselect",
			id: "treeNameCombo",
			css: 'brix_combo_1 searchComponetStyle',
			//			    value:"", 
			inputWidth: 240,
			options: [],
			on: {
				"onAfterRender": webix.once(function () {
					var popup = $$("treeNameCombo").getPopup();
					webix.html.addCss(popup.$view, "importListStyle");
					$.ajax({
						type: "POST",
						url: "getTreeNames",
						data: objForSearch.searchname,
						//							  async: false,
						contentType: "application/json; charset=utf-8",
						dataType: "json",
						success: function (comboResponse) {
							if (comboResponse.returncode == "403") {
								sessionOutPopup();
								$$("session_Out_Popup").show()
							}
							//								console.log(comboResponse);
							var list = $$("treeNameCombo").getPopup().getList();
							list.clearAll();
							list.parse(comboResponse.data);
							$$("treeNameCombo").setValue(comboResponse.data[0]);


						},
						error: function (XMLHttpRequest, textStatus, errorThrown) {
							$.LoadingOverlay("hide", spinner);
						}
					})
				}),
				"onChange": function (newv, oldv) {
					filterObj['treeName'] = newv;
					obj = JSON.stringify(filterObj);
					$.ajax({
						type: "POST",
						url: "getResultForTreeView",
						data: obj,
						contentType: "application/json; charset=utf-8",
						dataType: "json",
						success: function (response) {
							//								console.log(response);
							if (response.returncode == "403") {
								sessionOutPopup();
								$$("session_Out_Popup").show()
							}
							response = response
							if (response && response.returncode == '1' && response.data && response.data[0] && response.data[0].node) {
								$$("id_brix_search_tree").clearAll();
								$$('id_brix_search_tree').parse(response.data[0].node);
								$$('id_brix_search_tree').refresh();
//								$.LoadingOverlay("hide", spinner);
								fid = response.data[0].node[0].id;
								drawGraph($scope, obj, $compile, $http);
								$$('id_brix_search_tree').select(response.data[0].node[0].id);
							} else {
								webix.alert({
									title: "No Result",
									autoheight: true,
									autowidth: true,
									ok: "OK",
									text: resourceObj.brix_lbl_noResultFound,
									callback: function (result) {
										//											$$("clearFilterBtn").callEvent("onItemClick", []);
									}
								});
//								$.LoadingOverlay("hide", spinner);
							}
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) {
							$.LoadingOverlay("hide", spinner);
						}
					});

//					$.LoadingOverlay("hide", spinner);

				}
			}
		}, {
			gravity: 1,
			height: 700,
			multi: true,
			view: "accordion",
			cols: [{
					gravity: 0.3,
					css: "treeViewAccStyle",
					id: "treeView",
					width: 240,
					header: resourceObj.TreeView_lbl,
					body: {
						view: "tree",
						select: true,
						activeTitle: true,
						id: 'id_brix_search_tree',
						css: 'brix_tree brix_search_tree tree_fix_line',
						gravity: 2,
						minWidth: 240,
						drag: "source",
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
							onItemClick: function (id) {
								focusOnSelectedVertex(id);
								network.selectNodes([id], true);
								onSelect(null, id);
							},
						}

					}
				}, {
					view: "resizer"
				},
				{
					cols: [{
						css: "clrBorderStyle",
						template: "<div id='xyz'></div>",
						width: 0.01
					}, {
						css: "graphContainerStyle",
						cols: [{
							height: 800,
							width: 130,
							rows: [{
								height: 800,
								//								css:"borderRemoveStyle",
								template: "<div style='width:100px;' id='legends'></div>"
							}]
						}, {
							height: 800,
							rows: [{
								//								css:"btnContainerStyle",
								cols: [{
										template: resourceObj.Simulation_lbl,
										width: 80,
										gravity: 0.3,
										css: "borderRemoveStyle"
									}, {
										view: "toggle",
										type: "icon",
										css: "access_tgl",
										onIcon: "fa fa-toggle-on",
										offIcon: "fa fa-toggle-off",
										value: "1",
										height: 40,
										width: 55,
										on: {
											"onChange": function (newv, oldv) {
												var nodeId = network.getSelectedNodes()[0];
												focusOnSelectedVertex(nodeId);
												setSimulation(newv);
											}
										}
									},
									{
										width: 30
									},

									{
										template: resourceObj.ShowLabel,
										width: 100,
										css: "borderRemoveStyle"
									}, {

										view: "toggle",
										type: "icon",
										css: "access_tgl",
										onIcon: "fa fa-toggle-on",
										offIcon: "fa fa-toggle-off",
										value: "1",
										height: 40,
										width: 60,
										inputWidth: 60,
										on: {
											"onChange": function (newv, oldv) {
												showLabel(newv)
											}
										}
									}, {
										width: 50
									}, {
										view: "button",
										type: "icon",
										icon: "fa fa-expand",
										label: resourceObj.expandLbl,
										inputWidth: 100,
										css: "launchBtnCss brix_button",
										width: 100,
										tooltip: "Expand node",
										on: {
											"onItemClick": function () {
												var nodeId = network.getSelectedNodes()[0];
												expandGraphVertex(nodeId);
												focusOnSelectedVertex(nodeId);
											}
										}
									}, {
										view: "button",
										id: "launchBtn",
										type: "icon",
										icon: "fa fa-share-square",
										label: resourceObj.launch,
										inputWidth: 100,
										css: "launchBtnCss brix_button",
										tooltip: "Launch node",
										on: {
											"onItemClick": function () {
												var nodeId = network.getSelectedNodes()[0];
												launch(nodeId);
											},
											"onAfterRender": webix.once(function () {
												webix.html.addCss($$("launchBtn").$view, "disabled");
												webix.html.removeCss($$("launchBtn").$view, "enabled");
											})
										}
									}
								]
							}, {
								view: "template",
								gravity: 2,
								height: 760,
								template: '<div id="graphViewContainer"><div class="vis-network" tabindex="900" style="position: relative; overflow: hidden;touch-action: pan-y; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"><canvas  style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"></canvas></div></div>',
								css: 'graphContainerCss ',
								//								scrollAdd

							}]
						}]
					}]
				}, {
					id: "graphResizer",
					view: "resizer"
				},
				{
					gravity: 0.6,
					id: "detailView",
					collapsed: true,
					header: "<span id='grpname' style='text-align:center'></span>",
					css: "detailsAccStyle",
					minWidth: 300,
					width: 350,
					body: {
						css: "detailContainerStyle",
						rows: [{
							view: "datatable",
							scroll: true,
							tooltip: true,
							//									autoheight: true,
							css: "brix_datatable_2 nodetailsGridStyle rows",
							id: "nodeDetails",
							fixedRowHeight: false,
							select: false,
							header: false,
							columns: [{
								id: "key",
								header: "key",
								sort: "string",
								autoWidth: true,
								minWidth: 150,
								fillspace: true,

							}, {
								id: "value",
								header: "value",
								sort: "string",
								fillspace: true,
								minWidth: 150,
							}],
							data: [],
							on: {
								"onResize": function (id) {
									$$("nodeDetails").adjustRowHeight();
								}
							}
						}]
					},

				},
			],
			on: {
				onAfterExpand: function (id) {
					if (id == "detailView") {
						$$("nodeDetails").adjustRowHeight();
						webix.html.removeCss($$("graphResizer").$view, "disabled");
						webix.html.addCss($$("graphResizer").$view, "enabled");
						if (network)
							onSelect(null, network.getSelectedNodes()[0])
					}
				},
				onAfterCollapse: function (id) {
					if (id == "detailView") {
						if ($$("graphResizer"))
							webix.html.removeCss($$("graphResizer").$view, "enabled");
						webix.html.addCss($$("graphResizer").$view, "disabled");
						if (network)
							onSelect(null, network.getSelectedNodes()[0]);
					}
				}
			}
		}]
	});

}

function bindTreeData(itemId, containerId, treeId, $scope, $compile, $http) {
	//showSelectedTreeNodeView(itemId, containerId, treeId);
}
/**
 * @purpose This function for generating bubble grapg 
 * @param $scope,response obj ,$compile,$http
 * @author komal.shevane
 */
function drawGraph($scope, obj, $compile, $http) {
	var node;
	$.ajax({
		type: "POST",
		url: "getDataTreeBubble",
		data: obj,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (comboResponse) {
			//get response for bubble view  
			//			console.log(comboResponse);
			if (comboResponse.returncode == "403") {
				sessionOutPopup();
				$$("session_Out_Popup").show()
			}
			var tempNodes = comboResponse.data.nodes;
			var tempEdges = comboResponse.data.edges;
			var groups = [];
			for (var i = 0; i < tempNodes.length; i++) {
				groups.push(tempNodes[i].group);
			}
			uniqueGroups = groups.filter(function (item, pos) {
				return groups.indexOf(item) == pos;
			});

			x = 1;
			y = -400;
			step = 50;
			inc = 1;
			var legNodes = [];
			for (i = 0; i < uniqueGroups.length; i++) {
				legNodes.push({
					id: uniqueGroups[i],
					x: x,
					y: y + inc * step,
					label: uniqueGroups[i],
					group: uniqueGroups[i],
					title: "<span style='font-size:10px;'>"+uniqueGroups[i]+"</span>",
					value: 1,
					fixed: true,
					physics: false
				});
				inc++;
			}
			// create a network for legends
			var legendContainer = document.getElementById('legends');
			legendNode = new vis.DataSet(legNodes);
			edges = []
			var legendData = {
				nodes: legendNode,
				edges: edges
			};
			var LegendOption = {
				autoResize: false,
				width: '100%',
				height: '100%',
				nodes: {
					shape: 'dot',
					widthConstraint: {
						maximum: 80
					},

					size: 10,
					borderWidth: 1,
					font: {
						size: 12
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
					tooltipDelay: 200,

				}
			};
			legendnetwork = new vis.Network(legendContainer, legendData, LegendOption);

			// create a network for graph
			nodes = new vis.DataSet(tempNodes);
			edges = new vis.DataSet(tempEdges);
			//			console.log("NODES ", nodes)
			var container = document.getElementById('graphViewContainer');
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


			network = new vis.Network(container, data, options);
		/*	groups = {
				Project: {
					color: {
						highlight: {
							background: '#3B73AF',
							border: '#3B73AF'
						},
					}
				},
				TestPlan: {
					color: {
						highlight: {
							background: '#F4C20E',
							border: '#F4C20E'
						},
					}
				},
				Test: {
					color: {
						highlight: {
							background: 'red',
							border: 'red'
						},
					}
				},
				DataName: {
					color: {
						highlight: {
							background: '#45891E',
							border: '#45891E'
						},
					}
				},

			}
			network.setOptions({
				groups: groups
			});*/
			network.on("doubleClick", function (params) {
				var nodeId = params.nodes[0];
				expandGraphVertex(nodeId);
				focusOnSelectedVertex(nodeId);
			});

			network.on('select', onSelect);
			$$('id_brix_search_tree').callEvent("onItemClick", [comboResponse.data.nodes[0].id])
			$.LoadingOverlay("hide", spinner);
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			$.LoadingOverlay("hide", spinner);
		}
	})

}
/**
 * @purpose This function for setting focus and getting selected node on center
 * @param nodeId
 */
function focusOnSelectedVertex(nodeId) {
	var focusoption = {
		//scale: 0.1,
		locked: true,
		animation: { // -------------------> can be a boolean too!
			duration: 120,
			easingFunction: 'linear'
		}
	};

	network.focus(nodeId, focusoption);
}
/**
 * @purpose This function for getting connected edges of selected  node 
 * @param nodeId
 * @author komal.shevane
 */
function getEdgesOfNode(nodeId) {
	return edges.get().filter(function (edge) {
		return edge.from === nodeId || edge.to === nodeId;
	});
}
/**
 * @purpose This function for getting vertex details of selected  node 
 * @param nodeId
 * @author komal.shevane
 */
function onSelect(properties, id) {
	//move previous node to its location
	//	if(pNodeId!=undefined && pX != undefined && pY != undefined)
	//	network.moveNode(pNodeId,pX,pY);
	var data;
	var name;

	if (id != null)
		data = id;
	else if (properties) {
		data = properties.nodes[0];

		var pos = network.getPositions(data);
		var top;
		for (var i in pos) {
			if (i) {
				var obj = {};
				obj['key'] = i;
				obj['value'] = pos[i];
				top = obj.value["y"];
			}
		}
		if (data) {
			$$('id_brix_search_tree').unselectAll();

		}
	}
	if(data != undefined){
		
		if((lastNode!=null || lastNode != undefined) && network.body.nodeIndices.indexOf(lastNode)!=-1){
			 var node = network.body.nodes[lastNode];
			  node.setOptions({
				  borderWidth:1,
			  });
				
		}
		  var node = network.body.nodes[data];
		  node.setOptions({
			  borderWidth:5,
		  });
	var grp = nodes.get(data).group;
	if (grp == "DataName") {
		webix.html.removeCss($$("launchBtn").$view, "disabled");
		webix.html.addCss($$("launchBtn").$view, "enabled");
	} else {
		webix.html.removeCss($$("launchBtn").$view, "enabled");
		webix.html.addCss($$("launchBtn").$view, "disabled");;
	}
	if (data) {
		
		
		$.ajax({
			type: "POST",
			url: "getVertexDetails",
			data: data,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (response) {
				//			  console.log(response)
				if (response.returncode == "403") {
					sessionOutPopup();
					$$("session_Out_Popup").show()
				}
				var data = response.data;
				var grpName;
				for (var i in data.group) {
					if (i) {
						var obj = {};
						obj['key'] = i;
						obj['value'] = data.group[i];
						//                     console.log(obj);
						grpName = obj.value
					}
				}

				groupName = data.vertexData["group"];
				document.getElementById("grpname").innerHTML = grpName + " Details";
				prevNode=grpName;
				var vertexData = [];
				for (var i in data.vertexData) {
					if (i) {
						var obj = {};
						obj['key'] = i;
						obj['value'] = data.vertexData[i];
						vertexData.push(obj)
						if (obj['key'] == "URL" || obj['key'] == "url" || obj['key'] == "Url") {
							launchUrl = obj['value']
						}
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
				var span;
				var div;
				$$("nodeDetails").clearAll();
				$$("nodeDetails").parse(vertexData);
				$$("nodeDetails").adjustRowHeight();
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$.LoadingOverlay("hide", spinner);
			}
		});
	}else{
		document.getElementById("grpname").innerHTML = prevNode + " Details";
	}
	lastNode=data;
	}
}
/**
 * @purpose This function for set simulation of graph
 * @param selcted new value i.e on or off
 * @author komal.shevane
 */
function setSimulation(newv) {

	var bphysics;
	if (newv == 'true')
		bphysics = true;
	else
		bphysics = false;

	var options

	if (bhierarchyBtn) {
		options = {
			edges: {
				smooth: {
					type: 'cubicBezier',
					forceDirection: 'horizontal',
					roundness: 0.4
				},
				font: {
					color: '#070303',
					size: 0,
				},
				width: 2,
				selectionWidth: 4,
				hoverWidth: 3,

			},
			nodes: {
				scaling: {
					min: 10,
					max: 30,
					label: {
						enabled: false,
						min: 8,
						max: 30,
						drawThreshold: 8,
						maxVisible: 10
					}
				},
			},
			physics: {
				enabled: bphysics
			},
			layout: {
				hierarchical: {
					direction: "LR",
					levelSeparation: 700,
					nodeSpacing: 10,
					treeSpacing: 5,
				}

			},
		};
	} else {
		options = {
			layout: {
				hierarchical: false,
				improvedLayout: false,
				randomSeed: 2
			},
			nodes: {
				scaling: {
					min: 50,
					max: 50,
				},
				size: 40,
				mass: 3,
			},
			physics: {
				enabled: bphysics
			}

		};
	}

	network.setOptions(options);
}
/**
 * @purpose This function enabling and disabling the labels of graph nodes
 * @param nodeId
 * @author komal.shevane
 */
function showLabel(newv) {
	var options
	if (newv == 'true')
		options = {
			edges: {

				font: {
					color: '#070303',
					size: 10,
				},
			},
			nodes: {
				font: {
					color: '#070303',
					size: 10,
				},
			},
		};
	else
		options = {
			edges: {
				font: {
					color: '#070303',
					size: 0,
				},
			},
			nodes: {
				font: {
					color: '#070303',
					size: 0,
				},
			}
		};
	network.setOptions(options);
}
/**
 * @purpose This function enabling and disabling the hierarchical node view 
 * @param nodeId
 * @author komal.shevane
 */
function ChangeHierarchicalLayout(newv) {
	bhierarchyBtn = newv;
	var options
	if (newv == true)
		options = {
			edges: {
				smooth: {
					type: 'cubicBezier',
					forceDirection: 'horizontal',
					roundness: 0.4
				},
				font: {
					color: '#070303',
					size: 0,
				},
				width: 2,
				selectionWidth: 4,
				hoverWidth: 3,

			},
			nodes: {
				scaling: {
					min: 10,
					max: 30,
					label: {
						enabled: false,
						min: 8,
						max: 30,
						drawThreshold: 8,
						maxVisible: 10
					}
				},
			},
			layout: {
				hierarchical: {
					direction: "LR",
					levelSeparation: 400,
					nodeSpacing: 20,
					treeSpacing: 10,
				}

			},
		};
	else {
		options = {
			layout: {
				hierarchical: false,
			},
			nodes: {
				scaling: {
					min: 50,
					max: 50,
				},
				size: 40,
				mass: 3,
			},

		};
	}
	network.setOptions(options);
}

/**
 * @purpose This function is to redirect the page on double click of node or click on launch button to new tab
 * @param seleced nodeId 
 * @author komal.shevane
 */
function launch(nodeId) {
	var grp = nodes.get(nodeId).group;
	/*if (grp == "DataName" && launchUrl!=null) {
	var url=launchUrl.split("'")[1]
	window.open(url);
	}*/
	var url = window.location.href;
	if (grp == "DataName") {
		var win = window.open("/GraphSearch/measurementview?key=" + nodeId);
	}
}
/**
 * @purpose This function is to expand the selected node
 * @param event object
 */
function expandGraphVertex(nodeId) {
	//Dynamic addition of node on doubleClick
	$.ajax({
		type: "POST",
		url: "getTagNumOutVerticesAndEdges",
		data: nodeId,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (response) {
			if (response.returncode == "403") {
				sessionOutPopup();
				$$("session_Out_Popup").show()
			}
			//			console.log(response.data)
			var vertexData = [];
			for (var i in response.data.vertexData) {
				if (i) {
					var obj = {};
					obj['key'] = i;
					obj['value'] = response.data.vertexData[i];
					vertexData.push(obj)
				}
			}
			var edgeData = [];
			for (var i in response.data.edges) {
				if (i) {
					var obj = {};
					obj['key'] = i;
					obj['value'] = response.data.edges[i];
					edgeData.push(obj)
				}
			}
			var expand = false;
			var nodeArray = nodes._data;
			var edgeArray = edges._data;
			if (vertexData.length != 0) {
				for (var i = 0; i < vertexData.length; i++) {
					if (!(vertexData[i].key in nodeArray)) {
						expand = true;
						nodes.add({
							id: vertexData[i].value["id"],
							label: vertexData[i].value["label"],
							group: vertexData[i].value["group"],
							level: vertexData[i].value["level"],
							title: vertexData[i].value["label"],
						});
					}
					if (uniqueGroups.indexOf(vertexData[i].value["group"]) == -1) {
						uniqueGroups.push(vertexData[i].value["group"]);
						legendNode.add({
							id: vertexData[i].value["id"],
							x: x,
							y: y + inc * step,
							label: vertexData[i].value["group"],
							group: vertexData[i].value["group"],
							value: 1,
							fixed: true,
							physics: false
						});
						inc++;
					}
				}
				if (expand == false) {
					customAlert("No Data", resourceObj.NoNodetoexpand_Lbl);
				}
			} else {
				customAlert("No Data", resourceObj.NoNodetoexpand_Lbl);
			}

			var existEdge = getEdgesOfNode(nodeId);
			var flag = false;
			for (var i = 0; i < edgeData.length; i++) {

				for (var j = 0; j < existEdge.length; j++) {
					if (edgeData[i].value["inVertex"] == existEdge[j].to && edgeData[i].value["outVertex"] == existEdge[j].from) {
						flag = false;
						break;
					} else {
						flag = true;
					}
				}
				try {
					if (flag) {
						edges.add({
							id: edgeData[i].value["id"],
							from: edgeData[i].value["outVertex"],
							to: edgeData[i].value["inVertex"],
							label: edgeData[i].value["label"],
							arrows: edgeData[i].value["arrows"],
						});
					}
				} catch (err) {
					//        	console.log(err)
				}
			}

		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			$.LoadingOverlay("hide", spinner);
		}
	})
}