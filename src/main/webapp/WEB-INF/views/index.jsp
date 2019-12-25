<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <title>Login Page</title> -->
<title>Graph Search Portal</title>	
    <meta name="viewport" content="width=device-width, user-scalable=no" />
	<meta charset="UTF-8">
	
    <link rel="stylesheet" href="./resources/css/webix.css" type="text/css">
     <link rel="stylesheet" href="./resources/css/bootstrap.min.css" type="text/css">
         <link rel="stylesheet" href="./resources/css/brix.css" type="text/css">
    <link rel="stylesheet" href="./resources/css/style.css">
    
    <script src="./resources/js/angular.min.js" type="text/javascript"></script>
    <script src="./resources/js/angular-ui-router.min.js"></script>
    <script src="./resources/js/webix.js" type="text/javascript"></script>  
	<script src="./resources/js/index.js" type="text/javascript"></script>
	<script src="./resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
	
	<script src="./resources/js/jquery-1.11.1.min.js" type="text/javascript"></script>
	<script src="./resources/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="./resources/js/jquery-ui.js" type="text/javascript"></script>
	<script src="./resources/js/jquery.dd.min.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="./resources/js/loadingoverlay.min.js"></script>
	<script src="./resources/angular/app.js"></script>
	<script src="./resources/angular/controller/loginController.js"></script>
	
	<script>
	var spinner = {
			image       : "",
			fontawesome : "fa fa-spinner fa-pulse fa-3x fa-fw",
			zIndex      : 2000010,
			fade        : false,
			color       : "rgba(0, 0, 0, 0.3)"
			};
	(function() {
	    var link = document.querySelector("link[rel*='icon']") || document.createElement('link');
	    link.type = 'image/x-icon';
	    link.rel = 'shortcut icon';
	    link.href = './resources/images/iasys.ico';
	    document.getElementsByTagName('head')[0].appendChild(link);
	})();
	</script>
	
	<style>
		 .webix_template{
/* 				background: url("login_screen_ICONS.png"); */
			    background-origin: content-box;
			    background-position: center;
		 }
		 .webix_el_text{
		 	height:50px !important;
		 }
		 .webix_el_box{
		 	height:50px !important; 
		 }
 		 .webix_el_text input {
		    border-radius: 25px !important;
		    padding: 14px 20px;
		    height: 46px;
		    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif!important;
			font-weight:bold!important;
		 } 
		input::-webkit-input-placeholder {
		font-size: 16px!important;
		font-weight:bold!important;
		}
		input:-ms-input-placeholder {  
		font-size: 14px!important;
		font-weight:lighter!important;
		}
		 .webix_el_button{
		 	height:50px !important;
		 	
		 }
		 .webix_el_button button{
		 	border-radius:23px !important;
		 	background-color:#CC6B2A !important;
		 	color:#fff !important;
		 	font-size: 16px !important;
			font-weight:bold;
		 }
		 .changePassword .webix_el_box button{
		 	    background-color: transparent !important;
			    color: #23527c !important;
			    border-width: 0px !important;
			    font-weight: normal;
			    font-size: 14px !important;
		 }
		.webix_el_button button:hover{
			color:#ccc !important;
		}
		
	@media only screen and (min-device-width: 500px) and (max-device-width: 1024px){
     		
			.webix_view.webix_layout_line{
			 	overflow-y:scroll;
			 } 
			  body.webix_full_screen{
			 	overflow-y:scroll !important;
			 } 
			 .webix_view.webix_layout_line > .webix_view{
			 	overflow-y:scroll !important;
			 } 
			 .webix_template .webix_view{
			 	overflow-y:hidden !important;
			 }
			
		}
		
		
		
	</style>
</head>
<body>
<body ng-app="loginModel">
		<div ng-controller='loginController'  webix-ui view="layout" css="loginModel">
			<div webix-ui="login_config" id="loginModel">
				
			</div>
		</div>	
</body>


</body>
</html>