var colorArray = ['#1BCDD1', '#EC5657', '#4661EE', '#8FAABB', '#B08BEB', '#3EA0DD', '#F5A52A', '#23BFAA', '#FAA586', '#6D78AD', '#DF7970', '#C5A2B6', '#C9D45C', '#ff66ff', '#C39762', '#5A55A3', '#339933', '#00ffff', '#6600cc', '#996633'];
var meaQuantityId,meaResultId;
var dynamicDataCompareObj = {};
var size=1;
var sessionKey='555555555';
/*var canvasResourceObj={
		'brix_lbl_index':'Index',
		'brix_lbl_line_chart':'Line Chart',
		'brix_lbl_scatter_chart':'Scatter Chart',
		'brix_lbl_area_chart':'Area Chart',
		'brix_lbl_data_view':'Data View',
		'brix_lbl_data_compare':'Data Compare',
		"brix_lbl_channel_limi_validation":"Can not drop more than 20 channels",
		"brix_lbl_failed_to_load_data":"Failed to load data",
		"brix_lbl_dropped_measurement_already_Exists":"Dropped measurement already exists",
		"brix_lbl_can_not_drop":"Can not drop ",
		"brix_lbl_node":"node",
		"brix_lbl_channel_selection_validation":"You can not select this channel as x-axis as some of the measurements may not contain this channel",
		"brix_lbl_data_availability_for_channel":"Data is not available for selected channels",
		"brix_lbl_data_availability_for_channel_save_image":"Channels are  not available to save image",
		"brix_lbl_data_availability_for_channel_to_export":"Channels are not available to export",
		"brix_lbl_channel_already_dropped":"This channel is already dropped",
		"brix_lbl_channel_dont_have_data":"Channel don't have data",
		"brix_lbl_alise_name":"This alias name already exists",
		"brix_measurement_is_not_single_value":"Dropped Measurement is not a single value measurement",
		"brix_lbl_drp_channels_here":"Drop channels here",
		"brix_lbl_drp_measurements_here":"Drop Measurements here",
		"brix_lbl_generate_pdf":"Generate PDF",
		"brix_lbl_save_image":"Save Image",
		"brix_lbl_channel_list":"Channels List",
		"brix_lbl_select_channels_for_canvas":"Please select channels in order to draw chart",
		"brix_lbl_measurement_name":"Measurement Name",
		"brix_lbl_save":"Save",
		"brix_lbl_download_file":"Download File",
		"brix_lbl_cancel":"Cancel",
		"brix_lbl_change_alise":"Change Alias",
	    "brix_lbl_ok":"OK",
	     "brix_lbl_aliseval":"Alias name cn not be blank",
	     "brix_lbl_channel_selection_validation_on_drop":"Can not drop this channel. Please make sure selected measurement is having the x-axis channel",

}*/
var resourceObj;
var treeData=[{"nodeId":"MeaResult:3088","open":false,"value":"measurement1","nodeType":"MeaResult","data":[{"nodeId":"MeaQuantity:71624","value":"Transmission Type","actualId":"MeaQuantity:71624","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71623","value":"Transmission Oil","actualId":"MeaQuantity:71623","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71622","value":"Engine Oil","actualId":"MeaQuantity:71622","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"id":"MeaQuantity:71621","value":"Engine Oil Temperature","actualId":"MeaQuantity:71621","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71620","value":"Cooling Water Temperature","actualId":"MeaQuantity:71620","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71619","value":"HEX File","actualId":"MeaQuantity:71619","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71618","value":"ECU Type","actualId":"MeaQuantity:71618","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71617","value":"Test Purpose","actualId":"MeaQuantity:71617","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71616","value":"Test Title","actualId":"MeaQuantity:71616","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:71625","value":"IGLOG","actualId":"MeaQuantity:71625","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"deg"},{"nodeId":"MeaQuantity:71627","value":"Measurement End Ne","actualId":"MeaQuantity:71627","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"rpm"},{"nodeId":"MeaQuantity:71626","value":"Measurement Start Ne","actualId":"MeaQuantity:71626","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"rpm"},{"nodeId":"MeaQuantity:71628","value":"CO","actualId":"MeaQuantity:71628","meaId":"MeaResult:3088","nodeType":"MeaQuantity","unit":"%"}],"internalId":"MeaResult:3088"},{"nodeId":"MeaResult:3090","open":false,"nodeType":"MeaResult","value":"A027-1-1-2-3-100002-TestCond-1","data":[{"nodeId":"MeaQuantity:71638","value":"Point_10","actualId":"MeaQuantity:71638","meaId":"MeaResult:3090","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:71637","value":"Point_9","actualId":"MeaQuantity:71637","meaId":"MeaResult:3090","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:71636","value":"Point_8","actualId":"MeaQuantity:71636","meaId":"MeaResult:3090","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:71635","value":"Point_7","actualId":"MeaQuantity:71635","meaId":"MeaResult:3090","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:71634","value":"Point_6","actualId":"MeaQuantity:71634","meaId":"MeaResult:3090","nodeType":"MeaQuantity","unit":"G"}],"internalId":"MeaResult:3090"},{"nodeId":"MeaResult:3408","open":false,"value":"A027-1-1-2-3-100002-TestCond-1","nodeType":"MeaResult","data":[{"nodeId":"MeaQuantity:78460","value":"Point_10","actualId":"MeaQuantity:78460","meaId":"MeaResult:3408","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78459","value":"Point_9","actualId":"MeaQuantity:78459","meaId":"MeaResult:3408","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78458","value":"Point_8","actualId":"MeaQuantity:78458","meaId":"MeaResult:3408","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"3.4","value":"Point_7","actualId":"MeaQuantity:78457","meaId":"MeaResult:3408","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78456","value":"Point_6","actualId":"MeaQuantity:78456","meaId":"MeaResult:3408","nodeType":"MeaQuantity","unit":"G"}],"internalId":"MeaResult:3408"},{"nodeId":"MeaResult:3415","open":false,"value":"A027-1-1-2-3-100002-TestCond-1","nodeType":"MeaResult","data":[{"nodeId":"MeaQuantity:78500","value":"Point_10","actualId":"MeaQuantity:78500","meaId":"MeaResult:3415","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78499","value":"Point_9","actualId":"MeaQuantity:78499","meaId":"MeaResult:3415","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78498","value":"Point_8","actualId":"MeaQuantity:78498","meaId":"MeaResult:3415","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78497","value":"Point_7","actualId":"MeaQuantity:78497","meaId":"MeaResult:3415","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78496","value":"Point_6","actualId":"MeaQuantity:78496","meaId":"MeaResult:3415","nodeType":"MeaQuantity","unit":"G"}],"internalId":"MeaResult:3415"},{"nodeId":"MeaResult:3420","open":false,"value":"A027-1-1-2-3-100002-TestCond-1","nodeType":"MeaResult","data":[{"nodeId":"MeaQuantity:78525","value":"Point_10","actualId":"MeaQuantity:78525","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78524","value":"Point_9","actualId":"MeaQuantity:78524","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78523","value":"Point_8","actualId":"MeaQuantity:78523","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78522","value":"Point_7","actualId":"MeaQuantity:78522","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:78521","value":"Point_6","actualId":"MeaQuantity:78521","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"G"},{"nodeId":"MeaQuantity:53144","value":"Test Purpose","actualId":"MeaQuantity:53144","meaId":"MeaResult:3420","nodeType":"MeaQuantity","unit":"-"}],"internalId":"MeaResult:3420"},{"nodeId":"MeaResult:3376","open":false,"value":"0?????","nodeType":"MeaResult","data":[{"nodeId":"MeaQuantity:75956","value":"TIME","actualId":"MeaQuantity:75956","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"msec"},{"nodeId":"MeaQuantity:75957","value":"TIME_STEP","actualId":"MeaQuantity:75957","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"msec"},{"nodeId":"MeaQuantity:75958","value":"TIME_CYC","actualId":"MeaQuantity:75958","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"msec"},{"nodeId":"MeaQuantity:75959","value":"Ah-STEP","actualId":"MeaQuantity:75959","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75960","value":"SOC","actualId":"MeaQuantity:75960","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"%"},{"nodeId":"MeaQuantity:75961","value":"VOLT","actualId":"MeaQuantity:75961","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"V"},{"nodeId":"MeaQuantity:75962","value":"TMP","actualId":"MeaQuantity:75962","nodeType":"MeaQuantity","meaId":"MeaResult:3376","unit":"-"},{"nodeId":"MeaQuantity:75963","value":"CUR","actualId":"MeaQuantity:75963","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"A"},{"nodeId":"MeaQuantity:75964","value":"??1","actualId":"MeaQuantity:75964","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75965","value":"??2","actualId":"MeaQuantity:75965","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75966","value":"??3","actualId":"MeaQuantity:75966","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75967","value":"??4","actualId":"MeaQuantity:75967","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75968","value":"??5","actualId":"MeaQuantity:75968","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75969","value":"??6","actualId":"MeaQuantity:75969","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75970","value":"??7","actualId":"MeaQuantity:75970","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75971","value":"??8","actualId":"MeaQuantity:75971","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75972","value":"V_BC0000","actualId":"MeaQuantity:75972","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75973","value":"V_BC0001","actualId":"MeaQuantity:75973","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75974","value":"V_BC0002","actualId":"MeaQuantity:75974","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75975","value":"V_BC0003","actualId":"MeaQuantity:75975","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75976","value":"V_BC0004","actualId":"MeaQuantity:75976","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75977","value":"V_BC0005","actualId":"MeaQuantity:75977","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75978","value":"V_BC0006","actualId":"MeaQuantity:75978","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75979","value":"V_BC0007","actualId":"MeaQuantity:75979","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75980","value":"V_BC0008","actualId":"MeaQuantity:75980","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75981","value":"V_BC0009","actualId":"MeaQuantity:75981","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75982","value":"V_BC0010","actualId":"MeaQuantity:75982","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75983","value":"V_BC0011","actualId":"MeaQuantity:75983","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75984","value":"V_BC0012","actualId":"MeaQuantity:75984","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75985","value":"V_BC0013","actualId":"MeaQuantity:75985","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75986","value":"V_BC0014","actualId":"MeaQuantity:75986","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75987","value":"V_BC0015","actualId":"MeaQuantity:75987","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75988","value":"V_BC0016","actualId":"MeaQuantity:75988","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75989","value":"V_BC0017","actualId":"MeaQuantity:75989","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75990","value":"V_BC0018","actualId":"MeaQuantity:75990","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75991","value":"V_BC0019","actualId":"MeaQuantity:75991","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75992","value":"V_BC0020","actualId":"MeaQuantity:75992","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75993","value":"V_BC0021","actualId":"MeaQuantity:75993","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75994","value":"V_BC0022","actualId":"MeaQuantity:75994","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75995","value":"V_BC0023","actualId":"MeaQuantity:75995","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75996","value":"V_BC0024","actualId":"MeaQuantity:75996","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75997","value":"V_BC0025","actualId":"MeaQuantity:75997","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75998","value":"V_BC0026","actualId":"MeaQuantity:75998","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:75999","value":"V_BC0027","actualId":"MeaQuantity:75999","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76000","value":"V_BC0028","actualId":"MeaQuantity:76000","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76001","value":"V_BC0029","actualId":"MeaQuantity:76001","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76002","value":"V_BC0030","actualId":"MeaQuantity:76002","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76003","value":"V_BC0031","actualId":"MeaQuantity:76003","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76004","value":"V_BC0032","actualId":"MeaQuantity:76004","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76005","value":"V_BC0033","actualId":"MeaQuantity:76005","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76006","value":"V_BC0034","actualId":"MeaQuantity:76006","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76007","value":"V_BC0035","actualId":"MeaQuantity:76007","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76008","value":"V_BC0036","actualId":"MeaQuantity:76008","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76009","value":"V_BC0037","actualId":"MeaQuantity:76009","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76010","value":"V_BC0038","actualId":"MeaQuantity:76010","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76011","value":"V_BC0039","actualId":"MeaQuantity:76011","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76012","value":"V_BC0040","actualId":"MeaQuantity:76012","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76013","value":"V_BC0041","actualId":"MeaQuantity:76013","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"},{"nodeId":"MeaQuantity:76014","value":"V_BC0042","actualId":"MeaQuantity:76014","meaId":"MeaResult:3376","nodeType":"MeaQuantity","unit":"-"}],"internalId":"MeaResult:3376"},{"returncode":1,"message":"Channel List","nodeId":"MeaResult:3444","open":false,"value":"measurement2","nodeType":"MeaResult","data":[{"value":"Point_5","MinValue":"0.762","LocalColumnId":"81249","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3518","channelkey":"MeaQuantity:81229","nodeId":"MeaQuantity:81229","nodeType":"MeaQuantity","MaxValue":"0.762","AvgValue":"null","Unit":"G"},{"value":"Point_4","MinValue":"0.759","LocalColumnId":"81248","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3518","channelkey":"MeaQuantity:81228","nodeId":"MeaQuantity:81228","nodeType":"MeaQuantity","MaxValue":"0.759","AvgValue":"null","Unit":"G"},{"value":"Point_3","MinValue":"0.76","LocalColumnId":"81247","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3518","channelkey":"MeaQuantity:81227","nodeId":"MeaQuantity:81227","nodeType":"MeaQuantity","MaxValue":"0.76","AvgValue":"null","Unit":"G"},{"value":"Point_2","MinValue":"0.76","LocalColumnId":"81246","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3518","channelkey":"MeaQuantity:81226","nodeId":"MeaQuantity:81226","nodeType":"MeaQuantity","MaxValue":"0.76","AvgValue":"null","Unit":"G"},{"value":"Point_1","MinValue":"28.1","LocalColumnId":"81245","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3518","channelkey":"MeaQuantity:81225","nodeId":"MeaQuantity:81225","nodeType":"MeaQuantity","MaxValue":"28.1","AvgValue":"null","Unit":"G"}]},{"returncode":1,"message":"Channel List","nodeId":"MeaResult:3445","open":false,"value":"measurement3","nodeType":"MeaResult","data":[{"value":"Point_5","MinValue":"0.654","LocalColumnId":"81254","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3519","nodeId":"MeaQuantity:81234","MaxValue":"0.654","nodeType":"MeaQuantity","AvgValue":"null","Unit":"G"},{"value":"Point_4","MinValue":"0.9650000000000001","LocalColumnId":"81253","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3519","nodeId":"MeaQuantity:81233","MaxValue":"0.9650000000000001","nodeType":"MeaQuantity","AvgValue":"null","Unit":"G"},{"value":"Point_3","MinValue":"23.2","LocalColumnId":"81252","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3519","nodeId":"MeaQuantity:81232","nodeType":"MeaQuantity","MaxValue":"23.2","AvgValue":"null","Unit":"G"},{"value":"Point_2","MinValue":"0.85","LocalColumnId":"81251","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3519","nodeId":"MeaQuantity:81231","MaxValue":"0.85","nodeType":"MeaQuantity","AvgValue":"null","Unit":"G"},{"value":"Point_1","MinValue":"25.0","LocalColumnId":"81250","DataType":"class java.lang.Double","SubmatrixId":"SubMatrix:3519","nodeId":"MeaQuantity:81230","nodeType":"MeaQuantity","MaxValue":"25.0","AvgValue":"null","Unit":"G"}]}]
function showCanvasWindow(count,id){
	webix.ui({
		 view:"window",
		  height:600,
		  width:1000,
		  left:50, top:50,
		  id:'id_window',
		  move:true,
		  position:'center',
		  css:'brix_window',
		  scroll:true,
		  headHeight: 28, // Adjust headheight
		  head: "<span class='brix_window_header_title'>Header</span><span class='brix_window_close' onclick='closeWin()'>&times;</span>",
		  body:{
			rows:[
			 {
				 view:'template',
				 css:'windowBodyContainer',
			     template:'<div id="id_window_container" style="height:auto"></div>'
			 }
			 ]
	      },
	      on:{
	    	  onshow:function(){
	    		  var count=0;
	    		  var canvasJsonObj={
				     "canvasperrow": 1,
				     "chartoptions": ["Line Chart", "Scatter Chart", "Area Chart"],
				     "dataviewoptions": ["Data View", "Data Compare"],
				     "exportoptions": ["PDF", "IMAGE"],
				     "canvastitle": 1,
				     "axistitle": 1,
				     "maxnoofcanvases": 10,
				     "maximize":0,
				     "close":0
    			  }
	    		  createCanvasTemplateView(count, id, 'id_window_container',canvasJsonObj);
	    		  count=window["manageCount_"+actualString].shift();
		    	  appendLobipanelView(count, 'id_panel_'+id,id,canvasJsonObj)
	    	  }
	      }
	}).show();
}

function closeWin(){
	$$('id_window').close();
}
/*************************************************************************/
/**
 * @purpose Initial canvas view
 * @param count, id, containerId
 * @author Tejal
 */

//function createCanvasTemplateView(count, id, containerId,canvasJsonObj,resourceObj){
function createCanvasTemplateView(count, id, containerId,canvasJsonObj,resourceObj, authenticationKey, url){
//	console.log("authenticationKey :",authenticationKey);
//	 console.log("url",url);
	resourceObj=resourceObj;
//	console.log(resourceObj);
	var panelId='id_panel_'+id;
	actualString=id;
	window["manageCount_"+actualString]=[];
	if(count<1){
		for(var i=1;i<=canvasJsonObj.maxnoofcanvases;i++){
			window["manageCount_"+actualString].push(i);
		}
	}
	var canvasJsonObj={
	     "canvasperrow": 1,
	     "chartoptions": ["Line Chart", "Scatter Chart", "Area Chart"],
	     "dataviewoptions": ["Data View", "Data Compare"],
	     "exportoptions": ["PDF", "IMAGE"],
	     "canvastitle": 1,
	     "axistitle": 1,
	     "maxnoofcanvases": 10,
	     "maximize":0,
	     "close":0
	  }
	webix.ready(function () {
		webix.ui({
			container:containerId,
			rows:[{
				cols:[
				      {},
				      /*{
				    	    view:"button", 
						    id:"id_createCanvas_button_"+id, 
						    value:"Create Canvas",
						    css:'brix_button',
						    type:"form", 
						    width:150,
						    inputWidth:150,
						    height:40,
						    on:{
						    	onItemClick:function(){
						    		if(window["manageCount_"+actualString].length < 1){
						    			webix.alert('Can not generate more than 10 canvas')
						    			return;
						    		}else{
						    			//	count=window["manageCount_"+actualString].shift();
										//appendLobipanelView(count, panelId,id,canvasJsonObj)
						    		}
						    	}
						    }  
				      }*/
				]
				
			},
		    {
			  view:'template',
			  css:'genericCanvasContainer',
			  template:'<div class="'+panelId+'" style="height:auto;"></div>',
			},
			]
		});
		count=window["manageCount_"+actualString].shift();
	   	//appendLobipanelView(count, panelId ,id,canvasJsonObj)
		appendLobipanelView(count, panelId,id,canvasJsonObj,'Basic c3VwZXJ1c2VyOnNlY3JldA==',url)
	})
}
/**
 * @purpose This append the lobipane view
 * @param count, panelId, actualString, canvasJsonObj
 * @author Tejal
 */
