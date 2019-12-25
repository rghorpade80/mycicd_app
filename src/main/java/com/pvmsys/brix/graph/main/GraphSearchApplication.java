package com.pvmsys.brix.graph.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.pvmsys.brix.graph.beans.ConfigurationBean;
import com.pvmsys.brix.graph.beans.ElemParameter;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.EnumReturnCode;
import com.pvmsys.brix.graph.beans.RelationBean;
import com.pvmsys.brix.graph.beans.ResultRO;
import com.pvmsys.brix.graph.configreader.beans.DataTableColumn;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.Result;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;
import com.pvmsys.brix.graph.ml.reader.GraphMLService;
import com.pvmsys.brix.graph.rdf.reader.GraphRDFService;
import com.pvmsys.brix.graph.search.GraphSearchException;
import com.pvmsys.brix.graph.search.GraphSearchService;
import com.pvmsys.brix.graph.search.SearchException;
import com.pvmsys.brix.graph.session.watcher.SessionWatcher;
import com.pvmsys.brix.graph.session.watcher.SessionWatcherImpl;
import com.pvmsys.brix.graph.websession.WebAoException;
import com.pvmsys.brix.graph.websession.WebSession;
import com.pvmsys.brix.graph.xmi.reader.GraphXmiService;

import net.sf.json.JSONObject;

@SpringBootApplication
@RestController
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@ComponentScan(basePackages = {"com.pvmsys.brix","com.pvmsys.brix.graph.main"})
public class GraphSearchApplication implements CommandLineRunner
{

	private static Logger logger = Logger.getLogger(GraphSearchApplication.class);
	
	@Value("${OrientServerUrl}")
	String orientServerUrl;
	@Autowired
	GraphSearchService graphSearchService;
	@Autowired
	GraphXmiService graphXmiService;
	@Autowired
	GraphRDFService graphRDFService;
	@Autowired
	GraphMLService graphMLService;
	
