<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head><title>Graph Search Portal</title>	</head>

<!-- <link rel="stylesheet" href="./resources/brixsearchstyle/vis-network5.0.0.min.css" type="text/css">
<script type="text/javascript" src="./resources/brixsearchjs/vis-network5.0.0.min.js"></script> -->

<link rel="stylesheet" href="./resources/brixsearchstyle/webix.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/foundation-icons.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/brix.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/brixsearchstyle.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/smfStyle.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/vis-network.min.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/networkStyle/vis.css" type="text/css">

<link rel="stylesheet" href="./resources/brixsearchstyle/bootstrap.min_original.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/genericcanvasstyle.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="./resources/brixsearchstyle/lobipanel.css" type="text/css">

<script type="text/javascript" src="./resources/brixsearchjs/angular.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/angular-ui-router.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/webix.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/index.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/loadingoverlay.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jquery.canvasjs.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/canvasjs.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/vis.js"></script>

<script type="text/javascript" src="./resources/brixsearchjs/lobipanel.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/bootstrap.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jquery-ui.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jspdf.min.js"></script>



<script type="text/javascript" src="./resources/angular/brixSearchApp.js"></script>
<!-- <script type="text/javascript" src="./resources/angular/controller/BrixSearch.js"></script> -->
<script type="text/javascript" src="./resources/angular/services/graphSevice.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/genericCanvas.js"></script>
<script type="text/javascript" src="./resources/angular/controller/BrixSearchController.js"></script>
<script type="text/javascript" src="./resources/angular/controller/BrixSearchTableView.js"></script>
<script type="text/javascript" src="./resources/angular/controller/BrixSearchTreeView.js"></script>
<script type="text/javascript" src="./resources/angular/controller/BrixSearchBubbleView.js"></script>
<!-- <script type="text/javascript" src="./resources/angular/controller/GraphController.js"></script> -->
<script type="text/javascript" src="./resources/angular/controller/popup.js"></script>


<!-- <script type="text/javascript" src="brixsearchjs/angularjs-editable-dropdown.js"></script> -->
<script>
var spinner = {
		image       : "",
		fontawesome : "fa fa-spinner fa-pulse fa-3x fa-fw",
		zIndex      : 2000010,
		fade        : false,
		color       : "rgba(0, 0, 0, 0.3)"
		};
		
var resourceObj;
var resource = new Object();
debugger
resource='<%= session.getAttribute("resource")%>';
if(resource=='null')
	window.location="login";
resourceObj=JSON.parse(resource)
 

</script>

<div id="id_brix_search" ng-app="brixSearchApp"  style="width:99.2%">
			<div class='BrixSearchCss' ng-controller="BrixSearchController"  id='id_brix_search_controller' webix-ui="brix_search_config" style="width:99.2%;">
		</div>
</div>
</html>
<script>


</script>
	