function appendLobipanelView(count, panelId, actualString, canvasJsonObj, authenticationKey, url){
	var canvasJsonObj={
		     "canvasperrow": 1,
		     "chartoptions": ["Line Chart", "Scatter Chart", "Area Chart"],
		     "dataviewoptions": ["Data View", "Data Compare"],
		     "exportoptions": ["PDF", "IMAGE"],
		     "canvastitle": 1,
		     "axistitle": 1,
		     "maxnoofcanvases": 10,
		     "maximize":0,
		     "close":0
		  }
var cols='';
if(canvasJsonObj && canvasJsonObj.canvasperrow){
	var noOfCols= 12/parseInt(canvasJsonObj.canvasperrow);
	if(canvasJsonObj.canvasperrow>4){
		/*
		 * This condition is explicitely added to maintain the ui. 
		 */
		 cols='col-md-4 col-xs-12 col-sm-4 col-lg-4';
	}else{
		 cols='col-md-'+noOfCols+' col-xs-12  col-sm-'+noOfCols+' col-lg-'+noOfCols+'';
	}
}else{
	 cols='col-md-12 col-xs-12  col-sm-12 col-lg-12'
}
var dynamicCanvas = $("."+panelId).append("<div class='"+cols+" abcc'><div class='panel panel-default dynamicPanelClass dynamicLobiPanelClass_"+actualString+"__" + count + " '>" +
        "<div class='panel-heading dynamicDataHeaderSection "+actualString+" dataViewPanel" + count + " dataViewPanelHeaderCss'>" +
        "<div class='panel-title'>" +
        " <h5 id='id_custom_panel_"+actualString+"__" + count + "'>"+resourceObj.brix_lbl_line_chart+"</h5>" +
        "</div>" +
        "</div>" +
        " <div class='panel-body panelBodyOverriden'>" +
        "<div class='row' style='margin-right: 0px; margin-left: 0px;'><div class='col-md-4 col-xs-4  col-sm-4 col-lg-4'>" +
        "<ul class='nav navbar-nav header-nav rightSideIcons'>" +
        "<li id='settingIcon' class='dropdown graphIcon exportOption_"+count+"'>" +
        "<span class='glyphicon glyphicon-menu-hamburger settingBtn dbSettingBtn dynamicToggle' id='id_custom_options_"+actualString+"__" + count + "' onclick='canvasOptionsClickHandeler(this)' title='Export'></span>" +
        "<span class='caret helpDropDown'></span>" +
        "<ul class='dropdown-menu exportStyle administrationTruncate dynamicSettingBtn exportOptionList_"+count+"' id='id_custom_options_list_"+actualString+"__" + count + "'>" +
        "<li onclick='generateDynamicCSV(" + count + ")' class='configSettingHeader configSettingCSVHeader eleRemoveStyle'>Generate CSV</li>" +
        "<li onclick='generateDynamicPDF(this)' class='configSettingHeader configSettingPDFHeader eleRemoveStyle exportOptionsDataView_"+count+"' id='id_save_pdf_"+actualString+"__" + count + "'>"+resourceObj.brix_lbl_generate_pdf+"</li>" +
        "<li onclick='saveAsImage(this)' class='configSettingHeader configSettingImageHeader eleRemoveStyle exportOptionsDataView_"+count+"' id='id_save_image_"+actualString+"__" + count + "'>"+resourceObj.brix_lbl_save_image+"</li>" +
        "</ul>" +
        "<li><span class='fa fa-trash graphIcon graphTrashIconStyle' title='Clear Canvas'  onclick='clearGraph(this);' id='id_clear_graph_"+actualString+"__" + count + "'></span> <span class='caret helpDropDown'></span> </li>" +
        "</li>" +
        "</div><div class='col-md-2 col-xs-2  col-sm-2 col-lg-2' style=''>" +
        "<div style='margin-top: 8px;padding-right: 0px!important;'><b id='xaxislabel_"+actualString+"__" + count + "'>X-Axis</b></div></div>" + //end of col-sm-5
        "<div class='col-md-6 col-xs-6  col-sm-6 col-lg-6'>" +
        "<div class='form-group'>" +
        "<select class='form-control x-axis-style' id='channels_"+actualString+"__" + count + "' onchange='loadDynamicXChannelData(this)' >" +
        "</select>" +
        "</div>" +
        "</div>" +
        "</div>" +
        "<div class='row' style='margin-right: 0px; margin-left: 0px;'><div id=graph-panel_"+actualString+"__" + count + " class='graph-panelClass' style='height:307px; width: 99%'><div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div></div></div>" +
        "</div>" +
        "</div></div>");
	
	$('.panel').lobiPanel({
	    minWidth: 750,
	    minHeight: 400,
	    sortable: true,
	    editTitle: false,
	    unpin: false,
	    reload: false,
	});
    /*Object declaration*/
    window["isQuantityDropped_"+actualString+"__" + count]=[];
	window["compareGraphData_"+actualString+"__" + count]=[];
	window["dataView_"+actualString+"__" + count] = [];
	window["indexArr_"+actualString+"__" + count] = [];
	window["channelComboArr_"+actualString+"__" + count] = [];
	window["channelAxisArr_"+actualString+"__" + count] = [];
	//window["dropCount_"+actualString+"__" + count] = 0;
	window["dbArrayForPagination_"+actualString+"__" + count]=[];
	window["dyanamicChart_"+actualString+"__" + count] = null;
	window["dyanamicOptions_"+actualString+"__" + count] = null;
	window["isXAxisChanged_"+actualString+"__" + count] = false;
	window["csvArr_"+actualString+"__" + count] = [];
	window["isQuantityDroppedForCsv_"+actualString+"__" + count] = false;
	window["meaQuantityIdArr_"+actualString+"__" + count] = [];
	window["compareGraphData_"+actualString+"__" + count]=[];
	window["compareScatterGraphData_"+actualString+"__" + count]=[];
	window["compareAreaGraphData_"+actualString+"__" + count]=[];
	window["canvasTitleInfo_"+actualString+"__" + count]={};
	window["quantityObj_"+actualString+"__" + count]=[];
	window["dataCompareView_" +actualString+"__" + count]=[]
	window["dataCompareView_"+actualString+"__" + count] = [];
	window["meaResultDynamicArr_" +actualString+"__" + count]=[];
	window["dyanamicMeaResultNametoPersist_" +actualString+"__" + count]="";
	window["legendChangeFlag_" +actualString+"__" + count] = false;
	window["channelAliasValue_"+actualString+"__" + count]=null;
	window["channelCmbSelectedValue_"+actualString+"__" + count]='';
	window["dbArray_"+actualString+"__" + count]=[];
	window["chartAliseElementArray_"+actualString+"__" + count]=[];
	window["measurementDropAlise_"+actualString+"__" + count]=[];
	window["authentication_key_"+actualString+"__" + count]='';
	window["url_"+actualString+"__" + count]='';
	webix.DragControl.addDrop("graph-panel_"+actualString+"__" + count, {
	    $drop: function(source, target, event) {
	    	console.log(target.id)
	    	var subId = target.id.split('__')[1];
	    	if(window["compareAreaGraphData_"+actualString+"__" + subId].length>19){
	    		webix.alert(resourceObj.brix_lbl_channel_limi_validation);
	    		return;
	    	}else{
	    		//        var subId = target.id.split('_')[1];
		        var context = webix.DragControl.getContext();
		        if(context.source[0] && context.from.getItem(context.source[0]))
		        var item = context.from.getItem(context.source[0]);
		        var owner = context.from.data.owner;
		        var nodeType = item.nodeType;
		      //  setMeaResultAndMeaQuantityIds(nodeType, item, owner, authenticationKey, url);
		        setMeaResultAndMeaQuantityIds(nodeType, item, owner, actualString, subId,authenticationKey, url);
		         var obj={};
				 obj['MeaResult']=meaResultId;
				 obj=JSON.stringify(obj);
		     	var viewType = $("#view_type_"+actualString+"__" + subId).val()
		        if((nodeType=='Measurement' || nodeType=='MeaResult') && (viewType != "Data_Compare_"+actualString+"__" + subId)){
		        	$.LoadingOverlay("show", spinner);
					$.ajax({
                        type: "POST",
                        url: "/GraphSearch/gsearch/getChannelsByMeasurement?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
                        data: obj,
                        contentType:"application/json; charset=utf-8",
                        dataType:"json",
                        success: function(res){
                        	var channelsList=res;
    			     		console.log('channelsList'+JSON.stringify(channelsList));
    			     		 $.LoadingOverlay("hide", spinner);
    			     		if(channelsList && channelsList.returncode=='1'){
    			     			channelsList=channelsList.data;
    			     			showChannelListWindow(channelsList,item,owner,actualString,subId);
    			     		}else{
    			     			 $.LoadingOverlay("hide", spinner);
    			     			webix.alert(resourceObj.brix_lbl_failed_to_load_data)
    			     		}
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                             $.LoadingOverlay("hide", spinner);
                      }
                  });
		        }else if((nodeType=='Channel' || nodeType=='MeaQuantity') && (viewType != "Data_Compare_"+actualString+"__" + subId)){
		    		validateGraphData(target.id, actualString,subId,'line', canvasJsonObj, context, item, owner, nodeType);
			    	window["dbArrayForPagination_"+actualString+"__" + subId].push(target.id);
		        }else if((nodeType=='Measurement' || nodeType=='MeaResult') && (viewType == "Data_Compare_"+actualString+"__" + subId)){
		        	
		        	 var resultExist=false;	
		        	 if(window["meaResultDynamicArr_" +actualString+"__" + subId].length>0){
		        		 for(var i=0;i<window["meaResultDynamicArr_" +actualString+"__" + subId].length;i++){
		        			 if(window["meaResultDynamicArr_" +actualString+"__" + subId][i] == meaResultId){
		        				 resultExist=true;
		        				 break; 
		        			 }
		        		 }
		        	 }
		        	 if(resultExist){
		        		 webix.alert(resourceObj.brix_lbl_dropped_measurement_already_Exists);
		        		 return false;
		        	 }else{
		        		 window["dyanamicMeaResultNametoPersist_" +actualString+"__" + subId] = item.value;
		        		 createDynamicDataTableCompare(item.uid, item.value, subId,actualString)
		        		 getDynamicCompareDatatableData(subId, actualString,item.value);
		        	 }
		       
		        }else{
		        	webix.alert(resourceObj.brix_lbl_can_not_drop+' ' +nodeType +' '+resourceObj.brix_lbl_node);
		        	return;
		        }
	    	}
	    }
	});

	 $("."+actualString+".dataViewPanel"+count+" .dropdown").addClass("dataViewPanelcustomdropdown_"+actualString+"__" + count);
	 if(canvasJsonObj && canvasJsonObj.chartoptions && canvasJsonObj.chartoptions.length>0){
		 for(var i=0;i<canvasJsonObj.chartoptions.length;i++){
			 var str=canvasJsonObj.chartoptions[i];
			 str=str.replace(' ','_');
			 str=str + "_"+actualString+"__" + count;
			 window["channelComboArr_"+actualString+"__" + count].push('<option value="'+str+'">'+ canvasJsonObj.chartoptions[i] +'</option>');
		 }
	 }
	 if(canvasJsonObj && canvasJsonObj.dataviewoptions && canvasJsonObj.dataviewoptions.length>0){
		 for(var i=0;i<canvasJsonObj.dataviewoptions.length;i++){
			 var str=canvasJsonObj.dataviewoptions[i];
			 str=str.replace(' ','_');
			 str=str + "_"+actualString+"__" + count
			 window["channelComboArr_"+actualString+"__" + count].push('<option value="'+str+'">'+ canvasJsonObj.dataviewoptions[i] +'</option>');
		 }
	 }
	 
	 window["dyanamicOptions_"+actualString+"__" + count]
	 $('.dataViewPanelcustomdropdown_'+actualString+'__' + count + ' li:eq(0)').before("<li><select id='view_type_"+actualString+"__" + count + "' class='form-control dbform-control dbdynamicform-control view_type' onchange='changeDynamicChartView(view_type_"+actualString+"__" + count + ");'>" +
			 window["channelComboArr_"+actualString+"__" + count]+
		        "</select></li>");
	 	
		$('.lobipanel').on('onFullScreen.lobiPanel', function(ev, lobipanel) {
			var height = document.body.clientHeight - 20;
			var graphPanelHeight = height - 70 + "px";
			var dataTableGraphPanelHeight = height - 50 + "px";
            var grapgCamparePanelHeight = height - 50 + "px";
			 if($(this).attr('class').indexOf('dynamicLobiPanelClass_')>-1){
				 var classList= $(this).attr('class');
            	 var classElement=classList.split('dynamicLobiPanelClass_')[1];
            	 classElement=classElement.split(' ')[0];
            	 var actualString=classElement.split('__')[0];
            	 var count=classElement.split('__')[1];
            	 $("#graph-panel_"+actualString+"__" + count).css("height", graphPanelHeight);
            	 if(window["dyanamicChart_"+actualString+"__" + count]){
            		 window["dyanamicChart_"+actualString+"__" + count].render();
            	 }
            	 
            	 if ($$("dt_"+actualString+"__" + count)) {
                    $("#graph-panel_" +actualString+"__" + count).css("height", dataTableGraphPanelHeight);
                    var width = $("#graph-panel_"+actualString+"__" + count).width();
               		width = parseInt(width, 10);
               		$$("dt_"+actualString+"__" + count).config.width=width-5;
                    $$("dt_" +actualString+"__" + count).resize();
                    $$("dt_"+actualString+"__" + count).scrollTo(0, 0);
                 }
            	 
            	 if ($$("dataCompareTable_" +actualString+"__" + count)) {
                     $("#graph-panel_" +actualString+"__" + count).css("height", grapgCamparePanelHeight);
                     var width = $("#graph-panel_"+i).width();
                   	width = parseInt(width, 10);
           			$$("dataCompareTable_"+actualString+"__" + count).config.width=width-5;
                     $$("dataCompareTable_" +actualString+"__" + count).resize();
                     $$("dataCompareTable_" +actualString+"__" + count).scrollTo(0, 0);
                 }
			 }
		})
		 $('.lobipanel').on('onSmallSize.lobiPanel', function(ev, lobipanel) {
			 if($(this).attr('class').indexOf('dynamicLobiPanelClass_')>-1){
				 
				 var classList= $(this).attr('class');
            	 var classElement=classList.split('dynamicLobiPanelClass_')[1];
            	 classElement=classElement.split(' ')[0];
            	 var actualString=classElement.split('__')[0];
            	 var count=classElement.split('__')[1];
            	 var viewTypeCombo = $('#view_type_' +actualString+"__" + count).val();
            	 $("#graph-panel_"+actualString+"__" + count).css("height", "307px");
            	 if(window["dyanamicChart_"+actualString+"__" + count]){
            		 window["dyanamicChart_"+actualString+"__" + count].render();
            	 }
            	 
            	 if ($$("dt_" +actualString+"__" + count)) {
                     $("#graph-panel_"+actualString+"__" + count).css("height", "332px");
                     var width = $("#graph-panel_"+actualString+"__" + count).width();
             			width = parseInt(width, 10);
             		$$("dt_"+actualString+"__" + count).config.width=width-5;
                     $$("dt_" +actualString+"__" + count).resize();
                     $$("dt_" +actualString+"__" + count).scrollTo(0, 0);
                 }
            	 
            	 if ($$("dt_" +actualString+"__" + count)) {
                     $("#graph-panel_"+actualString+"__" + count).css("height", "332px");
                     var width = $("#graph-panel_"+actualString+"__" + count).width();
             			width = parseInt(width, 10);
             		$$("dt_"+actualString+"__" + count).config.width=width-5;
                     $$("dt_" +actualString+"__" + count).resize();
                     $$("dt_" +actualString+"__" + count).scrollTo(0, 0);
                 }
            	 
            	 if ($$("dataCompareTable_" +actualString+"__" + count)) {
                     $("#graph-panel_" +actualString+"__" + count).css("height", "332px");
                     	var width = $("#graph-panel_"+actualString+"__" + count).width();
                     	width = parseInt(width, 10);
             			$$("dataCompareTable_"+actualString+"__" + count).config.width=width-5;
                     $$("dataCompareTable_"+actualString+"__" + count).resize();
                     $$("dataCompareTable_" +actualString+"__" + count).scrollTo(0, 0);
                 }
            	 
            	 if (!$$("dt_"+actualString+"__" + count) && !$$("dataCompareTable_"+actualString+"__" + count)) {
                     if (viewTypeCombo == "datatable_"+ actualString+"__" + count|| viewTypeCombo == "dataCompare_"+actualString+"__" + count) {
                         $("#graph-panel_" + i).css("height", "332px");
                     } else {
                         $("#graph-panel_" + i).css("height", "307px");
                     }
                 }
			 }
		 })
		 $('.lobipanel').on('beforeClose.lobiPanel', function(ev, lobipanel) {
			 if($(this).attr('class').indexOf('dynamicLobiPanelClass_')>-1){
				 $(this).closest('.abcc').remove();
				var classList= $(this).attr('class');
            	var classElement=classList.split('dynamicLobiPanelClass_')[1];
            	classElement=classElement.split(' ')[0];
            	var actualString=classElement.split('__')[0];
            	var count=classElement.split('__')[1];
            	if(window["manageCount_"+actualString].indexOf(count)<0){
            	  	window["manageCount_"+actualString].push(count);
                	window["manageCount_"+actualString].sort(function(a,b){
                		return a-b;
                	});
                	console.log('actualString',actualString)
            	}
			 }
		 })
		if(canvasJsonObj.maximize =='1'){
			$('.lobipanelDropDownList').find("[data-title*='Fullscreen']").css('display','block');
			$('.lobipanelDropDownList').find("[data-title*='Minimize']").css('display','block');
		}else{
			$('.lobipanelDropDownList').find("[data-title*='Fullscreen']").css('display','none');
			$('.lobipanelDropDownList').find("[data-title*='Minimize']").css('display','none');
		}
		if(canvasJsonObj.close =='1'){
			$('.lobipanelDropDownList').find("[data-title*='Close']").css('display','block');
		}else{
			$('.lobipanelDropDownList').find("[data-title*='Close']").css('display','none');
		}
		
		 if(canvasJsonObj && canvasJsonObj.exportoptions && canvasJsonObj.exportoptions.length>0){
			for(var i=0;i<canvasJsonObj.exportoptions.length;i++){
				if(canvasJsonObj.exportoptions[i]=='CSV'){
					$('.configSettingCSVHeader').show();
				}else if(canvasJsonObj.exportoptions[i]=='PDF'){
					$('.configSettingPDFHeader').show();
				}else if(canvasJsonObj.exportoptions[i]=='IMAGE'){
					$('.configSettingImageHeader').show();
				}
			}
		 }
			
}
/**
 * @purpose Channel combo on change event
 * @param event
 * @author Tejal
 */
function loadDynamicXChannelData(ev){
	var eleId=ev.id;
	var str=eleId.split('channels_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	
	  var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
	  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
	  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
	  var selectedResultId=channelComboSelectedValue.split(",")[0];
	  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
	  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
	  var channelArr=[];
	  for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
		  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId != selectedQuantityId){
			  var newObj={};
			  newObj['MeaQuantity']=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
			  newObj['MeaResult']=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
			  newObj['name']=window["compareGraphData_"+actualString+"__" + subId][i].name.substring(0, window["compareGraphData_"+actualString+"__" + subId][i].name.lastIndexOf("(") + 0);
			  newObj['aliasName']=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
			  channelArr.push(newObj);
		  }
	  }
	  
	     var obj={};
	     obj['MeaResult']=meaResultId;
	     if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
	    	 obj['xaxischannelname']='';
	     }else{
	    	 for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
		   		  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == selectedQuantityId && window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == selectedResultId){
		   			  obj['xaxischannelname']=window["compareGraphData_"+actualString+"__" + subId][i].actualName;
		   			  obj['xaxischannelId']=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
		   		  }
	   	  	}
	    	 
	     }
	obj['channels']=channelArr;
	obj['xaxischannelId']=selectedQuantityId;
	 if(!$("#channels_"+actualString+"__" + subId).val()){
		 var target="graph-panel_"+actualString+"__"+subId;
		 window["channelCmbSelectedValue_"+actualString+"__" + subId]=''
		 resetDataArraysForChannelDrop(obj,actualString,subId,str,channelComboSelectedValue);
	 }else if($("#channels_"+actualString+"__" + subId).val()=="0"){
		  window["channelCmbSelectedValue_"+actualString+"__" + subId]=''
		  resetDataArraysForChannelDrop(obj,actualString,subId,str,channelComboSelectedValue)
	 }else{
		  console.log(JSON.stringify(selectedResultId))
		  console.log(JSON.stringify(selectedQuantityId))
		  obj=JSON.stringify(obj);
     		 var target="graph-panel_"+actualString+"__"+subId;
     		 $.LoadingOverlay("show", spinner);
     		$.ajax({
                type: "POST",
                url: "/GraphSearch/gsearch/checkSelectedChannelCanBeXAxis?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
                data: obj,
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                success: function(res){
                	 res=res;
    				 var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
    				 if(res && res.returncode=='1'){
    					 window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
    						$.ajax({
    			                type: "POST",
    			                url: "/GraphSearch/gsearch/getMultipleChannelDataByXAxis?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
    			                data: obj,
    			                contentType:"application/json; charset=utf-8",
    			                dataType:"json",
    			                success: function(response){
    			                	 response=response;
    			                	 $.LoadingOverlay("hide", spinner);
    	    						 if(response.data){
    	    							 drawGraphOnXAxisSelection(target, subId, actualString,str,channelComboSelectedValue, response.data, 'onChangeClicked')
    	    						 }
    			                }
    						})
    				 }else{
    					 $.LoadingOverlay("hide", spinner);
    					 webix.alert(resourceObj.brix_lbl_channel_selection_validation);
    					 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
    							$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
    					 }else{
    							$("#channels_"+actualString+"__" + subId).val('0')
    				     }
    					 return false;
    				 }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                     $.LoadingOverlay("hide", spinner);
              }
          });
		    console.log(JSON.stringify(channelArr))
		    console.log('obj'+JSON.stringify(obj))
	 }
	 console.log(JSON.stringify(channelComboSelectedValue))
	 console.log(JSON.stringify(window["compareGraphData_"+actualString+"__" + subId]))
	
}

