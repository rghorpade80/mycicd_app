<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
   <head>
    <title>Graph Search</title>	
	<meta charset="UTF-8">	
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- <link rel="stylesheet" href="/resources/css/bootstrap.min_original.css" type="text/css"> -->
    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> -->
    <link rel="stylesheet" href="./resources/css/webix.css" type="text/css">
    <link rel="stylesheet" href="./resources/css/style.css">
    <script src="./resources/js/angular.min.js" type="text/javascript"></script>
    <script src="./resources/js/angular-ui-router.min.js"></script>
<!--     <script src="./resources/js/webix.js" type="text/javascript"></script>   -->
    <script src="./resources/js/jquery.canvasjs.min.js" type="text/javascript"></script>  
	<script src="./resources/js/index.js" type="text/javascript"></script>
	<script src="./resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
	<script src="./resources/js/jqueryui.js" type="text/javascript"></script>
	<script src="./resources/js/loadingoverlay.min.js"></script>
	<script src="./resources/js/jquery.simplePagination.js"></script>
	
	<link rel="stylesheet" href="./resources/css/simplePagination.css">
	<script src="./resources/angular/app.js"></script>
	<script src="./resources/angular/controller/searchController.js"></script>
	<script>
	(function() {
	    var link = document.querySelector("link[rel*='icon']") || document.createElement('link');
	    link.type = 'image/x-icon';
	    link.rel = 'shortcut icon';
	    link.href = './resources/images/iasys.ico';
	    document.getElementsByTagName('head')[0].appendChild(link);
	})();
	</script>
</head>
<body ng-app="searchModel">
		<div ng-controller='searchController'  webix-ui view="layout" css="searchModel">
			<div webix-ui="search_config" id="searchModel"></div>
		</div>	
	
</body>
</html>