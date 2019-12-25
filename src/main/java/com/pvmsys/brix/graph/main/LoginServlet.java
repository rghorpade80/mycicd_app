package com.pvmsys.brix.graph.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pvmsys.brix.graph.search.GraphSearchService;
import com.pvmsys.brix.graph.session.watcher.SessionWatcher;
import com.pvmsys.brix.graph.session.watcher.SessionWatcherImpl;
import com.pvmsys.brix.graph.websession.WebSession;

@WebServlet
public class LoginServlet extends HttpServlet
{

	@Autowired
	GraphSearchService graphSearchService;
	private static Logger logger = Logger.getLogger(LoginServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	      config.getServletContext());
	  }
	  
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException
	{
		doPost(request, response);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException
	{
		PrintWriter pw = null;
		try
		{
			pw = response.getWriter();
			String string = request.getParameter("string");
			String language=request.getParameter("language");
			JsonObject jsonArray = new JsonParser().parse(string).getAsJsonObject();
			Map<String,String> map = new Gson().fromJson(jsonArray, Map.class);
			Map<String, WebSession> sessionMap = null;
			Properties localisationProperties = new Properties();
			String username = map.get("username");
			String password = map.get("password");
			try
			{
				String dir = System.getProperty("user.dir");
				BufferedReader in = null;
				if ("1".equalsIgnoreCase(language)) {
					 in = new BufferedReader( new InputStreamReader(new FileInputStream(dir + "/config/en_US.properties"), "UTF8"));
				} else {
					 in = new BufferedReader( new InputStreamReader(new FileInputStream(dir + "/config/jp_JP.properties"), "UTF8"));
				}
				localisationProperties.load(in); 
				if((Objects.isNull(username)||Objects.isNull(password)) || (username.isEmpty()||password.isEmpty())){
					throw new ConnectException("Invalid Credentials");
				}
				WebSession gsession = graphSearchService.Login(username, password);
				Random random = new Random();
				String sessionKey = username + + random.nextInt();
				SessionWatcher sessionWatcher = new SessionWatcherImpl();
				sessionWatcher.RegisterSessionInfo(sessionKey, gsession);
				HttpSession session = request.getSession();
				if (session.getAttribute("LoginSession") != null)
					sessionMap = (Map<String, WebSession>) session.getAttribute("LoginSession");
				else
					sessionMap = new HashMap<String, WebSession>();
				sessionMap.put(sessionKey, gsession);
				session.setAttribute("websession", gsession);
				session.setAttribute("username", username);
				session.setAttribute("sessionkey", sessionKey);
//				request.setAttribute("sessionkey", sessionKey);
				LinkedHashMap<String, String> perLangProps = new LinkedHashMap<>();
				Set<Object> keySet = localisationProperties.keySet();
				for(Object key : keySet){  
					String k = key.toString();
					String val = (String) localisationProperties.get(k);
					perLangProps.put(k, val.trim());
				}
				session.setAttribute("LoginSession", sessionMap);
				session.setAttribute("resource",new Gson().toJson(perLangProps));
				pw.write("brixsearch");
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.error("Error while login process");
				logger.error(e);
				pw.write("Invalid Credentials");
			}
		}
		catch (Exception e)
		{
			logger.error("Error while login process");
			logger.error(e);
			pw.write("Invalid Credentials");
		}
	}

}
