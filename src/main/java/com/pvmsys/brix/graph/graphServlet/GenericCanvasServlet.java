package com.pvmsys.brix.graph.graphServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pvmsys.brix.graph.beans.ResultRO;
import com.pvmsys.brix.graph.main.GraphSearchApplication;
import com.pvmsys.brix.graph.websession.WebSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GenericCanvasServlet
 */
public class GenericCanvasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STRING = "STRING";
	private static final String GET = "GET";
	private static Logger logger = Logger.getLogger(GraphSearchApplication.class);
	

	/**
	 * Default constructor.
	 */
	public GenericCanvasServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			req.setCharacterEncoding("UTF-8");
			@SuppressWarnings("unused")
			final HttpSession httpSession = req.getSession();
			PrintWriter responseWriter = resp.getWriter();
			try {
				String authenticationKey = (String) req.getParameter("authenticationKey");
				String baseURL = (String) req.getParameter("url");
				//final LoginSession loginSession = getLoginSession(authenticationKey, httpSession);
				@SuppressWarnings("unused")
				final HttpServletRequest request = req;
				Gson gson = new Gson();
				String resultData = "";
				String requestPath = req.getPathInfo();
				JSONObject jsonObject = null;
				switch (requestPath) {
					case "/getChannelData": {
						//String data = req.getParameter("data");
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						String meaquantityId = json.getString("MeaQuantity");
						String measurementId = json.getString("MeaResult");
						String xAxischannel = json.getString("xaxischannelname");
						ResultRO<?> resultRO = doGetSingleChannelData(measurementId, meaquantityId, xAxischannel,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/compareMeasurement": {
						//String data = req.getParameter("data");
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						String measurementId = json.getString("MeaResult");
						ResultRO<?> resultRO = doCompareMeasurement(measurementId,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/getChannelsByMeasurement": {
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						String measurementId = json.getString("MeaResult");
						ResultRO<?> resultRO = doGetChannelsByMeasurement(measurementId,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/checkSelectedChannelCanBeXAxis": {
						//String data = req.getParameter("data");
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						ResultRO<?> resultRO = doCheckSelectedChannelCanBeXAxis(json,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/getMultipleChannelDataByXAxis": {
						//String data = req.getParameter("data");
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						ResultRO<?> resultRO = doGetMultipleChannelDataByXAxis(json,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/getMultipleChannelData": {
						String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject json = JSONObject.fromObject(data);
						@SuppressWarnings("unused")
						String meaquantityIds = json.getString("channels");
						String measurementId = json.getString("MeaResult");
						String xAxis = json.getString("xaxischannelname"); 
						ResultRO<?> resultRO = doGetMutipleChannelData(measurementId, data,xAxis,authenticationKey,baseURL);
						resultData = gson.toJson(resultRO);
						break;
					}
					case "/getDataTableWithPagination": {
						final String data = req.getParameter("data");
						//final String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
						JSONObject jsonObj = JSONObject.fromObject(data);
						String xAxis = jsonObj.getString("xaxischannelname");
						int count = 5000;
						int pos = 0;
						try{
							/*** Get the position to start values from *******/
							pos = Integer.parseInt(req.getParameter("start").toString());
						}catch(Exception ex){
							logger.error(ex.getMessage());
						}
						final int startPostiotion = pos;
						String channelArr = jsonObj.getJSONArray("channels").toString();
						Type listType = new TypeToken<List<LinkedHashMap<String, Object>>>(){}.getType();
						List<LinkedHashMap<String, Object>> listOfChannels = new Gson().fromJson(channelArr, listType);
						ResultRO<?> resultRO =  doGetDataTableDataWithPagination(startPostiotion,count,listOfChannels,xAxis,authenticationKey,baseURL);
						//resultData = gson.toJson(resultRO);
						double total_count = 0;
						if(pos==0){
							List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) resultRO.getData();
							total_count = getTotalCount(list);
						}
						resultData = gson.toJson(resultRO);
						
						jsonObject = JSONObject.fromObject(resultData);
						jsonObject.put("pos",pos);
						if(pos==0){
							jsonObject.put("total_count",total_count);
						}
						resultData = null;
						break;
					}

				}
				if(resultData!=null && resultData.length()>0){
					responseWriter.println(JSONObject.fromObject(resultData));
				}else{
					responseWriter.println(JSONObject.fromObject(jsonObject));
				}
				responseWriter.close();
			} catch (Exception e) {
				log(e.getMessage());
			}
		} catch (Exception ex) {
			log(ex.getMessage());
		}
	}
	
	
	/**
	 * This method is used to return max count from no of channels
	 * @author 	gonkar
	 * @date   	July,11,2018	
	 * @param 	list
	 * @return 	ResultRO<List<LinkedHashMap<String, Object>>>
	 */
	private double getTotalCount(List<LinkedHashMap<String, Object>> list) {
		double total_count = 0;
		try {
			for (LinkedHashMap<String, Object> linkedHashMap : list) {
				if(!linkedHashMap.containsKey("Channel"))
					continue;
				if(linkedHashMap.get("Channel").toString().equalsIgnoreCase("Count")){
					ArrayList<String> arr = new ArrayList<String>(linkedHashMap.keySet());
					for (String keyName : arr) {
						if(!keyName.equalsIgnoreCase("Channel")){
							if(total_count<Double.parseDouble(linkedHashMap.get(keyName).toString())){
								total_count = Double.parseDouble(linkedHashMap.get(keyName).toString());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total_count;
	}
	
	
	
	/**
	 * This method is used to get data of all available channels by measurement
	 * @param measurementId
	 * @return ResultRO<?>
	 */
	@SuppressWarnings("rawtypes")
	protected ResultRO<?> doCompareMeasurement(String measurementId,String authenticationKey,String baseURL) {
		ResultRO resultRO = null;
		try {
			//String url = "measurements/"+measurementId+"/measurementdata"; measurements/measurementkey/MeaResult:3385/channels/channeldatavalue?start=0&count=5
			String url = "measurements/measurementkey/"+measurementId+"/channels/channeldata";
			resultRO = get(url,baseURL,authenticationKey);
			resultRO = checkChannelSingleValuedOrNot(resultRO);
		} catch (Exception e) {
			log(e.getMessage());
		}
		return resultRO;
	}
	
	
	
	/**
	 * This method is used to get 
	 * @param resultRO
	 * @return List<String>
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO checkChannelSingleValuedOrNot(ResultRO resultRO) {
		try {
			Gson gson = new Gson();
			String channelsDetails = gson.toJson(resultRO);
			JSONObject jsonObject = new JSONObject();
			JSONObject json = jsonObject.fromObject(channelsDetails);
			JSONArray array = json.getJSONArray("data");
			JSONArray result = new JSONArray();
			boolean flag = false;
			for (Object object : array) {
				JSONArray arrCompareResult = new JSONArray();
				json = jsonObject.fromObject(object);
				JSONArray values = json.getJSONArray("values");
				if(values.size()>1){
					flag = true;
					break;
				}
				json.put("value", values.get(0));
				arrCompareResult.add(json);
				result.add(arrCompareResult);
			}
			if(!flag)
				resultRO.setData(result);
			else{
				resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
				resultRO.setData("");
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
		return resultRO;
	}
	

	/**
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	protected ResultRO<?> doGetMultipleChannelDataByXAxis(JSONObject json,String authenticationKey,String baseURL) {
		ResultRO resultRO = new ResultRO<>();
		try {
			String data = "";
			Gson gson = new Gson();
			String xAxisName = URLEncoder.encode(json.getString("xaxischannelname"),"UTF-8");
			JSONArray arrChannels = json.getJSONArray("channels");
			String channelsData = gson.toJson(json);
			Map<String,String> mapOfAlias = getMapOfAlias(channelsData);
			List<JSONObject> list = new ArrayList<>();
			if(arrChannels.size()<=0 && xAxisName!=null && xAxisName.trim().length()>0){
				String url = "measurements/measurementkey/" + json.getString("MeaResult") + "/channels/channelkey/"
						+ json.getString("xaxischannelId") + "/channelgroups";
				resultRO = get(url,baseURL,authenticationKey);
				//if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS){
				if(xAxisName!=null && xAxisName.trim().length()>0){
					if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
						data = resultRO.getData().toString();
						JSONObject json1 = new JSONObject();
						/*** get values from json ***/
						String groupData = gson.toJson(resultRO);
						JSONObject jsonObject = json1.fromObject(groupData);
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						@SuppressWarnings("unused")
						String groupName = "";
						String groupId = "";
						for (Object object1 : jsonArray) {
							json1 = json.fromObject(object1);
							groupId = json1.getString("submatrixkey");
							groupName = json1.getString("name");
						}
						if (groupId != null && groupId.length() > 0) {
							url = "measurements/measurementkey/" + json.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
									+ "/channelpreview?start=1&count=200000&filterbyname=" + xAxisName;
							ResultRO  resultROXAxis = get(url,baseURL,authenticationKey);
							if (resultROXAxis.getReturnCode() == EnumReturnCode.I_SUCCESS) {
								String meaquantityIds = json.getString("xaxischannelId");
								/*meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
										.replaceAll("]", "");*/
								meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("]", "");
								url = "measurements/measurementkey/" + json.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
										+ "/channelpreview?start=1&count=200000&filterbykey=" +  meaquantityIds;
								resultRO = get(url,baseURL,authenticationKey);
								if(resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS){
									resultRO = formatData(resultRO, resultROXAxis,mapOfAlias);
									//Gson gson = new Gson();
									JSONObject resultData = new JSONObject();
									String strResultData = gson.toJson(resultRO);
									resultData = resultData.fromObject(strResultData);
									JSONObject jsonData = resultData.getJSONObject("data");
									jsonData.put("meaResultId", json.getString("MeaResult"));
									list.add(jsonData);
									resultRO.setData(jsonData);
									//resultData.put(key, value)
								}
							}
						}
					}else{
						resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
					}
				}
			}else{
				for (Object object : arrChannels) {
					data = gson.toJson(object);
					JSONObject result = new JSONObject().fromObject(data);
					/**** get groupInfo by measurement id and channel id ***/
					String url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channels/channelkey/"
							+ result.getString("MeaQuantity") + "/channelgroups";
					resultRO = get(url,baseURL,authenticationKey);
					//if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS){
					if(xAxisName!=null && xAxisName.trim().length()>0){
						if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
							data = resultRO.getData().toString();
							JSONObject json1 = new JSONObject();
							/*** get values from json ***/
							String groupData = gson.toJson(resultRO);
							JSONObject jsonObject = json1.fromObject(groupData);
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							@SuppressWarnings("unused")
							String groupName = "";
							String groupId = "";
							for (Object object1 : jsonArray) {
								json1 = json.fromObject(object1);
								groupId = json1.getString("submatrixkey");
								groupName = json1.getString("name");
							}
							if (groupId != null && groupId.length() > 0) {
								url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
										+ "/channelpreview?start=1&count=200000&filterbyname=" + xAxisName;
								ResultRO  resultROXAxis = get(url,baseURL,authenticationKey);
								if (resultROXAxis.getReturnCode() == EnumReturnCode.I_SUCCESS) {
									String meaquantityIds = result.getString("MeaQuantity");
									/*meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
											.replaceAll("]", "");*/
									meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("]", "");
									url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
											+ "/channelpreview?start=1&count=200000&filterbykey=" +  meaquantityIds;
									resultRO = get(url,baseURL,authenticationKey);
									if(resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS){
										resultRO = formatSingleChannelDataByXAxisForAlias(resultRO, resultROXAxis,mapOfAlias);
										//Gson gson = new Gson();
										JSONObject resultData = new JSONObject();
										String strResultData = gson.toJson(resultRO);
										resultData = resultData.fromObject(strResultData);
										JSONObject jsonData = resultData.getJSONObject("data");
										jsonData.put("meaResultId", result.getString("MeaResult"));
										list.add(jsonData);
										resultRO.setData(jsonData);
										//resultData.put(key, value)
									}
								}
							}
						}else{
							resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
							break;
						}
					}else{
						if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
							data = resultRO.getData().toString();
							JSONObject json1 = new JSONObject();
							/*** get values from json ***/
							String groupData = gson.toJson(resultRO);
							JSONObject jsonObject = json1.fromObject(groupData);
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							@SuppressWarnings("unused")
							String groupName = "";
							String groupId = "";
							for (Object object1 : jsonArray) {
								json1 = json.fromObject(object1);
								groupId = json1.getString("submatrixkey");
								groupName = json1.getString("name");
							}
							if (groupId != null && groupId.length() > 0) {
								/*url = "measurements/" + result.getString("MeaResult") + "/channelgroups/" + groupId
										+ "/channeldata?start=1&count=5&filterbyname=" + xAxisName;
								ResultRO  resultROXAxis = get(url);*/
								//if (resultROXAxis.getReturnCode() == EnumReturnCode.I_SUCCESS) {
									String meaquantityIds = result.getString("MeaQuantity");
									/*meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
											.replaceAll("]", "");*/
									meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("]", "");
									url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
											+ "/channelpreview?start=1&count=200000&filterbykey=" +  meaquantityIds;
									resultRO = get(url,baseURL,authenticationKey);
									if(resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS){
										resultRO = formatSingleChannelDataForAlias(resultRO,mapOfAlias);
										//Gson gson = new Gson();
										JSONObject resultData = new JSONObject();
										String strResultData = gson.toJson(resultRO);
										resultData = resultData.fromObject(strResultData);
										JSONObject jsonData = resultData.getJSONObject("data");
										jsonData.put("meaResultId", result.getString("MeaResult"));
										list.add(jsonData);
										resultRO.setData(jsonData);
										//resultData.put(key, value)
									}
								//}
							}
						}else{
							resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
							break;
						}
					}
				}
			}
			resultRO.setData(list);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;
	
	}
	
	
	/**
	 * This method is used to format single channel data and by xAxis and put provided alias into result
	 * @param resultRO
	 * @param resultROXAxis
	 * @param mapOfAlias
	 * @return ResultRO
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO formatData(ResultRO resultRO, ResultRO resultROXAxisData,
			Map<String, String> mapOfAlias) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json for Y ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			dataValues.put("id", dataValues.get("meaquantitykey"));
			dataValues.put("aliasName", mapOfAlias.get(dataValues.get("meaquantitykey")));
			dataValues.remove("meaquantitykey");
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			/*****************************************************/
			data = gson.toJson(resultROXAxisData);
			JSONObject xAxis = new JSONObject();
			xAxis = xAxis.fromObject(data);
			JSONArray json2 = xAxis.getJSONArray("data");
			Object obj1 = json2.get(0);
			String values1 = gson.toJson(obj1);
			JSONObject dataValues1 = xAxis.fromObject(values1); // meaquantitykey
			dataValues1.put("id", dataValues1.get("meaquantitykey"));
			dataValues1.put("aliasName", mapOfAlias.get(dataValues1.get("meaquantitykey")));
			dataValues1.remove("meaquantitykey");
			JSONArray jsonArray1 = dataValues1.getJSONArray("values");
			/*** get datatype of channel from json ***/
			/*String dataType1 = dataValues1.getString("dataType");
			dataType1 = dataType1.substring(dataType1.lastIndexOf(".") + 1);
			dataType1 = dataType1.toUpperCase();*/
			/**** get values from json for selected x-Axis *********/
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			if (jsonArray1.size() == jsonArray.size()) {
				for (Object object : jsonArray) {
					GraphCoordinates coordinates = new GraphCoordinates();
					coordinates.setX(jsonArray1.get(x));
					if (!dataType.equalsIgnoreCase(STRING)){
						/** if only one channel is plotted and if that channel selected as XAxis
						 then  Y value for that channel should be null. **/
						coordinates.setY(null);
					}
					else
						coordinates.setValue(object.toString());

					list.add(coordinates);
					x++;
				}
			}
			dataValues.put("Values", list);
			resultRO.setData(dataValues);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultRO;
	}

	/**
	 * 
	 * @param resultRO
	 * @param mapOfAlias
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO formatSingleChannelDataForAlias(ResultRO resultRO, Map<String, String> mapOfAlias) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			dataValues.put("id", dataValues.get("meaquantitykey"));
			dataValues.put("aliasName", mapOfAlias.get("meaquantitykey"));
			dataValues.remove("meaquantitykey");
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			for (Object object : jsonArray) {
				GraphCoordinates coordinates = new GraphCoordinates();
				coordinates.setX(x);
				if (!dataType.equalsIgnoreCase(STRING))
					coordinates.setY(object);
				else
					coordinates.setValue(object.toString());

				list.add(coordinates);
				x++;
			}
			dataValues.put("Values", list);
			resultRO.setData(dataValues);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while formatting single channel data "
					+ "\n GenericCanvasServlet.formatSingleChannelData(ResultRO resultRO) \n Reason :"
					+ e.getMessage());
		}
		return resultRO;
	}

	/**
	 * This method is used to format single channel data and by xAxis and put provided alias into result
	 * @param resultRO
	 * @param resultROXAxis
	 * @param mapOfAlias
	 * @return ResultRO
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked"})
	private ResultRO formatSingleChannelDataByXAxisForAlias(ResultRO resultRO, ResultRO resultROXAxisData,
			Map<String, String> mapOfAlias) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json for Y ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			if(dataValues.containsKey("meaquantitykey")){
				dataValues.put("id", dataValues.get("meaquantitykey"));
				dataValues.remove("meaquantitykey");
			}
			else{
				dataValues.put("id", dataValues.get("channelkey"));
				dataValues.remove("channelkey");
			}
			dataValues.put("aliasName", mapOfAlias.get(dataValues.get("id")));
			
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			/*****************************************************/
			data = gson.toJson(resultROXAxisData);
			JSONObject xAxis = new JSONObject();
			xAxis = xAxis.fromObject(data);
			JSONArray json2 = xAxis.getJSONArray("data");
			Object obj1 = json2.get(0);
			String values1 = gson.toJson(obj1);
			JSONObject dataValues1 = xAxis.fromObject(values1); // meaquantitykey
			dataValues1.put("id", dataValues1.get("meaquantitykey"));
			dataValues1.put("aliasName", mapOfAlias.get(dataValues1.get("meaquantitykey")));
			dataValues1.remove("meaquantitykey");
			JSONArray jsonArray1 = dataValues1.getJSONArray("values");
			/*** get datatype of channel from json ***/
			/*String dataType1 = dataValues1.getString("dataType");
			dataType1 = dataType1.substring(dataType1.lastIndexOf(".") + 1);
			dataType1 = dataType1.toUpperCase();*/
			/**** get values from json for selected x-Axis *********/
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			if (jsonArray1.size() == jsonArray.size()) {
				for (Object object : jsonArray) {
					GraphCoordinates coordinates = new GraphCoordinates();
					coordinates.setX(jsonArray1.get(x));
					if (!dataType.equalsIgnoreCase(STRING))
						coordinates.setY(object);
					else
						coordinates.setValue(object.toString());

					list.add(coordinates);
					x++;
				}
			}
			dataValues.put("Values", list);
			resultRO.setData(dataValues);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultRO;
	}

	/**
	 * This method is used to check selected channel can be xAXis or not
	 * @param data
	 * @return ResultRO<?>
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	protected ResultRO<?> doCheckSelectedChannelCanBeXAxis(JSONObject json,String authenticationKey,String baseURL) {
		ResultRO resultRO = new ResultRO<>();
		try {
			String data = "";
			Gson gson = new Gson();
			/*data = gson.toJson(data);
			JSONObject json = JSONObject.fromObject(data);*/
			String xAxisName = URLEncoder.encode(json.getString("xaxischannelname"),"UTF-8");
			JSONArray arrChannels = json.getJSONArray("channels");
			if(xAxisName!=null && xAxisName.trim().length()>0 && arrChannels.size()==0){
				String xaxischannelId = json.getString("xaxischannelId");
				String url = "measurements/measurementkey/" +json.getString("MeaResult") + "/channels/channelkey/"
						+ xaxischannelId + "/channelgroups";
				resultRO = get(url,baseURL,authenticationKey);
				if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
					data = resultRO.getData().toString();
					JSONObject json1 = new JSONObject();
					/*** get values from json ***/
					String groupData = gson.toJson(resultRO);
					JSONObject jsonObject = json1.fromObject(groupData);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					@SuppressWarnings("unused")
					String groupName = "";
					String groupId = "";
					for (Object object1 : jsonArray) {
						json1 = json.fromObject(object1);
						groupId = json1.getString("submatrixkey");
						groupName = json1.getString("name");
					}
					
					if (groupId != null && groupId.length() > 0) {
						url = "measurements/measurementkey/" + json.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
								+ "/channelpreview?start=1&count=200000&filterbyname=" + xAxisName;
						resultRO = get(url,baseURL,authenticationKey);
						if (!(resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS)) {
							resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
						}
					}
				}
			}else{
				for (Object object : arrChannels) {
					data = gson.toJson(object);
					JSONObject result = new JSONObject().fromObject(data);
					/**** get groupInfo by measurement id and channel id ***/
					String url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channels/channelkey/"
							+ result.getString("MeaQuantity") + "/channelgroups";
					resultRO = get(url,baseURL,authenticationKey);
					if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
						data = resultRO.getData().toString();
						JSONObject json1 = new JSONObject();
						/*** get values from json ***/
						String groupData = gson.toJson(resultRO);
						JSONObject jsonObject = json1.fromObject(groupData);
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						@SuppressWarnings("unused")
						String groupName = "";
						String groupId = "";
						for (Object object1 : jsonArray) {
							json1 = json.fromObject(object1);
							groupId = json1.getString("submatrixkey");
							groupName = json1.getString("name");
						}
						if (groupId != null && groupId.length() > 0) {
							url = "measurements/measurementkey/" + result.getString("MeaResult") + "/channelgroups/channelgroupkey/" + groupId
									+ "/channeldata?filterbyname=" + xAxisName;
							resultRO = get(url,baseURL,authenticationKey);
							if (!(resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS)) {
								resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
								break;
							}
						}
					}else{
						resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
						break;
					}
				}
			}
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;
	}

	/**
	 * This method is used to get channels by measurement id
	 * 
	 * @param measurementId
	 * @return ResultRO<?>
	 */
	@SuppressWarnings({ "rawtypes"})
	protected ResultRO<?> doGetChannelsByMeasurement(String measurementId,String authenticationKey,String baseURL) {
		ResultRO<?> resultRO = new ResultRO();
		try {
			String url = "measurements/measurementkey/" + measurementId + "/channels";
			resultRO = get(url,baseURL,authenticationKey);
			resultRO = formatChannelData(resultRO);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;
	}

	/**
	 * This method is used to format channel data.
	 * 
	 * @param resultRO
	 * @return ResultRO<?>
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO<?> formatChannelData(ResultRO resultRO) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray jsonArray = json.getJSONArray("data");
			JSONObject json1 = new JSONObject();
			JSONObject result = null;
			JSONArray resultArray = new JSONArray();
			for (Object object : jsonArray) {
				data = gson.toJson(object);
				result = new JSONObject();
				json1 = json.fromObject(data);
				result.put("id", json1.get("channelkey"));
				result.put("name", json1.get("name").toString());
				resultArray.add(result);
			}
			resultRO.setData(resultArray);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;
	}

	/**
	 * This method is used to get multiple channel data
	 * 
	 * @param measurementId
	 * @param meaquantityIds
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	protected ResultRO<?> doGetMutipleChannelData(String measurementId, String jsonData,String xAxis,String authenticationKey,String baseURL) {
		ResultRO resultRO = new ResultRO();
		try {
			List<JSONObject> listOfResults = new ArrayList<>();
			String meaquantityIds = getMeaQuantities(jsonData);
			Map<String,String> mapOfAlias = getMapOfAlias(jsonData);
			if(xAxis!=null && xAxis.trim().length()>0){
				//meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("]", "").substring(1);
				String [] arrChannelId = meaquantityIds.split(",");
				for (String id : arrChannelId) {
					String url = "measurements/measurementkey/" + measurementId + "/channels/channelkey/" + id + "/channelgroups";
					resultRO = get(url,baseURL,authenticationKey);
					int returnCode = resultRO.getReturnCode();
					if (returnCode == EnumReturnCode.I_SUCCESS) {
						@SuppressWarnings("unused")
						String data = resultRO.getData().toString();
						Gson gson = new Gson();
						JSONObject json = new JSONObject();
						/*** get values from json ***/
						String groupData = gson.toJson(resultRO);
						JSONObject jsonObject = json.fromObject(groupData);
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						@SuppressWarnings("unused")
						String groupName = "";
						String groupId = "";
						for (Object object : jsonArray) {
							json = json.fromObject(object);
							groupId = json.getString("submatrixkey");
							groupName = json.getString("name");
						}
						
						// 2. get channel info by group id and channel name
						if (groupId != null && groupId.length() > 0) {
							url = "measurements/measurementkey/" + measurementId + "/channelgroups/channelgroupkey/" + groupId
									+ "/channelpreview?start=1&count=200000&filterbyname=" + xAxis;
							ResultRO resultROXAxisData = get(url,baseURL,authenticationKey);
							if (resultROXAxisData.getReturnCode() == EnumReturnCode.I_SUCCESS) {
								Object channelInfo = resultROXAxisData.getData();
								if (channelInfo != null) {
									// 3. validate xAxis is valid or not , if not
									// valid then return false other wise get data
									// for that channel according to its xAxis
									/*id = id.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
											.replaceAll("]", "");*/
									id = id.replaceAll("\"", "").replaceAll("]", "");
									url = "measurements/measurementkey/" + measurementId + "/channelgroups/channelgroupkey/" + groupId
											+ "/channelpreview?start=1&count=200000&filterbykey=" + id;
									resultRO = get(url,baseURL,authenticationKey);
									formatMultiChannelDataByXAxis(resultRO,resultROXAxisData, measurementId,listOfResults,mapOfAlias);
								}
								else{
									resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
									log("XAxis not present  for measurement = " + measurementId);
								}
							}else{
								resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
								log("XAxis not present  for measurement = " + measurementId);
								break;
							}

						}else{
							resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
							log("Channel Group Information not exist for selected channel = " + id
									+ " and for measurement = " + measurementId);
							break;
						}
					}
				}
				if(resultRO.getReturnCode()!=EnumReturnCode.I_FAILURE && resultRO.getReturnCode()!=EnumReturnCode.I_NOT_FOUND){
					resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
					resultRO.setData(listOfResults);
				}
			}else{
				//meaquantityIds = meaquantityIds.replaceAll("\"", "").replaceAll("]", "").substring(1);
				String url = "measurements/measurementkey/" + measurementId
						+ "/channels/channelpreview?start=1&count=200000&filterbykey=" + meaquantityIds;
				resultRO = get(url,baseURL,authenticationKey);
				resultRO = formatMultiChannelData(resultRO, measurementId,mapOfAlias);
			}
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;
	}
	
	/**
	 * 
	 * @param jsonData
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	private Map<String, String> getMapOfAlias(String jsonData) {
		Map<String,String> map = new HashMap<>();
		try {
			Gson gson = new Gson();
			JSONObject jsonObject  = new JSONObject();
			jsonObject = jsonObject.fromObject(jsonData);
			JSONArray jsonArray = jsonObject.getJSONArray("channels");
			for (Object object : jsonArray) {
				String data = gson.toJson(object);
				jsonObject = jsonObject.fromObject(data);
				String alias = jsonObject.getString("aliasName");
				String channelId = "";
				if(jsonObject.containsKey("channelId"))
					channelId = jsonObject.getString("channelId");
				else
					channelId = jsonObject.getString("MeaQuantity");
				map.put(channelId, alias);
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
		return map;
	
	}

	/**
	 * This method is used to get meaquantity id's
	 * @param meaquantityIds
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	private String getMeaQuantities(String meaquantityIds) {
		String channels = null;
		try {
			Gson gson = new Gson();
			JSONObject jsonObject  = new JSONObject();
			jsonObject = jsonObject.fromObject(meaquantityIds);
			JSONArray jsonArray = jsonObject.getJSONArray("channels");
			for (Object object : jsonArray) {
				String data = gson.toJson(object);
				jsonObject = jsonObject.fromObject(data);
				if(channels!=null){
					channels = channels + "," + jsonObject.getString("channelId");
				}else{
					channels = jsonObject.getString("channelId");
				}
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
		return channels;
	}

	/**
	 * This method is used to format multiple channel data by xAxis
	 * @param resultRO
	 * @param resultROXAxisData
	 * @param measurementId
	 * @param mapOfAlias 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void formatMultiChannelDataByXAxis(ResultRO<?> resultRO, ResultRO resultROXAxisData,
			String measurementId , List<JSONObject> listOfResult, Map<String, String> mapOfAlias) {
		@SuppressWarnings("unused")
		ResultRO resultRO2 = new ResultRO<>();
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json for Y ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			dataValues.put("id", dataValues.get("channelkey"));
			dataValues.remove("channelkey");
			String aliasName = mapOfAlias.get(dataValues.get("id"));
			dataValues.put("aliasName", aliasName);
			//dataValues.remove("meaquantitykey");
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			/*****************************************************/
			data = gson.toJson(resultROXAxisData);
			JSONObject xAxis = new JSONObject();
			xAxis = xAxis.fromObject(data);
			JSONArray json2 = xAxis.getJSONArray("data");
			Object obj1 = json2.get(0);
			String values1 = gson.toJson(obj1);
			JSONObject dataValues1 = xAxis.fromObject(values1); // meaquantitykey
			dataValues1.put("id", dataValues1.get("channelkey"));
			aliasName = mapOfAlias.get(dataValues1.get("channelkey"));
			dataValues1.put("aliasName", aliasName);
			dataValues1.remove("channelkey");
			JSONArray jsonArray1 = dataValues1.getJSONArray("values");
			/*** get datatype of channel from json ***/
			/*String dataType1 = dataValues1.getString("dataType");
			dataType1 = dataType1.substring(dataType1.lastIndexOf(".") + 1);
			dataType1 = dataType1.toUpperCase();*/
			/**** get values from json for selected x-Axis *********/
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			if (jsonArray1.size() == jsonArray.size()) {
				for (Object object : jsonArray) {
					GraphCoordinates coordinates = new GraphCoordinates();
					coordinates.setX(jsonArray1.get(x));
					if (!dataType.equalsIgnoreCase(STRING))
						coordinates.setY(object);
					else
						coordinates.setValue(object.toString());

					list.add(coordinates);
					x++;
				}
			}
			dataValues.put("Values", list);
			listOfResult.add(dataValues);
		} catch (Exception e) {
			log(e.getMessage());
		}
	}

	/**
	 * 
	 * @param resultRO
	 * @param measurementId
	 * @param mapOfAlias 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO<?> formatMultiChannelData(ResultRO resultRO, String measurementId, Map<String, String> mapOfAlias) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray channelArr = json.getJSONArray("data");
			
			JSONArray channelResult = new JSONArray();
			for (Object object : channelArr) {
				String channelData = gson.toJson(object);
				JSONObject json1 = json.fromObject(channelData);
				String id = json1.getString("channelkey");
				json1.remove("channelkey");
				json1.put("id", id);
				String aliasName = mapOfAlias.get(id);
				json1.put("aliasName", aliasName);
				json1.put("meaReultId", measurementId);
				String dataType = json1.getString("dataType");
				dataType = dataType.substring(dataType.lastIndexOf("_")+1);
				dataType = dataType.toUpperCase();
				JSONArray jsonArray = json1.getJSONArray("values");
				int x = 0;
				List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
				for (Object object1 : jsonArray) {
					GraphCoordinates coordinates = new GraphCoordinates();
					coordinates.setX(x);
					if (!dataType.equalsIgnoreCase(STRING))
						coordinates.setY(object1);
					else
						coordinates.setValue(object1.toString());

					list.add(coordinates);
					x++;
				}
				json1.put("Values", list);
				channelResult.add(json1);
			}
			resultRO.setData(channelResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultRO;
	}

	/**
	 * This method is used to get channel data for provided measurement id and
	 * meaquantity id
	 * 
	 * @param measurementId
	 * @param meaquantityId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access"})
	protected ResultRO<?> doGetSingleChannelData(String measurementId, String meaquantityId, String xAxis,String authenticationKey,String baseURL) {
		ResultRO<?> resultRO = new ResultRO();
		try {
			// 1. get group info by channelId and measurement id
			// http://localhost:8087/pods/v1/measurements/MeaResult:3385/channel/channelkey/MeaQuantity:76478/channelgroup
			String url = "measurements/measurementkey/" + measurementId + "/channels/channelkey/" + meaquantityId + "/channelgroups";
			resultRO = get(url,baseURL,authenticationKey);
			int returnCode = resultRO.getReturnCode();
			if (returnCode == EnumReturnCode.I_SUCCESS) {
				@SuppressWarnings("unused")
				String data = resultRO.getData().toString();
				Gson gson = new Gson();
				JSONObject json = new JSONObject();
				/*** get values from json ***/
				String groupData = gson.toJson(resultRO);
				JSONObject jsonObject = json.fromObject(groupData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				@SuppressWarnings("unused")
				String groupName = "";
				String groupId = "";
				for (Object object : jsonArray) {
					json = json.fromObject(object);
					groupId = json.getString("submatrixkey");
					groupName = json.getString("name");
				}
				String xAxisName =URLEncoder.encode(xAxis,"UTF-8");
				if (xAxisName != null && xAxisName.length() > 0) {
					// 2. get channel info by group id and channel name
					if (groupId != null && groupId.length() > 0) {
						url = "measurements/measurementkey/" + measurementId + "/channelgroups/channelgroupkey/" + groupId
								+ "/channelpreview?start=1&count=200000&filterbyname=" + xAxisName;
						ResultRO resultROXAxisData = get(url,baseURL,authenticationKey);
						if (resultROXAxisData.getReturnCode() == EnumReturnCode.I_SUCCESS) {
							Object channelInfo = resultROXAxisData.getData();
							if (channelInfo != null) {
								// 3. validate xAxis is valid or not , if not
								// valid then return false other wise get data
								// for that channel according to its xAxis
								/*
								 * ----------- old url *************************
								 * url = "measurements/measurementkey/" +
								 * measurementId + "/channels/channelkey/" +
								 * meaquantityId + "/channeldata";
								 */

								/*meaquantityId = meaquantityId.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
										.replaceAll("]", "");*/
								
								meaquantityId = meaquantityId.replaceAll("\"", "").replaceAll("]", "");


								url = "measurements/measurementkey/" + measurementId + "/channelgroups/channelgroupkey/" + groupId
										+ "/channelpreview?start=1&count=200000&filterbykey=" + meaquantityId;
								resultRO = get(url,baseURL,authenticationKey);
								resultRO = formatSingleChannelDataByXAxis(resultRO, resultROXAxisData);
							}
							else{
								resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
								log("XAxis not present  for measurement = " + measurementId);
							}
						}else{
							if(!(resultROXAxisData.getReturnCode() == EnumReturnCode.I_FAILURE)){
								resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
							}else{
								resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
							}
							log("XAxis not present  for measurement = " + measurementId);
						}

					}else{
						resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
						log("Channel Group Information not exist for selected channel = " + meaquantityId
								+ " and for measurement = " + measurementId);
					}
				} else {
					if (groupId != null && groupId.length() > 0) {
						/*meaquantityId = meaquantityId.replaceAll("\"", "").replaceAll("MeaQuantity:", "")
								.replaceAll("]", "");*/
						meaquantityId = meaquantityId.replaceAll("\"", "").replaceAll("]", "");
						url = "measurements/measurementkey/" + measurementId + "/channelgroups/channelgroupkey/" + groupId
								+ "/channelpreview?start=1&count=200000&filterbykey=" + meaquantityId;
						resultRO = get(url,baseURL,authenticationKey);
						if (resultRO.getReturnCode() == EnumReturnCode.I_SUCCESS) {
							@SuppressWarnings("unused")
							Object channelInfo = resultRO.getData();
							resultRO = formatSingleChannelData(resultRO);
						}else{
							resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
							log("XAxis not present  for measurement = " + measurementId);
						}
					}
				}
			} else {
				resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
				log("Channel Group Information not exist for selected channel = " + meaquantityId
						+ " and for measurement = " + measurementId);
			}

		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			log(e.getMessage());
		}
		return resultRO;

	}

	/**
	 * This method is used to format single channel data by x-axis
	 * @param resultRO
	 * @param channelInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO<?> formatSingleChannelDataByXAxis(ResultRO resultRO, ResultRO resultROXAxisData) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json for Y ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			dataValues.put("id", dataValues.get("meaquantitykey"));
			dataValues.remove("meaquantitykey");
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			/*****************************************************/
			data = gson.toJson(resultROXAxisData);
			JSONObject xAxis = new JSONObject();
			xAxis = xAxis.fromObject(data);
			JSONArray json2 = xAxis.getJSONArray("data");
			Object obj1 = json2.get(0);
			String values1 = gson.toJson(obj1);
			JSONObject dataValues1 = xAxis.fromObject(values1); // meaquantitykey
			dataValues1.put("id", dataValues1.get("meaquantitykey"));
			dataValues1.remove("meaquantitykey");
			JSONArray jsonArray1 = dataValues1.getJSONArray("values");
			/*** get datatype of channel from json ***/
			/*String dataType1 = dataValues1.getString("dataType");
			dataType1 = dataType1.substring(dataType1.lastIndexOf(".") + 1);
			dataType1 = dataType1.toUpperCase();*/
			/**** get values from json for selected x-Axis *********/
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			if (jsonArray1.size() == jsonArray.size()) {
				for (Object object : jsonArray) {
					GraphCoordinates coordinates = new GraphCoordinates();
					coordinates.setX(jsonArray1.get(x));
					if (!dataType.equalsIgnoreCase(STRING))
						coordinates.setY(object);
					else
						coordinates.setValue(object.toString());

					list.add(coordinates);
					x++;
				}
			}
			dataValues.put("Values", list);
			resultRO.setData(dataValues);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultRO;

	}

	/**
	 * This method is used to format single channel data to set x & y values
	 * 
	 * @param resultRO
	 * @return ResultRO<?>
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	private ResultRO<?> formatSingleChannelData(ResultRO resultRO) {
		try {
			Gson gson = new Gson();
			JSONObject json = new JSONObject();
			String data = gson.toJson(resultRO);
			json = json.fromObject(data);
			JSONArray json1 = json.getJSONArray("data");
			/*** get values from json ***/
			Object obj = json1.get(0);
			String values = gson.toJson(obj);
			JSONObject dataValues = json.fromObject(values); // meaquantitykey
			dataValues.put("id", dataValues.get("meaquantitykey"));
			dataValues.remove("meaquantitykey");
			JSONArray jsonArray = dataValues.getJSONArray("values");
			/*** get datatype of channel from json ***/
			String dataType = dataValues.getString("dataType");
			dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
			dataType = dataType.toUpperCase();
			int x = 0;
			List<GraphCoordinates> list = new ArrayList<GraphCoordinates>();
			for (Object object : jsonArray) {
				GraphCoordinates coordinates = new GraphCoordinates();
				coordinates.setX(x);
				if (!dataType.equalsIgnoreCase(STRING))
					coordinates.setY(object);
				else
					coordinates.setValue(object.toString());

				list.add(coordinates);
				x++;
			}
			dataValues.put("Values", list);
			resultRO.setData(dataValues);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while formatting single channel data "
					+ "\n GenericCanvasServlet.formatSingleChannelData(ResultRO resultRO) \n Reason :"
					+ e.getMessage());
		}
		return resultRO;
	}

	private static final String AUTHORIZATION = "authorization";
	@SuppressWarnings("unused")
	private static final String BASIC = "Basic ";
	private static final String FAILED = "Failed : HTTP error code :";

	/**
	 * This method is used to get data from provided url
	 * 
	 * @param urlPath
	 * @return ResultRO<?>
	 */

	private ResultRO<?> get(String urlPath,String baseUrl,String authenticationKey) {
		ResultRO<?> resultRO = new ResultRO<>();
		try {

			 //URL url = new URL(BASE_URL + urlPath);
			//URL url = new URL("http://172.16.131.28:8080/pods-webservice/v1/" + urlPath);// 172.16.131.35:8080/pods/v1/
			URL url = new URL(baseUrl + urlPath);// 172.16.131.35:8080/pods/v1/
			/******* check for basic authentication ***********/
			//byte[] bytes = "superuser:secret".getBytes();
			//String authString = Base64.getEncoder().encodeToString(bytes);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(GET);
			connection.setDoOutput(true);
			//connection.setRequestProperty(AUTHORIZATION, "Basic " + authString);
			connection.setRequestProperty(AUTHORIZATION, authenticationKey);
			//connection.setRequestProperty("Content-Type", "text/plain"); 
		    connection.setRequestProperty("Accept-Charset", "utf-8");
			
			
			
			
			
			if (connection.getResponseCode() == 401)
				throw new RuntimeException(FAILED + connection.getResponseCode());

			if (connection.getResponseCode() != 200)
				throw new RuntimeException(FAILED + connection.getResponseCode());

			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content,Charset.forName("UTF-8")));
			String line;
			Gson gson = new Gson();
			while ((line = in.readLine()) != null) {
				resultRO = gson.fromJson(line, ResultRO.class);
			}
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error(
					"Error while getting channel data \n GenericCanvasServlet.get(String urlPath)" + e.getMessage());
		}
		return resultRO;
	}

	/**
	 * This method is used to get login session
	 * 
	 * @param currentSession
	 * @param httpSession
	 * @return LoginSession
	 */
	@SuppressWarnings("unused")
	protected WebSession getLoginSession(String currentSession, HttpSession httpSession) {

		WebSession obj_LoginSession = null;
		Map<String, WebSession> sessionMap = null;
		obj_LoginSession = (WebSession) httpSession.getAttribute("websession");
//		if (obj_LoginSession != null && sessionMap.containsKey(currentSession))
//			obj_LoginSession = sessionMap.get(currentSession);

//		if (obj_LoginSession == null) {
//			sessionMap = (Map<String, WebSession>) httpSession.getAttribute("websession");
//			if (sessionMap != null && sessionMap.containsKey(currentSession)) {
//				sessionMap.remove(currentSession);
//				httpSession.removeAttribute("sessionKey");
//				httpSession.invalidate();
//			}
//			obj_LoginSession = null;
//		}
		return obj_LoginSession;
	}
	
	
	
	/**
	 * This method is used to get data with pagination
	 * @param loginSession
	 * @param startPostiotion
	 * @param listOfChannels
	 * @return
	 */
	
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	protected ResultRO<List<LinkedHashMap<String, Object>>> doGetDataTableDataWithPagination(int start,int count, List<LinkedHashMap<String, Object>> listOfChannels,String XAxis,String authenticationKey,String baseURL) {
		ResultRO result = new ResultRO<>();
		try {
			LinkedHashMap<String, Object[]> mapOfChannelValues = new LinkedHashMap<>();
			List<LinkedHashMap<String, Object>> finalList = null;
			LinkedHashMap<String, Object> lmUnit = new LinkedHashMap<>();
			LinkedHashMap<String, Object> lmCount = new LinkedHashMap<>();
			List<String> channelNames = new ArrayList<String>();
			HashMap<String, String> dataType = new HashMap<>();
			
			lmUnit.put("Channel", "Unit");
			lmCount.put("Channel", "Count");
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject();
			//Map<String,Map<String,String>> channelsByMeasurement = getSortedChannelsByMeasuremnt(listOfChannels);
			double maxCount = 0;
			if(XAxis==null || XAxis.trim().length()<=0){
				Map<String,String> channelsByMeasurement = getSortedChannelsByMeasuremnt(listOfChannels);
				Map<String,String> aliasMapping = getAliasMapping(listOfChannels);
				Set<String> keySet = channelsByMeasurement.keySet();
				for (String key : keySet) {
					//String meaQuantitiesData = channelsByMeasurement.get(key).toString();
					//jsonObject = jsonObject.fromObject(meaQuantitiesData);
					String meaQuantities = channelsByMeasurement.get(key).toString();
					String url = "measurements/measurementkey/" + key
							+ "/channels/channeldata?start="+start+"&count="+count+"&filterbykey=" + meaQuantities;
					ResultRO resultRO = get(url,baseURL,authenticationKey);
					String data = gson.toJson(resultRO);
					jsonObject = jsonObject.fromObject(data);
					JSONArray array = jsonObject.getJSONArray("data");
					for (Object object : array) {
						data = gson.toJson(object);
						jsonObject = jsonObject.fromObject(data);
						String name = jsonObject.getString("name");
						String channelArr = gson.toJson(jsonObject.get("values"));
						Type listType = new TypeToken<Object[]>(){}.getType();
						Object[] arrValues = new Gson().fromJson(channelArr, listType);
						String meaQuantityId = jsonObject.getString("channelkey");
					//	int rowCount = arrValues.length;
						double rowCount = Double.parseDouble(jsonObject.getString("totalCount"));
						if (rowCount > maxCount)
							maxCount = rowCount;

						name  = aliasMapping.get(meaQuantityId);
						lmUnit.put(name, jsonObject.getString("unit"));
						lmCount.put(name, rowCount);

						channelNames.add(name);
						String channeldataType = jsonObject.getString("dataType");
						channeldataType = channeldataType.substring(channeldataType.lastIndexOf(".")+1);
						dataType.put(name, channeldataType);
						mapOfChannelValues.put(name, arrValues);
					}
				}
			}
			else{
				Map<String,String> channelsByMeasurement = getSortedChannelsByMeasuremnt(listOfChannels);
				Set<String> keySet = channelsByMeasurement.keySet();
				Map<String,String> aliasMapping = getAliasMapping(listOfChannels);
				for (String key : keySet) {
					//String meaQuantitiesData = channelsByMeasurement.get(key).toString();
					//jsonObject = jsonObject.fromObject(meaQuantitiesData);
					String meaQuantities = channelsByMeasurement.get(key).toString();
					String url = "measurements/measurementkey/" + key
							+ "/channels/channeldata?filterbykey=" + meaQuantities;
					ResultRO resultRO = get(url,baseURL,authenticationKey);
					String data = gson.toJson(resultRO);
					jsonObject = jsonObject.fromObject(data);
					JSONArray array = jsonObject.getJSONArray("data");
					for (Object object : array) {
						data = gson.toJson(object);
						jsonObject = jsonObject.fromObject(data);
						
						
						String name = jsonObject.getString("name");
						
						String channelArr = gson.toJson(jsonObject.get("values"));
						Type listType = new TypeToken<Object[]>(){}.getType();
						Object[] arrValues = new Gson().fromJson(channelArr, listType);
						String meaQuantityId = jsonObject.getString("channelkey");
						//int rowCount = arrValues.length;
						double rowCount = Double.parseDouble(jsonObject.getString("totalCount"));
						if (rowCount > maxCount)
							maxCount = rowCount;

						name  = aliasMapping.get(meaQuantityId);
						lmUnit.put(name, jsonObject.getString("unit"));
						lmCount.put(name, rowCount);
						
						channelNames.add(name);
						String channeldataType = jsonObject.getString("dataType");
						channeldataType = channeldataType.substring(channeldataType.lastIndexOf(".")+1);
						dataType.put(name, channeldataType);
						mapOfChannelValues.put(name, arrValues);
					}
				}
				int icount = 1;
				for (String key : keySet) {
					String meaQuantities = channelsByMeasurement.get(key).toString();
					String [] arrChannelId = meaQuantities.split(",");
					for (String id : arrChannelId) {
						String url = "measurements/measurementkey/" + key + "/channels/channelkey/" + id + "/channelgroups";
						ResultRO resultRO = get(url,baseURL,authenticationKey);
						int returnCode = resultRO.getReturnCode();
						if (returnCode == EnumReturnCode.I_SUCCESS) {
							String data = resultRO.getData().toString();
							JSONObject json = new JSONObject();
							/*** get values from json ***/
							String groupData = gson.toJson(resultRO);
							jsonObject = json.fromObject(groupData);
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							@SuppressWarnings("unused")
							String groupName = "";
							String groupId = "";
							for (Object object : jsonArray) {
								json = json.fromObject(object);
								groupId = json.getString("submatrixkey");
								groupName = json.getString("name");
							}
							// 2. get channel info by group id and channel name
							String xAxis = URLEncoder.encode(XAxis,"UTF-8");
							if (groupId != null && groupId.length() > 0) {
								url = "measurements/measurementkey/" + key + "/channelgroups/channelgroupkey/" + groupId
										+ "/channeldata?filterbyname=" + xAxis;
								ResultRO resultROXAxisData = get(url,baseURL,authenticationKey);
								if (resultROXAxisData.getReturnCode() == EnumReturnCode.I_SUCCESS) {
									Object channelInfo = resultROXAxisData.getData();
									if (channelInfo != null) {
										//int i = 1;
										data = gson.toJson(resultROXAxisData);
										jsonObject = jsonObject.fromObject(data);
										JSONArray array = jsonObject.getJSONArray("data");
										for (Object object : array) {
											data = gson.toJson(object);
											jsonObject = jsonObject.fromObject(data);
											String name = jsonObject.getString("name");
											String channelArr = gson.toJson(jsonObject.get("values"));
											Type listType = new TypeToken<Object[]>(){}.getType();
											Object[] arrValues = new Gson().fromJson(channelArr, listType);
											//int rowCount = arrValues.length;
											double rowCount = Double.parseDouble(jsonObject.getString("totalCount"));
											if (rowCount > maxCount)
												maxCount = rowCount;

											
											if(mapOfChannelValues.containsKey(name)){
												name = getNewNameForChannel(name,icount,mapOfChannelValues);
												icount++;
											}
											
											lmUnit.put(name, jsonObject.getString("unit"));
											lmCount.put(name, rowCount);

											channelNames.add(name);
											//String channeldataType="";
											
											String channeldataType = jsonObject.getString("dataType");
											channeldataType = channeldataType.substring(channeldataType.lastIndexOf(".")+1);
											dataType.put(name, channeldataType);
											mapOfChannelValues.put(name, arrValues);
										}
									}
									else{
										resultRO.setReturnCode(EnumReturnCode.I_NOT_FOUND);
										log("XAxis not present  for measurement = " + key);
									}
								}else{
									resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
									log("XAxis not present  for measurement = " + key);
									break;
								}

							}else{
								resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
								log("Channel Group Information not exist for selected channel = " + id
										+ " and for measurement = " + key);
								break;
							}
						}
					}
				}
			}
			if (mapOfChannelValues != null && !mapOfChannelValues.isEmpty()) {
				finalList = new ArrayList<>();
				if (start == 0) {
					finalList.add(lmUnit);
					finalList.add(lmCount);
				}
				getFinalData(mapOfChannelValues, channelNames, finalList, dataType, start);
			}
			
			/********  This method is used to format data for dataTable **********/
			result.setData(finalList);
			result.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			result.setReturnCode(EnumReturnCode.I_EXCEPTION);
		}
		return result;
	}
	
	
	
	/**
	 * This method is used to get sorted data by measurement.
	 * @param listOfChannels
	 * @return Map<String, List<String>>
	 */
	private Map<String, String> getSortedChannelsByMeasuremnt(
			List<LinkedHashMap<String, Object>> listOfChannels) {
		 Map<String,String> map = new LinkedHashMap<>();
		try {
			String channels = "";
			for (LinkedHashMap<String, Object> linkedHashMap : listOfChannels) {
				String measurementId = linkedHashMap.get("meaResultId").toString();
				if(map.containsKey(measurementId)){
					channels = map.get(measurementId);
					channels = channels + ","+linkedHashMap.get("meaQuantityId").toString();
				}else{
					channels = "";
					//map = new LinkedHashMap<>();
					channels = linkedHashMap.get("meaQuantityId").toString();
				}
				map.put(measurementId, channels);
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
		return map;
	}
	
	
	
	/**
	 * This method is used to get map alias name with channel Id
	 * @param listOfChannels
	 * @return
	 */
	private Map<String, String> getAliasMapping(List<LinkedHashMap<String, Object>> listOfChannels) {
		 Map<String,String> map = new LinkedHashMap<>();
		try {
			String channelKey = "";
			for (LinkedHashMap<String, Object> linkedHashMap : listOfChannels) {
				@SuppressWarnings("unused")
				String measurementId = linkedHashMap.get("meaResultId").toString();
				@SuppressWarnings("unused")
				String name = linkedHashMap.get("name").toString();
				String alias = linkedHashMap.get("aliasName").toString();
				channelKey = linkedHashMap.get("meaQuantityId").toString();
				map.put(channelKey, alias);
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
		return map;
	
	}

	private String getNewNameForChannel(String name, int i, LinkedHashMap<String, Object[]> mapOfChannelValues) {
		try {
			if(mapOfChannelValues.containsKey(name)){
				name = name +"_" + i;
				getNewNameForChannel(name, i++, mapOfChannelValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}



	private static final int COUNT = 50000;
	/**
	 * This method is used to format data for pagination view
	 * 
	 * @param mapOfChannelValues
	 * @param channelNames
	 * @param formattedData
	 * @param dataType
	 * @throws DataBrowserException
	 */
	private void getFinalData(LinkedHashMap<String, Object[]> mapOfChannelValues, List<String> channelNames,
			List<LinkedHashMap<String, Object>> formattedData, HashMap<String, String> dataType, double start) {
		@SuppressWarnings("unused")
		String inputParams = "";
		try {
			inputParams = " \n Input Params :LinkedHashMap<String, Object[]> mapOfChannelValues="+mapOfChannelValues.toString()+
					", List<String> channelNames="+channelNames.toString()+
			",List<LinkedHashMap<String, Object>> formattedData="+formattedData.size()+
			", HashMap<String, String> dataType="+dataType.toString()+", double start="+start;
			int maxCount = 0;
			for (String channel : channelNames) {
				if (channel != null) {
					if (mapOfChannelValues.get(channel) != null) {
						if (maxCount < mapOfChannelValues.get(channel).length) {
							maxCount = mapOfChannelValues.get(channel).length;
						}
					}
				}
			}
			double count = start;
			for (int i = 0; i < maxCount; i++) {
				LinkedHashMap<String, Object> values = new LinkedHashMap<>();
				if (count >= COUNT && count % COUNT == 0)
					values.put("Channel", count);
				else {
					count = count + 1;
					values.put("Channel", count);
				}

				for (String channel : channelNames) {
					if (channel != null) {
						if (mapOfChannelValues.get(channel) != null) {
							String dataTypeByChannel = dataType.get(channel).toString();
							if (dataTypeByChannel != null) {
								if (i < mapOfChannelValues.get(channel).length) {
									if (dataTypeByChannel.equalsIgnoreCase("String")
											|| dataTypeByChannel.equalsIgnoreCase("Date")) {
										values.put(channel, (mapOfChannelValues.get(channel)[i]).toString());
									} else {
										if (mapOfChannelValues.get(channel)[i].toString().equals("NaN"))
											values.put(channel, "");
										else
											values.put(channel,
													Double.valueOf((mapOfChannelValues.get(channel)[i]).toString()));
									}
								}
							}
						}
					}
				}
				if (!values.isEmpty() && values.size() > 0)
					formattedData.add(values);
			}
		} catch (Exception e) {
			log(e.getMessage());
		}
	}

	

	/**
	 * This method is used to get service reference for provided service name
	 * 
	 * @param type
	 * @return
	 *//*
	protected <T> T getService(Class<T> type) {
		BundleContext bc = FrameworkUtil.getBundle(type).getBundleContext();
		if (bc != null)
			return EntityReference.request(bc, type).getService();
		return null;
	}*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
