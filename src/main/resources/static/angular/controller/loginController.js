loginApp.controller("loginController", ["$scope", "$http", function ($scope, $http) {
	var language = 1;
	$scope.login_config = {
		autoheight: true,
		rows: [{
				gravity: 0.01,
				id: "navBar",
				minHeight: 30,
				css: "navBarCss",
				view: "flexlayout",
				cols: [{
					template: "<div class='iconContainer'><i class='webix_icon fa fa-line-chart iconCss'></i><span style='margin-left:20px;position: absolute;'>Graph Search Portal</span></div>",
					css: "titltcss",
					minWidth: 30,
				}]
			}, {},
			{
				height: 140
			},
			{
				cols: [{},
					{
						view: "form",
						scroll: false,
						position: "center",
						width: 360,
						id: "loginForm",
						elements: [{
								view: "text",
								placeholder: "User Name",
								name: "username"
							},
							{
								view: "text",
								type: "password",
								placeholder: "Password",
								name: "password"
							},
							{
								margin: 5,
								cols: [{
									view: "button",
									label: "Login",
									id: "login",
									type: "form",
									css: "login",
									click: loginFunction
								}, ]
							}
						],
						//			      	  rules:{
						//			              "userName":webix.rules.isNotEmpty,
						//			              "password":webix.rules.isNotEmpty
						//			          }
						/*on:{
							"onAfterRender":function() {
								$$('loginForm').setPosition(300, 300);
								
							}
						}*/
					},
					{}
				]
			},
			{
				height: 210,
				css: "loginPageOutLineStyle"
			},
			{
				template: "<div class='copyright'>A solution by <img style='width:65px' src='./resources/images/iasys.jpg'> <br> Engineering | IT | Consultancy </div>",
				height: 60,
				css: "footerCopyright"
			}
		]
	}

	function loginFunction() {
		$.LoadingOverlay("show", spinner);
		var form = $$("loginForm");
		//		console.log(document.getElementById("Language").value);
		//		var lang=document.getElementById("Language").value;
		//		if(lang=="1")
		//				language=1;
		//			else{
		//				language=2;
		//			}
		var loginValues = form.getValues();
		if (form.validate() && loginValues.username != "" && loginValues.password != "") {
			$http({
				url: 'LoginServlet',
				method: "POST",
				params: {
					"string": JSON.stringify(loginValues),
					"language": language
				}
			}).then(function (response) {
				console.log(response);
				if (response.data === "brixsearch") {
					window.location = "brixsearch";
					$.LoadingOverlay("hide", spinner);
				} else {
					$.LoadingOverlay("hide", spinner);
					webix.alert({
						title: "Login Fail",
						css: "create_instanceCss brix_popup",
						autoheight: true,
						autowidth: true,
						ok: "OK",
						text: response.data,
						callback: function (result) {
							form.clear();
						}
					});
				}
			});
		} else {
			$.LoadingOverlay("hide", spinner);
			webix.alert({
				title: "Login Fail",
				autoheight: true,
				autowidth: true,
				ok: "OK",
				text: "Please fill credentials",
				callback: function (result) {}
			});
		}

	}
	webix.UIManager.addHotKey("enter", function () {
		loginFunction();
	});

}]);