	private static final String mappingXmlName = "Relation.xml";
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args)
	{
		logger.info("<------------------------ SpringBoot application started. ----------------------->");
		final String dir = System.getProperty("user.dir");
		PropertyConfigurator.configure(dir + "/config/application.properties");
		logger.info("%%%%%%%%%%%%%%%%%%%%%------  GRAPH SEARCH PORTAL VERSION 2.0.0 Initializing ------%%%%%%%%%%%%%%%%%%%%%");
		logger.info("application.properties file configured successfully into SpringBoot application.");
		SpringApplication.run(GraphSearchApplication.class, args);
		logger.info("%%%%%%%%%%%%%%%%%%%%%------  GRAPH SEARCH PORTAL VERSION 2.0.0 Started. ------%%%%%%%%%%%%%%%%%%%%%");
	}
	
	
    /**
     * (non-Javadoc)
     * 
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
	public void run(String... args)
	{
		try
		{
			ConfigurationBean config = new ConfigurationBean(orientServerUrl,mappingXmlName);
			graphSearchService.setConfiguration(config);
			graphSearchService.initMapping();
			//session watcher thread
			SessionWatcherImpl watcher  = new SessionWatcherImpl();
			Thread thread = new Thread(watcher);
			thread.start();
		}
		catch (GraphSearchException e)
		{
			e.printStackTrace();
			logger.error("Error while searching information");
			logger.error(e);
		}
	}
		
		
		
	/**
	 * This method is used to create UI view JSON.
	 * 
	 * @return String
	 */
	@GetMapping("/search/getuser")
    @ResponseBody
    public String getUser(HttpServletRequest request) {
		try
		{
			logger.info("Retriving Username from session");
			Gson gson = new Gson();
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			Map<String,String> map = new HashMap<>();
			if(username==null)
				map.put("username","true");
			else
				map.put("username", username);				
			
			logger.info("Username found from session is ::"+ username);
			return gson.toJson(map);	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Error while retriving Username");
			logger.error(e);
		}
		return null;
    }
	
	/**
	 * This method is used to invalidate session on logout call.
	 * 
	 * @param request
	 * @return String
	 */
	@GetMapping("/search/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
		try
		{
			logger.info("logout Method called");
			HttpSession session = request.getSession();
//			String sessionKey = request.getParameter("sessionkey");
			if(session.getAttribute("sessionkey") != null){
				String sessionKey = session.getAttribute("sessionkey").toString();
				final WebSession obj_LoginSession = getLoginSession(sessionKey, session);
				if (sessionKey != null && obj_LoginSession == null)
				{
//					throw new ForbiddenException();
//					response.setStatus(403);
				}
				if(session!=null)
				{
					WebSession graph= (WebSession) session.getAttribute("websession");
					SessionWatcher sessionWatcher = new SessionWatcherImpl();
					sessionWatcher.RemoveSessionInfo(sessionKey, graph);
					if(graph!=null)
					{
//						graph.logout();	
					}
					session.invalidate();				
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error while logging out");
			logger.error(e);
		}
		return "login";
	}
	
	
	
    @GetMapping("/search/getMeasurementInfo")
    @ResponseBody
    public String getMeasurementInfo(@RequestParam(value="meaId") String meaId, HttpServletRequest request) {
		String result = "";
		try
		 {
			HttpSession session = request.getSession();
			logger.info("getgraphData Method called.");
			logger.info("getting information for measurement ID::" +meaId);
			WebSession loginSession= (WebSession) session.getAttribute("websession");
    		
			List<Map<String,Object>>  data  = loginSession.execute(new Callable<List<Map<String,Object>>>() {
				@Override
				public List<Map<String,Object>> call() throws Exception {
					return graphSearchService.getMeasurmentInfo(meaId);
				}
			});
			result = new Gson().toJson(data);;
			return result;
			
		}
		catch (Exception e) 
		{
			logger.error("Error while logging out");
			logger.error(e);
		}
		return result;
	}
	
	
    @GetMapping("/search/getMeasurement")
    @ResponseBody
    public String getMeasurements(@RequestParam(value="string") String tagNo,HttpServletRequest request) {
		try
		{
			HttpSession session = request.getSession();
			logger.info("getMeasurements Method called.");
			logger.info("getting information for measurements of tagNo ID::" +tagNo);
			
			WebSession loginSession= (WebSession) session.getAttribute("websession");
			
			final Object mauthKey = "" ;
			Map<String ,List<?>> measurements = loginSession.execute(new Callable<Map<String ,List<?>>>() {
				@Override
				public Map<String ,List<?>> call() throws Exception {
					return graphSearchService.getMeasurements(tagNo,mauthKey.toString());
				}
			});
			return new Gson().toJson(measurements);	
		}
		catch (Exception e) 
		{	
			ResultRO<?> resultRO = new ResultRO<>();
			resultRO.setMessage("Error occured while creating connection with server");
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error(e);
			logger.error("Error occured while creating connection with server"+e.getMessage());
			return new Gson().toJson(resultRO);
		}
	}
    @GetMapping("/search/getDocumentUrl")
    @ResponseBody
    public String getDocumentUrl(@RequestParam(value="string") String id,HttpServletRequest request) {
    	ResultRO<String> resultRO = new ResultRO<>();
		try
		{
			String url = "";
			HttpSession session = request.getSession();
			logger.info("getDocumentUrl Method called.");
			logger.info("getting information for url of document ID::" +id);
			
			WebSession loginSession= (WebSession) session.getAttribute("websession");
			
			url = loginSession.execute(new Callable<String>() {
				@Override
				public String call() throws Exception {
					return graphSearchService.getDocumentUrl(id);
				}
			});
			resultRO.setData(url);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
			return new Gson().toJson(resultRO);
		}
		catch (Exception e) 
		{	
			resultRO.setMessage("Not able to open document url");
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error(e);
			logger.error("Error occured while getting document url"+e.getMessage());
			return new Gson().toJson(resultRO);
		}
	}
    @SuppressWarnings("unchecked")
	@GetMapping("/search/getChannelInfo")
    @ResponseBody
    public String getChannelInfo(@RequestParam(value="string") String parameters) {
		try
		{
			 String[] params = parameters.split(",");
			String channelId= params[0];
			String baseUrl=params[1];
			String authenticationKey=params[2];
			ResultRO<List<Map<String, Object>>> resultRO = new ResultRO<>();
			logger.info("getChannelInfo Method called.");
			logger.info("getting information for Channel of ID::" +channelId);
			String urlPath = "channels/channelkey/" + channelId;
			
			URL url = new URL(baseUrl + urlPath);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("authorization","Basic "+ authenticationKey);
			if (connection.getResponseCode() == 401)
				throw new GraphSearchException("Unauthorized access to server: "+connection.getResponseCode());

			if (connection.getResponseCode() != 200)
				throw new GraphSearchException("Exception while trying to reconnect to server: " + connection.getResponseCode());

			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content,Charset.forName("UTF-8")));
			String line;
			Gson gson = new Gson();
			while ((line = in.readLine()) != null) {
				resultRO = gson.fromJson(line, ResultRO.class);
			}
			Map<String,String> mapData = (Map<String, String>) resultRO.getData();
			if(mapData.containsKey("localcolumnkey"))
				mapData.remove("localcolumnkey");
			if(mapData.containsKey("channelkey"))
				mapData.remove("channelkey");
			if(mapData.containsKey("submatrixkey"))
				mapData.remove("submatrixkey");
			
			return new Gson().toJson(mapData);	
		}
		catch (Exception e) 
		{	
			logger.error(e);
			logger.error("Error while searching information");
			
		}
		return null;
	}
    
    @GetMapping("/getAllSearchNames")
	@ResponseBody
	public String getAllSearchNames(HttpServletRequest request){
		HttpSession session = request.getSession();
		ResultRO<List<String>> resultRO = new ResultRO<>();
		Gson gson = new Gson();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
//			response.setStatus(403);
			return gson.toJson(resultRO);
		}
		WebSession loginSession= (WebSession) session.getAttribute("websession");
	    try {
	    	resultRO= loginSession.execute(new Callable<ResultRO<List<String>>>() {
			@Override
			public ResultRO<List<String>> call() throws Exception {
				return doGetAllAvailableSearchNames();
			}
	    });
	    } catch (Exception e) {
	    	resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting search parameter");
		}	
	    return gson.toJson(resultRO);
	}
	
	
	@PostMapping("/getSearchParameter")
	@ResponseBody
	public String getSearchParameter(@RequestBody String data, HttpServletRequest request){
    	Gson gson = new Gson();
    	ResultRO<SearchBean> resultRO = new ResultRO<>();
    	HttpSession session = request.getSession();
    	String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
//			response.setStatus(403);
			return gson.toJson(resultRO);
		}
    	JSONObject jsonObj = JSONObject.fromObject(data);
    	String searchName = jsonObj.getString("searchname");
    	String searchType = jsonObj.getString("searchtype");
    	WebSession loginSession= (WebSession) session.getAttribute("websession");
		try {
			resultRO = loginSession.execute(new Callable<ResultRO<SearchBean>>() {
				@Override
				public ResultRO<SearchBean> call() throws Exception {
					return doGetSearchParameters(searchName, searchType);
				}
			});
		} catch (Exception e) {
		resultRO.setMessage(e.getMessage());
	    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
	    logger.error("Exception while getting search parameter");
	}
    	return gson.toJson(resultRO);
	}
	
	
	@PostMapping("/getDataForParameter")
	@ResponseBody
	public String getDataForParameter(@RequestBody String data,HttpServletRequest request){
	    Gson gson = new Gson();
	    HttpSession session = request.getSession();
	    ResultRO<List<Object>> resultRO = new ResultRO<>();
	    String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		String key = jsonObj.getString("key");
		String searchName = jsonObj.getString("searchname");
		WebSession loginSession= (WebSession) session.getAttribute("websession");
		try {
			resultRO = loginSession.execute(new Callable<ResultRO<List<Object>>>() {
				@Override
				public ResultRO<List<Object>> call() throws Exception {
					return doGetDataForParameter(key, searchName);
				}
			});
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting data for parameters");
		    e.printStackTrace();
		}
		
		//ResultRO<List<Object>> resultRO = doGetDataForParameter(key, searchName, graph);
		String resultData = gson.toJson(resultRO);
		//resultData = resultData.replaceAll("\\\\", "");
		return resultData;
	}
	
	@PostMapping("/getResultViewList")
	@ResponseBody
	public String getResultViewList(HttpServletRequest request,@RequestBody String searchName){
		HttpSession session = request.getSession();
		ResultRO<List<HashMap<String,String>>> resultRO = new ResultRO<>();
		Gson gson = new Gson();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		WebSession loginSession= (WebSession) session.getAttribute("websession");
	    try {
	    	resultRO= loginSession.execute(new Callable<ResultRO<List<HashMap<String,String>>>>() {
			@Override
			public ResultRO<List<HashMap<String,String>>> call() throws Exception {
				return doGetResultViewList(searchName);
			}
	    });
	    } catch (Exception e) {
	    	resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting result view names");
		}	
	    return gson.toJson(resultRO);
	}
	
	@PostMapping("/getResultForTableView")
	@ResponseBody
	public String getResultForTableView(@RequestBody String data, HttpServletRequest request) {
		Gson gson = new Gson();
		HttpSession session = request.getSession();
		ResultRO<Map<String, Object>> resultRO = new ResultRO<>();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		try {
			SearchBean bean = SearchUtility.getSearchBean(jsonObj);
			int limit = Integer.parseInt(jsonObj.getString("searchlimit"));
			WebSession loginSession = (WebSession) session.getAttribute("websession");

			resultRO = loginSession.execute(new Callable<ResultRO<Map<String, Object>>>() {
				@Override
				public ResultRO<Map<String, Object>> call() throws Exception {
					return doGetResultForTableView(bean, limit);
				}
			});

			// resultRO = doGetResultForTableView(bean, limit);
		} catch (SearchException | WebAoException e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while getting result of TableView");
		}
		return gson.toJson(resultRO);
	}
	
	
	@PostMapping("/getResultForTreeView")
	@ResponseBody
	public String getResultForTreeView(@RequestBody String data, HttpServletRequest request){
		Gson gson = new Gson();
		ResultRO<List<Result>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		try {
			SearchBean bean = SearchUtility.getSearchBean(jsonObj);
			String treeName = jsonObj.getString("treeName");
			int limit = Integer.parseInt(jsonObj.getString("searchlimit"));
			WebSession loginSession = (WebSession) session.getAttribute("websession");

			resultRO = loginSession.execute(new Callable<ResultRO<List<Result>>>() {
				@Override
				public ResultRO<List<Result>> call() throws Exception {
					return doGetResultForTreeView(bean, treeName, limit);
				}
			});
		} 
		catch (SearchException | WebAoException e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting result of Tree view");
		}
		
		return gson.toJson(resultRO);
	}
	
	@PostMapping("/getResultForBubbleView")
	@ResponseBody
	public String getResultForBubbleView(@RequestBody String data, HttpServletRequest request){
		Gson gson = new Gson();
		ResultRO<List<Map<String, Object>>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		try {
			SearchBean bean = SearchUtility.getSearchBean(jsonObj);
			String treeName = jsonObj.getString("treeName");
			int limit = Integer.parseInt(jsonObj.getString("searchlimit"));
			WebSession loginSession = (WebSession) session.getAttribute("websession");

			resultRO = loginSession.execute(new Callable<ResultRO<List<Map<String, Object>>>>() {
				@Override
				public ResultRO<List<Map<String, Object>>> call() throws Exception {
					return doGetResultForBubbleView(bean, treeName, limit);
				}
			});
		} 
		catch (SearchException | WebAoException e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting result of Tree view");
		}
		
		return gson.toJson(resultRO);
	}
	
	
	@PostMapping("/getTreeNames")
	@ResponseBody
	public String getTreeNames(@RequestBody String searchName,HttpServletRequest request){
    	Gson gson = new Gson();
    	  ResultRO<List<String>> resultRO = new ResultRO<>();
    	HttpSession session = request.getSession();
    	String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
 	    WebSession loginSession= (WebSession) session.getAttribute("websession");
//    	final String searchName = request.getParameter("data");
    	try {
    		resultRO= loginSession.execute(new Callable<ResultRO<List<String>>>() {
				@Override
				public ResultRO<List<String>>  call() throws Exception {
					return doGetTreeNames(searchName);
				}
			});
    	} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting result of Tree view");
		}
    	return gson.toJson(resultRO);
	}
	
	
	@PostMapping("/getDataTreeBubble")
	@ResponseBody
	public String getDataTreeBubble(@RequestBody String data,HttpServletRequest request) {
	    Gson gson = new Gson();
	    ResultRO<Map<String, List<Map<String, String>>>> resultRO = new ResultRO<>();
	    HttpSession session = request.getSession();
	    String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		 WebSession loginSession= (WebSession) session.getAttribute("websession");
		try {
			SearchBean bean = SearchUtility.getSearchBean(jsonObj);
			String treeName = jsonObj.getString("treeName");
			int limit = Integer.parseInt(jsonObj.getString("searchlimit"));
			resultRO = loginSession.execute(new Callable<ResultRO<Map<String, List<Map<String, String>>>>>() {
				@Override
				public ResultRO<Map<String, List<Map<String, String>>>> call() throws Exception {
					ResultRO<Map<String, List<Map<String, String>>>> resultRO = new ResultRO<>();
					Map<String, List<Map<String, String>>> result = graphSearchService.getDataForTreeBubbleView(bean,treeName, limit);
					resultRO.setData(result);
					resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);   
					return resultRO;
				}
			});
		} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting saved criteria");
		}
		return gson.toJson(resultRO);
	}
	
	@GetMapping("/saveUserFilter")
	@ResponseBody
	public String saveUserFilter(HttpServletRequest request){
    	Gson gson = new Gson();
    	ResultRO<Integer> resultRO = new ResultRO<>();
    	HttpSession session = request.getSession();
    	String sessionKey = (String) session.getAttribute("sessionkey");
 		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
 		if (sessionKey != null && obj_LoginSession == null)
 		{
 			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
 			return gson.toJson(resultRO);
 		}
    	final String data = request.getParameter("data");
    	JSONObject jsonObj = JSONObject.fromObject(data);
    	String templateName = jsonObj.getString("myfiltername");
    	String searchName = jsonObj.getString("searchName");
    	
 	    WebSession loginSession= (WebSession) session.getAttribute("websession");
 	    
		try {
			List<Parameter> list = SearchUtility.getListOfParameters(data);
			resultRO = loginSession.execute(new Callable<ResultRO<Integer>>() {
				@Override
				public ResultRO<Integer> call() throws Exception {
					return doSaveUserFilter(list, templateName, searchName, "01");
				}
			});
		} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while saving user");
		}
		return gson.toJson(resultRO);
	}
	
	
	@GetMapping("/getSavedFilterParameters")
	@ResponseBody
	public String getSavedFilterParameters(HttpServletRequest request){
    	Gson gson = new Gson();
    	ResultRO<List<Map<String, Object>>> resultRO = new ResultRO<>();
    	HttpSession session = request.getSession();
    	String sessionKey = (String) session.getAttribute("sessionkey");
 		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
 		if (sessionKey != null && obj_LoginSession == null)
 		{
 			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
 			return gson.toJson(resultRO);
 		}
    	final String data = request.getParameter("data");
    	JSONObject jsonObj = JSONObject.fromObject(data);
    	String parameterName = "";
    	String templateName = jsonObj.getString("myfiltername");
    	String searchName = jsonObj.getString("searchName");
 	    WebSession loginSession= (WebSession) session.getAttribute("websession");
		try {
			resultRO = loginSession.execute(new Callable<ResultRO<List<Map<String, Object>>>>() {
				@Override
				public ResultRO<List<Map<String, Object>>> call() throws Exception {
					return doGetSavedFilterParameters(parameterName, templateName, searchName, "01");
				}
			});
		} 
	catch (Exception e) {
	    resultRO.setMessage(e.getMessage());
	    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
	    logger.error("Exception while getting saved filter params");
	}
    	
    	return gson.toJson(resultRO);
	}
	
	
	@GetMapping("/getSavedCriteria")
	@ResponseBody
	public String getSavedCriteria(HttpServletRequest request) {
		Gson gson = new Gson();
		ResultRO<List<String>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
 		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
 		if (sessionKey != null && obj_LoginSession == null)
 		{
 			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
 			return gson.toJson(resultRO);
 		}
		final String data = request.getParameter("data");
		JSONObject jsonObj = JSONObject.fromObject(data);
		String searchName = jsonObj.getString("searchName");
		WebSession loginSession = (WebSession) session.getAttribute("websession");

		try {
			resultRO = loginSession.execute(new Callable<ResultRO<List<String>>>() {
				@Override
				public ResultRO<List<String>> call() throws Exception {
					return doGetListOfTemplates(searchName, "01");
				}
			});
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while getting saved criteria");
		}
		return gson.toJson(resultRO);
	}
	

	@PostMapping("/getColumnNamesForResult")
	@ResponseBody
	public String getColumnNamesForResult(@RequestBody String data,HttpServletRequest request) {
		Gson gson = new Gson();
		ResultRO<List<DataTableColumn>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		JSONObject jsonObj = JSONObject.fromObject(data);
		String searchName = jsonObj.getString("searchName");
		String key = jsonObj.getString("key");
		WebSession loginSession= (WebSession) session.getAttribute("websession");

		try {
			resultRO = loginSession.execute(new Callable<ResultRO<List<DataTableColumn>>>() {
				@Override
				public ResultRO<List<DataTableColumn>> call() throws Exception {
					return doGetListOfColumnNames(searchName,key);
				}
			});
		} 
		catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while getting saved criteria");
		}
		return gson.toJson(resultRO);
	}
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/getVertexDetails")
	@ResponseBody
	public String getVertexDetails(@RequestBody String vertexId, HttpServletRequest request){
		Gson gson = new Gson();
		ResultRO<Map<String,Map>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		String[] split = vertexId.toString().split("#");
		String vertId = split[1];
		WebSession loginSession = (WebSession) session.getAttribute("websession");
		try {
		resultRO = loginSession.execute(new Callable<ResultRO<Map<String,Map>>>() {
			@Override
			public ResultRO<Map<String,Map>> call() throws Exception {
				return doGetVertexDetails(vertId);
			}
		});
		} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting vertex deatils");
		}
		return gson.toJson(resultRO);
		
	}
