
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

<script type="text/javascript" src="./resources/brixsearchjs/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/angular.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/angular-ui-router.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/webix.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/index.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/loadingoverlay.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jquery.canvasjs.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/canvasjs.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/lobipanel.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/bootstrap.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jquery-ui.min.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/genericCanvas.js"></script>
<script type="text/javascript" src="./resources/brixsearchjs/jspdf.min.js"></script>
<script type="text/javascript" src="./resources/angular/brixSearchApp.js"></script>
<script type="text/javascript" src="./resources/angular/controller/MeasurementViewController.js"></script>
<script>
var spinner = {
		image       : "",
		fontawesome : "fa fa-spinner fa-pulse fa-3x fa-fw",
		zIndex      : 2000010,
		fade        : false,
		color       : "rgba(0, 0, 0, 0.3)"
		};
var url = window.location.href;
var params = window.location.href.split("?")[1];
var params1=params.split("=")[1];
var nodeId;
if(params1.indexOf("!")!=-1){
 nodeId=params1.split("!")[1];
}else{
 nodeId=params1
}
</script>
<div ng-app="graphApp" onload="alert()" id="newGraphTab" webix-ui="newGraphTabConfig" ng-controller="MeasurementViewController"><script>$.LoadingOverlay("show", spinner);</script></div>