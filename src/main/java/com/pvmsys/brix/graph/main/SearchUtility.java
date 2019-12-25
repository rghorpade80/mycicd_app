package com.pvmsys.brix.graph.main;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;
import com.pvmsys.brix.graph.search.SearchException;

import net.sf.json.JSONObject;

public class SearchUtility {

	public static SearchBean getSearchBean(JSONObject jsonObject) throws SearchException {
		SearchBean search = null;
		try {
			search = (SearchBean) JSONObject.toBean(jsonObject, SearchBean.class);
//			search.setKey(search.getSearchName());
			search.setSearchType(search.getResponsetype());
			List<Parameter> list1 = new ArrayList<>();
			List<Parameter> list = search.getCriteria().getParameter();
			for (Object param : list) {
				JSONObject fromObject = JSONObject.fromObject(param);
				Parameter parameter = (Parameter) JSONObject.toBean(fromObject, Parameter.class);
				
				if(parameter.getFromDate()!=null){
					String strDate = fromObject.get("fromDate").toString();
					strDate = strDate.replaceAll("\"", "");
					 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
					parameter.setFromDate(date1);
					
				}else if(parameter.getToDate()!=null){
					String strDate = fromObject.get("toDate").toString();
					strDate = strDate.replaceAll("\"", "");
					 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
					parameter.setToDate(date1);
					
				}else if(parameter.getDate()!=null){
					String strDate = fromObject.get("date").toString();
					strDate = strDate.replaceAll("\"", "");
					 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
					parameter.setDate(date1);
					
				}
				
				list1.add(parameter);
			}
			search.getCriteria().setParameter(list1);
			return search;
		} catch (Exception e) {
			throw new SearchException("\n Unable to parse json \n SearchUtility.getSearchBean(JSONObject jsonObject) \n Reason : "+e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws SearchException
	 */
	public static List<Parameter> getListOfParameters(String data) throws SearchException {
		List<Parameter> list = null;
		try {
			
			
			JsonObject jsonObject = (new JsonParser()).parse(data).getAsJsonObject();
			jsonObject = jsonObject.getAsJsonObject("criteria");
			JsonArray jsonArray = null;
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				jsonArray = jsonObject.getAsJsonArray(entry.getKey());
				break;
			}
			list = new ArrayList<>();
			//list = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<Parameter>>() {}.getType());
			System.out.println();
			for (JsonElement jsonElement : jsonArray) {
				Parameter parameter = new Parameter();
				JsonObject jsonObject1 = jsonElement.getAsJsonObject();
				String displayType = jsonObject1.get("displaytype").toString();
				displayType = displayType.replaceAll("\\\"", "");
				/*if(displayType.startsWith("\\") && displayType.endsWith("\\"));{
					displayType = displayType.substring(1,displayType.lastIndexOf("\\"));
				}*/
				
				if(displayType.toString().equalsIgnoreCase("DateRange")){
					if(jsonObject1.has("fromDate")){
						String strDate = jsonObject1.get("fromDate").toString();
						strDate = strDate.replaceAll("\"", "");
						 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
						parameter.setFromDate(date1);
					}
					if(jsonObject1.has("toDate")){
						String strDate = jsonObject1.get("toDate").toString();
						 strDate = strDate.replaceAll("\"", "");
						 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
						parameter.setToDate(date1);
					}
					parameter.setDisplaytype(jsonObject1.get("displaytype").toString());
					parameter.setId(jsonObject1.get("id").toString());
					parameter.setKey(jsonObject1.get("key").toString());
						
				}else if(displayType.toString().equalsIgnoreCase("Date")){
					if(jsonObject1.has("date")){
						String strDate = jsonObject1.get("date").toString();
						strDate = strDate.replaceAll("\"", "");
						 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);  
						parameter.setDate(date1);
					}
					parameter.setDisplaytype(jsonObject1.get("displaytype").toString());
					parameter.setId(jsonObject1.get("id").toString());
					parameter.setKey(jsonObject1.get("key").toString());
						
				}
				else{
					parameter.setDisplaytype(displayType);
					parameter.setId(jsonObject1.get("id").toString());
					parameter.setKey(jsonObject1.get("key").toString());
					String value = jsonObject1.get("values").toString();
					String[] values = value.split(",");
					parameter.setFilterValues(values);
					
				}
				list.add(parameter);
			}
			System.out.println();
			//list = builder.create().fromJson(jsonArray, new TypeToken<ArrayList<Parameter>>() {}.getType());
			return list;
		} catch (Exception e) {
			throw new SearchException("\n Unable to parse json \n SearchUtility.getListOfParameters(JSONObject jsonObject) \n Reason : "+e.getMessage());
		}
	}
}
