/**
 * Popup for session time Out
 * @author skomal
 */
function sessionOutPopup() {
	webix.ui({
		view: "window",
		id: "session_Out_Popup",
		move: true,
		position: "top",
		css: "session_Out_PopupCss brix_popup",
		modal: true,
		head: "<span style='color:white'>Session Timed Out</span>",
		headHeight: 32,
		height: setHeightInPercentage(17),
		width: setWidthInPercentage(30),
		body: {
			rows: [{
					template: "<span style='vertical-align:center'>Your session has been expired, Please login again</span>",
					css: "delTextCss",
					inputHeight: 30,
				},
				{
					view: "button",
					label: "Logout",
					inputWidth: 80,
					css: "logoutBtnCss",
					align: "center",
					on: {
						onItemClick: function () {
							window.location.href = 'login'
						}
					}

				}
			]
		}
	});
}
/**
 * @purpose generic custom alert popup 
 * @param title of popup and message to display on popup
 * @author komal.shevane
 */
function customAlert(title, message) {
	webix.alert({
		title: title,
		css: "create_instanceCss brix_popup",
		autoheight: true,
		autowidth: true,
		ok: "OK",
		text: message,
		callback: function (result) {}
	});
}