/**
 * @purpose Resets the garph array on index selection
 * @param event
 * @author Tejal
 */
function resetDataArraysForChannelDrop(obj,actualString,subId,str,strselectedChannelVal){
	 var xaxischannelname=obj['xaxischannelname'];
	 obj=JSON.stringify(obj);
	 $.LoadingOverlay("show", spinner);
	 $.ajax({
         type: "POST",
         url: "/GraphSearch/gsearch/getMultipleChannelDataByXAxis?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
         data: obj,
         contentType:"application/json; charset=utf-8",
         dataType:"json",
         success: function(res){
        	 res=res;
     		 console.log('res'+JSON.stringify(res));
     		 if(res && res.returncode == '1' && res.data){
		 		$.LoadingOverlay("hide", spinner);
		 		var arr=[];
		 		if(window["compareGraphData_"+actualString+"__" + subId].length>0){
		 			for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
		 				arr.push(window["compareGraphData_"+actualString+"__" + subId][i])
		 			}
		 		}
         		var target="graph-panel_"+actualString+"__"+subId;
         		drawGraphOnXAxisSelection(target, subId, actualString,str,strselectedChannelVal,res.data)
     		 }else{
     			 $.LoadingOverlay("hide", spinner);
     			 webix.alert(resourceObj.brix_lbl_data_availability_for_channel);
     		 }
         },error: function(XMLHttpRequest, textStatus, errorThrown) {
             $.LoadingOverlay("hide", spinner);
         }
		})

}

/**
 * @purpose Shows channels list on measurement drop
 * @param channelsList,item,owner,actualString,subId
 * @author Tejal
 */
function showChannelListWindow(channelsList,item,owner,actualString,subId){
	var checkClass="check_"+actualString+"__"+subId;
	webix.ui({
		  view:"window",
		  id:'id_window_measurement_list_'+actualString+'__'+subId,
		  move:true,
		  position:'center',
		  css:'brix_window',
		  resize:true,
		  headHeight: 28, // Adjust headheight
		  head: "<span class='brix_window_header_title'>"+resourceObj.brix_lbl_channel_list+"</span><span class='brix_window_close' id='id_channelWindowclose_"+actualString+'__'+subId+"' onclick='closeChannelWindow(this)'>&times;</span>",
		  body:{
			  height: setHeightInPercentage(50),
              width: setWidthInPercentage(55),
              rows: [],
			  id: 'id_channel_list_container'+actualString+'__'+subId,
		  },
		  on:{
			  onShow:function (){
					$$('id_channel_list_container'+actualString+'__'+subId).addView({
						rows: [
	    				        {
	    				        	gravity:0.3,
	    				        	view:'template',
	    				        	template:'<div><div class="channelHeader">'+resourceObj.brix_lbl_select_channels_for_canvas+'</div><div><span class="channelHeader">'+resourceObj.brix_lbl_measurement_name+': </span><span class="measurement_name'+actualString+'__'+subId+'"></span></div></div>',
	    				        	on:{
	    				        		onAfterRender:function(){
	    				        			 $('.measurement_name'+actualString+'__'+subId).text('')
		 		    						 $('.measurement_name'+actualString+'__'+subId).text(item.value);
	    				        		},
	    				        		
	    				        	}	
	    				        },
	    				       {
	    				    	  gravity:0.9,
	    				    	  view:"list",
	 		    				  id:'id_project_list'+actualString+'__'+subId,
	 		    				  css:"listcss project_list_"+actualString+"__"+subId,
	 		    				  scroll:"auto",
	 		    				  tooltip:webix.template("#value#"),
	 		    				  type:{
	 		    				    markCheckbox:function(obj ){
	 		    				    	 return "<span class='check webix_icon fa-"+(obj.markCheckbox?"check-":"")+"square-o'></span>";
	 		    				    	 }
	 		    				  },
	 		    				  data:[],
	 		    				  template:"{common.markCheckbox()} #value#",
	 		    				  on:{
	 		    					  onAfterRender:webix.once(function(){
	 		    						 var channelData = [];
	 		    						 
	 		    						  for (var i = 0; i < channelsList.length; i++) {
	 		    							 channelData.push({
	 		    						      'id': channelsList[i].id,
	 		    						      'value': channelsList[i].name,
	 		    						      "markCheckbox": 0
	 		    						    });
	 		    						  }
	 		    						  for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
	 		    							 for (var j = 0; j < channelData.length; j++) {
		 		    							 if(channelData[j].id == window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId){
		 		    								channelData[j]["markCheckbox"]= 1
		 		    							 }
	 		    							 }
	 		    						  }
	 		    						 	
	 		    						  	if($$('id_project_list'+actualString+'__'+subId)){
	 		    						  		$$('id_project_list'+actualString+'__'+subId).clearAll();
	 		    							    $$('id_project_list'+actualString+'__'+subId).parse(channelData);
	 		    							    $$('id_project_list'+actualString+'__'+subId).sort("#markCheckbox#");
	 		    							    $$('id_project_list'+actualString+'__'+subId).refresh();
	 		    							    $$('id_project_list'+actualString+'__'+subId).scrollTo(0,0);
	 		    						  	}
		 		    						 setTimeout(function(){
		 		    							if($("#channels_"+actualString+"__" + subId).val() && $("#channels_"+actualString+"__" + subId).val()!='0'){
		 		    						 		var selectedChannelEle=$("#channels_"+actualString+"__" + subId).val();
		 		    						 		var meaResId= selectedChannelEle.split(',')[0];
		 		    						 		var meaQutId= selectedChannelEle.split(',')[1];
		 		    						 		$(".project_list_"+actualString+"__" + subId).find("[webix_l_id='" + meaQutId + "']").addClass('disabled') 
		 		    						 	}
		 		    						 })
	 		    					  }),
	 		    					  
	 		    				  },
	 		    				 onClick: {
	 		    					 	"check": function(e, id) {
	 		                            	var item = $$('id_project_list'+actualString+'__'+subId).getItem(id);
	 		                                item.markCheckbox = item.markCheckbox ? 0 : 1;
	 		                                $$('id_project_list'+actualString+'__'+subId).updateItem(id, item);
		 		                            var checkedList = $$('id_project_list'+actualString+'__'+subId).data.pull;
		 		                            console.log(checkedList)
		 		                            var arrOfCheckedList = [];
    							    	    for (var i in checkedList) arrOfCheckedList.push(i);
    							    	    var flag=false;
    							    	    for (var i = 0; i < arrOfCheckedList.length; i++) {
    							    	        if (checkedList[arrOfCheckedList[i]].markCheckbox == 1) {
    							    	           flag=true;
    							    	           break;
    							    	        } else {
    							    	            
    							    	        }
    							    	    }
    							    	    
    							    	    if(flag){
    							    	    	$('.saveBtnStyle').removeClass('disabled');
    							    	    }else {
    							    	    	$('.saveBtnStyle').addClass('disabled');
    							    	    }
	 		                            }
	 		                      },
	    				       },
	    				       {
	    				    	   gravity:0.1,
	    				    	   cols:[
	    				    	         {},
		    				    	   {
		    				    		   view:"button", 
		    							    value:resourceObj.brix_lbl_ok,
		    							    css:'brix_button saveBtnStyle disabled',
		    							    type:"form",
		    							    width:120,
		    							    on:{
			    							    	onItemClick:function(){
		    							    		var checkedList = $$('id_project_list'+actualString+'__'+subId).data.pull;
				 		                            console.log(checkedList)
				 		                            var arrOfCheckedList = [];
				 		                            var channelIdArray=[];
				 		                            var channelArrOfObj=[];
		    							    	    for (var i in checkedList) arrOfCheckedList.push(i);
		    							    	    for (var i = 0; i < arrOfCheckedList.length; i++) {
		    							    	        if (checkedList[arrOfCheckedList[i]].markCheckbox == 1) {
		    							    	        	var newObj={};
		    							    	        	newObj['channelId']=checkedList[arrOfCheckedList[i]].id;
		    							    	        	newObj['aliasName']='';
		    							    	        	channelIdArray.push(checkedList[arrOfCheckedList[i]].id);
			    							  				channelArrOfObj.push(newObj);
		    							    	        }
		    							    	    }
		    							    	    
		    							    	    if(window["compareGraphData_"+actualString+"__" + subId].length>0){
   							    	        		 for (var j = 0; j < window["compareGraphData_"+actualString+"__" + subId].length; j++) {
   							    	        			 for(i=0;i<channelArrOfObj.length;i++){
   							    	        				if(channelArrOfObj[i].channelId == window["compareGraphData_"+actualString+"__" + subId][j].meaQuantityId){
   							    	        					channelArrOfObj[i]['channelId']=window["compareGraphData_"+actualString+"__" + subId][j].meaQuantityId;
   							    	        					if(window["compareGraphData_"+actualString+"__" + subId][j].legendText){
   							    	        						channelArrOfObj[i]['aliasName']=window["compareGraphData_"+actualString+"__" + subId][j].legendText;
   				    							  				}
	    							    	        		}
   							    	        			 }
   							    	        		 }
		    							    	    }
		    							    	     var obj={};
		    							    		 obj['MeaResult']=meaResultId;
			    							   		 obj['channels']=channelArrOfObj;
			    							   		 
			    							   		 if(!$("#channels_"+actualString+"__" + subId).val()){
			    										 obj['xaxischannelname']='';
			    									 }else if($("#channels_"+actualString+"__" + subId).val()=="0"){
			    										 obj['xaxischannelname']='';
			    									 }else{
			    										 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
			    										 var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
			    										 var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
			    										 var meaQuanity= strselectedChannelVal.split(',')[1];
			    										 var meaResult= strselectedChannelVal.split(',')[0];
			    										 if(channelIdArray.length>0){
			    											 var index=-1;
			    											 for(var k=0;k<channelIdArray.length;k++){
			    												 if(channelIdArray[k] == meaQuanity){
			    													 index=k;
			    												 }
			    											 }
			    											 if(index>-1){
			    												 channelIdArray.splice(index,1);
			    											 }
			    										 }
			    										 var elementInd=-1;
			    										 for(var i=0;i<channelArrOfObj.length;i++){
			    											 if(channelArrOfObj[i].channelId == meaQuanity){
			    												 elementInd=i;
			    											 }
			    										 }
			    										 if(elementInd>-1){
			    											 channelArrOfObj.splice(elementInd,1)
			    										 }
			    										 for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
			    									   		  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == meaQuanity && window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == meaResult){
			    									   			obj['xaxischannelname']=window["compareGraphData_"+actualString+"__" + subId][i].actualName;
			    									   		  }
			    								   	  	}
			    									 }
			    							   		 	if(window["compareAreaGraphData_"+actualString+"__" + subId].length>20 || channelArrOfObj.length>20){
			    							   		 		webix.alert(resourceObj.brix_lbl_channel_limi_validation);
			    							   		 		return false;
			    							   		 	}else{
			    							   		 		resetDataArrays(obj,actualString,subId, str,strselectedChannelVal)
			    							   		 	}
			    							         	
			    							    }
		    							    }
		    				    	   },
		    				    	   {
		    				    		   view:"button", 
		    							    value:resourceObj.brix_lbl_cancel,
		    							    css:'brix_button',
		    							    width:120,
		    							    type:"form", 
		    							    on:{
		    							    	onItemClick:function(){
		    							    		$$('id_window_measurement_list_'+actualString+'__'+subId).close()
		    							    	}
		    							    }
		    				    	   },{}
	    				    	   ]
	    				       }
	    				  ],
					})
				
			  }
		  	
		  }
	}).show();
}

/**
 * @purpose Resets graph array on channel selection
 * @param obj,actualString,subId,str,strselectedChannelVal
 * @author Tejal
 */
function resetDataArrays(obj,actualString,subId,str,strselectedChannelVal){
	 var xaxischannelname=obj['xaxischannelname'];
	 obj=JSON.stringify(obj);
	  $.LoadingOverlay("show", spinner);
	 $.ajax({
         type: "POST",
         url: "/GraphSearch/gsearch/getMultipleChannelData?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
         data: obj,
         contentType:"application/json; charset=utf-8",
         dataType:"json",
         success: function(res){
     		 res=res;
     		 console.log('res 1111111'+JSON.stringify(res));
     		 if(res && res.returncode == '1' && res.data){
     			  $.LoadingOverlay("hide", spinner);
     			 		var arr=[];
     			 		if(window["compareGraphData_"+actualString+"__" + subId].length>0){
     			 			for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
     			 				arr.push(window["compareGraphData_"+actualString+"__" + subId][i])
     			 			}
     			 		}
         			   /*If user deselect an element on measurement drop*/
         			   if(xaxischannelname ==''){
         	    	      if(window["compareGraphData_"+actualString+"__" + subId].length>0){
         	    	    	 var meaResultArr=[];
         	    	    	for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
         	    	    		if(meaResultId == window["compareGraphData_"+actualString+"__" + subId][i].meaResultId){
         	    	    			meaResultArr.push(i);
         	    	    		}
         	    	    	}
         	    	    	if(meaResultArr.length>0){
    	         	    	    	for(var j=0;j<meaResultArr.length;j++){
    	         	    	    		window["compareGraphData_"+actualString+"__" + subId].splice(meaResultArr[j],1)
    	         	    	    	}
    	         	    	    }
         	    	     }
         	    	     if(window["compareScatterGraphData_"+actualString+"__" + subId].length>0){
         	    	    	 var meaResultArr=[];
             	    	    	for(var i=0;i<window["compareScatterGraphData_"+actualString+"__" + subId].length;i++){
             	    	    		if(meaResultId == window["compareScatterGraphData_"+actualString+"__" + subId][i].meaResultId){
             	    	    			meaResultArr.push(i);
             	    	    		}
             	    	    	}
             	    	    	if(meaResultArr.length>0){
    	         	    	    	for(var j=0;j<meaResultArr.length;j++){
    	         	    	    		window["compareScatterGraphData_"+actualString+"__" + subId].splice(meaResultArr[j],1)
    	         	    	    	}
    	         	    	    }
             	    	     }
         	    	    if(window["compareAreaGraphData_"+actualString+"__" + subId].length>0){
         	    	    	var meaResultArr=[];
         	    	    	for(var i=0;i<window["compareAreaGraphData_"+actualString+"__" + subId].length;i++){
         	    	    		if(meaResultId == window["compareAreaGraphData_"+actualString+"__" + subId][i].meaResultId){
         	    	    			meaResultArr.push(i);
         	    	    		}
         	    	    	}
         	    	    	if(meaResultArr.length>0){
             	    	    	for(var j=0;j<meaResultArr.length;j++){
             	    	    		window["compareAreaGraphData_"+actualString+"__" + subId].splice(meaResultArr[j],1)
             	    	    	}
             	    	    }
         	    	     }
         	    	   if(window["dbArray_" +actualString+"__" + subId].length>0){
    	    	    	    var meaResultArr=[];
    	   	    	    	for(var i=0;i<window["dbArray_" +actualString+"__" + subId].length;i++){
    	   	    	    		if(meaResultId == window["dbArray_"+actualString+"__" + subId][i].meaResultId){
    	   	    	    			meaResultArr.push(i);
    	   	    	    		}
    	   	    	    	}
       	    	    	if(meaResultArr.length>0){
         	    	    	for(var j=0;j<meaResultArr.length;j++){
         	    	    		window["dbArray_"+actualString+"__" + subId].splice(meaResultArr[j],1)
         	    	    	}
         	    	    }
       	    	     }
         			   }
         			  //if(xaxischannelname ==''){
         			   console.log('arr::'+JSON.stringify(arr));
         			  var target="graph-panel_"+actualString+"__"+subId;
         	    	    for(var i=0;i<res.data.length;i++){
    	         			(function (i) {
    	         				var newMeaQuantityId = res.data[i].id;
    	         				var newmeaResultId = res.data[i].meaReultId;
    	         				var charData = res.data[i].Values;
    	         				var nodeName=res.data[i].name;
    	         				var unitName = res.data[i].unit;
    	         				if(charData.length>0){
    	         				setDynamicArray('line', charData, nodeName, subId, unitName, newMeaQuantityId, actualString,meaResultId, arr, res.data[i]);
    	                        setDynamicArray('scatter', charData, nodeName, subId, unitName, newMeaQuantityId, actualString,meaResultId, arr, res.data[i]);
    	                        setDynamicArray('area', charData, nodeName, subId, unitName, newMeaQuantityId, actualString,meaResultId, arr,res.data[i]);
    	                        getDynamicDatatableData(newMeaQuantityId,meaResultId, subId,nodeName, unitName,actualString, res.data[i]);
    	         				}else{
    	         					webix.alert(nodeName +" has no data")
    	         				}
    	         			})(i);
    	         		 }
         			  //}
    	         		
    	         		var indexArr=[];
         	    	    for(var j=0;j<window["compareGraphData_"+actualString+"__" + subId].length;j++){
         	    	    	var eleMeaResultId =window["compareGraphData_"+actualString+"__" + subId][j].meaResultId;
         	    	    	if(eleMeaResultId == meaResultId){
         	    	    		indexArr.push(j);
         	    	    	}
         	    	    }
         	    	    if(indexArr.length>0){
         	    	    	for(var i=0;i<indexArr.length;i++){
         	    	    		window["quantityObj_"+actualString+"__" + subId].splice(i,1);
         	    	    	}
         	    	    }
         	    	    var remianingResultIndexArr=[];
         	    	   for(var i=0;i<window["quantityObj_"+actualString+"__" + subId].length;i++){
     	    	    		var ele=window["quantityObj_"+actualString+"__" + subId][i];
     	    	    		var meaRes=ele.split(',')[0];
     	    	    		if(meaRes == meaResultId){
     	    	    			remianingResultIndexArr.push(i);
     	    	    		}
     	    	    	}
         	    	   if(remianingResultIndexArr.length>0){
         	    		  for(var i=0;i<remianingResultIndexArr.length;i++){
         	    	    		window["quantityObj_"+actualString+"__" + subId].splice(i,1);
         	    	    	}
         	    	   }
         	    	    if(window["compareGraphData_"+actualString+"__" + subId].length>0){
         	    	    	for(i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
         	    	    		if(window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == meaResultId){
         	    	    			var str=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId + ',' +window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
         	    	    			window["quantityObj_"+actualString+"__" + subId].push(str);
         	    	    		}
         	    	    	}
         	    	    }	
    	         		 if(xaxischannelname ==''){
    	         			  validateChartPanelsType(target, subId, '', actualString,'','','MeasurementDrop') 
    	         		 }else{
    	         			var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
    	         			  //drawGraphOnXAxisSelection(target, subId, actualString,str,strselectedChannelVal,res.data)
    	         			var viewType = $("#view_type_"+actualString+"__" + subId).val();
    	         			switch(viewType){
    		        			case "Line_Chart_"+actualString+"__" + subId:{
    		        				var modifiedArr=JSON.parse(JSON.stringify(window["compareGraphData_"+actualString+"__" + subId]))
    		        				if(strselectedChannelVal && strselectedChannelVal!='0'){
    			         				 var meaQuanity= strselectedChannelVal.split(',')[1];
    			         				 var meaResult= strselectedChannelVal.split(',')[0];
    			         				
    			         				 if(modifiedArr.length>0){
    			         					 var elementToBeSliced=-1;
    			         					 for(var i=0;i<modifiedArr.length;i++){
    			         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
    			         							 elementToBeSliced=i;
    			         						 }
    			         					 }
    			         					 
    			         					 if(elementToBeSliced >-1){
    			         						modifiedArr.splice(elementToBeSliced,1);
    			         					 }
    			         				 }
    		        				}
    					    		drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
    					    		break;
    		        			} 
    		        			case "Scatter_Chart_"+actualString+"__" + subId:{
    		        				 var modifiedArr=JSON.parse(JSON.stringify(window["compareScatterGraphData_"+actualString+"__" + subId]))
    		        				if(strselectedChannelVal && strselectedChannelVal!='0'){
    		        					 var meaQuanity= strselectedChannelVal.split(',')[1];
    			         				 var meaResult= strselectedChannelVal.split(',')[0];
    		        					if(modifiedArr.length>0){
    			         					 var elementToBeSliced=-1;
    			         					 for(var i=0;i<modifiedArr.length;i++){
    			         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
    			         							 elementToBeSliced=i;
    			         						 }
    			         					 }
    			         					 if(elementToBeSliced >-1){
    			         						modifiedArr.splice(elementToBeSliced,1);
    			         					 }
    			         				 }
    		        				}
    					    		drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
    					    		break;
    		        			}
    		        			case "Area_Chart_"+actualString+"__" + subId:{
    		        				var modifiedArr=JSON.parse(JSON.stringify(window["compareAreaGraphData_"+actualString+"__" + subId]))
    		        				
    		        				if(strselectedChannelVal && strselectedChannelVal!='0'){
    		        					 var meaQuanity= strselectedChannelVal.split(',')[1];
    			         				 var meaResult= strselectedChannelVal.split(',')[0];
    			         				
    		        					 if(modifiedArr.length>0){
    			         					 var elementToBeSliced=-1;
    			         					 for(var i=0;i<modifiedArr.length;i++){
    			         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
    			         							 elementToBeSliced=i;
    			         						 }
    			         					 }
    			         					 if(elementToBeSliced >-1){
    			         						modifiedArr.splice(elementToBeSliced,1);
    			         					 }
    			         				 }
    		        				}
    					    		drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
    					    		break;
    		        			}case "Data_View_"+actualString+"__" + subId:{
    					    		 if ($$('dt_' +actualString+"__" + subId) != undefined)
    			                         $$('dt_' +actualString+"__" + subId).destructor();
    			                     $('#graph-panel_' +actualString+"__" + subId).html('');
    			                     if(window["dbArray_"+actualString+"__" + subId].length>0){
    			                    	 createDataTableView(subId,actualString);
    			                     }
    			                     break;
    					    	}
    	         		 }
    	         			window["channelAxisArr_"+actualString+"__" + subId]=[];
    	         			for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
    	         				var quantityId=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
    	         				var resultId=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
    	         				var channelName=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
    	         				//var unitName=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
    	         				if(window["channelAxisArr_"+actualString+"__" + subId].length<1){
    	         					window["channelAxisArr_"+actualString+"__" + subId].push('<option value="0">'+ resourceObj.brix_lbl_index +'</option>');
    	         				}
    	         				window["channelAxisArr_"+actualString+"__" + subId].push('<option value="' + resultId + ',' + quantityId + '">' + channelName + '</option>');
    	         			}
    	         			 
    	         			$("#channels_"+actualString+"__" + subId).html('');
    	         			 for (var i = 0; i < window["channelAxisArr_"+actualString+"__" + subId].length; i++) {
    	         			    	$("#channels_"+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
    	         			 }
    	         			 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
    			    				$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
    			    		 }
    	         		}
     		 }else{
     			  $.LoadingOverlay("hide", spinner);
     			 webix.alert(resourceObj.brix_lbl_data_availability_for_channel);
     		 }
     		 
     		 if($$('id_window_measurement_list_'+actualString+'__'+subId)){
     			$$('id_window_measurement_list_'+actualString+'__'+subId).close()
     		 }
     	 
         },error: function(XMLHttpRequest, textStatus, errorThrown) {
             $.LoadingOverlay("hide", spinner);
         }
		})
}