/**
 * This method is used to show vertex details
 * @param vertexId
 * @param request
 * @return
 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getPreviewedVertexDetails")
	@ResponseBody
	public String getPreviewedVertexDetails(@RequestBody String vertexId, HttpServletRequest request){
		logger.info("getPreviewedVertexDetails method called.");
		Gson gson = new Gson();
		ResultRO<Map<String,Map>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		WebSession loginSession = (WebSession) session.getAttribute("websession");
		try {
		resultRO = loginSession.execute(new Callable<ResultRO<Map<String,Map>>>() {
			@Override
			public ResultRO<Map<String,Map>> call() throws Exception {
				return doGetPreviewedVertexDetails(vertexId);
			}
		});
		} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting vertex deatils");
		}
		return gson.toJson(resultRO);
		
	}
	@SuppressWarnings("rawtypes")
	@PostMapping("/getTagNumOutVerticesAndEdges")
	@ResponseBody
	public String getTagNumOutVerticesAndEdges(@RequestBody String vertexId, HttpServletRequest request){
		Gson gson = new Gson();
		ResultRO<Map<String,Map>> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		String[] split = vertexId.toString().split("#");
		String vertId = split[1];
		WebSession loginSession = (WebSession) session.getAttribute("websession");
		try {
				Map<String, Map> vertexData = loginSession.execute(new Callable<Map<String, Map>>() {
					@Override
					public Map<String, Map> call() throws Exception {
						return graphSearchService.getOutVertexAndEdgesForTagNum(vertId);
					}
				});
				resultRO.setData(vertexData);
				resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} 
		catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting TagNum out vertices and edges");
		}
		return gson.toJson(resultRO);
		
	}
	
	/**
	 * This method is used to upload file for import
	 * @param file
	 * @param request
	 * @return
	 */
	@PostMapping("/uploadFile") 
	@ResponseBody
	public ResultRO<List<ElementBean>> uploadFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
		
		logger.info("uploadFile method called");
		HttpSession session = request.getSession();
		ResultRO<List<ElementBean>> resultRO = new ResultRO<>();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
