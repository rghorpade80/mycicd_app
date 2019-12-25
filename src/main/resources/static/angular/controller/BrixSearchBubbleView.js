var filterDatatableObj;
var nodesArray = [];
var edgesArray = [];
var nodes;
var edges;
var fnode;
var buniqueGroups;
var x = 1;
var y = -360;
var step = 50;
var inc = 1;
/**
 * @purpose Show Table View
 * @param $scope,ele,filterObj
 * @author Tejal
 */
function showBubbleView($scope, ele, filterObj, $compile, $http) {
	webix.ui({
		container: ele,
		gravity: 1,
		cols: [{
			gravity: 0.5,
			height: 800,
			view: "template",
			css: "borderRemoveStyle",
			template: "<div style='width:100px;' id='bubbleLegends'></div>"
		}, {
			gravity: 2.5,
			rows: [{
				gravity: 0.2,
				minHeight: 50,
				cols: [{
					template: resourceObj.Simulation_lbl,
					width: 80,
					gravity: 0.3,
					css: "borderRemoveStyle"
				}, {
					view: "toggle",
					type: "icon",
					css: "access_tgl",
					onIcon: "fas fa-toggle-on",
					offIcon: "fas fa-toggle-off",
					value: "1",
					height: 40,
					width: 60,
					on: {
						"onChange": function (newv, oldv) {
							setSimulation(newv);
						}
					}
				}, {
					template: resourceObj.ShowLabel,
					width: 120,
					gravity: 0.3,
					css: "borderRemoveStyle"
				}, {

					view: "toggle",
					type: "icon",
					css: "access_tgl",
					onIcon: "fas fa-toggle-on",
					offIcon: "fas fa-toggle-off",
					value: "0",
					height: 40,
					inputWidth: 60,
					on: {
						"onChange": function (newv, oldv) {
							showLabel(newv)
						}
					}
				}, {

					view: "button",
					id: "expandBtn",
					value: "Expand Node",
					type: "form",
					inputWidth: 100,
					css: "brix_button",
					//					    disabled:true
					on: {
						"onItemClick": function () {
							//					    		console.log(network.getSelectedNodes()[0])
							var nodeId = network.getSelectedNodes()[0];
							inc++;
							expandNode(nodeId);
						}
					}
				}, {

					view: "button",
					id: "blaunchBtn",
					value: "Launch",
					type: "form",
					inputWidth: 100,
					css: "brix_button",
					//					    disabled:true
					on: {
						"onItemClick": function () {
							var nodeId = network.getSelectedNodes()[0];
							launch(nodeId);
						}
					}

				}]
			}, {
				height: 800,
				view: "template",
				template: '<div id="bubbleViewContainer"><div class="vis-network" tabindex="900" style="position: relative; overflow: hidden;touch-action: pan-y; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"><canvas width="800" height="800" style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); width: 100%; height: 100%;"></canvas></div></div>',
				css: 'graphContainerCss clrBorderStyle',

			}]

		}, {
			view: "resizer"
		}, {
			view: "accordion",
			cols: [{
				id: "BdetailView",
				collapsed: true,
				header: "<span id='Bgrpname' style='text-align:center'></span>",
				css: "detailsAccStyle",
				body: {

					css: "detailContainerStyle",
					autoheight: true,
					rows: [{
						view: "datatable",
						rowHeight: 30,
						scroll: "auto",
						tooltip: true,
						autoheight: true,
						css: "brix_datatable_2 nodetailsGridStyle rows",
						id: "BnodeDetails",
						fixedRowHeight: false,
						select: false,
						header: false,
						columns: [{
							id: "key",
							header: "key",
							sort: "string",
							width: 80,
							fillspace: true,

						}, {
							id: "value",
							header: "value",
							sort: "string",
							fillspace: true,
						}],
						data: []
					}, {
						height: 40
					}, {
						height: 30,
						css: "relHeadStyle",
						template: resourceObj.Relation_lbl
					}, {
						view: "datatable",
						rowHeight: 30,
						scroll: "auto",
						tooltip: true,
						autoheight: true,
						css: "brix_datatable_2 nodetailsGridStyle rows",
						id: "BlinkDetailView",
						select: false,
						header: false,
						columns: [{
							id: "label",
							header: "Relation",
							sort: "string",
							width: 80,
							fillspace: true,

						}, {
							id: "direction",
							header: "Direction",
							template: "<span id='#id#' data-inVertex='#inVertex#' data-inVertexLabel='#inVertexLabel#' data-outVertex='#outVertex#'  data-outVertexLabel='#outVertexLabel#'class='#direction#'  onclick='showEdge(this)'></span>",
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
					if (network)
						onBubbleSelect(null, network.getSelectedNodes()[0])
				},
				onAfterCollapse: function () {
					if (network)
						onBubbleSelect(null, network.getSelectedNodes()[0]);
				}
			}

		}]
	});


}
/**
 * @purpose generate bubbleView
 */
function drawBubbleView(response) {
	var tempNodes = response.data;
	var groups = [];
	for (var i = 0; i < tempNodes.length; i++) {
		groups.push(tempNodes[i].group);
	}

	buniqueGroups = groups.filter(function (item, pos) {
		return groups.indexOf(item) == pos;
	});

	var x = 1;
	var y = -360;
	var step = 50;
	var inc = 1;
	var legNodes = [];
	for (i = 0; i < buniqueGroups.length; i++) {
		legNodes.push({
			id: buniqueGroups[i],
			x: x,
			y: y + inc * step,
			label: buniqueGroups[i],
			group: buniqueGroups[i],
			value: 1,
			fixed: true,
			physics: false
		});
		inc++;
	}
	// create a network for legends
	var legendContainer = document.getElementById('bubbleLegends');
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
		}
	};
	legendnetwork = new vis.Network(legendContainer, legendData, LegendOption);

	// create a network for graph
	nodes = new vis.DataSet(response.data);
	edges = new vis.DataSet(edgesArray);
	var container = document.getElementById('bubbleViewContainer');
	var data = {
		nodes: nodes,
		edges: edges
	};
	var options = {
		nodes: {
			shape: 'dot',
			size: 16,
			borderWidth: 1,
			borderWidthSelected: 3,


			color: {

				border: '#222222',
				background: '#666666'
			},
			font: {
				color: '#070303',
				size: 0,
			}
		},
		edges: {
			color: 'lightgray',
			font: {
				color: '#070303',
				size: 0,
			}
		},
		interaction: {
			tooltipDelay: 200,
			zoomView: false,
			navigationButtons: true,
			keyboard: true,
		}
	};
	network = new vis.Network(container, data, options);
	network.on('select', onBubbleSelect);
	network.on("doubleClick", function (params) {
		var nodeId = params.nodes[0];
		launch(nodeId)
	});

}


function onBubbleSelect(properties) {
	console.log(properties);
	var data;
	var name;
	if (properties)
		data = properties.nodes[0];
	if (data) {
		$.ajax({
			type: "POST",
			url: "getVertexDetails",
			data: data,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (response) {
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
				//			  console.log(data.vertexData["Bgroup"]);
				//			   groupName=data.vertexData["Bgroup"];
				document.getElementById("Bgrpname").innerHTML = grpName + " Details";
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
				console.log(vertexData);
				console.log(edges);
				$$("BnodeDetails").clearAll();
				$$("BnodeDetails").parse(vertexData);
				$$("BnodeDetails").adjustRowHeight();
				$$("BlinkDetailView").clearAll();
				$$("BlinkDetailView").parse(edges);
				var span;
				var div;


				//			  drawLinkForEdges(edges);

			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$.LoadingOverlay("hide", spinner);
			}
		});
	}
}

function expandNode(nodeId) {

	$.ajax({
		type: "POST",
		url: "getTagNumOutVerticesAndEdges",
		data: nodeId,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (response) {
			console.log(response.data)
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
			var nodeArray = nodes._data;
			var edgeArray = edges._data;
			for (var i = 0; i < vertexData.length; i++) {
				if (!(vertexData[i].key in nodeArray)) {
					nodes.add({
						id: vertexData[i].value["id"],
						label: vertexData[i].value["label"],
						group: vertexData[i].value["group"],
						title: vertexData[i].value["title"],
					});
				}
				if (buniqueGroups.indexOf(vertexData[i].value["group"]) == -1) {
					buniqueGroups.push(vertexData[i].value["group"]);
					legendNode.add({
						id: vertexData[i].value["id"],
						x: x,
						y: y + inc * step,
						label: vertexData[i].value["label"],
						group: vertexData[i].value["group"],
						value: 1,
						fixed: true,
						physics: false
					});
					inc++;
				}
			}

			var existEdge = getEdgesOfNode(nodeId);
			console.log(existEdge);
			console.log(edgeData);
			var flag = false;
			if (existEdge.length > 0) {
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
			} else {
				for (var i = 0; i < edgeData.length; i++) {
					edges.add({
						id: edgeData[i].value["id"],
						from: edgeData[i].value["outVertex"],
						to: edgeData[i].value["inVertex"],
						label: edgeData[i].value["label"],
						arrows: edgeData[i].value["arrows"],
					});
				}
			}

		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			$.LoadingOverlay("hide", spinner);
		}
	})


}

function showEdge(obj) {
	console.log(obj);
	var edgeId = obj.getAttribute("id");
	var inV = obj.getAttribute("data-invertex");
	var outV = obj.getAttribute("data-outvertex");
	var inVLabel = obj.getAttribute("data-invertexLabel");
	var outVLabel = obj.getAttribute("data-outvertexLabel");
	var nodeArray = nodes._data;
	console.log(nodeArray)
	/*	if(!(outV in nodeArray)){
			nodes.add({
			    id: outV,
			    label: outVLabel,
			});
		}
		if(!(inV in nodeArray)){
			nodes.add({
			    id: inV,
			    label: inVLabel,
			});
		}
		if(inV in nodeArray && outV in nodeArray)
		edges.add({
		    id: edgeId,
		    from: outV,
		    to: inV
		});*/

}