/**
 * @purpose Closes channels list widow
 * @param event
 * @author Tejal
 */
function closeChannelWindow(ev){
	var eleId=ev.id;
	var str=eleId.split('id_channelWindowclose_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	$$('id_window_measurement_list_'+actualString+'__'+subId).close();
}

/**
 * @purpose Clears graph and its all objects
 * @param event
 * @author Tejal
 */
function clearGraph(ev){
	var eleId=ev.id;
	var str=eleId.split('id_clear_graph_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	if ($$('dt_' +actualString+"__"+subId) != undefined)
		$$('dt_' +actualString+"__"+subId).destructor();
	$("#graph-panel_"+actualString+"__"+subId).html('');
	$('#view_type_' +actualString+"__" + subId).val('Line_Chart_'+actualString+"__"+subId);
	 $("#channels_"+actualString+"__" + subId).show();
	 $("#id_custom_options_"+actualString+"__" + subId).show();
	 $("#xaxislabel_"+actualString+"__" + subId).show();
	 $("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_line_chart);
	$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");
	$("#channels_"+actualString+"__" + subId).html('');
	  window["isQuantityDropped_"+actualString+"__" + subId]=[];
		window["compareGraphData_"+actualString+"__" + subId]=[];
		window["dataView_"+actualString+"__" + subId] = [];
		window["indexArr_"+actualString+"__" + subId] = [];
		window["channelComboArr_"+actualString+"__" + subId] = [];
		window["channelAxisArr_"+actualString+"__" + subId] = [];
		//window["dropCount_"+actualString+"__" + count] = 0;
		window["dbArrayForPagination_"+actualString+"__" + subId]=[];
		window["dyanamicChart_"+actualString+"__" + subId] = null;
		window["dyanamicOptions_"+actualString+"__" + subId] = null;
		window["isXAxisChanged_"+actualString+"__" + subId] = false;
		window["csvArr_"+actualString+"__" + subId] = [];
		window["isQuantityDroppedForCsv_"+actualString+"__" + subId] = false;
		window["meaQuantityIdArr_"+actualString+"__" + subId] = [];
		window["compareGraphData_"+actualString+"__" + subId]=[];
		window["compareScatterGraphData_"+actualString+"__" + subId]=[];
		window["compareAreaGraphData_"+actualString+"__" + subId]=[];
		window["canvasTitleInfo_"+actualString+"__" + subId]={};
		window["quantityObj_"+actualString+"__" + subId]=[];
		window["dataCompareView_" +actualString+"__" + subId]=[]
		window["dataCompareView_"+actualString+"__" + subId] = [];
		window["meaResultDynamicArr_" +actualString+"__" + subId]=[];
		window["dyanamicMeaResultNametoPersist_" +actualString+"__" + subId]="";
		window["legendChangeFlag_" +actualString+"__" + subId] = false;
		window["channelAliasValue_"+actualString+"__" + subId]=null;
		window["channelCmbSelectedValue_"+actualString+"__" + subId]='';
		window["dbArray_"+actualString+"__" + subId]=[];
		window["chartAliseElementArray_"+actualString+"__" + subId]=[];
		window["measurementDropAlise_"+actualString+"__" + subId]=[];
}

/**
 * @purpose Image save
 * @param event
 * @author Tejal
 */
function saveAsImage(ev){
	var eleId=ev.id;
	var str=eleId.split('id_save_image_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	
	if ($("#graph-panel_"+actualString+"__" + subId + " .canvasjs-chart-canvas").get(0) != undefined || ($("#graph-panel_"+actualString+"__" + subId + " .dtCss").get(0) != undefined && window["csvArr_"+actualString+"__" + subId].length > 0) || $("#graph-panel_"+actualString+"__" + subId + " .data_compare_css").get(0) != undefined) {
    	var window = webix.ui({
    		view:"window",
    		id: "id_saveImage_download_window_dynamic_"+actualString+"__" + subId,
    		head: "<span class='brix_window_header_title'>"+resourceObj.brix_lbl_download_file+"</span><span class='brix_window_close' onclick='closeSaveImg(this)' id='id_save_image_head_"+actualString+"__" + subId+"'>&times;</span>",
    		css:"brix_window",
    		width: 300,
    	    height: 100,
    	    modal:true,
    	    position:"center",
    	    headHeight: 28,
    	    body:{
    	        template:"<div style='text-align:center;margin-top: 6%;'><a  onclick='closeSaveImageDyanamicViewDownloadWindow(this)' id='id_save_image_link_"+actualString+"__" + subId+"' style='text-decoration: none; cursor:pointer;'><i class='fa fa-download' aria-hidden='true'></i> Download</a></div>"
    	    }
      	}).show();
    } else {
        webix.alert(resourceObj.brix_lbl_data_availability_for_channel_save_image);
    }
}

/**
 * @purpose generates pdf
 * @param event
 * @author Tejal
 */
function generateDynamicPDF(ev){
	//id_save_pdf_
	var eleId=ev.id;
	var str=eleId.split('id_save_pdf_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	
	  if ($("#graph-panel_"+actualString+"__" + subId + " .canvasjs-chart-canvas").get(0) != undefined) {
	    	var window = webix.ui({
	    		view:"window",
	    		id:"pdf_download_window_dynamic_"+actualString+"__" + subId,
	    		head: "<span class='brix_window_header_title'>"+resourceObj.brix_lbl_download_file+"</span><span class='brix_window_close' onclick='closePDFWindow(this)' id='id_save_pdf_head_"+actualString+"__" + subId+"'>&times;</span>",
	    		css:"brix_window",
	    		width: 300,
	    	    height: 100,
	    	    modal:true,
	    	    position:"center",
	    	    headHeight: 28,
	    	    body:{
	    	        template:"<div style='text-align:center;margin-top: 6%;'><a  onclick='downloadPDF(this)' id='id_save_pdf_link_"+actualString+"__" + subId+"' style='text-decoration: none; cursor:pointer;'><i class='fa fa-download' aria-hidden='true'></i> Download</a></div>"
	    	    }
	    	}).show();
	    } else {
	        webix.alert(resourceObj.brix_lbl_data_availability_for_channel_to_export);
	    }
}


/**
 * @purpose Closes pdf window
 * @param event
 * @author Tejal
 */
function closePDFWindow(ev){
	var eleId=ev.id;
	var str=eleId.split('id_save_pdf_head_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	console.log(subId)
	$$("pdf_download_window_dynamic_"+actualString+"__" + subId).hide();
}

/**
 * @purpose Download pdf event
 * @param event
 * @author Tejal
 */
function downloadPDF(ev){
	var eleId=ev.id;
	var str=eleId.split('id_save_pdf_link_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	var pdf = new jsPDF("l", "mm", "a4");
	var imgData = $("#graph-panel_"+actualString+"__" + subId+ " .canvasjs-chart-canvas").get(0).toDataURL("image/jpeg","1.0");
//    pdf.addImage(imgData, 'JPEG',"0","0"); 
	pdf.addImage(imgData, 'JPEG', 0, 0, 270, 90); 
    pdf.save("download.pdf");
    $$("pdf_download_window_dynamic_"+actualString+"__" + subId).hide();
}

/**
 * @purpose Closes save image window
 * @param event
 * @author Tejal
 */
function closeSaveImg(ev){
	var eleId=ev.id;
	var str=eleId.split('id_save_image_head_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	console.log(subId)
	$$("id_saveImage_download_window_dynamic_"+actualString+"__" + subId).hide();
}


/**
 * @purpose Save image download
 * @param event
 * @author Tejal
 */
function closeSaveImageDyanamicViewDownloadWindow(ev){
	var eleId=ev.id;
	var str=eleId.split('id_save_image_link_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	console.log(subId)
	if(window["dyanamicChart_"+actualString+"__" + subId]){
		window["dyanamicChart_"+actualString+"__" + subId].exportChart({format: "jpg"});
	}
	$$("id_saveImage_download_window_dynamic_"+actualString+"__" + subId).hide();
}


/**
 * @purpose Grapg validation, generated measurement, quantity id
 * @param target,actualString,subId, chartType, canvasJsonObj, context, item, owner, nodeType
 * @author Tejal
 */
function validateGraphData(target,actualString,subId, chartType, canvasJsonObj, context, item, owner, nodeType){
	var viewType = $("#view_type_"+actualString+"__" + subId).val();
	window["isQuantityDropped_"+actualString+"__" + subId] = false;
	if(canvasJsonObj.canvastitle =='1'){
		window["canvasTitleInfo_"+actualString+"__" + subId]['canvasTitle']="Canavs "+subId;
	}
	var xAxisTitle='';
	var yAxisTitle='';
	if(canvasJsonObj.axistitle =='1'){
		window["canvasTitleInfo_"+actualString+"__" + subId]['xAxisTitle']="X-axis";
		window["canvasTitleInfo_"+actualString+"__" + subId]['yAxisTitle']="Y-axis";
	}
	
	if(viewType!="Data_Compare_"+actualString+"__" + subId){
		validateCsvArray(meaQuantityId, meaResultId, item, subId);
	}
	 if (viewType != "Data_Compare_"+actualString+"__" + subId) {
		 
		 var obj={};
		 obj['MeaQuantity']=meaQuantityId;
		 obj['MeaResult']=meaResultId;
		 var elementExists=false;
		 if(window["compareGraphData_"+actualString+"__" + subId].length>0 ){
			 for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
   				 if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == meaQuantityId && window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == meaResultId){
   					 elementExists=true;
   				 }
   			 }
		 }
   		 if(elementExists==false){
   			 var str=meaResultId +','+ meaQuantityId; 
   			 window["quantityObj_"+actualString+"__" + subId].push(str);
   		 }else{
   			 webix.alert(resourceObj.brix_lbl_channel_already_dropped);
   			 elementExists=false;
   			 return false;
   		 }
		 if(!$("#channels_"+actualString+"__" + subId).val()){
			 obj['xaxischannelname']='';
		 }else if($("#channels_"+actualString+"__" + subId).val()=="0"){
			 obj['xaxischannelname']='';
		 }else{
			 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
			 var str1=$("#channels_"+actualString+"__" + subId+" option:selected").val();
			 var meaQuanity = str1.split(',')[1];
			 var meaResult = str1.split(',')[0];
			 var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
			 for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
		   		  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == meaQuanity && window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == meaResult){
		   			obj['xaxischannelname']=window["compareGraphData_"+actualString+"__" + subId][i].actualName;
		   		  }
	   	  	}
			// obj['xaxischannelname']=modifiedLegendTextVal;
		 }
		 $.LoadingOverlay("show", spinner);
		 obj=JSON.stringify(obj);
		 $.ajax({
	         type: "POST",
	         url: "/GraphSearch/gsearch/getChannelData?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
	         data: obj,
	         contentType:"application/json; charset=utf-8",
	         dataType:"json",
	         success: function(res){
	          		console.log(res)
	          		res=res;
	          		$.LoadingOverlay("hide", spinner);
	          		if(res && res.returncode && res.returncode=='1'){
	          			
	          	   		 /*if(window["quantityObj_"+actualString+"__" + subId].length>0){
	          	   			 for(var i=0;i<window["quantityObj_"+actualString+"__" + subId].length;i++){
	          	   				 var arrEle=window["quantityObj_"+actualString+"__" + subId][i];
	          	   				 var arrEleMeaQuantity=arrEle.split(',')[1];
	          	   				 var arrEleMeaResult=arrEle.split(',')[0];
	          	   				 if(arrEleMeaQuantity == meaQuantityId && arrEleMeaResult == meaResultId){
	          	   					 elementExists=true;
	          	   				 }
	          	   			 }
	          	   		 }*/
	          			 
	          			 
	          			 var nodeName = null;
	                      var unitName = null;
	                      if(res.data && res.data.Values){
	                     	 var charData = res.data.Values;
	                          if (charData.length > 0) {
	                              nodeName = res.data.name;
	                              unitName = res.data.unit;
	                              setDynamicArray('line', charData, nodeName, subId, unitName, meaQuantityId, actualString,meaResultId);
	                              setDynamicArray('scatter', charData, nodeName, subId, unitName, meaQuantityId, actualString,meaResultId);
	                              setDynamicArray('area', charData, nodeName, subId, unitName, meaQuantityId, actualString,meaResultId);
	                              getDynamicDatatableData(meaQuantityId,meaResultId, subId,nodeName, unitName,actualString);
	                              validateChartPanelsType(target, subId, nodeName, actualString,item,owner);
	                              //drawDynamicChartOnPanels(target,charData, subId, nodeName, actualString);
	                          }else{
	                        	  webix.alert(resourceObj.brix_lbl_data_availability_for_channel); 
	                          }
	                      }
	                      
	          		}else{
	          			$.LoadingOverlay("hide", spinner);
	          			if(res && res.returncode && res.returncode=='10'){
	          				webix.alert(resourceObj.brix_lbl_channel_selection_validation_on_drop);
	          			}else{
	          				webix.alert(resourceObj.brix_lbl_channel_dont_have_data);
	          			}
	          			 
	          		}
	      		  
	         },error: function(XMLHttpRequest, textStatus, errorThrown) {
	             $.LoadingOverlay("hide", spinner);
	         }
			})
	 
    }
}

/**
 * @purpose Created data view
 * @param meaQuantityId,meaResultId, subId,nodeName, unitName,actualString
 * @author Tejal
 */
function getDynamicDatatableData(meaQuantityId,meaResultId, subId,nodeName, unitName,actualString){
	  var dbObj = {};
      dbObj['meaResultId'] = meaResultId;
      if (nodeName.indexOf('$') == '0') {
      	 	 dbObj['name']=' '+nodeName ;
      }else{
         dbObj['name'] = nodeName;
      }
      dbObj['aliasName'] = nodeName + '('+unitName+')';
      dbObj['meaQuantityId'] = meaQuantityId;
      window["dbArray_" +actualString+"__" + subId].push(dbObj);
      var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
	  if(strselectedChannelVal && strselectedChannelVal!='0'){
		  var meaQua= strselectedChannelVal.split(',')[1];
		  var meaRes= strselectedChannelVal.split(',')[0];
	      if(window["dbArray_"+actualString+"__" + subId].length>0){
			 var elementToBeSliced=-1;
			 for(var i=0;i<window["dbArray_"+actualString+"__" + subId].length;i++){
				 if(meaQua == window["dbArray_"+actualString+"__" + subId][i].meaQuantityId){
					 elementToBeSliced=i;
				 }
			 }
			 
			 if(elementToBeSliced >-1){
				 window["dbArray_"+actualString+"__" + subId].slice(elementToBeSliced,1);
			 }
		 }
	  }
}

/**
 * @purpose Creates grapg array objects
 * @param chartType, charData, nodeName, count, unitName, meaQuantityId,actualString,meaResultId,previousArr, data
 * @author Tejal
 */
function setDynamicArray(chartType, charData, nodeName, count, unitName, meaQuantityId,actualString,meaResultId,previousArr, data) {
	var legendText='';
	if(data && data.aliasName && data.aliasName!=''){
		 legendText = data.aliasName;
	}else{
		 legendText = nodeName + '(' + unitName + ')';
	}
    if (chartType != null) {
    	 var lineData = {
             type: chartType, //try changing to column, area
             showInLegend: true,
             legendText: legendText,
             name: legendText,
             actualName: nodeName,
             "meaQuantityId":meaQuantityId,
             "meaResultId":meaResultId,
             toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
             xValueFormatString:"#.##",
             yValueFormatString:"#######.##",
             dataPoints: charData
         }
    	var scatterData = {
            type: 'scatter', //try changing to column, area
            showInLegend: true,
            legendText: legendText,
            name: legendText,
            actualName: nodeName,
            "meaQuantityId":meaQuantityId,
            "meaResultId":meaResultId,
            toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
            xValueFormatString:"#.##",
            yValueFormatString:"#######.##",
            dataPoints: charData
        }
    	 var areaData = {
            type: 'area', //try changing to column, area
            showInLegend: true,
            legendText: legendText,
            name: legendText,
            actualName: nodeName,
            "meaQuantityId":meaQuantityId,
            "meaResultId":meaResultId,
            toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
            xValueFormatString:"#.##",
            yValueFormatString:"#######.##",
            dataPoints: charData
        }
        switch (chartType) {
            case 'line':
                {
            	    var index=-1;
            	    if(window["compareGraphData_"+actualString+"__" + count].length>0){
            	    	for(var i=0;i<window["compareGraphData_"+actualString+"__" + count].length;i++){
            	    		if(window["compareGraphData_"+actualString+"__" + count][i].meaQuantityId == meaQuantityId && meaResultId == window["compareGraphData_"+actualString+"__" + count][i].meaResultId){
            	    			index=i;
            	    		}
            	    	}
            	    }
            	    if(index>-1){
            	    	window["compareGraphData_"+actualString+"__" + count].splice(index, 1, lineData);
            	    }else{
            	    	window["compareGraphData_"+actualString+"__" + count].push(lineData);
            	    }
                    break;
                }
            case 'scatter':
                {
            	    var index=-1;
	            	if(window["compareScatterGraphData_"+actualString+"__" + count].length>0){
	         	    	for(var i=0;i<window["compareScatterGraphData_"+actualString+"__" + count].length;i++){
	         	    		if(window["compareScatterGraphData_"+actualString+"__" + count][i].meaQuantityId == meaQuantityId && meaResultId == window["compareScatterGraphData_"+actualString+"__" + count][i].meaResultId){
	         	    			index=i;
	         	    		}
	         	    	}
	         	    }
	        	    if(index>-1){
	        	    	window["compareScatterGraphData_"+actualString+"__" + count].splice(index, 1, scatterData);
	        	    }else{
	                    window["compareScatterGraphData_"+actualString+"__" + count].push(scatterData);
	                }
	        	    break;
                }
            case 'area':
                {
	            	var index=-1;
	            	if(window["compareAreaGraphData_"+actualString+"__" + count].length>0){
	         	    	for(var i=0;i<window["compareAreaGraphData_"+actualString+"__" + count].length;i++){
	         	    		if(window["compareAreaGraphData_"+actualString+"__" + count][i].meaQuantityId == meaQuantityId && meaResultId == window["compareAreaGraphData_"+actualString+"__" + count][i].meaResultId){
	         	    			index=i;
	         	    		}
	         	    	}
	         	    }
	        	    if(index>-1){
	        	    	window["compareAreaGraphData_"+actualString+"__" + count].splice(index, 1, areaData);
	        	    }else{
	        	    	window["compareAreaGraphData_"+actualString+"__" + count].push(areaData);
	                }
                    break;
                }
            case 'datacompare':
                {
                    var arr = [];
                    var arrData = [];
                    for (var i in charData) arrData.push(i);
                    for (var i = 0; i < arrData.length; i++) {
                        if (arrData[i].indexOf("id") >= 0) continue;
                        arr.push({
                            'meaResultName': charData["meaResultName"],
                            'nodeName': arrData[i],
                            'data': charData[arrData[i]],
                            'meaQuantIdForDataTable': charData["id"],
                            'meaResultNametoPersist': charData["meaResultName"]
                        });
                      
                    }
                    window["dataCompareView_" +actualString+"__" + count].unshift(arr);
                    break;
                }
        }
		
		
		 

    } 
}

/**
 * @purpose Creates graph array objects
 * @param target, subId, nodeName, actualString,item,owner,MeasurementDrop
 * @author Tejal
 */
function validateChartPanelsType(target, subId, nodeName, actualString,item,owner,MeasurementDrop){
	var viewType = $("#view_type_"+actualString+"__" + subId).val();
	console.log(viewType)
	var unitVal='';
    var similarChannelName = null;
    if(!MeasurementDrop){
		switch(viewType){
			case "Line_Chart_"+actualString+"__" + subId:{
				if (window["compareGraphData_" +actualString+"__" + subId].length > 0) {
                    for (var i = 0; i < window["compareGraphData_" +actualString+"__" + subId].length; i++) {
                        for (var j = i + 1; j < window["compareGraphData_" +actualString+"__" + subId].length; j++) {
                            // Adding if condition in order to check the unit name difference with alise name difference
                            if (window["compareGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                                var initialEleVal = window["compareGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                                var str1 = window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                                var str2 = window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                                str1 = parseInt(str1) + 1
                                var modifiedInitialElementUnitVal = window["compareGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                                var eleVal = window["compareGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                                var str3 = window["compareGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                                var str4 = window["compareGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                                str3 = parseInt(str3) + 1
                                var modifiedEleUnitVal = window["compareGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                                var modifiedCurrentNodeName;

                                if (initialEleVal == eleVal) {
                                    //window["compareGraphData_"+subId][j].legendText = eleVal + '_' + j+'('+modifiedEleUnitVal+')';
                                    similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                    unitVal=modifiedEleUnitVal;
                                    window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                    index = j;
                                }
                            } else if (window["compareGraphData_" +actualString+"__" + subId][i].legendText == window["compareGraphData_" +actualString+"__" + subId][j].legendText) {
                                similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                unitVal=modifiedEleUnitVal;
                                window["legendChangeFlag_"+actualString+"__" + subId] = true;
                                index = j;
                            }
                        }
                    }
                }
                if (window["legendChangeFlag_" +actualString+"__" + subId]) {
                    window["legendChangeFlag_" +actualString+"__" + subId] = false;
                    dynamicChangeLegendText(similarChannelName, index, 'line', subId, actualString,nodeName, unitVal);
                } else {
                    window["legendChangeFlag_" + subId] = false;
                    var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
                    var modifiedArr=JSON.parse(JSON.stringify(window["compareGraphData_"+actualString+"__" + subId]))
    				if(strselectedChannelVal && strselectedChannelVal!='0'){
         				 var meaQuanity= strselectedChannelVal.split(',')[1];
         				 var meaResult= strselectedChannelVal.split(',')[0];
         				
         				 if(modifiedArr.length>0){
         					 var elementToBeSliced=-1;
         					 for(var i=0;i<modifiedArr.length;i++){
         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
         							 elementToBeSliced=i;
         						 }
         					 }
         					 
         					 if(elementToBeSliced >-1){
         						modifiedArr.splice(elementToBeSliced,1);
         					 }
         				 }
    				}
                    
                    drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
                }
				
				break;
			}	
			case "Scatter_Chart_"+actualString+"__" + subId:{
				if (window["compareScatterGraphData_" +actualString+"__" + subId].length > 0) {
                    for (var i = 0; i < window["compareScatterGraphData_" +actualString+"__" + subId].length; i++) {
                        for (var j = i + 1; j < window["compareScatterGraphData_" +actualString+"__" + subId].length; j++) {
                            // Adding if condition in order to check the unit name difference with alise name difference
                            if (window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                                var initialEleVal = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                                var str1 = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                                var str2 = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                                str1 = parseInt(str1) + 1
                                var modifiedInitialElementUnitVal = window["compareScatterGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                                var eleVal = window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                                var str3 = window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                                var str4 = window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                                str3 = parseInt(str3) + 1
                                var modifiedEleUnitVal = window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                                var modifiedCurrentNodeName;

                                if (initialEleVal == eleVal) {
                                    //window["compareGraphData_"+subId][j].legendText = eleVal + '_' + j+'('+modifiedEleUnitVal+')';
                                    similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                    unitVal=modifiedEleUnitVal;
                                    window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                    index = j;
                                }
                            } else if (window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText == window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText) {
                                similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                unitVal=modifiedEleUnitVal;
                                window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                index = j;
                            }
                        }
                    }
                }
                if (window["legendChangeFlag_" +actualString+"__" + subId]) {
                    window["legendChangeFlag_" +actualString+"__" + subId] = false;

                    dynamicChangeLegendText(similarChannelName, index, 'scatter', subId, actualString,nodeName, unitVal);
                } else {
                    window["legendChangeFlag_" + subId] = false;
                    var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
                    var modifiedArr=JSON.parse(JSON.stringify(window["compareScatterGraphData_"+actualString+"__" + subId]))
    				if(strselectedChannelVal && strselectedChannelVal!='0'){
         				 var meaQuanity= strselectedChannelVal.split(',')[1];
         				 var meaResult= strselectedChannelVal.split(',')[0];
         				
         				 if(modifiedArr.length>0){
         					 var elementToBeSliced=-1;
         					 for(var i=0;i<modifiedArr.length;i++){
         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
         							 elementToBeSliced=i;
         						 }
         					 }
         					 
         					 if(elementToBeSliced >-1){
         						modifiedArr.splice(elementToBeSliced,1);
         					 }
         				 }
    				}
                    drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
                }
				break;
			}
			case "Area_Chart_"+actualString+"__" + subId:{
				if (window["compareAreaGraphData_" +actualString+"__" + subId].length > 0) {
                    for (var i = 0; i < window["compareAreaGraphData_" +actualString+"__" + subId].length; i++) {
                        for (var j = i + 1; j < window["compareAreaGraphData_" +actualString+"__" + subId].length; j++) {
                            // Adding if condition in order to check the unit name difference with alise name difference
                            if (window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                                var initialEleVal = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                                var str1 = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                                var str2 = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                                str1 = parseInt(str1) + 1
                                var modifiedInitialElementUnitVal = window["compareAreaGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                                var eleVal = window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                                var str3 = window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                                var str4 = window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                                str3 = parseInt(str3) + 1
                                var modifiedEleUnitVal = window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                                var modifiedCurrentNodeName;

                                if (initialEleVal == eleVal) {
                                    //window["compareGraphData_"+subId][j].legendText = eleVal + '_' + j+'('+modifiedEleUnitVal+')';
                                    similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                    unitVal=modifiedEleUnitVal;
                                    window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                    index = j;
                                }
                            } else if (window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText == window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText) {
                                similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                unitVal=modifiedEleUnitVal;
                                window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                index = j;
                            }
                        }
                    }
                }
                if (window["legendChangeFlag_" +actualString+"__" + subId]) {
                    window["legendChangeFlag_" +actualString+"__" + subId] = false;
                    dynamicChangeLegendText(similarChannelName, index, 'area', subId, actualString,nodeName, unitVal);
                } else {
                    window["legendChangeFlag_" + subId] = false;
                    var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
                    var modifiedArr=JSON.parse(JSON.stringify(window["compareAreaGraphData_"+actualString+"__" + subId]))
    				if(strselectedChannelVal && strselectedChannelVal!='0'){
         				 var meaQuanity= strselectedChannelVal.split(',')[1];
         				 var meaResult= strselectedChannelVal.split(',')[0];
         				
         				 if(modifiedArr.length>0){
         					 var elementToBeSliced=-1;
         					 for(var i=0;i<modifiedArr.length;i++){
         						 if(meaQuanity == modifiedArr[i].meaQuantityId){
         							 elementToBeSliced=i;
         						 }
         					 }
         					 
         					 if(elementToBeSliced >-1){
         						modifiedArr.splice(elementToBeSliced,1);
         					 }
         				 }
    				}
    				drawDynamicChartOnPanels(modifiedArr,target, subId, actualString);
                }
				break;
			}
			case "Data_View_"+actualString+"__" + subId:{
				  var duplicateFlag = false;
				  var currentNodeName, actualNodeName, unitName;
				if ($$('dt_' +actualString+"__"+subId) != undefined)
					$$('dt_' +actualString+"__"+subId).destructor();
				 $("#channels_"+actualString+"__" + subId).hide();
				 $("#id_custom_options_"+actualString+"__" + subId).hide();
				 $("#xaxislabel_"+actualString+"__" + subId).hide();
				 $("#graph-panel_"+actualString+"__" + subId).html('');
				 if (window["dbArray_" +actualString+"__" + subId].length > 0) {
                     for (var i = 0; i < window["dbArray_" +actualString+"__" + subId].length; i++) {
                         for (var j = i + 1; j < window["dbArray_" +actualString+"__" + subId].length; j++) {
                             if (window["dbArray_" +actualString+"__" + subId][i].aliasName == window["dbArray_" +actualString+"__" + subId][j].aliasName) {
                            	 actualNodeName=window["dbArray_" +actualString+"__" + subId][j].name;
                            	 var str1=window["dbArray_" +actualString+"__" + subId][j].aliasName.lastIndexOf("(");
                     			 var str2=window["dbArray_" +actualString+"__" + subId][j].aliasName.lastIndexOf(")");
                     			 str1=parseInt(str1)+1
                     			 unitName =window["dbArray_" +actualString+"__" + subId][j].aliasName.slice(str1, str2);
                            	 currentNodeName = window["dbArray_" +actualString+"__" + subId][j].aliasName + '_' + j;
                                 window["dbArray_" +actualString+"__" + subId][j].aliasName = currentNodeName;
                                 index = j;
                                 duplicateFlag = true;
                                 break;
                             }
                         }
                     }
                 }
                 if (duplicateFlag) {
                     if ($$('dt_' +actualString+"__" + subId) != undefined)
                         $$('dt_'+actualString+"__" + subId).destructor();
                     $('#graph-panel_'+actualString+"__" + subId).html('');
                     //legendText, index, chartType, subId,actualString, nodeName, unitVal
                     dynamicChangeLegendText(currentNodeName, index, 'datatable', subId,actualString, actualNodeName,unitName);
                 } else {
                     if ($$('dt_' +actualString+"__" + subId) != undefined)
                         $$('dt_' +actualString+"__" + subId).destructor();
                     $('#graph-panel_' +actualString+"__" + subId).html('');
                     if(window["dbArray_"+actualString+"__" + subId].length<1){
                 		$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");
                 	 }
                     if(window["dbArray_"+actualString+"__" + subId].length>0){
                    	 createDataTableView(subId,actualString);
                     }
                 }
				
				//drawDynamicChartOnPanels(target,charData, subId, nodeName, actualString);
				break;
			}
			case "Data_Compare_"+actualString+"__" + subId:{
				//drawDynamicChartOnPanels(target,charData, subId, nodeName, actualString);
				break;
			}
		}
    }else{
    	switch(viewType){
		case "Line_Chart_"+actualString+"__" + subId:{
			if (window["compareGraphData_" +actualString+"__" + subId].length > 0) {
                for (var i = 0; i < window["compareGraphData_" +actualString+"__" + subId].length; i++) {
                    for (var j = i + 1; j < window["compareGraphData_" +actualString+"__" + subId].length; j++) {
                        // Adding if condition in order to check the unit name difference with alise name difference
                        if (window["compareGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                            var initialEleVal = window["compareGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                            var str1 = window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                            var str2 = window["compareGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                            str1 = parseInt(str1) + 1
                            var modifiedInitialElementUnitVal = window["compareGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                            var eleVal = window["compareGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                            var str3 = window["compareGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                            var str4 = window["compareGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                            str3 = parseInt(str3) + 1
                            var modifiedEleUnitVal = window["compareGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                            var modifiedCurrentNodeName;
                            if (initialEleVal == eleVal) {
                                similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                unitVal=modifiedEleUnitVal;
                                window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                index = j;
                                var obj={};
                                obj['similarChannelName']=similarChannelName;
                                obj['actualChannelName']=eleVal;
                                obj['unitVal']=unitVal;
                                obj['index']=j;
                                obj['meaResultId']=window["compareGraphData_"+actualString+"__" + subId][j].meaResultId;
                                obj['meaQuantityId']=window["compareGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                                window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                            }
                        } else if (window["compareGraphData_" +actualString+"__" + subId][i].legendText == window["compareGraphData_" +actualString+"__" + subId][j].legendText) {
                            similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                            unitVal=modifiedEleUnitVal;
                            window["legendChangeFlag_"+actualString+"__" + subId] = true;
                            index = j;
                            var obj={};
                            obj['similarChannelName']=similarChannelName;
                            obj['actualChannelName']=eleVal;
                            obj['unitVal']=unitVal;
                            obj['index']=j;
                            obj['meaResultId']=window["compareGraphData_"+actualString+"__" + subId][j].meaResultId;
                            obj['meaQuantityId']=window["compareGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                            window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                        }
                    }
                }
            }
            if (window["legendChangeFlag_" +actualString+"__" + subId]) {
                window["legendChangeFlag_" +actualString+"__" + subId] = false;
                dynamicChangeLegendTextForMeasurementDrop(window["measurementDropAlise_"+actualString+"__" + subId], index, 'line', subId, actualString,nodeName, unitVal);
            } else {
                window["legendChangeFlag_" + subId] = false;
                drawDynamicChartOnPanels(window["compareGraphData_"+actualString+"__" + subId],target, subId, actualString);
            }
			
			break;
		}	
		case "Scatter_Chart_"+actualString+"__" + subId:{
			if (window["compareScatterGraphData_" +actualString+"__" + subId].length > 0) {
                for (var i = 0; i < window["compareScatterGraphData_" +actualString+"__" + subId].length; i++) {
                    for (var j = i + 1; j < window["compareScatterGraphData_" +actualString+"__" + subId].length; j++) {
                        // Adding if condition in order to check the unit name difference with alise name difference
                        if (window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                            var initialEleVal = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                            var str1 = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                            var str2 = window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                            str1 = parseInt(str1) + 1
                            var modifiedInitialElementUnitVal = window["compareScatterGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                            var eleVal = window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                            var str3 = window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                            var str4 = window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                            str3 = parseInt(str3) + 1
                            var modifiedEleUnitVal = window["compareScatterGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                            var modifiedCurrentNodeName;
                            if (initialEleVal == eleVal) {
                            	  similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                  unitVal=modifiedEleUnitVal;
                                  window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                  index = j;
                                  var obj={};
                                  obj['similarChannelName']=similarChannelName;
                                  obj['actualChannelName']=eleVal;
                                  obj['unitVal']=unitVal;
                                  obj['index']=j;
                                  obj['meaResultId']=window["compareScatterGraphData_"+actualString+"__" + subId][j].meaResultId;
                                  obj['meaQuantityId']=window["compareScatterGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                                  window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                            }
                        } else if (window["compareScatterGraphData_" +actualString+"__" + subId][i].legendText == window["compareScatterGraphData_" +actualString+"__" + subId][j].legendText) {
                        	similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                            unitVal=modifiedEleUnitVal;
                            window["legendChangeFlag_"+actualString+"__" + subId] = true;
                            index = j;
                            var obj={};
                            obj['similarChannelName']=similarChannelName;
                            obj['actualChannelName']=eleVal;
                            obj['unitVal']=unitVal;
                            obj['index']=j;
                            obj['meaResultId']=window["compareScatterGraphData_"+actualString+"__" + subId][j].meaResultId;
                            obj['meaQuantityId']=window["compareScatterGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                            window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                        }
                    }
                }
            }
			 if (window["legendChangeFlag_" +actualString+"__" + subId]) {
                window["legendChangeFlag_" +actualString+"__" + subId] = false;
                dynamicChangeLegendTextForMeasurementDrop(window["measurementDropAlise_"+actualString+"__" + subId], index, 'scatter', subId, actualString,nodeName, unitVal);
            } else {
                window["legendChangeFlag_" + subId] = false;
                drawDynamicChartOnPanels(window["compareScatterGraphData_"+actualString+"__" + subId],target, subId, actualString);
            }
			break;
		}
		case "Area_Chart_"+actualString+"__" + subId:{
			if (window["compareAreaGraphData_" +actualString+"__" + subId].length > 0) {
                for (var i = 0; i < window["compareAreaGraphData_" +actualString+"__" + subId].length; i++) {
                    for (var j = i + 1; j < window["compareAreaGraphData_" +actualString+"__" + subId].length; j++) {
                        // Adding if condition in order to check the unit name difference with alise name difference
                        if (window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.indexOf('(') != -1 && window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.indexOf('(') != -1) {
                            var initialEleVal = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.substring(0, window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(") + 0);
                            var str1 = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf("(");
                            var str2 = window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText.lastIndexOf(")");
                            str1 = parseInt(str1) + 1
                            var modifiedInitialElementUnitVal = window["compareAreaGraphData_"+actualString+"__" + subId][i].legendText.slice(str1, str2);
                            var eleVal = window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.substring(0, window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.lastIndexOf("(") + 0);
                            var str3 = window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf("(");
                            var str4 = window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText.lastIndexOf(")");
                            str3 = parseInt(str3) + 1
                            var modifiedEleUnitVal = window["compareAreaGraphData_"+actualString+"__" + subId][j].legendText.slice(str3, str4);
                            var modifiedCurrentNodeName;

                            if (initialEleVal == eleVal) {
                                //window["compareGraphData_"+subId][j].legendText = eleVal + '_' + j+'('+modifiedEleUnitVal+')';
                            	 similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                                 unitVal=modifiedEleUnitVal;
                                 window["legendChangeFlag_" +actualString+"__" + subId] = true;
                                 index = j;
                                 var obj={};
                                 obj['similarChannelName']=similarChannelName;
                                 obj['actualChannelName']=eleVal;
                                 obj['unitVal']=unitVal;
                                 obj['index']=j;
                                 obj['meaResultId']=window["compareAreaGraphData_"+actualString+"__" + subId][j].meaResultId;
                                 obj['meaQuantityId']=window["compareAreaGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                                 window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                            }
                        } else if (window["compareAreaGraphData_" +actualString+"__" + subId][i].legendText == window["compareAreaGraphData_" +actualString+"__" + subId][j].legendText) {
                        	similarChannelName = eleVal + '_' + j + '(' + modifiedEleUnitVal + ')';
                            unitVal=modifiedEleUnitVal;
                            window["legendChangeFlag_"+actualString+"__" + subId] = true;
                            index = j;
                            var obj={};
                            obj['similarChannelName']=similarChannelName;
                            obj['actualChannelName']=eleVal;
                            obj['unitVal']=unitVal;
                            obj['index']=j;
                            obj['meaResultId']=window["compareAreaGraphData_"+actualString+"__" + subId][j].meaResultId;
                            obj['meaQuantityId']=window["compareAreaGraphData_"+actualString+"__" + subId][j].meaQuantityId;
                            window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                        }
                    }
                }
            }
			 if (window["legendChangeFlag_" +actualString+"__" + subId]) {
	                window["legendChangeFlag_" +actualString+"__" + subId] = false;
	                dynamicChangeLegendTextForMeasurementDrop(window["measurementDropAlise_"+actualString+"__" + subId], index, 'area', subId, actualString,nodeName, unitVal);
	            } else {
	                window["legendChangeFlag_" + subId] = false;
	                drawDynamicChartOnPanels(window["compareAreaGraphData_"+actualString+"__" + subId],target, subId, actualString);
	            }
			break;
		}
		case "Data_View_"+actualString+"__" + subId:{
			  var duplicateFlag = false;
			  var currentNodeName, actualNodeName, unitName;
			if ($$('dt_' +actualString+"__"+subId) != undefined)
				$$('dt_' +actualString+"__"+subId).destructor();
			 $("#channels_"+actualString+"__" + subId).hide();
			 $("#id_custom_options_"+actualString+"__" + subId).hide();
			 $("#xaxislabel_"+actualString+"__" + subId).hide();
			 $("#graph-panel_"+actualString+"__" + subId).html('');
			 if (window["dbArray_" +actualString+"__" + subId].length > 0) {
                 for (var i = 0; i < window["dbArray_" +actualString+"__" + subId].length; i++) {
                     for (var j = i + 1; j < window["dbArray_" +actualString+"__" + subId].length; j++) {
                         if (window["dbArray_" +actualString+"__" + subId][i].aliasName == window["dbArray_" +actualString+"__" + subId][j].aliasName) {
                        	 actualNodeName=window["dbArray_" +actualString+"__" + subId][j].name;
                        	 var str1=window["dbArray_" +actualString+"__" + subId][j].aliasName.lastIndexOf("(");
                 			 var str2=window["dbArray_" +actualString+"__" + subId][j].aliasName.lastIndexOf(")");
                 			 str1=parseInt(str1)+1
                 			 unitName =window["dbArray_" +actualString+"__" + subId][j].aliasName.slice(str1, str2);
                        	 currentNodeName = window["dbArray_" +actualString+"__" + subId][j].aliasName + '_' + j;
                             window["dbArray_" +actualString+"__" + subId][j].aliasName = currentNodeName;
                             index = j;
                             duplicateFlag = true;
                             
                             var obj={};
                             obj['similarChannelName']=currentNodeName;
                             obj['actualChannelName']=actualNodeName;
                             obj['unitVal']=unitName;
                             obj['index']=j;
                             obj['meaResultId']=window["dbArray_"+actualString+"__" + subId][j].meaResultId;
                             obj['meaQuantityId']=window["dbArray_"+actualString+"__" + subId][j].meaQuantityId;
                             window["measurementDropAlise_"+actualString+"__" + subId].push(obj)
                             break;
                         }
                     }
                 }
             }
             if (duplicateFlag) {
                 if ($$('dt_' +actualString+"__" + subId) != undefined)
                     $$('dt_'+actualString+"__" + subId).destructor();
                 $('#graph-panel_'+actualString+"__" + subId).html('');
                 //legendText, index, chartType, subId,actualString, nodeName, unitVal
                 dynamicChangeLegendTextForMeasurementDrop(window["measurementDropAlise_"+actualString+"__" + subId], index, 'datatable', subId, actualString,nodeName, unitName);
                // dynamicChangeLegendText(currentNodeName, index, 'datatable', subId,actualString, actualNodeName,unitName);
             } else {
                 if ($$('dt_' +actualString+"__" + subId) != undefined)
                     $$('dt_' +actualString+"__" + subId).destructor();
                 $('#graph-panel_' +actualString+"__" + subId).html('');
                 if(window["dbArray_"+actualString+"__" + subId].length<1){
             		$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");
             	 }
                 if(window["dbArray_"+actualString+"__" + subId].length>0){
                	 createDataTableView(subId,actualString);
                 }
             }
			break;
		}
	}
    }
	if(MeasurementDrop){
		window["channelAxisArr_"+actualString+"__" + subId]=[];
		for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
			var quantityId=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
			var resultId=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
			var channelName=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
			//var unitName=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
			if(window["channelAxisArr_"+actualString+"__" + subId].length<1){
				window["channelAxisArr_"+actualString+"__" + subId].push('<option value="0">'+ resourceObj.brix_lbl_index +'</option>');
			}
			window["channelAxisArr_"+actualString+"__" + subId].push('<option value="' + resultId + ',' + quantityId + '">' + channelName + '</option>');
		}
		$("#channels_"+actualString+"__" + subId).html('');
		 for (var i = 0; i < window["channelAxisArr_"+actualString+"__" + subId].length; i++) {
		    	$("#channels_"+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
		 }
	}else{
		 fillDynamicPanelsChannelCombo(actualString,subId, nodeName,item,owner);
	}
	
}

/**
 * @purpose Alias window for measurement drop
 * @param target, subId, nodeName, actualString,item,owner,MeasurementDrop
 * @author Tejal
 */
function dynamicChangeLegendTextForMeasurementDrop(obj, index, chartType, subId,actualString, nodeName, unitVal){
	console.log('obj:::'+JSON.stringify(obj));
	var value = webix.ui({
		  view:"window",
		  modal:true,  		
		  id : 'id_win_change_channel_name_measurement_drop'+actualString+"__" + subId,
		  position: 'center',
		  head:resourceObj.brix_lbl_change_alise,
		  headHeight: 28, 
		  css:"dbAnalysisaliasWindow brix_popup",
		  body:{
			  height: setHeightInPercentage(32),
              width: setWidthInPercentage(30),
              view:'form',
			  elements:[
			           {  
			        	   gravity:1,
						  rows:[
									{
										  id:'id_mesurement_alise_view_'+actualString+"__" + subId,
										  height: setHeightInPercentage(25),
										  rows:[],
									},
									{
										 height:40,
										cols:[
										      {
										    	  
										      },
										      {
										    	     view : 'button', 
												    value:resourceObj.brix_lbl_ok, 
												     width:120,
												    css:"brix_button",
												    id:'id_ok_btn_mesurement_alise_view_'+actualString+"__" + subId,
												    click : function(){
												      var modifiedObj=JSON.parse(JSON.stringify(obj));
												      for(var i=0;i<obj.length;i++){
												    	  for(var j=0;j<modifiedObj.length;j++){
												    		  if((modifiedObj[j].meaResultId == obj[i].meaResultId) && (modifiedObj[j].meaQuantityId == obj[i].meaQuantityId)){
												    			 var value= $$('id_measurement_drop_text_box_'+actualString+"__" + subId+'___'+i).getValue();
												    			 value = value.trim();
												    			 if(value!=''){
												    				 modifiedObj[j].similarChannelName=value;
												    			 }
												    		  }
												    	  }
													  }
												      console.log('modifiedObj:::'+JSON.stringify(modifiedObj));
												      var channelAvailFlag=0;
												    	if(chartType == 'line'){
													    	for(var l=0;l<window["compareGraphData_"+actualString+"__" + subId].length;l++){
													    		for(var  k=0;k<modifiedObj.length;k++){
													    			if(window["compareGraphData_"+actualString+"__" + subId][l].legendText == modifiedObj[k].similarChannelName){
														    			channelAvailFlag++;
														    			break;
														    		}
													    		}
													    	}
												    	}
												    	if(chartType == 'scatter'){
													    	for(var l=0;l<window["compareScatterGraphData_"+actualString+"__" + subId].length;l++){
													    		if(window["compareScatterGraphData_"+actualString+"__" + subId][l].legendText == window["channelAliasValue_"+actualString+"__" + subId]){
													    			for(var  k=0;k<modifiedObj.length;k++){
														    			if(window["compareScatterGraphData_"+actualString+"__" + subId][l].legendText == modifiedObj[k].similarChannelName){
															    			channelAvailFlag++;
															    			break;
															    		}
														    		}
													    		}
													    	}
												    	}
												    	if(chartType == 'area'){
													    	for(var l=0;l<window["compareAreaGraphData_"+actualString+"__" + subId].length;l++){
													    		for(var  k=0;k<modifiedObj.length;k++){
													    			if(window["compareAreaGraphData_"+actualString+"__" + subId][l].legendText == modifiedObj[k].similarChannelName){
														    			channelAvailFlag++;
														    			break;
														    		}
													    		}
													    	}
												    	}
												    	if(channelAvailFlag>=1){
												    			webix.alert(resourceObj.brix_lbl_alise_name);
												    	}else{
												    		for(var  k=0;k<modifiedObj.length;k++){
													    		for(var l=0;l<window["compareGraphData_"+actualString+"__" + subId].length;l++){
													    			if(window["compareGraphData_"+actualString+"__" + subId][l].meaQuantityId == modifiedObj[k].meaQuantityId){
													    				window["compareGraphData_"+actualString+"__" + subId][l].legendText=modifiedObj[k].similarChannelName;
													    			}
														    	}
												    		}
												    		for(var  k=0;k<modifiedObj.length;k++){
													    		for(var l=0;l<window["compareScatterGraphData_"+actualString+"__" + subId].length;l++){
													    			if(window["compareScatterGraphData_"+actualString+"__" + subId][l].meaQuantityId == modifiedObj[k].meaQuantityId){
													    				window["compareScatterGraphData_"+actualString+"__" + subId][l].legendText=modifiedObj[k].similarChannelName;
													    			}
														    	}
												    		}
												    		for(var  k=0;k<modifiedObj.length;k++){
													    		for(var l=0;l<window["compareAreaGraphData_"+actualString+"__" + subId].length;l++){
													    			if(window["compareAreaGraphData_"+actualString+"__" + subId][l].meaQuantityId == modifiedObj[k].meaQuantityId){
													    				window["compareAreaGraphData_"+actualString+"__" + subId][l].legendText=modifiedObj[k].similarChannelName;
													    			}
														    	}
												    		}
												    		for(var  k=0;k<modifiedObj.length;k++){
													    		for(var l=0;l<window["dbArray_"+actualString+"__" + subId].length;l++){
													    			if(window["dbArray_"+actualString+"__" + subId][l].meaQuantityId == modifiedObj[k].meaQuantityId){
													    				window["dbArray_"+actualString+"__" + subId][l].aliasName=modifiedObj[k].similarChannelName;
													    			}
														    	}
												    		}
												    		
												    		window["channelAxisArr_"+actualString+"__" + subId]=[];
															for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
																var quantityId=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
																var resultId=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
																var channelName=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
																//var unitName=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
																if(window["channelAxisArr_"+actualString+"__" + subId].length<1){
																	window["channelAxisArr_"+actualString+"__" + subId].push('<option value="0">'+ resourceObj.brix_lbl_index +'</option>');
																}
																window["channelAxisArr_"+actualString+"__" + subId].push('<option value="' + resultId + ',' + quantityId + '">' + channelName + '</option>');
															}
															 $("#channels_"+actualString+"__" + subId).html(''); 
															 for (var i = 0; i < window["channelAxisArr_"+actualString+"__" + subId].length; i++) {
															    	$("#channels_"+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
															 }
															 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
												    				$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
												    		 }
															var target="graph-panel_"+actualString+"__"+subId;
												    		if(chartType == 'line'){
													    		drawDynamicChartOnPanels(window["compareGraphData_"+actualString+"__" + subId],target, subId, actualString);
												    		} if(chartType == 'scatter'){
													    		drawDynamicChartOnPanels(window["compareScatterGraphData_"+actualString+"__" + subId],target, subId, actualString);
													    	}if(chartType == 'area'){
													    		drawDynamicChartOnPanels(window["compareAreaGraphData_"+actualString+"__" + subId],target, subId, actualString);
													    	}if(chartType == 'datatable'){
													    		 if ($$('dt_' +actualString+"__" + subId) != undefined)
											                         $$('dt_' +actualString+"__" + subId).destructor();
											                     $('#graph-panel_' +actualString+"__" + subId).html('');
											                     if(window["dbArray_"+actualString+"__" + subId].length>0){
											                    	 createDataTableView(subId,actualString);
											                     }
													    	}
												    		
												    		for(var i=0;i<obj.length;i++){
														  		  $$('id_mesurement_alise_view_'+actualString+"__" + subId).removeView('id_measurement_drop_text_box_'+actualString+"__" + subId+'___'+i)
														  	  }
												    		window["measurementDropAlise_"+actualString+"__" + subId]=[];
														  	  $$('id_win_change_channel_name_measurement_drop'+actualString+"__" + subId).close();
												    	}
												  	  
												    }
										      },
										      {
										    	  
										      }
										]
										
									}
						        ]
			           }
			  ]
			 
		  },
		  on:{
			  onShow:function(){
				  for(var i=0;i<obj.length;i++){
					  $$('id_mesurement_alise_view_'+actualString+"__" + subId).addView({
						  	view: "text",
							id: 'id_measurement_drop_text_box_'+actualString+"__" + subId+'___'+i,
							name: obj[i].similarChannelName,
							value:obj[i].similarChannelName,
							label: obj[i].actualChannelName,
							height:40,
							css: 'brix_textbox',
					  })
				  }
			  }
		  }
		}).show();
}

/**
 * @purpose Alias window for channel drop
 * @param target, subId, nodeName, actualString,item,owner,MeasurementDrop
 * @author Tejal
 */
function dynamicChangeLegendText(legendText, index, chartType, subId,actualString, nodeName, unitVal){
	$.LoadingOverlay("hide", spinner);
	var target="graph-panel_"+actualString+"__"+subId;
	legndVal=nodeName+'('+unitVal+')'
	var modifiedLgendText, actaulUnitVal;
	if(chartType == 'datatable'){
		modifiedLgendText=legendText
	}else{
		if(legendText.indexOf('(') != -1){
			modifiedLgendText = legendText.substring(0, legendText.lastIndexOf("(") + 0);
			var str1=legendText.lastIndexOf("(");
			var str2=legendText.lastIndexOf(")");
			str1=parseInt(str1)+1
			actaulUnitVal =legendText.slice(str1, str2);
			
		}else{
			modifiedLgendText=legendText
		}
	}
	var value = webix.ui({
		  view:"window",
		  modal:true,  		
		  id : 'id-win-change-channel-Name_'+actualString+"__" + subId,
		  width:220,
		  height: 150,
		  position: 'center',
		  head:resourceObj.brix_lbl_change_alise,
		  headHeight: 28, 
		  css:"dbAnalysisaliasWindow brix_popup",
		  body:{
			  padding:10,
			    rows : [{
			      view : 'text',
			      id:"id-text-change-channel-Name_" +actualString+"__" + subId,
				  value : modifiedLgendText
			    },{
			    	height:15
			    },{
			      view : 'button', 
			      value:resourceObj.brix_lbl_ok, 
			      css:"brix_button",
			      id:'id-ok_btn-change-channel-Name_'+actualString+"__" + subId,
			      click : function(){
			    	var eleValaue=$$('id-text-change-channel-Name_' +actualString+"__" + subId).getValue(); 
			    	if(eleValaue !=''){
			    		window["channelAliasValue_"+actualString+"__" + subId] = $$('id-text-change-channel-Name_' +actualString+"__" + subId).getValue();
				    	var nodeVal=window["channelAliasValue_"+actualString+"__" + subId];
				    	if(actaulUnitVal){
				    		window["channelAliasValue_"+actualString+"__" + subId] = window["channelAliasValue_"+actualString+"__" + subId]+'('+actaulUnitVal+')'
				    	}
				    	var channelAvailFlag=0;
				    	if(chartType == 'line'){
					    	for(var l=0;l<window["compareGraphData_"+actualString+"__" + subId].length;l++){
					    		if(window["compareGraphData_"+actualString+"__" + subId][l].legendText == window["channelAliasValue_"+actualString+"__" + subId]){
					    			channelAvailFlag++;
					    			break;
					    		}
					    	}
				    	}
				    	if(chartType == 'scatter'){
					    	for(var l=0;l<window["compareScatterGraphData_"+actualString+"__" + subId].length;l++){
					    		if(window["compareScatterGraphData_"+actualString+"__" + subId][l].legendText == window["channelAliasValue_"+actualString+"__" + subId]){
					    			channelAvailFlag++;
					    			break;
					    		}
					    	}
				    	}
				    	if(chartType == 'area'){
					    	for(var l=0;l<window["compareAreaGraphData_"+actualString+"__" + subId].length;l++){
					    		if(window["compareAreaGraphData_"+actualString+"__" + subId][l].legendText == window["channelAliasValue_"+actualString+"__" + subId]){
					    			channelAvailFlag++;
					    			break;
					    		}
					    	}
				    	}
				    	if(channelAvailFlag>=1){
				    			webix.alert(resourceObj.brix_lbl_alise_name);
				    	}else{
					    	window["compareGraphData_"+actualString+"__" + subId][index].legendText = window["channelAliasValue_"+actualString+"__" + subId];
					    	window["compareScatterGraphData_"+actualString+"__" + subId][index].legendText = window["channelAliasValue_"+actualString+"__" + subId];
					    	window["compareAreaGraphData_"+actualString+"__" + subId][index].legendText = window["channelAliasValue_"+actualString+"__" + subId];

					    	if(window["dbArray_"+actualString+"__" + subId] && window["dbArray_"+actualString+"__" + subId][index]){
						    	window["dbArray_"+actualString+"__" + subId][index].aliasName =nodeVal;
					    	}
					    	if(window["channelAxisArr_"+actualString+"__" + subId].length > 0){
					    		for(var i=0;i<window["channelAxisArr_"+actualString+"__" + subId].length;i++){
					    			if(window["channelAxisArr_"+actualString+"__" + subId][i].indexOf(nodeName) != -1){
					    				window["legendChangeFlag_"+actualString+"__" + subId] = true;
					    				index = i;
					    			}
					    		}
					    		if(window["legendChangeFlag_"+actualString+"__" + subId]){
					    			if(index>-1){
					    				var selectedChannelEle=$("#channels_"+actualString+"__" + subId).val();
						    			window["legendChangeFlag_"+actualString+"__" + subId] = false;
						    			var opt = window["channelAxisArr_"+actualString+"__" + subId][index];
						    			var newOpt = opt.replace(legndVal, window["channelAliasValue_"+actualString+"__" + subId]);
						    			window["channelAxisArr_"+actualString+"__" + subId][index] = newOpt;
						    			$('#channels_'+actualString+"__" + subId).html('');
						    			for(var i=0;i<window["channelAxisArr_"+actualString+"__" + subId].length;i++){
						    				$('#channels_'+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
						    			}
						    			if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
						    				$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
						    			//	$('#channels_'+actualString+"__" + subId).trigger('change');
						    			}
					    			}
					    		}
					    	}
					    	if(chartType == 'line'){
					    		drawDynamicChartOnPanels(window["compareGraphData_"+actualString+"__" + subId],target, subId, actualString);
					    	} if(chartType == 'scatter'){
					    		drawDynamicChartOnPanels(window["compareScatterGraphData_"+actualString+"__" + subId],target, subId, actualString);
					    	}if(chartType == 'area'){
					    		drawDynamicChartOnPanels(window["compareAreaGraphData_"+actualString+"__" + subId],target, subId, actualString);
					    	}if(chartType == 'datatable'){
					    		 if ($$('dt_' +actualString+"__" + subId) != undefined)
			                         $$('dt_' +actualString+"__" + subId).destructor();
			                     $('#graph-panel_' +actualString+"__" + subId).html('');
			                     if(window["dbArray_"+actualString+"__" + subId].length>0){
			                    	 createDataTableView(subId,actualString);
			                     }
					    	}
							$$('id-win-change-channel-Name_'+actualString+"__" + subId).hide();
				    	}
			    	}else{
			    		webix.alert(resourceObj.brix_lbl_aliseval)
			    	}
			      }
			    }]
		  }
		}).show();
}

/**
 * @purpose Maintains graph objects
 * @param target, subId, actualString,xAxisName,xAxisVal, data, clickedFrom
 * @author Tejal
 */
function drawGraphOnXAxisSelection(target, subId, actualString,xAxisName,xAxisVal, data, clickedFrom){
	if(xAxisName && xAxisVal){
		var viewType = $("#view_type_"+actualString+"__" + subId).val();
		var lineDataArr=[];
		var areaDataArr=[];
		var scatterDataArr=[];
		 for(var i=0;i<data.length;i++){
  			(function (i) {
  				var newMeaQuantityId = data[i].id;
  				var newmeaResultId;
  				if(data[i].meaResultId){
  					newmeaResultId = data[i].meaResultId;
  				}else{
  					newmeaResultId = data[i].meaReultId;
  				}
  				 
  				var charData = data[i].Values;
  				var nodeName= data[i].name;
  				var unitName = data[i].unit;
  				var legendText='';
  				if(data[i].aliasName){
  					legendText = data[i].aliasName;
  				}else{
  					legendText = nodeName + '(' + unitName + ')';
  				}
  			    console.log('charData'+JSON.stringify(charData));
  			    console.log('window["compareGraphData_"+actualString+"__" + subId]'+JSON.stringify(window["compareGraphData_"+actualString+"__" + subId]))
	  			if( viewType =="Line_Chart_"+actualString+"__" + subId){
	  				var obj={
	  					 type: 'line', //try changing to column, area
	  		             showInLegend: true,
	  		             legendText: legendText,
	  		             name: legendText,
	  		             actualName:charData.name,
	  		             "meaQuantityId":newMeaQuantityId,
	  		             "meaResultId":newmeaResultId,
	  		             toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
	  		             xValueFormatString:"#.##",
	  		             yValueFormatString:"#######.##",
	  		             dataPoints: charData
	  				}
	  				lineDataArr.push(obj)
	  			}	
	  			else if(viewType =="Scatter_Chart_"+actualString+"__" + subId){
	  				var obj={
		  					 type: 'scatter', //try changing to column, area
		  		             showInLegend: true,
		  		             legendText: legendText,
		  		             name: legendText,
		  		             "meaQuantityId":newMeaQuantityId,
		  		             "meaResultId":newmeaResultId,
		  		           actualName:charData.name,
		  		             toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
		  		             xValueFormatString:"#.##",
		  		             yValueFormatString:"#######.##",
		  		             dataPoints: charData
		  				}
	  				scatterDataArr.push(obj)
	  			}
	  			else if(viewType =="Area_Chart_"+actualString+"__" + subId){
	  				var obj={
		  					 type: 'area', //try changing to column, area
		  		             showInLegend: true,
		  		             legendText: legendText,
		  		             name: legendText,
		  		             actualName:charData.name,
		  		             "meaQuantityId":newMeaQuantityId,
		  		             "meaResultId":newmeaResultId,
		  		             toolTipContent: "<span style='\"'color: {color};'\"'>{name}</span>: {y}",
		  		             xValueFormatString:"#.##",
		  		             yValueFormatString:"#######.##",
		  		             dataPoints: charData
		  				}
	  				areaDataArr.push(obj)
	  			}
  			})(i);
  		 }
		var meaQuanity=xAxisVal.split(',')[1];
		var meaResult=xAxisVal.split(',')[0];
		switch(viewType){
			case "Line_Chart_"+actualString+"__" + subId:{
				 if(window["compareGraphData_"+actualString+"__" + subId].length>0){
						for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
							for(var j=0;j<lineDataArr.length;j++){
								if((window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == lineDataArr[j].meaQuantityId) &&(window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == lineDataArr[j].meaResultId)){
									lineDataArr[j].legendText = window["compareGraphData_"+actualString+"__" + subId][i].legendText;
									lineDataArr[j].name = window["compareGraphData_"+actualString+"__" + subId][i].legendText;
									lineDataArr[j].actualName= window["compareGraphData_"+actualString+"__" + subId][i].actualName;
								}
							}
						}
					}
				drawDynamicChartOnPanels(lineDataArr,target, subId, actualString);
				break;
			}	
			case "Scatter_Chart_"+actualString+"__" + subId:{
				 if(window["compareScatterGraphData_"+actualString+"__" + subId].length>0){
						for(var i=0;i<window["compareScatterGraphData_"+actualString+"__" + subId].length;i++){
							for(var j=0;j<scatterDataArr.length;j++){
								if((window["compareScatterGraphData_"+actualString+"__" + subId][i].meaQuantityId == scatterDataArr[j].meaQuantityId) &&(window["compareScatterGraphData_"+actualString+"__" + subId][i].meaResultId == scatterDataArr[j].meaResultId)){
									scatterDataArr[j].legendText = window["compareScatterGraphData_"+actualString+"__" + subId][i].legendText;
									scatterDataArr[j].name = window["compareScatterGraphData_"+actualString+"__" + subId][i].legendText;
									scatterDataArr[j].actualName= window["compareScatterGraphData_"+actualString+"__" + subId][i].actualName;
								}
							}
						}
					}
				drawDynamicChartOnPanels(scatterDataArr,target, subId, actualString);
				break;
			}
			case "Area_Chart_"+actualString+"__" + subId:{
				 if(window["compareAreaGraphData_"+actualString+"__" + subId].length>0){
						for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
							for(var j=0;j<areaDataArr.length;j++){
								if((window["compareAreaGraphData_"+actualString+"__" + subId][i].meaQuantityId == areaDataArr[j].meaQuantityId) &&(window["compareAreaGraphData_"+actualString+"__" + subId][i].meaResultId == areaDataArr[j].meaResultId)){
									areaDataArr[j].legendText = window["compareAreaGraphData_"+actualString+"__" + subId][i].legendText;
									areaDataArr[j].name = window["compareAreaGraphData_"+actualString+"__" + subId][i].legendText;
									areaDataArr[j].actualName= window["compareAreaGraphData_"+actualString+"__" + subId][i].actualName;
								}
							}
						}
					}
				drawDynamicChartOnPanels(areaDataArr,target, subId, actualString);
				break;
			}
		}
			
			window["channelAxisArr_"+actualString+"__" + subId]=[];
			for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
				var quantityId=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
				var resultId=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
				var channelName=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
				//var unitName=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
				if(window["channelAxisArr_"+actualString+"__" + subId].length<1){
					window["channelAxisArr_"+actualString+"__" + subId].push('<option value="0">'+ resourceObj.brix_lbl_index +'</option>');
				}
				window["channelAxisArr_"+actualString+"__" + subId].push('<option value="' + resultId + ',' + quantityId + '">' + channelName + '</option>');
			}
			 $("#channels_"+actualString+"__" + subId).html(''); 
			 for (var i = 0; i < window["channelAxisArr_"+actualString+"__" + subId].length; i++) {
			    	$("#channels_"+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
			 }
			if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
				$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
			}
	}
}

/**
 * @purpose Fill up channel combo
 * @param actualString,subId, nodeName,item,owner
 * @author Tejal
 */
function fillDynamicPanelsChannelCombo(actualString,subId, nodeName,item,owner){
	var quantityId,measurementId
	if(item.measurementId){
		measurementId=item.measurementId;
	}else{
		measurementId=$$(owner).getItem($$(owner).getParentId(item.id)).nodeId;
	}
	
	if(item.nodeId){
		quantityId=item.nodeId;
	}
	
	if(window["channelAxisArr_"+actualString+"__" + subId].length<1){
		window["channelAxisArr_"+actualString+"__" + subId].push('<option value="0">'+ resourceObj.brix_lbl_index +'</option>');
	}
	window["channelAxisArr_"+actualString+"__" + subId].push('<option value="' + measurementId  + ',' +quantityId+ '">' + item.value + '(' + item.unit + ')</option>');
	$("#channels_"+actualString+"__" + subId).html('');
    for (var i = 0; i < window["channelAxisArr_"+actualString+"__" + subId].length; i++) {
    	$("#channels_"+actualString+"__" + subId).append(window["channelAxisArr_"+actualString+"__" + subId][i]);
    }
    
    if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!=''){
		$('#channels_'+actualString+"__" + subId).val(window["channelCmbSelectedValue_"+actualString+"__" + subId]);
	}
}

/**
 * @purpose Creates graph
 * @param graphData,target, subId, actualString
 * @author Tejal
 */
function drawDynamicChartOnPanels(graphData,target, subId, actualString){
	 for (var j=0; j<graphData.length; j++){
  		if(j<21){
  			graphData[j].color=colorArray[j];
  		}
     } 
	 var xAxisTitle='';
	 var yAxisTitle=''; 
	 var canvasTitle='';
	 if(window["canvasTitleInfo_"+actualString+"__" + subId] && window["canvasTitleInfo_"+actualString+"__" + subId].xAxisTitle){
		 xAxisTitle=window["canvasTitleInfo_"+actualString+"__" + subId].xAxisTitle;
	 }
	 if(window["canvasTitleInfo_"+actualString+"__" + subId] && window["canvasTitleInfo_"+actualString+"__" + subId].yAxisTitle){
		 yAxisTitle=window["canvasTitleInfo_"+actualString+"__" + subId].yAxisTitle;
	 }
	 if(window["canvasTitleInfo_"+actualString+"__" + subId] && window["canvasTitleInfo_"+actualString+"__" + subId].canvasTitle){
		 canvasTitle=window["canvasTitleInfo_"+actualString+"__" + subId].canvasTitle;
	 }
	 window["dyanamicChart_"+actualString+"__" + subId] = new CanvasJS.Chart(target, {
		    axisY:{
				includeZero: false,
				title: yAxisTitle,
				titleFontWeight: "bold",
				titleFontSize:16,
				titleFontColor: "#333",
				gridColor: 'white'
			},
			axisX:{
				 title:xAxisTitle,
				 titleFontWeight: "bold",
				 titleFontSize:16,
				 titleFontColor: "#333",
				 crosshair: {
				    enabled: true,
				    snapToDataPoint: true,
				    lineDashType: "solid",
				}
			},
			title:{
				text:canvasTitle,
				fontFamily:'Helvetica Neue, Helvetica, Arial, sans-serif',
		        fontWeight: "bold",
		        fontSize:18,
		        fontColor: "#333",
			},
 			toolTip: {
 				shared: true,
 				borderColor: "#191919",
 			},
			legend : {
				horizontalAlign : "left",
				cursor : 'pointer',
				verticalAlign : "bottom",
				fontFamily : "Times New Roman",
				fontSize: 15,
				itemclick: function (e) {
	            if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
	                e.dataSeries.visible = false;
	            } else {
	                e.dataSeries.visible = true;
	            }

	            e.chart.render();
	        }
			},
			theme : "theme",
			animationEnabled : true,
			zoomEnabled : true,
			data: graphData,
		});
		window["dyanamicChart_"+actualString+"__" + subId].render();
}

/**
 * @purpose validateCsvArray
 * @param meaQuantityId, meaResultId, item, subId
 * @author Tejal
 */
function validateCsvArray(meaQuantityId, meaResultId, item, subId) {
    if ( window["csvArr_"+actualString+"__" + subId].length > 0) {
        for (var i = 0; i < window["csvArr_"+actualString+"__" + subId].length; i++) {
            if (window["csvArr_"+actualString+"__" + subId][i].meaQuantityId == meaQuantityId) {
                window["isQuantityDroppedForCsv_"+actualString+"__" + subId] = true;
                break;
            } else {
                window["isQuantityDroppedForCsv_"+actualString+"__" + subId] = false;
            }
        }
        if (!window["isQuantityDroppedForCsv_"+actualString+"__" + subId]) {
            window["csvArr_"+actualString+"__" + subId].push({
                'meaQuantityId': meaQuantityId,
                'meaResultId': meaResultId,
                'fileName':  item.value
            });
        }
    } else {
        window["csvArr_"+actualString+"__" + subId].push({
            'meaQuantityId': meaQuantityId,
            'meaResultId': meaResultId,
            'fileName':  item.value
        });
    }
}

/**
 * @purpose Sets measurement and quantity ids
 * @param nodeType, item, onwer
 * @author Tejal
 */
function setMeaResultAndMeaQuantityIds(nodeType, item, onwer,actualString, subId,authenticationKey, url) {
    switch (nodeType) {
        case 'Measurement':
        case 'MeaResult':
            {
                meaQuantityId = '';
                meaResultId = item.nodeId;
                break;
            }
        case 'Channel':
        case 'MeaQuantity':
            {
                meaQuantityId = item.nodeId;
                if(item.measurementId){
                	meaResultId = item.measurementId;
                }else if($$(onwer).getItem($$(onwer).getParentId(item.id)).nodeId){
                	meaResultId = $$(onwer).getItem($$(onwer).getParentId(item.id)).nodeId;
                } 
                
                break;
            }
    }
    
    window["authentication_key_"+actualString+"__" + subId]=authenticationKey;
    window["url_"+actualString+"__" + subId]=url;
}

/**
 * @purpose Changes chart view
 * @param event,count
 * @author Tejal
 */
function changeDynamicChartView(ev,count){
	var id=ev.id;
	console.log('id'+id)
	var selectBoxVal=$('#'+id).val();
	selectBoxVal=selectBoxVal.split('_')[0];
	var eleId=id;
	var str=eleId.split('view_type_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
	var target="graph-panel_"+actualString+"__"+subId;
	var viewType = $("#view_type_"+actualString+"__" + subId).val();
	if(viewType == "datatable_"+ actualString+"__" + subId){
		if(window["dataCompareView_"+actualString+"__" + subId].length<1){
			$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_measurements_here+"</div>");
		}
	}else{
		if(window["compareGraphData_"+actualString+"__" + subId].length<1){
			$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");
		}
	}
	switch(viewType){
		case "Line_Chart_"+actualString+"__" + subId:{
			 $("#channels_"+actualString+"__" + subId).show();
			 $("#id_custom_options_"+actualString+"__" + subId).show();
			 $("#xaxislabel_"+actualString+"__" + subId).show();
			 $("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_line_chart);
			 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!='0'){

				 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
				  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
				  //window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var selectedResultId=channelComboSelectedValue.split(",")[0];
				  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var channelArr=[];
				  for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
					  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId != selectedQuantityId){
						  var newObj={};
						  newObj['MeaQuantity']=window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId;
						  newObj['MeaResult']=window["compareGraphData_"+actualString+"__" + subId][i].meaResultId;
						  newObj['name']=window["compareGraphData_"+actualString+"__" + subId][i].name.substring(0, window["compareGraphData_"+actualString+"__" + subId][i].name.lastIndexOf("(") + 0);
						  newObj['aliasName']=window["compareGraphData_"+actualString+"__" + subId][i].legendText;
						  channelArr.push(newObj);
					  }
				  }
				  
			     var obj={};
			     obj['MeaResult']=meaResultId;
			     if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
			    	 obj['xaxischannelname']='';
			     }else{
			    	 for(var i=0;i<window["compareGraphData_"+actualString+"__" + subId].length;i++){
				   		  if(window["compareGraphData_"+actualString+"__" + subId][i].meaQuantityId == selectedQuantityId && window["compareGraphData_"+actualString+"__" + subId][i].meaResultId == selectedResultId){
				   			  obj['xaxischannelname']=window["compareGraphData_"+actualString+"__" + subId][i].actualName;
				   		  }
			   	  	}
			     }
				 obj['channels']=channelArr;
				 resetDataArraysForChannelDrop(obj,actualString,subId,str,channelComboSelectedValue)
			 }else{
				 if(window["compareGraphData_"+actualString+"__" + subId].length>0){
					 drawDynamicChartOnPanels(window["compareGraphData_"+actualString+"__" + subId],target, subId, actualString);
				 } 
			 }
			 
			
			break;
		}	
		case "Scatter_Chart_"+actualString+"__" + subId:{
			$("#channels_"+actualString+"__" + subId).show();
			 $("#id_custom_options_"+actualString+"__" + subId).show();
			 $("#xaxislabel_"+actualString+"__" + subId).show();
			 $("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_scatter_chart);
			 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!='0'){
				 
				 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
				  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
				  //window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var selectedResultId=channelComboSelectedValue.split(",")[0];
				  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var channelArr=[];
				  for(var i=0;i<window["compareScatterGraphData_"+actualString+"__" + subId].length;i++){
					  if(window["compareScatterGraphData_"+actualString+"__" + subId][i].meaQuantityId != selectedQuantityId){
						  var newObj={};
						  newObj['MeaQuantity']=window["compareScatterGraphData_"+actualString+"__" + subId][i].meaQuantityId;
						  newObj['MeaResult']=window["compareScatterGraphData_"+actualString+"__" + subId][i].meaResultId;
						  newObj['name']=window["compareScatterGraphData_"+actualString+"__" + subId][i].name.substring(0, window["compareScatterGraphData_"+actualString+"__" + subId][i].name.lastIndexOf("(") + 0);
						  newObj['aliasName']=window["compareScatterGraphData_"+actualString+"__" + subId][i].legendText;
						  channelArr.push(newObj);
					  }
				  }
				  
			     var obj={};
			     obj['MeaResult']=meaResultId;
			     if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
			    	 obj['xaxischannelname']='';
			     }else{
			    	 for(var i=0;i<window["compareScatterGraphData_"+actualString+"__" + subId].length;i++){
				   		  if(window["compareScatterGraphData_"+actualString+"__" + subId][i].meaQuantityId == selectedQuantityId && window["compareScatterGraphData_"+actualString+"__" + subId][i].meaResultId == selectedResultId){
				   			  obj['xaxischannelname']=window["compareScatterGraphData_"+actualString+"__" + subId][i].actualName;
				   		  }
			   	  	}
			     }
				 obj['channels']=channelArr;
				 resetDataArraysForChannelDrop(obj,actualString,subId,str,channelComboSelectedValue)
			 }else{
				 if(window["compareScatterGraphData_"+actualString+"__" + subId].length>0){
					 drawDynamicChartOnPanels(window["compareScatterGraphData_"+actualString+"__" + subId],target, subId, actualString);
				 }
			 }
			 
			break;
		}
		case "Area_Chart_"+actualString+"__" + subId:{
			 $("#channels_"+actualString+"__" + subId).show();
			 $("#id_custom_options_"+actualString+"__" + subId).show();
			 $("#xaxislabel_"+actualString+"__" + subId).show();
			 $("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_area_chart);
			 if(window["channelCmbSelectedValue_"+actualString+"__" + subId]!='0'){
				 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
				  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
				  //window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var selectedResultId=channelComboSelectedValue.split(",")[0];
				  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
				  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
				  var channelArr=[];
				  for(var i=0;i<window["compareAreaGraphData_"+actualString+"__" + subId].length;i++){
					  if(window["compareAreaGraphData_"+actualString+"__" + subId][i].meaQuantityId != selectedQuantityId){
						  var newObj={};
						  newObj['MeaQuantity']=window["compareAreaGraphData_"+actualString+"__" + subId][i].meaQuantityId;
						  newObj['MeaResult']=window["compareAreaGraphData_"+actualString+"__" + subId][i].meaResultId;
						  newObj['name']=window["compareAreaGraphData_"+actualString+"__" + subId][i].name.substring(0, window["compareAreaGraphData_"+actualString+"__" + subId][i].name.lastIndexOf("(") + 0);
						  newObj['aliasName']=window["compareAreaGraphData_"+actualString+"__" + subId][i].legendText;
						  channelArr.push(newObj);
					  }
				  }
				  
			     var obj={};
			     obj['MeaResult']=meaResultId;
			     if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
			    	 obj['xaxischannelname']='';
			     }else{
			    	 for(var i=0;i<window["compareAreaGraphData_"+actualString+"__" + subId].length;i++){
				   		  if(window["compareAreaGraphData_"+actualString+"__" + subId][i].meaQuantityId == selectedQuantityId && window["compareAreaGraphData_"+actualString+"__" + subId][i].meaResultId == selectedResultId){
				   			  obj['xaxischannelname']=window["compareAreaGraphData_"+actualString+"__" + subId][i].actualName;
				   		  }
			   	  	}
			     }
				 obj['channels']=channelArr;
				 resetDataArraysForChannelDrop(obj,actualString,subId,str,channelComboSelectedValue)
			 }else{
				 if(window["compareAreaGraphData_"+actualString+"__" + subId].length>0){
					 drawDynamicChartOnPanels(window["compareAreaGraphData_"+actualString+"__" + subId],target, subId, actualString);
				 }
			 }
			break;
		}
		case "Data_View_"+actualString+"__" + subId:{
			$("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_data_view);
			 $("#channels_"+actualString+"__" + subId).hide();
			 if ($$('dt_' +actualString+"__" + subId) != undefined)
                 $$('dt_' +actualString+"__" + subId).destructor();
             $('#graph-panel_' +actualString+"__" + subId).html('');
             if(window["dbArray_"+actualString+"__" + subId].length>0){
            	 createDataTableView(subId,actualString);
             }else{
           		$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");

             }
			
			break;
		}
		case "Data_Compare_"+actualString+"__" + subId:{
			$("#id_custom_panel_"+actualString+"__" + subId).text(resourceObj.brix_lbl_data_compare);
			createDynamicDataTableCompare(meaResultId, window["dyanamicMeaResultNametoPersist_" +actualString+"__" + subId], subId,actualString);
			break;
		}
	}
}

/**
 * @purpose Generates Data view
 * @param subId,actualString
 * @author Tejal
 */
function createDataTableView(subId,actualString){
	 $("#channels_"+actualString+"__" + subId).hide();
	 $("#id_custom_options_"+actualString+"__" + subId).hide();
	 $("#xaxislabel_"+actualString+"__" + subId).hide();
	 if(window["dbArray_"+actualString+"__" + subId].length<1){
		$("#graph-panel_"+actualString+"__"+subId).html("<div class='labelTextStyle'>"+resourceObj.brix_lbl_drp_channels_here+"</div>");
	 }
	 var modifiedArr=JSON.parse(JSON.stringify(window["dbArray_"+actualString+"__" + subId]))
	 var strselectedChannelVal=$("#channels_"+actualString+"__" + subId).val();
		if(strselectedChannelVal && strselectedChannelVal!='0'){
			 var meaQuanity= strselectedChannelVal.split(',')[1];
			 var meaResult= strselectedChannelVal.split(',')[0];
			
			 if(modifiedArr.length>0){
				 var elementToBeSliced=-1;
				 for(var i=0;i<modifiedArr.length;i++){
					 if(meaQuanity == modifiedArr[i].meaQuantityId){
						 elementToBeSliced=i;
					 }
				 }
				 
				 if(elementToBeSliced >-1){
					modifiedArr.splice(elementToBeSliced,1);
				 }
			 }
		}
	if ($$('dt_' +actualString+"__" + subId) == undefined) {
        webix.ui({
            container: 'graph-panel_' +actualString+"__" + subId,
            rows: [{
                view: "datatable",
                id: 'dt_' +actualString+"__" + subId,
                tooltip: true,
                css: 'dtCss dbTable',
                autoConfig: true,
                sort: false,
                topSplit: 2,
                datathrottle: 165,
                fixedRowHeight:false,
                on: {
                    onBeforeSort: function() {
                        return false
                    },
                    onBeforeLoad: function() {
                        this.__loadCounter = (this.__loadCounter || 0) + 1;
                        $.LoadingOverlay("show", spinner);
                    },
                    onAfterLoad: function() {
                        this.__loadCounter--;
                        if (this.__loadCounter == 0)
                        	$.LoadingOverlay("hide", spinner);
                    },
                    onDataRequest: function() {
                    	 var obj={};
                    	 obj["channels"]=modifiedArr;
                    	 if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
                    		 obj["xaxischannelname"]=''; 
                    	 }else{
                    		  var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
                    		  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
                    		  //window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
                    		  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
                    		  var selectedResultId=channelComboSelectedValue.split(",")[0];
                    		  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
                    		  obj['xaxischannelname']=modifiedLegendTextVal;
                    	 }
                    	/* var newArr=JSON.stringify(obj);
//                        $$("dt_" +actualString+"__" + subId).load('getDataTableWithPagination?sessionKey=' + sessionKey + '&data=' + newArr, function(text, data, http_request) {
//                   	 	});
                    	 $$("dt_" +actualString+"__" + subId).load(function(){
                 		    return $.ajax({
	                                 type: "POST",
	                                 url: "/GraphSearch/gsearch/getDataTableWithPagination?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
	                                 data: newArr,
	                                 contentType:"application/json; charset=utf-8",
	                                 dataType:"json",
	                                 success: function(res){
	                 		     			 $.LoadingOverlay("hide", spinner);
	                                 },
	                                 error: function(XMLHttpRequest, textStatus, errorThrown) {
	                                      $.LoadingOverlay("hide", spinner);
	                               }
	                           });
	                 	 });*/
                    	 var newArr=obj;
                    	 var modifiedNewArr=encodeURIComponent(JSON.stringify(newArr))
                    	 $$("dt_" +actualString+"__" + subId).load("/GraphSearch/gsearch/getDataTableWithPagination?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId]+"&data="+modifiedNewArr, function(text, data, http_request) {});
                    	 $.LoadingOverlay("hide", spinner);
                    },
                    onStructureLoad: function() {
                        var columns = webix.toArray(this.config.columns);
                        var index = this.getColumnIndex("Channel")
                        var config = columns.splice(index, 1);
                        columns.insertAt(config[0], 0);

                        for (var i = 0; i < this.config.columns.length; i++) {
                            var text = this.config.columns[i].header[0].text;
                            var id = this.config.columns[i].id;
                            this.config.columns[i].header[0].height = 30;
                            this.config.columns[i].header[0].text = "<span title='" + text + "'>" + decodeURIComponent(text) + "</span>";
                            this.config.columns[i].minWidth = 200;
					        this.config.columns[i].fillspace = false;
					        this.adjustColumn(id, 'header'); 
					        this.refresh();
                        }
                        this.adjustRowHeight();  
                    },

                }
            }]
        });
    }
	 var obj={};
	 obj["channels"]=modifiedArr;
	 if(!$("#channels_"+actualString+"__" + subId).val() || $("#channels_"+actualString+"__" + subId).val()=="0"){
		 obj["xaxischannelname"]=''; 
	 }else{
		 var str=$("#channels_"+actualString+"__" + subId+" option:selected").text();
		  var channelComboSelectedValue=$("#channels_"+actualString+"__" + subId).val();
		  //window["channelCmbSelectedValue_"+actualString+"__" + subId]=channelComboSelectedValue;
		  var modifiedLegendTextVal= str.substring(0, str.lastIndexOf("(") + 0);
		  var selectedResultId=channelComboSelectedValue.split(",")[0];
		  var selectedQuantityId=channelComboSelectedValue.split(",")[1];
	   	  obj['xaxischannelname']=modifiedLegendTextVal;
	 }
	// var newArr=JSON.stringify(obj);
//	 $$("dt_" +actualString+"__" + subId).load('getDataTableWithPagination?sessionKey=' + sessionKey + '&data=' + newArr, function(text, data, http_request) {
//	 });
	 
	 var newArr=obj;
	 var modifiedNewArr=encodeURIComponent(JSON.stringify(newArr))
	 
	 $$("dt_" +actualString+"__" + subId).load("/GraphSearch/gsearch/getDataTableWithPagination?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId]+"&data="+modifiedNewArr, function(text, data, http_request) {
		//dataview complete data
		 $$("dt_" +actualString+"__" + subId).clearAll();
		 $$("dt_" +actualString+"__" + subId).parse(data.data);
		 $.LoadingOverlay("hide", spinner);
      });
	/* $$("dt_" +actualString+"__" + subId).load(function(){
		    return $.ajax({
                type: "POST",
                url: "/GraphSearch/gsearch/getDataTableWithPagination?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
                data: newArr,
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                success: function(res){
		     			 $.LoadingOverlay("hide", spinner);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                     $.LoadingOverlay("hide", spinner);
              }
          });
	 });*/
}

/**
 * @purpose Generates Data compare view and binds its data
 * @param subId, actualString,meaResultNametoPersist
 * @author Tejal
 */
function getDynamicCompareDatatableData(subId, actualString,meaResultNametoPersist){
 	
		 var obj={};
		 obj['MeaResult']=meaResultId;
		 obj=JSON.stringify(obj);
		 console.log(obj);
		 $.LoadingOverlay("show", spinner);
		 $.ajax({
	         type: "POST",
	         url: "/GraphSearch/gsearch/compareMeasurement?url=" + window["url_"+actualString+"__" + subId]+"&authenticationKey="+ window["authentication_key_"+actualString+"__" + subId],
	         data: obj,
	         contentType:"application/json; charset=utf-8",
	         dataType:"json",
	         success: function(res){
	        	 $.LoadingOverlay("hide", spinner);
				 res=res;
				 console.log('compare response:'+JSON.stringify(res))
		   		if(res && res.returncode=='1'){
		            var grid_data;
		            var dynamicResponse = res.data;
		            if(window["meaResultDynamicArr_" +actualString+"__" + subId].length<1){
		            	  window["meaResultDynamicArr_" +actualString+"__" + subId].push(meaResultId);
		            }
		          
		            if (dynamicResponse != null && dynamicResponse.length > 0) {
		                var grid_data = [];
		                var obj = {};
		                var key = "dataCompareTbl_" +actualString+"__" + subId;
		                var cols = [];
		                cols.push({
		                    id: "Measurement",
		                    header: '<b>Measurement</b>',
		                });
		                for (var i = 0; i < dynamicResponse.length; i++) {
		                    for (var j = 0; j < dynamicResponse[i].length; j++) {
		                        cols.push({
		                            id: dynamicResponse[i][j].name,
		                            header: '<b>' + dynamicResponse[i][j].name + '</b>',
		                        });
		                        $$("dataCompareTable_"+actualString+"__" + subId).refreshColumns(cols);
		                    }
		                }
		                obj['Measurement'] = meaResultNametoPersist;
		                for (var i = 0; i < dynamicResponse.length; i++) {
		                    for (var j = 0; j < dynamicResponse[i].length; j++) {
		                        obj[dynamicResponse[i][j].name] = dynamicResponse[i][j].value;
		                    }
		                }
		                grid_data = obj;
		                if (!(key in dynamicDataCompareObj)) {
		                    dynamicDataCompareObj["dataCompareTbl_"+actualString+"__" + subId] = obj;
		                }
		                var modifiedArr = dynamicDataCompareObj["dataCompareTbl_" +actualString+"__" + subId];
		                var modifiedArrLength = Object.keys(modifiedArr).length;
		                var modifiedObjLength = Object.keys(obj).length;
		                modifiedArrLength = modifiedArrLength - 2;
		                modifiedObjLength = modifiedObjLength - 2;
		                var cnt = 0;
		                if (modifiedArrLength < modifiedObjLength) {
		                    for (var j in obj) {
		                        cnt++;
		                        if (cnt > modifiedArrLength) {}
		                    }
		                }
		                var modifiedArrCount = 0;
		                for (var i in modifiedArr) {
		                    modifiedArrCount++;
		                    var objCount = 0;
		                    for (var j in obj) {
		                        objCount++;
		                        if (modifiedArrCount > 1 && objCount > 1) {
		                            if (i == j) {
		                                if (modifiedArr[i] != obj[j]) {
		                                    obj[j] = '<p class="compareDataStyle" style="background:#e97070; width:200px; margin-left:-10px!important;">' + obj[j] + '</p>'
		                                }
		                            }
		                        }

		                    }
		                }
		              
		                $$('dataCompareTable_' +actualString+"__" + subId).parse(obj, 'json');
		                $$('dataCompareTable_'+actualString+"__" + subId).refresh();
		                setDynamicArray("datacompare", grid_data, meaResultNametoPersist, subId,'','',actualString);
		                $.LoadingOverlay("hide", spinner);
		            }
		   		}else{
		   			$.LoadingOverlay("hide", spinner);
		   			webix.alert(resourceObj.brix_measurement_is_not_single_value);
		   			return false;
		   		}
	  	 
	         },error: function(XMLHttpRequest, textStatus, errorThrown) {
	             $.LoadingOverlay("hide", spinner);
	         }
			})
}
/**
 * @purpose Generates Data compare view
 * @param subId, actualString,meaResultNametoPersist
 * @author Tejal
 */
function createDynamicDataTableCompare(meaResultId, meaResultName, subId,actualString){

	 $("#channels_"+actualString+"__" + subId).hide();
	 $("#id_custom_options_"+actualString+"__" + subId).hide();
	 $("#xaxislabel_"+actualString+"__" + subId).hide();
	 if ($$('dataCompareTable_' +actualString+"__" + subId) != undefined)
         $$('dataCompareTable_' +actualString+"__" + subId).destructor();
     $('#graph-panel_' +actualString+"__" + subId).html('');
	 var obj={};
	 obj['MeaResult']=meaResultId;
	 obj=JSON.stringify(obj);
	 console.log(obj);
	        webix.ui({
	            container: 'graph-panel_'+actualString+"__" + subId,
	            rows: [{
	                    view: "datatable",
	                    id: 'dataCompareTable_' +actualString+"__" + subId,
	                    css: "data_compare_css",
	                    tooltip: true,
	                    columns: [],
	                    fixedRowHeight:false,
	                    /**
	                     * 	This config is added to solve	0000236: Channel name is not fully displayed and No tool tip is there in Data view
	                     * @author Tejal
	                     */
	                    on: {
	                        "onStructureLoad": function() {
	                            for (var i = 0; i < this.config.columns.length; i++) {
	                                var text = $(this.config.columns[i].header[0].text).text();
	                                var id = this.config.columns[i].id;
	                                this.config.columns[i].header[0].text = "<span title='" + text + "'>" + text + "</span>";
	                                this.config.columns[i].minWidth = 200;
							        this.config.columns[i].fillspace = false;
							        this.adjustColumn(id, 'header'); 
							      //Removeing following line as data compare is not working. -Tejal
								      //  this.config.rowHeight ="auto";
							        this.refresh();
	                            }
	                            this.adjustRowHeight();    
	                        }
	                    }
	                    /**
	                     * 	#end
	                     */
	                },
	                {
	                    view: "button",
	                    value: "add column",
	                    id: 'add_dynamnic_row_' +actualString+"__" + subId,
	                    hidden: true,
	                    on: {
	                        onClick: function() {
	                            var grid_data = [];
	                            var dataCompareArr = [];
	                            var dataCompareObj = {};
	                            var obj = {};
	                            var key = "dataCompareTbl_" +actualString+"__" + subId;
	                            if (window["dataCompareView_" +actualString+"__" + subId].length > 0) {
	                                for (var i = 0; i < window["dataCompareView_" +actualString+"__" + subId].length; i++) {
	                                    var cols = [];
	                                    for (var j = 0; j < window["dataCompareView_" +actualString+"__" + subId][i].length; j++) {
	                                        cols.push({
	                                            id: window["dataCompareView_" +actualString+"__" + subId][i][j].nodeName,
	                                            header: '<b>' + window["dataCompareView_" +actualString+"__" + subId][i][j].nodeName + '</b>',
	                                        });
	                                    }
	                                }
	                                $$("dataCompareTable_"+actualString+"__" + subId).refreshColumns(cols);
	                                obj['Measurement'] = meaResultName;
	                                for (var k = 0; k < window["dataCompareView_"+actualString+"__" + subId].length; k++) {
	                                    var obj = {};
	                                    for (var j = 0; j < window["dataCompareView_"+actualString+"__" + subId][k].length; j++) {
	                                        obj[window["dataCompareView_" +actualString+"__" + subId][k][j].nodeName] = window["dataCompareView_" +actualString+"__" + subId][k][j].data;
	                                    }
	                                    dataCompareArr.push(obj);
	                                    dataCompareObj["data"] = dataCompareArr;
	                                    if (!(key in dynamicDataCompareObj)) {
	                                        dynamicDataCompareObj["dataCompareTbl_" +actualString+"__" + subId] = obj;
	                                    }
	                                    var modifiedArr = dynamicDataCompareObj["dataCompareTbl_" +actualString+"__" + subId];
	                                    var modifiedArrLength = Object.keys(modifiedArr).length;
	                                    var modifiedObjLength = Object.keys(obj).length;
	                                    modifiedArrLength = modifiedArrLength - 2;
	                                    modifiedObjLength = modifiedObjLength - 2;
	                                    var cnt = 0;
	                                    if (modifiedArrLength < modifiedObjLength) {
	                                        for (var j in obj) {
	                                            cnt++;
	                                            if (cnt > modifiedArrLength) {
	                                                obj[j]='<p class="compareDataStyle" style="background:#e97070; width:91px">'+obj[j]+'</p>'
	                                            }
	                                        }
	                                    }
	                                    var modifiedArrCount = 0;

	                                    for (var i in modifiedArr) {
	                                        modifiedArrCount++;
	                                        var objCount = 0;
	                                        for (var j in obj) {
	                                            objCount++;
	                                            if (modifiedArrCount > 1 && objCount > 1) {
	                                                if (i == j) {
	                                                    if (modifiedArr[i] && modifiedArr[i] != '' && modifiedArr[i] != undefined && modifiedArr[i] != obj[j]) {
	                                                    	var dataVal=obj[j];
	                                                    	if(dataVal.indexOf('>')>-1){
	                                                    		var newDataVal =dataVal.split('>')[1];
	                                                    		dataVal=newDataVal.split('</')[0]
	                                                    	}
	                                                        obj[j] = '<p class="compareDataStyle" style="background:#e97070; width:200px; margin-left:-10px!important;">' + dataVal + '</p>'
	                                                    }
	                                                }
	                                            }

	                                        }
	                                    }
	                                }
	                                $$('dataCompareTable_' +actualString+"__" + subId).parse(dataCompareObj, 'json');
	                                $$('dataCompareTable_' +actualString+"__" + subId).refresh();
	                            }
	                        }
	                    }
	                }, {
	                    view: "button",
	                    value: "Compare",
	                    id: 'data_compare',
	                    hidden: true,
	                    click: function() {

	                        var allData = $$('dataCompareTable').serialize();
	                        a = allData[0];
	                        if (allData.length > 1) {
	                            for (var i = 1; i < allData.length; i++) {
	                                b = allData[i];
	                                diffObjArr.push(diffObject(a, b));
	                            }
	                        }
	                    }
	                }
	            ]
	    })
	    var check = $$('add_dynamnic_row_' +actualString+"__" + subId).callEvent('onClick');
}

function canvasOptionsClickHandeler(ev){
	var eleId= ev.id;
	var str=eleId.split('id_custom_options_')[1];
	var actualString=str.split('__')[0];
	var subId=str.split('__')[1];
}

/**
 * @purpose Hides options of canvas like Save as image etc..
 * @param event
 * @author Tejal
 */
$(document).ready(function(){
	$('body').click(function(evt) {
	    if ($(evt.target).closest('.dynamicToggle').length) {
	    	var eleId= evt.target.id;
	    	var str=eleId.split('id_custom_options_')[1];
	    	var actualString=str.split('__')[0];
	    	var subId=str.split('__')[1];
	    	$(".dynamicSettingBtn").hide();
	    	$('#id_custom_options_list_'+actualString+'__'+subId).toggle();
	    } else {
	        $(".dynamicSettingBtn").hide();
	    }
	})
})
/**
 * @purpose This function is used get width of browser and convert it into percentage
 * @param percentage
 * @author Tejal
 */
function setWidthInPercentage(percentage){
    return width = ($( window ).width() / 100)* percentage;
}
/**
 * @purpose This function is used get height of browser and convert it into percentage
 * @param percentage
 * @author Tejal
 */
function setHeightInPercentage(percentage){
    return height = ($( window ).height() / 100)* percentage;
}