//			response.setStatus(403);
			return resultRO;
		}
		WebSession loginSession = (WebSession) session.getAttribute("websession");
		if (file.isEmpty()) {
			resultRO.setMessage("Uploaded file is empty");
			return resultRO;
		}
		try {
			// Get the file and save it in temp folder
			String[] files = file.getOriginalFilename().replaceAll(Pattern.quote("\\"), "\\\\").split("\\\\");
			String fileName = null;
			if(files.length>1)
				fileName = files[files.length-1];
			else
				fileName = file.getOriginalFilename();
			
			final String tempFileName = fileName;
			byte[] bytes = file.getBytes();
			Path path = Paths.get(System.getProperty("java.io.tmpdir") + fileName);
			Files.write(path, bytes);
			resultRO.setMessage("Successfully uploaded a file :" + fileName);
			String fileExtension = FilenameUtils.getExtension(fileName);

			resultRO = loginSession.execute(new Callable<ResultRO<List<ElementBean>>>() {
				@Override
				public ResultRO<List<ElementBean>> call() throws Exception {
					if(fileExtension.equalsIgnoreCase("xml")){

						String filePath = System.getProperty("java.io.tmpdir") + "\\" + tempFileName;
						File fXmlFile = new File(filePath);
						DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document doc = dBuilder.parse(fXmlFile);
						if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("graphml")){
							return doGetGraphMLFile(filePath,loginSession);

						}else if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("xmi") ||
								doc.getDocumentElement().getNodeName().equalsIgnoreCase("xmi:XMI")){

						/*	if(!doc.getDocumentElement().getAttribute("xmi.version").equals("1.1")) {
								throw new Exception("XMI version must be 1.1");
							}else*/
								return doGetXMIFile(filePath,loginSession);
						}
					}else if (fileExtension.equalsIgnoreCase("rdf"))
						return doGetRDFFile(System.getProperty("java.io.tmpdir") + "\\" + tempFileName , loginSession);
					else if (fileExtension.equalsIgnoreCase("graphml"))
						return doGetGraphMLFile(System.getProperty("java.io.tmpdir") + "\\" + tempFileName,loginSession);

					return null;
				}
			});

		} catch (IOException e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while uploading XMI file");
		} catch (WebAoException e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while uploading XMI file");
		}catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while uploading XMI file");
		}
		return resultRO;
	}

	@GetMapping("/uploadFile/getGroupElements")
	@ResponseBody
	public String getGroupElements(HttpServletRequest request) {
		logger.info("uploadFile - getGroupElements method called..");
		ResultRO<List<String>> resultRO = new ResultRO<>();
		Gson gson = new Gson();
		try{
			HttpSession session = request.getSession();
			String sessionKey = (String) session.getAttribute("sessionkey");
			WebSession obj_LoginSession = getLoginSession(sessionKey, session);
			if (sessionKey != null && obj_LoginSession == null)
			{
				resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
				return gson.toJson(resultRO);
			}
			WebSession loginSession= (WebSession) session.getAttribute("websession");
			List<String>  elements = loginSession.execute(new Callable<List<String>>() {
				@Override
				public List<String> call() throws Exception {
					return graphSearchService.getGroupElements();
				}
			});
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
			resultRO.setData(elements);
			return  gson.toJson(resultRO);	
		}
		catch (Exception e) 
		{
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Error while getting group elements");
			logger.error(e);
		}
		return gson.toJson(resultRO);
	}
	@PostMapping("/sendHeartBeat")
	public ResultRO<String> sendHeartBeat(HttpServletRequest request){
		
		ResultRO<String> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		if(session.getAttribute("sessionkey") != null){
			String sessionKey = session.getAttribute("sessionkey").toString();
			final WebSession obj_LoginSession = getLoginSession(sessionKey, session);
			
			if(obj_LoginSession != null){
				SessionWatcher sessionWatcher = new SessionWatcherImpl();
				sessionWatcher.setSessionInfo(sessionKey, obj_LoginSession);
				resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
			}else{
				resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			}
		
		}
		return resultRO;
	}
	/**
	 * This method is used to get data to show the preview graph.
	 * @param data
	 * @param request
	 * @return
	 */
	@PostMapping("/getDataForPreview")
	@ResponseBody
	public String getDataForPreview(@RequestBody String data,HttpServletRequest request){
		logger.info("getDataForPreview method called.");
		Gson gson = new Gson();
		ResultRO<String[]> resultRO = new ResultRO<>();
		HttpSession session = request.getSession();
		String sessionKey = (String) session.getAttribute("sessionkey");
		WebSession obj_LoginSession = getLoginSession(sessionKey, session);
		if (sessionKey != null && obj_LoginSession == null)
		{
			resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
			return gson.toJson(resultRO);
		}
		
		String dataString = data.substring(1, data.length()-1).replaceAll(Pattern.quote("\"},{\""), Matcher.quoteReplacement("\"}${\""));
		String[] jsonObjects = dataString.split("\\$");
		List<ElementBean> lstElements = new ArrayList<>();
		for (String jsonStr : jsonObjects) {
			JSONObject jsonObj = JSONObject.fromObject(jsonStr);
			ElementBean elementBean = new ElementBean();
			elementBean.setGroup((String) jsonObj.get("group"));
			elementBean.setId((String) jsonObj.get("id"));
			elementBean.setLabel(jsonObj.getString("label"));
			//for element parameters
			List<ElemParameter> elemParameters = new ArrayList<>();
			String json =  (String) jsonObj.get("parameter");
			if(json != null && !json.isEmpty()){
				String elemParaStr = json.substring(1, json.length()-1).replaceAll(Pattern.quote("},{"), Matcher.quoteReplacement("}${"));
				if(elemParaStr != null && !elemParaStr.isEmpty()){
					String[] elemParamsArr = elemParaStr.split("\\$");
					for (String jsonString : elemParamsArr) {
						
						JSONObject jsonObject = JSONObject.fromObject(jsonString);
						if(jsonObject.get("propertyKey") != null && jsonObject.get("propertyValue") != null) {
							ElemParameter elemParameter = new ElemParameter();
							elemParameter.setPropertyKey((String) jsonObject.get("propertyKey"));
							elemParameter.setPropertyValue(jsonObject.get("propertyValue"));
							elemParameters.add(elemParameter);
						}
					}
				}
			}
			
			elementBean.setParameter(elemParameters);
			lstElements.add(elementBean);
		}

		WebSession loginSession= (WebSession) session.getAttribute("websession");
		List<RelationBean> lstRelations = loginSession.getRelations();
		//set elements to session
		loginSession.setElements(lstElements);
		try {
			resultRO = loginSession.execute(new Callable<ResultRO<String[]>>() {
				@Override
				public ResultRO<String[]> call() throws Exception {
					return doGetDataForPreview(lstElements,lstRelations);
				}
				
			});
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting data to show preview");
		    e.printStackTrace();
		}
		
		String resultData = gson.toJson(resultRO);
		return resultData;
	}
	/**
	 * This method is used to import previewed data to database
	 * @param data
	 * @param request
	 * @return
	 */
	@PostMapping("/importPreviewData")
	@ResponseBody
	public boolean importPreviewData(@RequestBody String data,HttpServletRequest request){
		logger.info("importPreviewData method called.");
		ResultRO<Boolean> resultRO = new ResultRO<>();
		try {
			HttpSession session = request.getSession();
			String sessionKey = (String) session.getAttribute("sessionkey");
			WebSession obj_LoginSession = getLoginSession(sessionKey, session);
			if (sessionKey != null && obj_LoginSession == null)
			{
				resultRO.setReturnCode(EnumReturnCode.I_SESSION_KEY_EXPIRE);
//				return gson.toJson(resultRO);
			}
			/*String dataString = data.substring(1, data.length()-1).replaceAll(Pattern.quote("\"},{\""), Matcher.quoteReplacement("\"}${\""));
			String[] jsonObjects = dataString.split("\\$");
			List<ElementBean> lstElements = new ArrayList<>();
		
			for (String jsonStr : jsonObjects) {
				JSONObject jsonObj = JSONObject.fromObject(jsonStr);
				ElementBean elementBean = new ElementBean();
				elementBean.setGroup((String) jsonObj.get("group"));
				elementBean.setId((String) jsonObj.get("id"));
				elementBean.setLabel(jsonObj.getString("label"));
				String primaryProperty = graphSearchService.getPrimaryAttribute(elementBean.getGroup());
				//for element parameters
				List<ElemParameter> elemParameters = new ArrayList<>();
				String json =  (String) jsonObj.get("parameter");
				if(json != null && !json.isEmpty()){
					String elemParaStr = json.substring(1, json.length()-1).replaceAll(Pattern.quote("},{"), Matcher.quoteReplacement("}${"));
					String[] elemParamsArr = elemParaStr.split("\\$");
					if(elemParaStr != null && !elemParaStr.isEmpty()){
						for (String jsonString : elemParamsArr) {
							JSONObject jsonObject = JSONObject.fromObject(jsonString);
							if(jsonObject.get("propertyKey") != null && !jsonObject.get("propertyKey").toString().equals(primaryProperty)){
								ElemParameter elemParameter = new ElemParameter();
								elemParameter.setPropertyKey((String) jsonObject.get("propertyKey"));
								elemParameter.setPropertyValue(jsonObject.get("propertyValue"));
								elemParameters.add(elemParameter);
							}
						}
					}
				}
				
				elementBean.setParameter(elemParameters);
				lstElements.add(elementBean);
			}*/
			WebSession loginSession= (WebSession) session.getAttribute("websession");
			List<RelationBean> lstRelations = loginSession.getRelations();
			List<ElementBean> lstElements = loginSession.getElements();
			resultRO = loginSession.execute(new Callable<ResultRO<Boolean>>() {
				@Override
				public ResultRO<Boolean> call() throws Exception {
					return doImportPreviewData(lstElements,lstRelations);
				}
			});
		} catch (Exception e) {
			resultRO.setMessage("Exception while importing data to database" +e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while importing data to database");
		}
		
		return resultRO.getData();
	}
	/**
	 * to import previewed data
	 * @param lstElement
	 * @param lstRelation
	 * @return
	 */
	protected ResultRO<Boolean> doImportPreviewData(List<ElementBean> lstElement, List<RelationBean> lstRelation) {
		ResultRO<Boolean> resultRO = new ResultRO<>();
		try {
			boolean importFlag = graphSearchService.importProcess(lstElement, lstRelation);
			resultRO.setData(importFlag);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			resultRO.setMessage("Exception while importing data to database \n GraphSearchApplication.doGetimportPreviewData()");
			logger.error("Exception while while importing data to database \n GraphSearchApplication.doGetimportPreviewData()");
		}
		return resultRO;
	}
	
	/**
	 * to read XMI file
	 * @param path
	 * @param loginSession
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ResultRO<List<ElementBean>> doGetXMIFile(String path, WebSession loginSession) {
		ResultRO<List<ElementBean>> resultRO = new ResultRO<>();
		try {
			Map<String,List<?>> elemRelMap = graphXmiService.getElements(path); 
			if(elemRelMap != null && !elemRelMap.isEmpty()){
			List<ElementBean> elementList	=(List<ElementBean>) elemRelMap.get("elementList");
			List<RelationBean> relationList= (List<RelationBean>) elemRelMap.get("relationList");
			resultRO.setData(elementList);
			//set elements and relations to session
//			loginSession.setElements(elementList);
			loginSession.setRelations(relationList);
			}
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			resultRO.setMessage("Exception while reading XMI file \n GraphSearchApplication.doGetXMIFile() ,Reason : "+e.getMessage());
			logger.error("Exception while reading XMI file \n GraphSearchApplication.doGetXMIFile() ,Reason : "+e.getMessage());
		}
		
		return resultRO;
	}
	/**
	 * to read Graph ML file
	 * @param filePath
	 * @param loginSession
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ResultRO<List<ElementBean>> doGetGraphMLFile(String filePath, WebSession loginSession) {
		ResultRO<List<ElementBean>> resultRO = new ResultRO<>();
		try {
			Map<String,List<?>> elemRelMap = graphMLService.getGraphMLElements(filePath);; 
			if(elemRelMap != null && !elemRelMap.isEmpty()){
				List<ElementBean> elementList	=(List<ElementBean>) elemRelMap.get("elementList");
				List<RelationBean> relationList= (List<RelationBean>) elemRelMap.get("relationList");
				resultRO.setData(elementList);
				//set elements and relations to session
//				loginSession.setElements(elementList);
				loginSession.setRelations(relationList);
			}
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			resultRO.setMessage("Exception while reading graph ml file \n GraphSearchApplication.doGetGraphMLFile() ,Reason : "+e.getMessage());
			logger.error("Exception while reading graph ml file \n GraphSearchApplication.doGetGraphMLFile() ,Reason : "+e.getMessage());
		}
		
		return resultRO;
	}
	
	/**
	 * to read RDF file
	 * @param path file path
	 * @param loginSession
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ResultRO<List<ElementBean>> doGetRDFFile(String path , WebSession loginSession) {
		ResultRO<List<ElementBean>> resultRO = new ResultRO<>();
		try {
			Map<String,List<?>> elemRelMap = graphRDFService.getRdfData(path); 
			if(elemRelMap != null && !elemRelMap.isEmpty()){
				List<ElementBean> elementList	=(List<ElementBean>) elemRelMap.get("elementList");
				List<RelationBean> relationList= (List<RelationBean>) elemRelMap.get("relationList");
				resultRO.setData(elementList);
				//set elements and relations to session
				loginSession.setRelations(relationList);
//				loginSession.setElements(elementList);
			}
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		}catch (Exception e) {
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			resultRO.setMessage("Exception while reading RDF file \n GraphSearchApplication.doGetRDFFile() ,Reason : "+e.getMessage());
			logger.error("Exception while reading RDF file \n GraphSearchApplication.doGetRDFFile()");
		}
		return resultRO;
	}
	/**
	 * get nodes and edges to show preview.
	 * @param lstElements
	 * @param lstRelations
	 * @return
	 */
	protected ResultRO<String[]> doGetDataForPreview(List<ElementBean> lstElements,
			List<RelationBean> lstRelations) {
		Gson gson = new Gson();
		String[] str = new String[2];
		ResultRO<String[]> resultRO = new ResultRO<>();
		try {
			if(lstElements != null)
				str[0] = gson.toJson(lstElements);
			if(lstRelations != null)
				str[1] = gson.toJson(lstRelations);

			resultRO.setData(str);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
			logger.error("Exception while getting data for preview");
		}
		return resultRO;
	}
	/**
	 * get vertex details for preview
	 * @param vertId Id of vertex
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected ResultRO<Map<String,Map>> doGetPreviewedVertexDetails(String vertId) {
		ResultRO<Map<String,Map>> resultRO = new ResultRO<>();
		try {
			Map<String,Map> vertexData = graphSearchService.getPreviewdVertexDetailsToDisplay(vertId);
			resultRO.setData(vertexData);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting vertex detail for preview");
		}
		return resultRO;
	}
	
	@SuppressWarnings("rawtypes")
	protected ResultRO<Map<String,Map>> doGetVertexDetails(String vertId) {
		ResultRO<Map<String,Map>> resultRO = new ResultRO<>();
		try {
			Map<String,Map> vertexData = graphSearchService.getVertexDetailsToDisplay(vertId);
			resultRO.setData(vertexData);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (SearchException e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_EXCEPTION);
		    logger.error("Exception while getting saved criteria");
		}
		return resultRO;
	}


	/**
	 * 
	 * @param string
	 * @return
	 */
	protected ResultRO<List<String>> doGetListOfTemplates(String searchName,String userId) {
		ResultRO<List<String>> resultRO = new ResultRO<>();
		try {
			List<String> list = graphSearchService.getTemplateList(searchName,userId); 
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
				resultRO.setMessage(e.getMessage());
		    	resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting list of templates \n GraphSearchApplication.doGetListOfTemplates(String userId)");
		}
		return resultRO;
	}

	
	/**
	 * This method is used to get tree names for selected search name/business
	 * object
	 * 
	 * @param searchName
	 * @return ResultRO<HashMap<String, Object>>
	 */
	protected ResultRO<List<String>> doGetTreeNames(String searchName) {
		ResultRO<List<String>> resultRO = new ResultRO<>();
		try {
			resultRO.setData(graphSearchService.getTreeNameList(searchName));
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
		    logger.error("Exception while getting filter params \n GraphSearchApplication.doGetTreeNames(String searchName)");
		}
		return resultRO;
	}

	/**
	 * 
	 * @param username
	 * @param myfiltername
	 * @return
	 */
	protected ResultRO<List<Map<String,Object>>> doGetSavedFilterParameters(String parameterName,String templateName, String searchName,String userId) {
		ResultRO<List<Map<String,Object>>> resultRO = new ResultRO<>();
		try {
			List<Map<String,Object>> list = graphSearchService.getTemplateDetails(templateName, searchName, userId);
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
		    logger.error("Exception while getting filter params \n GraphSearchApplication.doGetSavedFilterParameters(String parameterName,String templateName, String searchName,String userId)");
		}
		return resultRO;
	}

	/**
	 * 
	 * @param searchType
	 * @param searchName
	 * @return
	 */
	protected ResultRO<Integer> doSaveUserFilter(List<Parameter> parameters , String fileName,String searchName , String userId) {
		ResultRO<Integer> resultRO = new ResultRO<>();
		try {
			graphSearchService.saveUserFilter(parameters, fileName,searchName, userId);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		}
		catch (Exception e) {
			resultRO.setMessage(e.getMessage());
		    resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
		    logger.error("Exception while saving User filter \n GraphSearchApplication.doSaveUserFilter(List<Parameter> parameters , String fileName , String userId)");
		}
		return resultRO;
	}
	
	/**
	 * 
	 * @param responseType
	 * @param searchName
	 * @return  ResultRO<List<Result>
	 */

	protected ResultRO<List<HashMap<String,String>>> doGetResultViewList(String searchName) {
		ResultRO<List<HashMap<String,String>>> resultRO = new ResultRO<>();
		try {
			List<HashMap<String,String>> list = graphSearchService.getResultViewList(searchName);
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting result for TreeView \n GraphSearchApplication.doGetResultForTreeView(SearchBean searchBean, String treeName,int limit)");
		}
		return resultRO;
	}

	/**
	 * 
	 * @param responseType
	 * @param searchName
	 * @return  ResultRO<List<Result>
	 */

	protected ResultRO<List<Result>> doGetResultForTreeView(SearchBean searchBean, String treeName,int limit) {
		ResultRO<List<Result>> resultRO = new ResultRO<>();
		try {
			List<Result> list = graphSearchService.getResultForTreeView(searchBean, treeName,limit);
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting result for TreeView \n GraphSearchApplication.doGetResultForTreeView(SearchBean searchBean, String treeName,int limit)");
		}
		return resultRO;
	}
	
	/**
	 * 
	 * @param responseType
	 * @param searchName
	 * @return  ResultRO<List<Result>
	 */

	protected ResultRO<List<Map<String, Object>>> doGetResultForBubbleView(SearchBean searchBean, String treeName,int limit) {
		ResultRO<List<Map<String, Object>>> resultRO = new ResultRO<>();
		try {
			List<Map<String, Object>> list = graphSearchService.getResultForBubbleView(searchBean, limit);
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting result for TreeView \n GraphSearchApplication.doGetResultForTreeView(SearchBean searchBean, String treeName,int limit)");
		}
		return resultRO;
	}

	/**
	 * This method is used to get result for table view.
	 * 
	 * @param responseType
	 * @param searchName
	 * @return ResultRO<Map>
	 */
	protected ResultRO<Map<String, Object>> doGetResultForTableView(SearchBean searchBean,int limit) {
		ResultRO<Map<String, Object>> resultRO = new ResultRO<>();
		try {
			Map<String, Object> dataTable = new HashMap<>();
			List<Map<String, Object>> mapOfTableResult = graphSearchService.getResultForTableView(searchBean,limit);
		/*	if (list != null && !list.isEmpty()) {
				for (List<Parameter> params : list) {
					Map<String, Object> result = params.stream().collect(Collectors.toMap(Parameter::getName, param -> param.getValue()!=null?param.getValue():""));
					mapOfTableResult.add(result);
				}

			}*/
			List<HashMap<String, String>> columnConfig = graphSearchService.getColumnConfigurations(searchBean.getSearchName(),
					searchBean.getKey());
			
			dataTable = graphSearchService.isCheckBoxReqAndIsLinkRequired(searchBean.getSearchName(), searchBean.getKey());
			dataTable.put("searchname", searchBean.getSearchName());
			dataTable.put("searchkey", searchBean.getKey());
			dataTable.put("searchresult", mapOfTableResult);
			dataTable.put("columnconfigurations", columnConfig);
			resultRO.setData(dataTable);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
		    resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting result of TableView \n GraphSearchApplication.doGetResultForTableView(SearchBean searchBean,int limit)");
		}
		return resultRO;
	}

	
	/**
	 * 
	 * @param key
	 * @param searchName
	 * @return
	 */
	protected ResultRO<List<Object>> doGetDataForParameter(String key, String searchName) {
		ResultRO<List<Object>> resultRO = new ResultRO<>();
		try{
			List<Object> list = graphSearchService.getDataForParameter(searchName, key);
			resultRO.setData(list);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error("Exception while getting data for parameter \n GraphSearchApplication.doGetDataForParameter(String key, String searchName)");
		}
		return resultRO;
	}

	
	/**
	 * This method is used to get Search parameter.
	 * 
	 * @param searchName
	 * @param searchType
	 * @return ResultRO<SearchBean>
	 * 
	 */
	public ResultRO<SearchBean> doGetSearchParameters(String searchName, String searchType) {
		ResultRO<SearchBean> resultRO = new ResultRO<>();
		try {
			SearchBean searchBean = graphSearchService.getSearchParametersInformation(searchName, searchType);
			resultRO.setData(searchBean);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error(" Exception while getting search parameter \n GraphSearchApplication.doGetSearchParameters(String searchName, String searchType)");
		}
		return resultRO;
	}

	
	/**
	 * This method is used to get All available search names.
	 * 
	 * @return ResultRO<List<String>>
	 */
	protected ResultRO<List<String>> doGetAllAvailableSearchNames() {
		ResultRO<List<String>> resultRO = new ResultRO<>();
		try {
			List<String> listOfAvailbaleSearch = graphSearchService.getAllAvailableSearch();
			resultRO.setData(listOfAvailbaleSearch);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error(" Exception while getting All Available search names \n GraphSearchApplication.doGetListOfTemplates(String userId)");
		}
		return resultRO;
	}

	

	public ResultRO<List<DataTableColumn>> doGetListOfColumnNames(String searchName, String key) {
		ResultRO<List<DataTableColumn>> resultRO = new ResultRO<>();
		try {
			List<DataTableColumn> dataTableColumns = graphSearchService.getListForDataTableColumnName(searchName, key);
			resultRO.setData(dataTableColumns);
			resultRO.setReturnCode(EnumReturnCode.I_SUCCESS);
		} catch (Exception e) {
			resultRO.setMessage(e.getMessage());
			resultRO.setReturnCode(EnumReturnCode.I_FAILURE);
			logger.error(" Exception while getting search parameter "
					+ "\n GraphSearchApplication.doGetListOfColumnNames(String searchName)");
		}
		return resultRO;
	}
	@SuppressWarnings("unchecked")
	protected static WebSession getLoginSession(String strCurrentSession, HttpSession httpSession)
	{
		WebSession obj_LoginSession = null;
		Map<String, WebSession> sessionMap = null;
		sessionMap = (Map<String, WebSession>) httpSession.getAttribute("LoginSession");
		if (sessionMap != null && sessionMap.containsKey(strCurrentSession))
			obj_LoginSession = sessionMap.get(strCurrentSession);

		if (obj_LoginSession != null && obj_LoginSession.isExpired())
		{
			sessionMap = (Map<String, WebSession>) httpSession.getAttribute("LoginSession");
			if (sessionMap.containsKey(strCurrentSession))
			{
				sessionMap.remove(strCurrentSession);
				httpSession.removeAttribute("sessionKey");
				httpSession.invalidate();
			}

			obj_LoginSession = null;
		}
		return obj_LoginSession;
	}
	
}

