package com.pvmsys.brix.graph.search.impl;

import static org.apache.tinkerpop.gremlin.process.traversal.P.gt;
import static org.apache.tinkerpop.gremlin.process.traversal.P.within;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.V;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.jena.query.QueryException;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pvmsys.brix.graph.beans.ConfigurationBean;
import com.pvmsys.brix.graph.beans.ElemParameter;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.LocalColumn;
import com.pvmsys.brix.graph.beans.Measurement;
import com.pvmsys.brix.graph.beans.RelationBean;
import com.pvmsys.brix.graph.beans.RelationParameter;
import com.pvmsys.brix.graph.beans.ResultRO;
import com.pvmsys.brix.graph.configreader.SearchConfigReaderException;
import com.pvmsys.brix.graph.configreader.SearchConfigReaderService;
import com.pvmsys.brix.graph.configreader.beans.Criteria;
import com.pvmsys.brix.graph.configreader.beans.DataTableColumn;
import com.pvmsys.brix.graph.configreader.beans.ENUM;
import com.pvmsys.brix.graph.configreader.beans.Filter_Type;
import com.pvmsys.brix.graph.configreader.beans.Layout;
import com.pvmsys.brix.graph.configreader.beans.Node;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.Result;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;
import com.pvmsys.brix.graph.connection.ConnectionException;
import com.pvmsys.brix.graph.mappings.webServices.HodsWebService;
import com.pvmsys.brix.graph.mappings.webServices.HodsWebServices;
import com.pvmsys.brix.graph.search.AppElement;
import com.pvmsys.brix.graph.search.DuplicateTemplateException;
import com.pvmsys.brix.graph.search.GraphImportException;
import com.pvmsys.brix.graph.search.GraphSearchException;
import com.pvmsys.brix.graph.search.GraphSearchService;
import com.pvmsys.brix.graph.search.HttpRequest;
import com.pvmsys.brix.graph.search.SearchException;
import com.pvmsys.brix.graph.websession.WebAoException;
import com.pvmsys.brix.graph.websession.WebAppSessionContext;
import com.pvmsys.brix.graph.websession.WebSession;
import com.pvmsys.brix.graph.websession.impl.GraphSessionContext;
import com.pvmsys.brix.graph.xml.mapping.beans.Elements;
import com.pvmsys.brix.graph.xml.mapping.beans.Elements.Element;
import com.pvmsys.brix.graph.xml.mapping.beans.Elements.Element.ElementItem;
import com.pvmsys.brix.graph.xml.relation.beans.Relation;
import com.pvmsys.brix.graph.xml.relation.beans.Relations;

import net.sf.json.JSONObject;

@Service
public class GraphSearchProvider implements GraphSearchService{

	@Autowired
	SearchConfigReaderService configReaderService;

	private static Logger logger = Logger.getLogger(GraphSearchProvider.class);
	
	private static final String[] dataType = { "UNKNOWN", "STRING", "SHORT", "FLOAT", "BOOLEAN", "BYTE", "LONG",
			"DOUBLE", "LONGLONG", "ID", "DATE", "DT_BYTESTR", "BLOB", "COMPLEX", "DCOMPLEX", };

	private Relations relationinfo;
	private Elements elements;
	private Map<String, Set<String>> displayAttributes;
	public static ConfigurationBean mconfig;
	private InheritableThreadLocal<WebSession> mSessionStorage = new InheritableThreadLocal<WebSession>();
	private static Map<String, String> primaryAttributes;
	private static String DUMMY_VERTEX_ID;
	final static String webServiceConfigFileName = "webServicesURL.xml";
	final static String elementInformationFileName = "ElementInformation.xml";
	public static List<HodsWebService> hodsWebServicesConfigList;

	public Map<String, Set<String>> getDisplayAttributes()
	{
		return displayAttributes;
	}
	
	@Override
	public void setConfiguration(ConfigurationBean config){
		 mconfig = config;
	}
	
	@Override
	public void initMapping() throws GraphSearchException
	{
		try
		{	
			final String dir = System.getProperty("user.dir");
			File file = new File(dir+"/config/"+mconfig.getMappingXmlName());
			InputStream inputStream = new FileInputStream(file);
			JAXBContext jaxbContext = JAXBContext.newInstance(Relations.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			relationinfo = (Relations) jaxbUnmarshaller.unmarshal(inputStream);
			
			File file2 = new File(dir+"/config/"+"ElementInformation.xml");
			InputStream inputStream2 = new FileInputStream(file2);
			jaxbContext = JAXBContext.newInstance(Elements.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			elements = (Elements) jaxbUnmarshaller.unmarshal(inputStream2);
			displayAttributes = initDisplayAttbr();

			File file3 = new File(dir + "/config/" + webServiceConfigFileName);
			InputStream inputStream3 = new FileInputStream(file3);
			jaxbContext = JAXBContext.newInstance(HodsWebServices.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			HodsWebServices webServicesType = (HodsWebServices) jaxbUnmarshaller.unmarshal(inputStream3);
			hodsWebServicesConfigList = webServicesType.getHodsWebService();

			// Set optional nodes
			List<String> lstOptNodes = new ArrayList<>();
			for (int i = 0; i < elements.getElement().size(); i++) {
				if (elements.getElement().get(i).getIsOptional())
					lstOptNodes.add(elements.getElement().get(i).getName());
			}
			mconfig.setOptionalNodes(lstOptNodes);
			List<String> optionalNodesTableList = new ArrayList<>();
			optionalNodesTableList.addAll(lstOptNodes);
			mconfig.setOptionalNodesTable(optionalNodesTableList);
			if(mconfig.getOptionalNodesTable().contains("TestPlan"))
				mconfig.getOptionalNodesTable().remove("TestPlan");
			if(mconfig.getOptionalNodesTable().contains("DocumentPlan"))
				mconfig.getOptionalNodesTable().remove("DocumentPlan");
			// Initialize primary attribute
			initPrimaryAttbr();
			// Initialize dummy vertex
			initDummyVertex();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in initMapping method, Error occured while initialising mapping files.");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * This method is used to initialized display attrbiutes.
	 * 
	 * @return Map<String, Set<String>>
	 * @throws GraphSearchException
	 */
	private Map<String, Set<String>> initDisplayAttbr() throws GraphSearchException
	{
		try
		{
			Map<String, Set<String>> map = new HashMap<>();
			for (Element element : elements.getElement())
			{
				if (element.getIscompulsory())
				{
					Set<String> set = new HashSet<>();
					for (ElementItem elementItem : element.getElementItem())
					{
						set.add(elementItem.getGraphAttribute());
					}
					map.put(element.getName(), set);
				}
			}
			return map;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error(
					"Exception in initDisplayAttbr method, Error occured while initialising Display attributess");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * This method is used to initialized display attrbiutes.
	 * 
	 * @return Map<String, Set<String>>
	 * @throws GraphSearchException
	 */
	private void initPrimaryAttbr() throws GraphSearchException
	{
		try
		{
			primaryAttributes = new HashMap<>();
			for (com.pvmsys.brix.graph.xml.mapping.beans.Elements.Element element : elements.getElement())
			{
				primaryAttributes.put(element.getName(), element.getPrimaryAttribute());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error(
					"Exception in initDisplayAttbr method, Error occured while initialising Display attributess");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * This method is used to initialized display attrbiutes.
	 * 
	 * @return Map<String, Set<String>>
	 * @throws GraphSearchException
	 */
	@SuppressWarnings("resource")
	private void initDummyVertex() throws GraphSearchException
	{
		try
		{
			Graph graph = null;
			String ORIENT_SERVER_URL = mconfig.getOrientServerUrl();
			OrientGraphFactory fac = new OrientGraphFactory(ORIENT_SERVER_URL).setupPool(5, 20);
			fac.setLabelAsClassName(true);
			if (graph == null) {
				graph = fac.getNoTx();
			}
			Helper orient = new Helper(graph);
			Vertex vertex = orient.getVertexByClassName("Dummy");
			DUMMY_VERTEX_ID = vertex.id().toString();
			graph.close();
			fac.close();
			fac = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error(
					"Exception in initDummyVertex method, Error occured while creating dummy vertex");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}
	
	
	/**
	 * @author kpramod
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get all available searches.
	 */
	public List<String> getAllAvailableSearch() throws SearchException {
		
		List<String> listAvailableSearches = null;
		try {
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			listAvailableSearches = configReaderService.getAllAvailableSearch();
		} catch (Exception e) {
			log(e, "Message: Error while getting all available searches");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return listAvailableSearches;
	}

	/**
	 * @author kpramod
	 * @param String
	 *            filterName
	 * @param String
	 *            filterType
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get search parameters information for
	 *          provided filters
	 */
	@Override
	public SearchBean getSearchParametersInformation(String filterName, String filterType) throws SearchException {
		
		
		SearchBean searchBean = null;
		try {
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			searchBean = configReaderService.readSearchConfigurations(filterName);
			List<Parameter> params = configReaderService.readSearchCriteriaParameters(filterName,
					getEnumValue(filterType));
			setDisplayType(params);
			searchBean.getCriteria().setParameter(params);
			Layout layout = configReaderService.getLayOut(searchBean);
			searchBean.getCriteria().setLayout(layout);
			/**************************************************************
			 * Result configuration not required to configure criteria UI
			 **************************************************************/
			// searchBean.setResults(null);
			searchBean.setSearchName(filterName);
			searchBean.setSearchType(filterType);
		} catch (Exception e) {
			log(e, "Message: Error while getting search parameters inforamtion \n GraphSearchProvider.");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return searchBean;
	}

	/**
	 * @param params
	 * @throws SearchException
	 */

	private void setDisplayType(List<Parameter> params) throws SearchException {
		try {
			Iterator<Parameter> it = params.listIterator();
			while (it.hasNext()) {
				Parameter parameter = it.next();
				parameter.setDisplaytype(getEnumValue(parameter.getDisplaytype()).getEnumVal());
				// set parameter length and its data type
				setParameterInfo(parameter);
			}
		} catch (Exception e) {
			log(e, "Message: Error while setting display type to parameter");
			throw new SearchException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * @author kpramod
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get data for provided search parameters
	 */
	@Override
	public List<Object> getDataForParameter(String filterName, String parameterName) throws SearchException {
		
		
		List<Object> list = null;
		try {
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			SearchBean searchBean = configReaderService.readSearchConfigurations(filterName);
			if (searchBean != null) {
				Parameter params = searchBean.getCriteria().getParameter().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(parameterName)).findFirst().orElse(null);
				if (params != null) {
					String source = params.getSource();
					// String businessObject = searchBean.getOutputobject();
					if (ENUM.FIXED.getId().equalsIgnoreCase(source)) {
						list = new ArrayList<Object>(Arrays.asList(params.getValues().split(",")));
					} else {
						list = getResult(params);
					}
				}
			}

		} catch (Exception e) {
 			log(e, "Message: Error while getting search parameters inforamtion \n GraphSearchProvider.getDataForParameter(String filterName,String parameterName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return list;
	}

	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	private List<Object> getResult(Parameter parameter) throws SearchException {
		
		List<Object> list = null;
		try {
			
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
			GraphTraversal<Vertex, Vertex>  temp =  gr.hasLabel(parameter.getBusinessobject()).dedup();
			list = new ArrayList<>();
			while(temp.hasNext()){
				Vertex row = temp.next();
				if(!row.value(parameter.getName()).toString().isEmpty())
					list.add(row.value(parameter.getName()));
				
			}
		} catch (Exception e) {
		    	e.printStackTrace();
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return list;
	}

	/**
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get data for provided search parameters
	 */
	@Override
	public List<Map<String, Object>>  getResultForTableView(SearchBean searchCriteria, int limit) throws SearchException {
		
		
		List<Map<String, Object>>  testPlanlist = null;
		List<Map<String, Object>>  docPlanlist = null;
		List<Map<String, Object>>  listUpdated = new LinkedList<Map<String,Object>>();
		AppElement rootElement = null;
		try {
			/*
			 * ********************************************************* 
			 * Get Search Criteria parameters selected by User
			 * *********************************************************
			 */
			Criteria in_criteria = searchCriteria.getCriteria();
			
				/*
				 * ****************************************************** 
				 * Get Search configurations data available in search file
				 * ***************************************************
				 */
				
				if (configReaderService == null)
					throw new SearchConfigReaderException("Unable to get configReaderService reference");

				SearchBean searchBean = configReaderService.readSearchConfigurations(searchCriteria.getSearchName());
				if (searchBean != null) {
					/*
					 * ********************************************************
					 * Get output object name from search configuration files
					 * *******************************************************
					 */
					String outputObj = searchBean.getOutputobject(); // searchCriteria.getOutputObj
					rootElement = getRootElement(outputObj);
					if (rootElement == null)
						throw new SearchException(outputObj + " Business Object Not Found In Bom.xml");

					/*
					 * *********************************************************
					 * Iterate all search criteria parameters entered by user
					 * and read configured details of that parameter from file
					 * ********************************************************
					 */
					
					List<Parameter> params = new ArrayList<>();
					if(in_criteria!=null)
					   params = in_criteria.getParameter();
					
				 GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelationsTestPlan = getConditions_RelationsTestPlan(searchCriteria.getKey(), params, outputObj, searchBean,
							rootElement);
				 GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelationsDocPlan = getConditions_RelationsDocPlan(searchCriteria.getKey(), params, outputObj, searchBean,
							rootElement);
				 
				 testPlanlist = getResultList(conditionsAndRelationsTestPlan, searchBean, searchCriteria, limit);
				 docPlanlist = getResultList(conditionsAndRelationsDocPlan, searchBean, searchCriteria, limit);
				 
				 if(testPlanlist != null && testPlanlist.size() > 0)
					 listUpdated.addAll(testPlanlist);
				 if(docPlanlist != null && docPlanlist.size() > 0)
					 listUpdated.addAll(docPlanlist);
				
				if(searchCriteria.getSearchName().equalsIgnoreCase("Project")){
					Comparator<Map<String, Object>> mapComparator = new Comparator<Map<String, Object>>() {
						 public int compare(Map<String, Object> m1,Map<String, Object> m2) {
							 String m1ProjCode = (String) m1.get("ProjectCode");
							 String m2ProjCode = (String) m2.get("ProjectCode");
						        return m1ProjCode.compareTo(m2ProjCode);
						    }
					};
					
					Collections.sort(listUpdated, mapComparator);
			 
			} 
				
				int  SrNo = 1;
				for(Map<String, Object> resMap : listUpdated){
					resMap.put("SrNo", SrNo);
					SrNo++;
				}
				
		}
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
			e.printStackTrace();
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return listUpdated;
	}
	
	private List<Map<String, Object>> getResultList(GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelations, 
			SearchBean searchBean, SearchBean searchCriteria, int limit) throws SearchException {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, List<Parameter>> conditionMap = new HashMap<>();
		List<Parameter> selectparams = null;
		Result result = searchBean.getResults().getResult().stream()
					.filter(x -> x.getKey().equalsIgnoreCase(searchCriteria.getKey())).findFirst().orElse(null);
			if (result != null)
				selectparams = result.getParameter();
			
			if (selectparams != null) {
				for (Parameter parameter : selectparams) {
					String keybussName = parameter.getBusinessobject();
					if(keybussName!=null && keybussName.length() > 0){
						if(conditionMap.containsKey(keybussName)){
							conditionMap.get(keybussName).add(parameter);
						}else{
							List<Parameter> param = new ArrayList<>() ;
							param.add(parameter);
							conditionMap.put(keybussName, param);
						}
					}
				}
			}
			
			list = getResultforTable(conditionsAndRelations,conditionMap);
		if (list != null)
			list = list.subList(0, list.size() > limit ? limit : list.size());
		
		return list;
	}

	/**
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get data for provided search parameters
	 */
	@Override
	public List<Map<String, Object>>  getResultForBubbleView(SearchBean searchCriteria, int limit) throws SearchException {
		
		
		List<Map<String, Object>>  list = null;
		AppElement rootElement = null;
		try {
			/*
			 * ********************************************************* Get
			 * Search Criteria parameters selected by User
			 * *********************************************************
			 */
			Criteria in_criteria = searchCriteria.getCriteria();
			
				/*
				 * ****************************************************** Get
				 * Search configurations data available in search file
				 * ***************************************************
				 */
				
				if (configReaderService == null)
					throw new SearchConfigReaderException("Unable to get configReaderService reference");

				SearchBean searchBean = configReaderService.readSearchConfigurations(searchCriteria.getKey());
				if (searchBean != null) {
					/*
					 * ********************************************************
					 * Get output object name from search configuration files
					 * *******************************************************
					 */
					String outputObj = searchBean.getOutputobject(); // searchCriteria.getOutputObj
					rootElement = getRootElement(outputObj);
					if (rootElement == null)
						throw new SearchException(outputObj + " Business Object Not Found In Bom.xml");

					/*
					 * *********************************************************
					 * Iterate all search criteria parameters entered by user
					 * and read configured details of that parameter from file
					 * ********************************************************
					 */
					
					List<Parameter> params = new ArrayList<>();
					if(in_criteria!=null)
					   params = in_criteria.getParameter();
					
				 GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelations = getConditions_Relations_Bubble(params, outputObj, searchBean,
							rootElement);
				 
				Map<String, List<Parameter>> conditionMap = new HashMap<>();
				List<Parameter> selectparams = null;
				Result result = searchBean.getResults().getResult().stream()
							.filter(x -> x.getType().equalsIgnoreCase("Table")).findFirst().orElse(null);
					if (result != null)
						selectparams = result.getParameter();
					
					if (selectparams != null) {
						for (Parameter parameter : selectparams) {
							String keybussName = parameter.getBusinessobject();
							if(conditionMap.containsKey(keybussName)){
								conditionMap.get(keybussName).add(parameter);
							}else{
								List<Parameter> param = new ArrayList<>() ;
								param.add(parameter);
								conditionMap.put(keybussName, param);
							}
						}
					}
					
					list = getResultforBubble(conditionsAndRelations,conditionMap, outputObj);
				if (list != null)
					list = list.subList(0, list.size() > limit ? limit : list.size());
				
			} 
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return list;
	}
	
	
	
	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private final static SimpleDateFormat dtOnlyformatter = new SimpleDateFormat("yyyy/MM/dd");
	
	/**
	 * This method is used to create conditions.
	 * 
	 * @param parameter
	 * @return String
	 * @throws SearchException
	 */
	private GraphTraversal<Vertex, Vertex>  getConditions_Value(Parameter parameter,GraphTraversal<Vertex, Vertex> travarsal ) throws SearchException {
		try {
			
			if (parameter.getValues() != null && parameter.getValues().length() > 0) {
				String[] values = parameter.getValues().split(",");
				if(values.length > 1 ){
					return travarsal.has( parameter.getName() , within(values));
				}else if(values.length > 0 ){
					String val = values[0] ;
					if(val.contains("*")){
						val = val.replace("*", "[\\w\\s`~!@#$%^&*()-_=+|'\";:\\/.>,<]*");
						return travarsal.has(parameter.getName(), new P<String>((String t, String u)->t.matches(u), val));
					}
					return travarsal.has(parameter.getName(), new P<String>((String t, String u)->t.toUpperCase().contains(u.toUpperCase()), val));
				}
				
			} else if (parameter.getDisplaytype().equalsIgnoreCase("DateRange")) {
				if (parameter.getFromDate() != null && parameter.getToDate() == null) {
					return travarsal.has(parameter.getName(), getAfterPredicate(formatter, parameter.getFromDate()));
				} else if (parameter.getFromDate() == null && parameter.getToDate() != null) {
					return	travarsal.has(parameter.getName(), getBeforePredicate(formatter, parameter.getToDate()));
				}
			} else if (parameter.getDisplaytype().equalsIgnoreCase("Date")) {
				if (parameter.getDate() != null) {
					return travarsal.has(parameter.getName(), getEqualPredicate(dtOnlyformatter, parameter.getDate()));
//					return travarsal.has(parameter.getName(), getEqualPredicate(dtOnlyformatter, parameter.getFromDate()));
				}
			} else
				return travarsal;

		} catch (Exception e) {
			log(e, "Message: Exception while forming condition \n GraphSearchProvider.getConditions(Parameter parameter)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return null;
	}
	

	private P<Date> getAfterPredicate(SimpleDateFormat formatter, Date fromDateStr) {
		BiPredicate<Date, Date> abc = (Date t, Date u)->{
			try {
				return t.after(u);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
			};
		return new P<Date>(abc, fromDateStr);
	}
	
	private P<Date> getBeforePredicate(SimpleDateFormat formatter, Date fromDateStr) {
		BiPredicate<Date, Date> abc = (Date t, Date u)->{
			try {
				return t.before(u);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
			};
		return new P<Date>(abc, fromDateStr);
	}
	
	private P<Date> getEqualPredicate(SimpleDateFormat formatter, Date fromDateStr) {
		BiPredicate<Date, Date> abc = (Date t, Date u)->{
			try {
				return formatter.format(t).compareTo(formatter.format(u)) == 0 ? true:false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
			};
		return new P<Date>(abc, fromDateStr);
	}

	@Override
	public List<List<Result>> getResultForTreeView(SearchBean searchCriteria, int limit) throws SearchException {

		List<List<Result>> listOfResults = new ArrayList<>();

		try{
		if (configReaderService == null)
			throw new SearchConfigReaderException("Unable to get configReaderService reference");

	
		/*
		 * ********************************************************* Get
		 * Search Criteria parameters selected by User
		 * ********************************************************
		 */
		SearchBean searchBean = configReaderService.readSearchConfigurations(searchCriteria.getKey());
		if (searchBean != null) {

			List<Result> resultTree = searchBean.getResults().getResult().stream()
					.filter(x -> x.getType().equalsIgnoreCase("Tree")).collect(Collectors.toList());
			for (Result result : resultTree) {
				if (result != null) {

					List<Result> resultsingle = getResultForTreeView(searchCriteria,result.getKey(),limit);
					listOfResults.add(resultsingle);
				}
			}
		}
		
	} catch (Exception e) {
		log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
		throw new SearchException(e.getMessage(), e.getCause());
	}

		return listOfResults;

	}
	
	/**
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get data for provided search parameters
	 */
	@Override
	public List<Result> getResultForTreeView(SearchBean searchCriteria, String treeName, int limit)
			throws SearchException {
		
		@SuppressWarnings("unused")
		List<Node> listOFTreeNode = null;
		List<Result> results = null;
		AppElement rootElement = null;
		try {
			/*
			 * ***************************************************** Get Search
			 * configurations data available in search file
			 * *****************************************************
			 */
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");

				/*
				 * ********************************************************* Get
				 * Search Criteria parameters selected by User
				 * ********************************************************
				 */
				Criteria in_criteria = searchCriteria.getCriteria();
				SearchBean searchBean = configReaderService.readSearchConfigurations(searchCriteria.getSearchName());
				if (searchBean != null) {
					/*
					 * ********************************************************
					 * Get output object name from search configuration files
					 * *******************************************************
					 */
					String outputObj = searchBean.getOutputobject(); // searchCriteria.getOutputObj
					/*
					 * ********************************************************
					 * Read BOM file data from BOmgraphsessionService
					 * *******************************************************
					 */
					rootElement = getRootElement(outputObj);
					if (rootElement == null)
						throw new SearchException(outputObj + " Business Object Not Found In Bom.xml");
					
					List<List<Parameter>>   listTable = null ;
					List<Parameter> params = new ArrayList<>();
					if(in_criteria!=null)
					   params = in_criteria.getParameter();
					
				    GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelations = getConditions_Relations_Tree(treeName,params, outputObj, searchBean,
							rootElement);
				    
				    Map<String, List<String>> conditionMap = new HashMap<>();
					Result result = searchBean.getResults().getResult().stream().filter(
							x -> (x.getType().equalsIgnoreCase("Tree") && x.getKey().equalsIgnoreCase(treeName)))
							.findFirst().orElse(null);
					
						if (result != null){
							List<Node> nodes = result.getNode();
							for (Node node1 : nodes) {
								String keybussName = node1.getBusinessobject();
								String paramtree = node1.getParametername() ;
								if(conditionMap.containsKey(keybussName)){
									conditionMap.get(keybussName).add(paramtree);
								}else{
									List<String> param = new ArrayList<>() ;
									param.add(paramtree);
									conditionMap.put(keybussName, param);
								}
							}
						}
				    
				    listTable = getResultforTable_Tree (conditionsAndRelations,conditionMap);
						
				    Result treeStructure = searchBean.getResults().getResult().stream().filter(f -> "Tree".equalsIgnoreCase(f.getType()) && treeName.equalsIgnoreCase(f.getKey()) ).findFirst().get();
				  results = findResult(treeStructure, listTable);
				}
			
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return results;
	}


	
	
	private List<Result> findResult(Result treeStructure, List<List<Parameter>> listTable) {
		Map<String, Map<String, Node>> map = new HashMap<>();
		Map<String, Node> allNodesMap = new HashMap<>();
		Map<String, Node> structureMap = new HashMap<>();
		for(Node treelevel : treeStructure.getNode()){
			structureMap.put(treelevel.getBusinessobject(), treelevel);
		}
		
		for(Node treelevel : treeStructure.getNode()){
//			String nodeType = treelevel.getNodetype();
			String param = treelevel.getParametername();
			String bObject = treelevel.getBusinessobject();
			String parent = treelevel.getParent();
			
			for (List<Parameter> list : listTable) {
				Map<String, Parameter> tempMap = new HashMap<>();
				String key = bObject + "=>id"; 
				for (Parameter parameter : list) {
					tempMap.put(parameter.getBusinessobject() + "=>" + parameter.getName(), parameter);
				}
				
				if(tempMap.containsKey(key)){
					Parameter parameter = tempMap.get(key);
					Map<String, Node> abc = map.get(bObject);
					if(abc == null){
						map.put(bObject, new HashMap<>());
						abc = map.get(bObject);
					}
					Node node = abc.get(parameter.getValue());
					if(node == null){
						
						node = new Node();
						abc.put(parameter.getValue(), node);
						node.setBusinessobject(bObject);
						node.setId(parameter.getValue());
						node.setNodetype(bObject);
						String value = tempMap.get(bObject + "=>" + param).getValue();
						node.setValue(value);
						allNodesMap.put(bObject + "=>" + value+"_"+node.getId(), node);
						if(parent != null){
							Node parentLevel = structureMap.get(parent);
							String allNodeKey = parentLevel.getBusinessobject() + "=>" + tempMap.get(parentLevel.getBusinessobject() + "=>" + parentLevel.getParametername()).getValue()+"_"+tempMap.get(parentLevel.getBusinessobject() + "=>" + "id").getValue();
							Node parentNode = allNodesMap.get(allNodeKey);
							if(parentNode.getChildNodes() == null){
								parentNode.setChildNodes( new ArrayList<>());
							}
							parentNode.getChildNodes().add(node);
							node.setParent(parentNode.getId());
						}
					}
				}
			}
		}
		
		Map<String, Node> rootLevel = map.get(treeStructure.getNode().get(0).getBusinessobject());
		List<Result> results = new ArrayList<>();
		List<Node> TreeNodes = new ArrayList<>();
		rootLevel.forEach((k ,v) -> {
			
			Node	rootNode = new Node();
			rootNode.setBusinessobject(v.getBusinessobject());
			rootNode.setId(v.getId());
			rootNode.setNodetype(v.getNodetype());
			rootNode.setChildNodes(v.getChildNodes());
			rootNode.setValue(v.getValue());
			TreeNodes.add(rootNode);
			/*Result res = new Result();
			res.setType(v.getNodetype());
			res.setKey(v.getValue());
			res.setNode(v.getChildNodes());
			results.add(res);*/
		});
		
		
		Result result = new Result();
		result.setNode(TreeNodes);
		results.add(result);
		return results;
	}


	/**
	 * This method is used to get Tree Result
	 * 
	 * @param query
	 * @return List<Node>
	 * @throws SearchException
	 */
	@SuppressWarnings("unused")
	private List<Node> getResultforTree_Graph(List<List<Parameter>>  result ,Result treeview,Node parentNode) throws SearchException {
		List<Node> tree = null;
		try {
			Iterator<List<Parameter>> iterator = result.iterator();
			tree = new ArrayList<>();
			HashMap<String, Object> nodeIndex = new HashMap<>();
			Map<Integer, Object> mapLevelInformation = null;
			while (iterator.hasNext()) {
				List<Parameter> row = iterator.next();
				Node rootNode = null;
				int count = 1;
				int lvlCount = 1;
				mapLevelInformation = new HashMap<>();
				Map<String, Object> nodeData = new HashMap<>();
				for (int i = 0; i < row.size(); i++) {
					Parameter cell = row.get(i);
					if ((count & 1) != 0)
						nodeData.put("Id", cell.getValue().toString());
					else {
						String link = cell.getBusinessobject();
						nodeData.put("Name", cell.getValue().toString());
						mapLevelInformation.put(lvlCount, nodeData);
						lvlCount++;
						if (count == 2) {
							// for first node
							if (nodeIndex != null && nodeIndex.size() > 0
									&& nodeIndex.containsKey(nodeData.get("Name").toString()))
								rootNode = tree.get((int) nodeIndex.get(nodeData.get("Name")));
							else {
								rootNode = new Node();
								rootNode.setId(link + ":" + (nodeData.get("Id").toString()));
								rootNode.setValue(nodeData.get("Name").toString());
								rootNode.setLink(link);
								rootNode.setOpen(false);
								tree.add(rootNode);
								nodeIndex.put(nodeData.get("Name").toString(), tree.indexOf(rootNode));
							}
						} else {
							// for child node
							int level = count / 2;
							fillChildNodes(rootNode, level, 2, nodeData, mapLevelInformation, link);
						}
						nodeData = new HashMap<>();
					}
					count++;
				}
				if (!tree.contains(rootNode))
					tree.add(rootNode);
			}

		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n SearchImpl.getResultforTree(String query)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return tree;
	}

	/**
	 * This method is used to fill child nodes and return node
	 * 
	 * @param rootNode
	 * @param level
	 * @param currLevel
	 * @param mapData
	 * @param mapLevelInfo
	 * @param link
	 * @return Node
	 * @throws SearchException
	 */
	@SuppressWarnings("unchecked")
	private Node fillChildNodes(Node rootNode, int level, int currLevel, Map<String, Object> mapData,
			Map<Integer, Object> mapLevelInfo, String link) throws SearchException {
		
		
		try {
			boolean isNodePresent = false;
			for (int i = currLevel; i <= level; i++) {
				List<Node> lstChildNode = rootNode.getChildNodes();
				if (lstChildNode != null && lstChildNode.size() > 0) {
					Map<String, Object> mapLvlNodeData = (Map<String, Object>) mapLevelInfo.get(i);
					String name = (String) mapLvlNodeData.get("Name");
					for (Node node : lstChildNode) {
						if (node.getValue().equals(name)) {
							isNodePresent = true;
							if (i < level)
								fillChildNodes(node, level, i, mapData, mapLevelInfo, link);
						}
						break;
					}
					if (!isNodePresent && i == level) {
						Node node1 = new Node();
						node1.setValue(mapData.get("Name").toString());
						node1.setId(link + ":" + mapData.get("Id").toString());
						node1.setLink(link);
						node1.setOpen(false);
						lstChildNode.add(node1);
						rootNode.setChildNodes(lstChildNode);
					}
				} else if (i == level) {
					List<Node> lstChildrens = new ArrayList<>();
					Node node1 = new Node();
					node1.setValue(mapData.get("Name").toString());
					node1.setId(link + ":" + mapData.get("Id").toString());
					node1.setLink(link);
					node1.setOpen(false);
					lstChildrens.add(node1);
					rootNode.setChildNodes(lstChildrens);
				}
			}
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.fillChildNodes(Node rootNode, int level, int currLevel, Map<String, Object> mapData,"
					+ "Map<Integer, Object> mapLevelInfo,String link)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
			return rootNode;
	}

	
	
	/**
	 * This method is used to get string with conditions and result
	 * 
	 * @param params
	 * @param outputObj
	 * @param searchBean
	 * @return String
	 * @throws SearchException
	 */
	private GraphTraversal<Vertex, Map<String, Object>>  getConditions_RelationsTestPlan(String searchkey, List<Parameter> params, String outputObj, SearchBean searchBean,
			AppElement rootElement) throws SearchException {

		WebSession session = currentSession();
		WebAppSessionContext contex = session.getSessionContext();
		GraphSessionContext gcontex = (GraphSessionContext) contex;
		
		GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
		List<String> resultelement = new ArrayList<>() ;
		GraphTraversal<Vertex, Map<String, Object>> gremlinresult = null;
		try {
			GraphTraversal<Vertex, Vertex> travarsal =  null;
			Map<String, List<Parameter>> conditionMap = new HashMap<>();
			if(params.size() ==0 ){
				List<Parameter> param = new ArrayList<>() ;
				conditionMap.put(outputObj, param);
			}
			for (Parameter parameter : params) {
			    
				Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
				
				if(xmlParam!=null){
					String keybussName = xmlParam.getBusinessobject();
					if(keybussName!=null && keybussName.length() >0){
							if(conditionMap.containsKey(keybussName)){
								conditionMap.get(keybussName).add(parameter);
							}else{
								List<Parameter> param = new ArrayList<>() ;
								param.add(parameter);
								conditionMap.put(keybussName, param);
							}
					}
				}
			}
			
			Set<String> setelememtRel = conditionMap.keySet();
			List<String> lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String value = (String) iterator.next();
				lstelememtRel.add(value);
				
			}
			
			List<Parameter> selectparams = null;
			Result result = searchBean.getResults().getResult().stream()
					.filter(x -> x.getKey().equalsIgnoreCase(searchkey)).findFirst().orElse(null);
			if (result != null)
				selectparams = result.getParameter();
			
			if (selectparams != null) {
				for (Parameter parameter : selectparams) {
					String element = parameter.getBusinessobject();
					
					if(element!=null && element.length() >0){
					if(!resultelement.contains(element))
						resultelement.add(element);
					
					if(!lstelememtRel.contains(element))
						lstelememtRel.add(element);
					}
				}
			}
			
			List<String> allelement = getElementList();
			String fromelement = "" ;
			String lastFromElem = "";
			String[] defaultFromElem = new String[]{"TestPlan"};
			for (Iterator<String> iterator = allelement.iterator(); iterator.hasNext();)
			{
				String eletorel = (String) iterator.next();
				if(lstelememtRel.contains(eletorel) ){
					if(travarsal == null){
						travarsal = gr.hasLabel(eletorel).as(eletorel) ;
						fromelement = eletorel ;
					}else {
						if(eletorel.equalsIgnoreCase("DocumentPlan") || eletorel.equalsIgnoreCase("Document"))
							continue;
						else if("EngineSubSystem".equalsIgnoreCase(eletorel) || "Prototype".equalsIgnoreCase(eletorel)
								|| "Rot".equalsIgnoreCase(eletorel)){
							for (String string : defaultFromElem) {
								List<Relation> lstrel = getFromRelatedElementList(eletorel);
								if(lstrel!=null && lstrel.size() > 0){
									Relation relation = null;
									for (Relation rel : lstrel) {
										if(rel.getFrom().equalsIgnoreCase(string))
											relation = rel;
									}
									if(relation == null)
										relation = lstrel.get(0) ;
									fromelement =relation.getFrom();
									lastFromElem = eletorel;
									if(relation.getReltype().equalsIgnoreCase("out")){
										if(mconfig.getOptionalNodesTable().contains(eletorel)){
											if(eletorel.equals("DataName")){
												//fromelement = "TestPlan";
//												travarsal.select(fromelement).choose(out("TestInfo").out(relation.getName()).count().is(gt(0)), out("TestInfo").out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
												travarsal.select(fromelement).optional(out("TestInfo").out(relation.getName())).as(eletorel);
											}else{
												travarsal.select(fromelement).optional(out(relation.getName())).as(eletorel);
//											travarsal.select(fromelement).choose(out(relation.getName()).count().is(gt(0)), out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
											}
										}
										else
											travarsal.select(fromelement).out(relation.getName()).as(eletorel);
									}
									else
										travarsal.select(fromelement).in(relation.getName()).as(eletorel);
								}else{
									fromelement = "" ;
								}
							}
						}else{
							List<Relation> lstrel = getFromRelatedElementList(eletorel);
							if(lstrel!=null && lstrel.size() > 0){
								Relation relation = null;
								for (Relation rel : lstrel) {
									if(rel.getFrom().equalsIgnoreCase(lastFromElem))
										relation = rel;
								}
								if(relation == null)
									relation = lstrel.get(0) ;
								fromelement =relation.getFrom();
								lastFromElem = eletorel;
								if(relation.getReltype().equalsIgnoreCase("out")){
									if(mconfig.getOptionalNodesTable().contains(eletorel)){
										if(eletorel.equals("DataName")){
											//fromelement = "TestPlan";
											travarsal.select(fromelement).optional(out("TestInfo").out(relation.getName())).as(eletorel);
//											travarsal.select(fromelement).choose(out("TestInfo").out(relation.getName()).count().is(gt(0)), out("TestInfo").out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
										}else{
											travarsal.select(fromelement).optional(out(relation.getName())).as(eletorel);
//										travarsal.select(fromelement).choose(out(relation.getName()).count().is(gt(0)), out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
										}
									}
									else
										travarsal.select(fromelement).out(relation.getName()).as(eletorel);
								}
								else
									travarsal.select(fromelement).in(relation.getName()).as(eletorel);
							}else{
								fromelement = "" ;
							}
						}
					}
				}
				
			}
			
			
			
		    Set<String> busskeys = conditionMap.keySet();
			List<Parameter> maincond = conditionMap.get(outputObj) ;
			
			if(maincond!=null){
				for (Parameter parameter : maincond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null && xmlParam.getBusinessobject().equalsIgnoreCase(outputObj)) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam,travarsal);
							}
							
					
				}
			}
			for (Iterator<String> iterator = busskeys.iterator(); iterator.hasNext();){
				String keyname = (String) iterator.next();
				
				  if(!keyname.equalsIgnoreCase(outputObj)){
						List<Parameter> subcond = conditionMap.get(keyname) ;
						for (Parameter parameter : subcond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam, travarsal);
							}
							
						}
				  }
				
			}
			if(resultelement.size() ==1){
				String first = resultelement.remove(0);
				gremlinresult  = travarsal.select(first,first) ;
			}else if(resultelement.size() > 1){
				resultelement.remove("DocumentPlan");
				resultelement.remove("Document");
				String first = resultelement.remove(0);
				String second = resultelement.remove(0);
				String remains[] = resultelement.toArray(new String[0]);
				gremlinresult  = remains.length > 0 ? travarsal.select(first, second, remains) : travarsal.select(first, second);
			}
			
		
			

		} catch (Exception e) {
			log(e, "Message: Exception while building query \n GraphSearchProvider.getConditionsAndRelations(List<Parameter> params, String outputObj,"
					+ "SearchBean searchBean,AppElement rootElement)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return gremlinresult;
	}
	
	
	/**
	 * This method is used to get string with conditions and result
	 * 
	 * @param params
	 * @param outputObj
	 * @param searchBean
	 * @return String
	 * @throws SearchException
	 */
	private GraphTraversal<Vertex, Map<String, Object>>  getConditions_RelationsDocPlan(String searchkey, List<Parameter> params, String outputObj, SearchBean searchBean,
			AppElement rootElement) throws SearchException {

		WebSession session = currentSession();
		WebAppSessionContext contex = session.getSessionContext();
		GraphSessionContext gcontex = (GraphSessionContext) contex;
		
		GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
		List<String> resultelement = new ArrayList<>() ;
		GraphTraversal<Vertex, Map<String, Object>> gremlinresult = null;
		try {
			GraphTraversal<Vertex, Vertex> travarsal =  null;
			Map<String, List<Parameter>> conditionMap = new HashMap<>();
			if(params.size() ==0 ){
				List<Parameter> param = new ArrayList<>() ;
				conditionMap.put(outputObj, param);
			}
			for (Parameter parameter : params) {
			    
				Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
				
				if(xmlParam!=null){
					String keybussName = xmlParam.getBusinessobject();
					if(keybussName!=null && keybussName.length() >0){
							if(conditionMap.containsKey(keybussName)){
								conditionMap.get(keybussName).add(parameter);
							}else{
								List<Parameter> param = new ArrayList<>() ;
								param.add(parameter);
								conditionMap.put(keybussName, param);
							}
					}
				}
			}
			
			Set<String> setelememtRel = conditionMap.keySet();
			List<String> lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String value = (String) iterator.next();
				lstelememtRel.add(value);
				
			}
			
			List<Parameter> selectparams = null;
			Result result = searchBean.getResults().getResult().stream()
					.filter(x -> x.getKey().equalsIgnoreCase(searchkey)).findFirst().orElse(null);
			if (result != null)
				selectparams = result.getParameter();
			
			if (selectparams != null) {
				for (Parameter parameter : selectparams) {
					String element = parameter.getBusinessobject();
					
					if(element!=null && element.length() >0){
					if(!resultelement.contains(element))
						resultelement.add(element);
					
					if(!lstelememtRel.contains(element))
						lstelememtRel.add(element);
					}
				}
			}
			
			List<String> allelement = getElementList();
			String fromelement = "" ;
			String lastFromElem = "";
			String[] defaultFromElem = new String[]{"DocumentPlan"};
			for (Iterator<String> iterator = allelement.iterator(); iterator.hasNext();)
			{
				String eletorel = (String) iterator.next();
				if(lstelememtRel.contains(eletorel) ){
					if(travarsal == null){
						travarsal = gr.hasLabel(eletorel).as(eletorel) ;
						fromelement = eletorel ;
					}else {
						if(eletorel.equalsIgnoreCase("TestPlan") || eletorel.equalsIgnoreCase("DataName"))
							continue;
						else if("EngineSubSystem".equalsIgnoreCase(eletorel) || "Prototype".equalsIgnoreCase(eletorel)
								|| "Rot".equalsIgnoreCase(eletorel)){
							for (String string : defaultFromElem) {
								List<Relation> lstrel = getFromRelatedElementList(eletorel);
								if(lstrel!=null && lstrel.size() > 0){
									Relation relation = null;
									for (Relation rel : lstrel) {
										if(rel.getFrom().equalsIgnoreCase(string))
											relation = rel;
									}
									if(relation == null)
										relation = lstrel.get(0) ;
									fromelement =relation.getFrom();
									lastFromElem = eletorel;
									if(relation.getReltype().equalsIgnoreCase("out")){
										if(mconfig.getOptionalNodesTable().contains(eletorel)){
											if(eletorel.equals("DataName")){
												//fromelement = "TestPlan";
												travarsal.select(fromelement).optional(out("TestInfo").out(relation.getName())).as(eletorel);
//												travarsal.select(fromelement).choose(out("TestInfo").out(relation.getName()).count().is(gt(0)), out("TestInfo").out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
											}else{
												travarsal.select(fromelement).optional(out(relation.getName())).as(eletorel);
//											travarsal.select(fromelement).choose(out(relation.getName()).count().is(gt(0)), out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
											}
										}
										else
											travarsal.select(fromelement).out(relation.getName()).as(eletorel);
									}
									else
										travarsal.select(fromelement).in(relation.getName()).as(eletorel);
								}else{
									fromelement = "" ;
								}
							}
						}else{
							List<Relation> lstrel = getFromRelatedElementList(eletorel);
							if(lstrel!=null && lstrel.size() > 0){
								Relation relation = null;
								for (Relation rel : lstrel) {
									if(rel.getFrom().equalsIgnoreCase(lastFromElem))
										relation = rel;
								}
								if(relation == null)
									relation = lstrel.get(0) ;
								fromelement =relation.getFrom();
								lastFromElem = eletorel;
								if(relation.getReltype().equalsIgnoreCase("out")){
									if(mconfig.getOptionalNodesTable().contains(eletorel)){
										if(eletorel.equals("DataName")){
											travarsal.select(fromelement).optional(out("TestInfo").out(relation.getName())).as(eletorel);
//											travarsal.select(fromelement).choose(out("TestInfo").out(relation.getName()).count().is(gt(0)), out("TestInfo").out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
										}else{
											travarsal.select(fromelement).optional(out(relation.getName())).as(eletorel);
//										travarsal.select(fromelement).choose(out(relation.getName()).count().is(gt(0)), out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
										}
									}
									else
										travarsal.select(fromelement).out(relation.getName()).as(eletorel);
								}
								else
									travarsal.select(fromelement).in(relation.getName()).as(eletorel);
							}else{
								fromelement = "" ;
							}
						}
					}
				}
				
			}
			
			
			
		    Set<String> busskeys = conditionMap.keySet();
			List<Parameter> maincond = conditionMap.get(outputObj) ;
			
			if(maincond!=null){
				for (Parameter parameter : maincond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null && xmlParam.getBusinessobject().equalsIgnoreCase(outputObj)) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam,travarsal);
							}
							
					
				}
			}
			for (Iterator<String> iterator = busskeys.iterator(); iterator.hasNext();){
				String keyname = (String) iterator.next();
				
				  if(!keyname.equalsIgnoreCase(outputObj)){
						List<Parameter> subcond = conditionMap.get(keyname) ;
						for (Parameter parameter : subcond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam, travarsal);
							}
							
						}
				  }
				
			}
			if(resultelement.size() ==1){
				String first = resultelement.remove(0);
				gremlinresult  = travarsal.select(first,first) ;
			}else if(resultelement.size() > 1){
				resultelement.remove("TestPlan");
				resultelement.remove("DataName");
				String first = resultelement.remove(0);
				String second = resultelement.remove(0);
				String remains[] = resultelement.toArray(new String[0]);
				gremlinresult  = remains.length > 0 ? travarsal.select(first, second, remains) : travarsal.select(first, second);
			}
			
		
			

		} catch (Exception e) {
			log(e, "Message: Exception while building query \n GraphSearchProvider.getConditionsAndRelations(List<Parameter> params, String outputObj,"
					+ "SearchBean searchBean,AppElement rootElement)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return gremlinresult;
	}
	
	/**
	 * This method is used to get string with conditions and result
	 * 
	 * @param params
	 * @param outputObj
	 * @param searchBean
	 * @return String
	 * @throws SearchException
	 */
	private GraphTraversal<Vertex, Map<String, Object>>  getConditions_Relations_Bubble(List<Parameter> params, String outputObj, SearchBean searchBean,
			AppElement rootElement) throws SearchException {

		WebSession session = currentSession();
		WebAppSessionContext contex = session.getSessionContext();
		GraphSessionContext gcontex = (GraphSessionContext) contex;
		
		GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
		List<String> resultelement = new ArrayList<>() ;
		GraphTraversal<Vertex, Map<String, Object>> gremlinresult = null;
		try {
			GraphTraversal<Vertex, Vertex> travarsal =  null;
			Map<String, List<Parameter>> conditionMap = new HashMap<>();
			if(params.size() ==0 ){
				List<Parameter> param = new ArrayList<>() ;
				conditionMap.put(outputObj, param);
			}
			for (Parameter parameter : params) {
			    
				Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
				
				if(xmlParam!=null){
					String keybussName = xmlParam.getBusinessobject();
					if(keybussName!=null && keybussName.length() >0){
							if(conditionMap.containsKey(keybussName)){
								conditionMap.get(keybussName).add(parameter);
							}else{
								List<Parameter> param = new ArrayList<>() ;
								param.add(parameter);
								conditionMap.put(keybussName, param);
							}
					}
				}
			}
			
			Set<String> setelememtRel = conditionMap.keySet();
			List<String> lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String value = (String) iterator.next();
				lstelememtRel.add(value);
				
			}
			
			List<Parameter> selectparams = null;
			Result result = searchBean.getResults().getResult().stream()
					.filter(x -> x.getType().equalsIgnoreCase("Table")).findFirst().orElse(null);
			if (result != null)
				selectparams = result.getParameter();
			
			if (selectparams != null) {
				for (Parameter parameter : selectparams) {
					String element = parameter.getBusinessobject();
					
					if(element!=null && element.length() >0){
					if(!resultelement.contains(element))
						resultelement.add(element);
					
					if(!lstelememtRel.contains(element))
						lstelememtRel.add(element);
					}
				}
			}
			
			List<String> allelement = getElementList();
			String fromelement = "" ;
			
			for (Iterator<String> iterator = allelement.iterator(); iterator.hasNext();)
			{
				
				String eletorel = (String) iterator.next();
				if(lstelememtRel.contains(eletorel) ){
					if(travarsal == null){
						travarsal = gr.hasLabel(eletorel).as(eletorel) ;
						fromelement = eletorel ;
					}else {
						List<Relation> lstrel = getFromRelatedElementList(eletorel);
						if(lstrel!=null && lstrel.size() > 0){
							Relation relation = lstrel.get(0) ;
							fromelement =relation.getFrom();
							if(relation.getReltype().equalsIgnoreCase("out"))
					            travarsal.select(fromelement).out(relation.getName()).as(eletorel);
							else
								travarsal.select(fromelement).in(relation.getName()).as(eletorel);
						}else{
							fromelement = "" ;
						}
					}
				}
				
			}
			
			
			
		    Set<String> busskeys = conditionMap.keySet();
			List<Parameter> maincond = conditionMap.get(outputObj) ;
			
			if(maincond!=null){
				for (Parameter parameter : maincond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null && xmlParam.getBusinessobject().equalsIgnoreCase(outputObj)) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam,travarsal);
							}
							
					
				}
			}
			
			
			for (Iterator<String> iterator = busskeys.iterator(); iterator.hasNext();){
				String keyname = (String) iterator.next();
				
				  if(!keyname.equalsIgnoreCase(outputObj)){
						List<Parameter> subcond = conditionMap.get(keyname) ;
						for (Parameter parameter : subcond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam, travarsal);
							}
							
						}
				  }
				
			}
			String first = resultelement.remove(0);
			String second = resultelement.remove(0);
			String remains[] = resultelement.toArray(new String[0]);
			gremlinresult  = remains.length > 0 ? travarsal.select(first, second, remains) : travarsal.select(first, second);
			

		} catch (Exception e) {
			log(e, "Message: Exception while building query \n GraphSearchProvider.getConditionsAndRelations(List<Parameter> params, String outputObj,"
					+ "SearchBean searchBean,AppElement rootElement)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return gremlinresult;
	}
	
	
	/**
	 * This method is used to get string with conditions and result
	 * 
	 * @param params
	 * @param outputObj
	 * @param searchBean
	 * @return String
	 * @throws SearchException
	 */
  private GraphTraversal<Vertex, Map<String, Object>>  getConditions_Relations_Tree(String treeName ,List<Parameter> params, String outputObj, SearchBean searchBean,
			AppElement rootElement) throws SearchException {
		
		

		WebSession session = currentSession();
		WebAppSessionContext contex = session.getSessionContext();
		GraphSessionContext gcontex = (GraphSessionContext) contex;
		
		GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
		List<String> resultelement = new ArrayList<>() ;
		GraphTraversal<Vertex, Map<String, Object>> gremlinresult = null;
		try {
			GraphTraversal<Vertex, Vertex> travarsal =  null;
			Map<String, List<Parameter>> conditionMap = new HashMap<>();
			if(params.size() ==0 ){
				List<Parameter> param = new ArrayList<>() ;
				conditionMap.put(outputObj, param);
			}
			for (Parameter parameter : params) {
			    
				Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
				
				if(xmlParam!=null){
					String keybussName = xmlParam.getBusinessobject();
					if(keybussName!=null && keybussName.length() >0){
						if(conditionMap.containsKey(keybussName)){
							conditionMap.get(keybussName).add(parameter);
						}else{
							List<Parameter> param = new ArrayList<>() ;
							param.add(parameter);
							conditionMap.put(keybussName, param);
						}
					}
				}
			}
			
			Set<String> setelememtRel = conditionMap.keySet();
			List<String> lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String value = (String) iterator.next();
				lstelememtRel.add(value);
				
			}
			
			Result result = searchBean.getResults().getResult().stream().filter(
					x -> (x.getType().equalsIgnoreCase("Tree") && x.getKey().equalsIgnoreCase(treeName)))
					.findFirst().orElse(null);
			
			if (result != null){
				List<Node> nod = result.getNode();
				for (Node node1 : nod) {
					String element = node1.getBusinessobject();
					if(!resultelement.contains(element))
						resultelement.add(element);
					
					if(!lstelememtRel.contains(element))
						lstelememtRel.add(element);
				}
			}
			
			List<String> allelement = getElementList();
			String fromelement = "" ;
			
			for (Iterator<String> iterator = allelement.iterator(); iterator.hasNext();)
			{
				
				String eletorel = (String) iterator.next();
				if(lstelememtRel.contains(eletorel) ){
					if(travarsal == null){
						travarsal = gr.hasLabel(eletorel).as(eletorel) ;
						fromelement = eletorel ;
					}else {
						List<Relation> lstrel = getFromRelatedElementList(eletorel);
						if(lstrel!=null && lstrel.size() > 0){
							Relation relation = lstrel.get(0) ;
							fromelement =relation.getFrom();
							if(relation.getReltype().equalsIgnoreCase("out"))
								if(mconfig.getOptionalNodes().contains(eletorel)){
									travarsal.select(fromelement);
									travarsal.choose(out(relation.getName()).count().is(gt(0)), out(relation.getName()), V(DUMMY_VERTEX_ID)).as(eletorel);
								}
								else
									travarsal.select(fromelement).out(relation.getName()).as(eletorel);
							else
								 travarsal.select(fromelement).in(relation.getName()).as(eletorel);
						}else{
							fromelement = "" ;
						}
					}
				}
				
			}
			
			
			
		    Set<String> busskeys = conditionMap.keySet();
			List<Parameter> maincond = conditionMap.get(outputObj) ;
			
			if(maincond!=null){
				for (Parameter parameter : maincond) {
							Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
									.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
							if (xmlParam != null && xmlParam.getBusinessobject().equalsIgnoreCase(outputObj)) {
								setValues(xmlParam, parameter);
								travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
								travarsal =	getConditions_Value(xmlParam,travarsal);
							}
							
					
				}
			}
			
			
			for (Iterator<String> iterator = busskeys.iterator(); iterator.hasNext();){
				String keyname = (String) iterator.next();
				if(!keyname.equalsIgnoreCase(outputObj)){
					List<Parameter> subcond = conditionMap.get(keyname) ;
					for (Parameter parameter : subcond) {
						Parameter xmlParam = searchBean.getCriteria().getParameter().stream()
								.filter(x -> x.getKey().equalsIgnoreCase(parameter.getKey())).findFirst().orElse(null); // parameter.getDispalyName();
						if (xmlParam != null) {
							setValues(xmlParam, parameter);
							travarsal =travarsal.select(xmlParam.getBusinessobject()) ;
							travarsal =	getConditions_Value(xmlParam, travarsal);
						}
						
					}
				}
			}
				
			if(resultelement.size() ==1){
				String first = resultelement.remove(0);
				gremlinresult  = travarsal.select(first,first) ;
			}else{
				String first = resultelement.remove(0);
				String second = resultelement.remove(0);
				String remains[] = resultelement.toArray(new String[0]);
				gremlinresult  = remains.length > 0 ? travarsal.select(first, second, remains) : travarsal.select(first, second);
			}
			

		} catch (Exception e) {
			log(e, "Message: Exception while building query \n GraphSearchProvider.getConditionsAndRelations(List<Parameter> params, String outputObj,"
					+ "SearchBean searchBean,AppElement rootElement)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		
		return gremlinresult;
	}
  
  
  
	
  
	/**
	 * 
	 * @param parameter1
	 * @param parameter
	 * @throws SearchException
	 */
	private void setValues(Parameter xmlParam, Parameter parameter) throws SearchException {
		try {
			if (parameter.getValues() != null && parameter.getValues().length() > 0) {
				xmlParam.setValues(parameter.getValues());
				xmlParam.setDataType(parameter.getDataType());
			} else if (parameter.getFromDate() != null) {
				xmlParam.setFromDate(parameter.getFromDate());
				xmlParam.setDataType(parameter.getDataType());
				xmlParam.setToDate(null);
			} else if (parameter.getToDate() != null) {
				xmlParam.setToDate(parameter.getToDate());
				xmlParam.setDataType(parameter.getDataType());
				xmlParam.setFromDate(null);
			}else if (parameter.getDate() != null) {
				xmlParam.setDate(parameter.getDate());
				xmlParam.setDataType(parameter.getDataType());
			}
		} catch (Exception e) {
			log(e, "Message: Exception setting values \n GraphSearchProvider.setValues(Parameter xmlParam, Parameter parameter)");
			throw new SearchException(e.getMessage(), e.getCause());
		}

	}

	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	private List<Map<String, Object>> getResultforTable(GraphTraversal<Vertex, Map<String, Object>> gt ,Map<String, List<Parameter>> conditionMap ) throws SearchException {
		List<Map<String, Object>>  result = new ArrayList<>();
		
		int srno=1;
		try {
			while (gt.hasNext())
			{
				Map<String, Object> mapresult = gt.next();
				Map<String, Object> finalresultmap = new HashMap<>();
				Set<String> keyResult =mapresult.keySet();
				finalresultmap.put("SrNo", srno+"");
				srno++;
				
				for (Iterator<String> iteratorrest = keyResult.iterator(); iteratorrest.hasNext();)
				{
					String key = (String) iteratorrest.next();
					Vertex vertex =(Vertex) mapresult.get(key);
					if(vertex.id().toString().equals(DUMMY_VERTEX_ID))
						continue;
					Set<String> keysVer =vertex.keys();
					List<Parameter> selectparam= conditionMap.get(vertex.label());
					Map<String, Object> mapTemp = new HashMap<>();
					
					finalresultmap.put("id_"+vertex.label(), vertex.id().toString());
					for (Iterator<String> iteratorVwe = keysVer.iterator(); iteratorVwe.hasNext();)
					{
						String vertexpropkey = (String) iteratorVwe.next();
						mapTemp.put(vertexpropkey, vertex.value(vertexpropkey)+"");
					}
					
					for (Iterator<Parameter> iterator = selectparam.iterator(); iterator.hasNext();)
					{
						Parameter parametervale = (Parameter) iterator.next();
						if(mapTemp.containsKey(parametervale.getName())){
						   Object  value = mapTemp.get(parametervale.getName()) ;
						   finalresultmap.put(parametervale.getKey(), value);
						}
					}
				}
				result.add(finalresultmap);
			}
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResultforTable(String query)");
			e.printStackTrace();
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	private List<Map<String, Object>> getResultforBubble(GraphTraversal<Vertex, Map<String, Object>> gt ,Map<String, List<Parameter>> conditionMap,
			String outputObject) throws SearchException {
		List<Map<String, Object>>  result = new ArrayList<>();
		List<String> lstVertexIds = new ArrayList<>();
		try {
			while (gt.hasNext())
			{
				Map<String, Object> mapresult = gt.next();
				Vertex vertex = (Vertex) mapresult.get(outputObject);
				if (!lstVertexIds.contains(vertex.id().toString())) {
					Map<String, Object> finalresultmap = new HashMap<>();
					String vertexpropkey = vertex.property(primaryAttributes.get(outputObject)).key();
					finalresultmap.put("id",vertex.id().toString());
					finalresultmap.put("group",outputObject);
					finalresultmap.put("label",vertex.value(vertexpropkey)+"");
					finalresultmap.put("title","Name : "+vertex.value(vertexpropkey)+"<br> Label : "+outputObject);
					result.add(finalresultmap);
					lstVertexIds.add(vertex.id().toString());
				}
			}
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResultforTable(String query)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	private List<List<Parameter>> getResultforTable_Tree(GraphTraversal<Vertex, Map<String, Object>> gt ,Map<String, List<String>> conditionMap ) throws SearchException {
		List<List<Parameter>>  result = new ArrayList<>();
		try {
			while (gt.hasNext())
			{
				Map<String, Object> mapresult = gt.next();
				List<Parameter> row = new ArrayList<>();
				Set<String> keyResult =mapresult.keySet();
				for (Iterator<String> iteratorrest = keyResult.iterator(); iteratorrest.hasNext();)
				{
					String key = (String) iteratorrest.next();
					Vertex vertex =(Vertex) mapresult.get(key);
					if(vertex.id().toString().equals(DUMMY_VERTEX_ID))
						continue;
					Set<String> vertexKeySet =vertex.keys();
					List<String> selectparam= conditionMap.get(vertex.label());
					Map<String, Object> mapTemp = new HashMap<>();
					
					Parameter parameter = new Parameter();
					parameter.setName("id");
					parameter.setValue(vertex.id().toString());
					parameter.setBusinessobject(vertex.label());
					row.add(parameter);
					
					for (Iterator<String> keyIterator = vertexKeySet.iterator(); keyIterator.hasNext();)
					{
						String vertexpropkey = (String) keyIterator.next();
						mapTemp.put(vertexpropkey, vertex.value(vertexpropkey)+"");
					}
					
					for (Iterator<String> iterator = selectparam.iterator(); iterator.hasNext();)
					{
						String parametervale =  iterator.next();
						if(mapTemp.containsKey(parametervale)){
						   Object  value = mapTemp.get(parametervale) ;
						    parameter = new Parameter();
							parameter.setName(parametervale);
							parameter.setValue(value.toString());
							parameter.setBusinessobject(vertex.label());
							row.add(parameter);
							
						}
					}
				}
				result.add(row);
			}
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResultforTable(String query)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	@Override
	public Map<String, List<Map<String, String>>> getDataForTreeBubbleView(SearchBean searchCriteria, String treeName, int limit) throws SearchException {
		Map<String, List<Map<String, String>>>  graphDataset = new HashMap<>() ;
		Map<String, String> structureLevelMap = new HashMap<>();
		SearchBean searchBean = null;
		try {
			searchBean = configReaderService.readSearchConfigurations(searchCriteria.getSearchName());
		} catch (SearchConfigReaderException e) {
			e.printStackTrace();
		}
		
		Result result = searchBean.getResults().getResult().stream().filter(
				x -> (x.getType().equalsIgnoreCase("Tree") && x.getKey().equalsIgnoreCase(treeName)))
				.findFirst().orElse(null);
		
		int level = 1;
		for(Node treelevel :result.getNode()){
			structureLevelMap.put(treelevel.getBusinessobject(), level+"");
			level++;
		}
		graphDataset =  getResultForGraphData(searchCriteria, treeName, limit);
		return graphDataset;
	}

	
	/**
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get data for provided search parameters
	 */
	public Map<String, List<Map<String, String>>> getResultForGraphData(SearchBean searchCriteria, String treeName, int limit)
			throws SearchException {
		
		@SuppressWarnings("unused")
		List<Node> listOFTreeNode = null;
		AppElement rootElement = null;
		Map<String, List<Map<String, String>>>  graphDataset = new HashMap<>() ;
		try {
			/*
			 * ***************************************************** Get Search
			 * configurations data available in search file
			 * *****************************************************
			 */
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");

				/*
				 * ********************************************************* Get
				 * Search Criteria parameters selected by User
				 * ********************************************************
				 */
				Criteria in_criteria = searchCriteria.getCriteria();
				SearchBean searchBean = configReaderService.readSearchConfigurations(searchCriteria.getSearchName());
				if (searchBean != null) {
					/*
					 * ********************************************************
					 * Get output object name from search configuration files
					 * *******************************************************
					 */
					String outputObj = searchBean.getOutputobject(); // searchCriteria.getOutputObj
					/*
					 * ********************************************************
					 * Read BOM file data from BOmgraphsessionService
					 * *******************************************************
					 */
					rootElement = getRootElement(outputObj);
					if (rootElement == null)
						throw new SearchException(outputObj + " Business Object Not Found In Bom.xml");
					
					List<Parameter> params = new ArrayList<>();
					if(in_criteria!=null)
					   params = in_criteria.getParameter();
					
				    GraphTraversal<Vertex, Map<String, Object>> conditionsAndRelations = getConditions_Relations_Tree(treeName,params, outputObj, searchBean,
							rootElement);
				    
				    
				    Set<Vertex> allNode = new HashSet<>();
				    
				    Set<Map<String, Object>> setVertrex =  conditionsAndRelations.toSet();
				    for (Iterator<Map<String, Object>> iterator = setVertrex.iterator(); iterator.hasNext();) {
						Map<String, Object> mapresult = (Map<String, Object>) iterator.next();
						Set<String> keyResult =mapresult.keySet();
						for (Iterator<String> iteratorrest = keyResult.iterator(); iteratorrest.hasNext();)
						{
							String key = (String) iteratorrest.next();
							Vertex vertex =(Vertex) mapresult.get(key);
							if(!allNode.contains(vertex) && !vertex.label().equalsIgnoreCase("Dummy"))
								allNode.add(vertex);
						}
					}
				    
				    
				    List< Map<String, String>>  vertexes = new ArrayList<>();
				    
				    for (Iterator<Vertex> iterator = allNode.iterator(); iterator.hasNext();) {
				    	Vertex vertextarget = iterator.next();
				    	String vertexlabel = vertextarget.label() ;
						String  propertyKey = primaryAttributes.get(vertexlabel) ;
						
						Map<String, String> vertexData = new HashMap<>();
						
						String valueprimery = "" ;
						if(vertextarget.property(propertyKey)!=null){
							try{
							valueprimery =(String) vertextarget.property(propertyKey).value();
							}catch (Exception e) {
							}
						}
						
						vertexData.put("level", "1");
						vertexData.put("label", valueprimery);
						vertexData.put("group", vertexlabel);
						vertexData.put("id", vertextarget.id().toString());
						vertexData.put("title", "Name : "+valueprimery+" Label : "+vertexlabel);
						vertexes.add(vertexData);
					}
				    
				    WebSession session = currentSession();
					WebAppSessionContext contex = session.getSessionContext();
					GraphSessionContext gcontex = (GraphSessionContext) contex;
					List<Object> edgelist = gcontex.getGraph().traversal().V(allNode).aggregate("node").outE().as("edge").inV().where(within("node")).select("edge").toList();
					
					List< Map<String, String>>  edges = new ArrayList<>();
					for (Iterator<Object> iterator = edgelist.iterator(); iterator.hasNext();) {
				    	Edge edge = (Edge) iterator.next();
				    	Map<String, String> edgeData = new HashMap<>();
				    	edgeData.put("label", edge.label());
						edgeData.put("id", edge.id().toString()); 
						edgeData.put("from", edge.outVertex().id().toString());
						edgeData.put("to", edge.inVertex().id().toString());
						edgeData.put("arrows", "to");
						edges.add(edgeData);
					}
					graphDataset.put("nodes", vertexes);
				    graphDataset.put("edges", edges);
				}
			
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResult(String filterName, String parameterName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return graphDataset;
	}

	
	@SuppressWarnings("unused")
	private void getChildren(List<Node> children,List< Map<String, String>> vertexes,String fromid,List< Map<String, String>>  edges,Map<String, String> structureLevelMap){
		if(children!=null && children.size() >0){
			Iterator<Node> nodeitr = children.iterator();
			while (nodeitr.hasNext()) {
				Node childnode = (Node) nodeitr.next();
				Map<String, String> mapvertex = new HashMap<>();
				mapvertex.put("id",childnode.getId());
				mapvertex.put("group",childnode.getBusinessobject());
				mapvertex.put("label",childnode.getValue());
				mapvertex.put("level",structureLevelMap.get(childnode.getBusinessobject()));
				mapvertex.put("title","Name :  "+childnode.getValue()+" label : "+childnode.getBusinessobject());
				vertexes.add(mapvertex);
				
				Map<String, String> mapedges = new HashMap<>();
				mapedges.put("from",fromid);
				mapedges.put("to",childnode.getId());
				mapedges.put("arrows","to");
				mapedges.put("label",childnode.getBusinessobject()+"Info");
				edges.add(mapedges);
				getChildren(childnode.getChildNodes(),vertexes,childnode.getId() ,edges,structureLevelMap);
			}
		}
	}
	
	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	@SuppressWarnings("unused")
	private List<Map<String, String>> getResultforGraph_BubbleVertiex(GraphTraversal<Vertex, Map<String, Object>> gt ,Map<String, List<Parameter>> conditionMap ) throws SearchException {
		List< Map<String, String>>  result = new ArrayList<>();
		try {
			 Map<String, Map<String, String>> vertix = new HashMap<>();
			while (gt.hasNext())
			{
				Map<String, Object> mapresult = gt.next();
				Set<String> keyResult =mapresult.keySet();
				for (Iterator<String> iteratorrest = keyResult.iterator(); iteratorrest.hasNext();)
				{
					String key = (String) iteratorrest.next();
					Vertex vertex =(Vertex) mapresult.get(key);
					Set<String> keysVer =vertex.keys();
					List<Parameter> selectparam= conditionMap.get(vertex.label());
					Map<String, String> mapTemp = new HashMap<>();
					Map<String, String> mapTemp1 = new HashMap<>();
					
				//	finalresultmap.put("id_"+vertex.label(), vertex.id().toString());
					mapTemp.put("id",vertex.label()+":"+vertex.id().toString());
					mapTemp.put("group",vertex.label());
					
					for (Iterator<String> iteratorVwe = keysVer.iterator(); iteratorVwe.hasNext();)
					{
						String vertexpropkey = (String) iteratorVwe.next();
						mapTemp1.put(vertexpropkey, vertex.value(vertexpropkey)+"");
					}
					
					for (Iterator<Parameter> iterator = selectparam.iterator(); iterator.hasNext();)
					{
						Parameter parametervale = (Parameter) iterator.next();
						if(mapTemp1.containsKey(parametervale.getName())){
						   Object  value = mapTemp1.get(parametervale.getName()) ;
						   //finalresultmap.put(parametervale.getKey(), value);
						   mapTemp.put("label",value.toString());
						}
					}
					
					vertix.put(vertex.id().toString(), mapTemp);
				}
				
			}
			
			vertix.forEach((k,v)->{
				result.add(v);
			});
			
			
			
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResultforTable(String query)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return result;
	}

	private List<String> getElementList(){
		List<String> lst = new ArrayList<>();
		Iterator<Relation> result = relationinfo.getRelation().iterator() ;
		while (result.hasNext())
		{
			Relation rel =  result.next();
			if(!lst.contains(rel.getFrom())){
				lst.add(rel.getFrom());
			}
			if(!lst.contains(rel.getTo())){
				lst.add(rel.getTo());
			}
		}
		return lst;
	}
	
	
	private List<Relation> getFromRelatedElementList(String element){
		List<Relation> lst = new ArrayList<>();
		Iterator<Relation> result = relationinfo.getRelation().iterator() ;
		while (result.hasNext())
		{
			Relation rel =  result.next();
			if(element.equalsIgnoreCase(rel.getTo())  ){
				lst.add(rel);
			}
			
		}
		return lst;
	}
	
	
	/**
	 * This Method used to get Root Element of Business Object
	 * 
	 * @param Business
	 *            Object
	 * @return AppElement Root Element Of Business Object
	 * @throws QueryException
	 *             if there's any issues to get root element of business object
	 */
	protected AppElement getRootElement(String businessobject)  {
		AppElement element = null;
		try {
			String fromrel =businessobject ;
			String torel   =businessobject;
			element = new AppElement();
			element.elementname = businessobject;
			Iterator<Relation> result = relationinfo.getRelation().stream()
			.filter(x -> x.getFrom().equalsIgnoreCase(fromrel)|| x.getTo().equalsIgnoreCase(torel)).iterator();
			
			while (result.hasNext())
			{
				Relation rel =  result.next();
				element.relationcheck.add(rel);
			}
			
		} catch (Exception e) {
			log(e, "Message: Problem To Get Business Object Root Element");
		}
		return element;
	}

	

	/**
	 * This method is used to get element information by its name.
	 * 
	 * @param businessObjectName
	 * @return List<NameValue>
	 * @throws ElementDetailsServiceException
	 */
	private void setParameterInfo(Parameter parameter) throws SearchException {
		parameter.setDataType(GraphSearchProvider.dataType[1]);
		parameter.setLength(200);
	}

	/**
	 * @author kpramod
	 * @return List<Result>
	 * @throws SearchException
	 * @purpose This method is used to get column configurations
	 */

	@Override
	public List<HashMap<String, String>> getColumnConfigurations(String searchName, String resultKey)
			throws SearchException {
		
		
		List<HashMap<String, String>> columnConfig = null;
		try {
			columnConfig = new ArrayList<>();
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			SearchBean bean = configReaderService.readSearchConfigurations(searchName);
			if (bean != null) {
				Result result = bean.getResults().getResult().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(resultKey)).findFirst().orElse(null);
				if (result != null) {
					List<Parameter> params = result.getParameter();
					for (Parameter parameter : params) {
						// 1 means true
						if (parameter.getSubfilter().equals("1")) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("key", parameter.getKey());
							hashMap.put("columnfilter", getFilter(parameter.getFiltertype()));
							columnConfig.add(hashMap);
						}
					}
				}
			}
		} catch (Exception e) {
			log(e, "Error while getting column configurations\n GraphSearchProvider.getColumnConfigurations(String searchName, String resultType)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return columnConfig;
	}

	/**
	 * @author kpramod
	 * @return List<Result>
	 * @throws SearchException
	 * @purpose This method is used to get link and check box configuration
	 */

	public Map<String, Object> isCheckBoxReqAndIsLinkRequired(String searchName, String resultKey)
			throws SearchException {
		Map<String, Object> columnConfig = null;
		try {
			columnConfig = new HashMap<String, Object>();
			
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			SearchBean bean = configReaderService.readSearchConfigurations(searchName);
			if (bean != null) {
				Result result = bean.getResults().getResult().stream()
						.filter(x -> x.getKey().equalsIgnoreCase(resultKey)).findFirst().orElse(null);
				if (result != null) {
					columnConfig.put("checkboxrequired", result.getCheckboxrequired());
					columnConfig.put("islink", result.getIslink());
					columnConfig.put("link", result.getLink());
				}
			}
		} catch (Exception e) {
			log(e, "Error while getting column configurations");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return columnConfig;
	}

	/**
	 * @author kpramod
	 * @return List<String>
	 * @throws SearchException
	 * @purpose This method is used to get tree name list.
	 */
	public List<String> getTreeNameList(String filterName) throws SearchException {
		List<String> listOfTreeNames = null;
		try {
			if (configReaderService == null)
				throw new SearchConfigReaderException("Unable to get configReaderService reference");
			SearchBean bean = configReaderService.readSearchConfigurations(filterName);
			if (bean != null) {
				List<Result> listOFTree = bean.getResults().getResult().stream()
						.filter(x -> x.getType().equalsIgnoreCase("Tree")).collect(Collectors.toList());
				if (listOFTree != null) {
					listOfTreeNames = listOFTree.stream().map(Result::getKey).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			log(e, "Message: Error while getting all available tree names");
			throw new SearchException(e.getMessage(), e.getCause());
		}
				return listOfTreeNames;
	}

	

	
	/**
	 * This method is used to save template for search criteria. User can create
	 * multiple templates for criteria.Template name must be unique.
	 * 
	 * @param List<Parameter>
	 *            parameters
	 * @param String
	 *            SavedFilterName
	 * @param String
	 *            userId
	 * @throws SearchException
	 * @throws DuplicateTemplateException
	 */
	@Override
	public void saveUserFilter(List<Parameter> parameters, String SavedFilterName,String searchName, String userId)
			throws SearchException {
		
		
		try {
			String templatePath = getTemplatePath(searchName,userId);
			isDuplicateTemplate(SavedFilterName, searchName, userId);
			/*
			 * Map<String, String> criteria = parameters.stream()
			 * .collect(Collectors.toMap(Parameter::getKey,
			 * Parameter::getValues));
			 * 
			 * Map<String, String> id = parameters.stream()
			 * .collect(Collectors.toMap("Id", ""));
			 */

			// Set<String> keySet = criteria.keySet();
			Map<String, Object> data = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			for (Parameter parameter : parameters) {
				HashMap<String, Object> hm = new HashMap<>();
				String key = parameter.getKey();
				hm.put("key", key.replaceAll("\\\"", ""));
				if (parameter.getFromDate() != null) {
					String date1 = new SimpleDateFormat("dd/MM/yyyy").format(parameter.getFromDate());
					hm.put("fromDate", date1);
				} else if (parameter.getToDate() != null) {
					String date1 = new SimpleDateFormat("dd/MM/yyyy").format(parameter.getToDate());
					hm.put("toDate", date1);
				} else {
					String[] values = parameter.getFilterValues();
					hm.put("values", values);
				}
				String id = parameter.getId();
				hm.put("id", id.replaceAll("\\\"", ""));
				String dispalyType = parameter.getDisplaytype();
				hm.put("displaytype", dispalyType.replaceAll("\\\"", ""));
				list.add(hm);
			}
			data.put("Parameter", list);

			File folder = new File(templatePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file1 = new File(folder.getPath() + File.separator + SavedFilterName + ".json");
			if (!file1.exists())
				file1.createNewFile();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(file1, JSONObject.fromObject(data));
		} catch (DuplicateTemplateException e) {
			throw new SearchException(e.getMessage(), e.getCause());
		} catch (Exception e) {
			log(e, "Message: Exception while creating template \n GraphSearchProvider.saveUserFilter(List<Parameter> parameters, String SavedFilterName, String userId)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
	
	}

	/**
	 * This method is used to check whether template with same name for provided
	 * user id is present or not.
	 * 
	 * @param templateName
	 * @param userId
	 * @return boolean
	 * @throws DuplicateTemplateException
	 * @throws SearchException
	 */
	private void isDuplicateTemplate(String templateName,String searchName, String userId)
			throws DuplicateTemplateException, SearchException {
		try {
			String templatePath = getTemplatePath(searchName,userId);
			File file = new File(templatePath + File.separator + templateName + ".json");
			if (file.exists())
				throw new DuplicateTemplateException(
						"\n Template with same name already exist \n Template Name = " + templateName);
		} catch (DuplicateTemplateException e) {
			throw new DuplicateTemplateException(e.getMessage(), e.getCause());
		} catch (Exception e) {
			log(e, "Message: Exception while creating template \n GraphSearchProvider.isDuplicateTemplate(String templateName,String userId)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * @author kpramod
	 * @return List<Parameter>
	 * @throws SearchException
	 * @purpose This method is used to get parameters from template which is
	 *          saved by user as default criteria parameter
	 */
	public List<Map<String, Object>> getTemplateDetails(String savedFilterName,String searchName, String userId) throws SearchException {
		List<Map<String, Object>> listOfSavedParameters = null;
		try {
			String templatePath = getTemplatePath(searchName,userId);

			String data = new String(
					Files.readAllBytes(Paths.get(templatePath + File.separator + savedFilterName + ".json")));
			listOfSavedParameters = getListOfParameters(data);
			/*
			 * Map<String, Object> criteria = listOfSavedParameters.stream()
			 * .collect(Collectors.toMap(Parameter::getKey,
			 * Parameter::getValues));
			 */

			/*
			 * Need to discuss if(criteria.containsKey("")){
			 * listOfSavedParameters = (List<Parameter>) criteria.get(""); }
			 */

		} catch (Exception e) {
			log(e, "Message: Exception while creating template \n GraphSearchProvider.isDuplicateTemplate(String templateName,String userId)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return listOfSavedParameters;
	}

	/**
	 * This method is used to get ListOfParameters
	 * 
	 * @param data
	 * @return List<Parameter>
	 * @throws SearchException
	 */
	private List<Map<String, Object>> getListOfParameters(String data) throws SearchException {
		List<Map<String, Object>> listOfCriteria = null;
		try {
			JsonObject jsonObject = (new JsonParser()).parse(data).getAsJsonObject();
			// jsonObject = jsonObject.getAsJsonObject("criteria");
			JsonArray jsonArray = null;
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				jsonArray = jsonObject.getAsJsonArray(entry.getKey());
				break;
			}
			listOfCriteria = new ArrayList<>();
			for (JsonElement jsonElement : jsonArray) {
				JsonObject jsonObject1 = jsonElement.getAsJsonObject();
				if (!jsonObject1.isJsonNull() && jsonObject1 != null && jsonObject1.toString().length() > 0) {
					Map<String, Object> map = new HashMap<>();
					String displayType = jsonObject1.get("displaytype").toString();
					displayType = displayType.replaceAll("\\\"", "");
					if (displayType.equalsIgnoreCase("DateRange")) {
						if (jsonObject1.has("fromDate")) {
							String strDate = jsonObject1.get("fromDate").toString();
							strDate = strDate.replaceAll("\"", "");
							// Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
							map.put("fromDate", strDate);
						}
						if (jsonObject1.has("toDate")) {
							String strDate = jsonObject1.get("toDate").toString();
							strDate = strDate.replaceAll("\"", "");
							// Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
							map.put("toDate", strDate);
						}
					} else {
						JsonArray jasonValues = jsonObject1.get("values").getAsJsonArray();
						String[] values = new String[jasonValues.size()];
						int i = 0;
						for (JsonElement jsonElement2 : jasonValues) {
							String value =  jsonElement2.getAsString().replaceAll("\\\"", "");
							values[i] = value;
							i++;
						}
						map.put("values", values);
					}
					String key = jsonObject1.get("key").toString();
					map.put("key", key.replaceAll("\\\"", ""));
					map.put("displaytype", displayType);
					String id = jsonObject1.get("id").toString();
					map.put("id", id.replaceAll("\\\"", ""));
					listOfCriteria.add(map);
				}
			}
			// list = new Gson().fromJson(jsonArray, new
			// TypeToken<ArrayList<Parameter>>() {
			// }.getType());
			return listOfCriteria;
		} catch (Exception e) {
			throw new SearchException(
					"\n Unable to parse json \n GraphSearchProvider.getListOfParameters(JSONObject jsonObject) \n Reason : "
							+ e.getMessage());
		}
	}

	/**
	 * @author kpramod
	 * @param String
	 *            SavedFilterName
	 * @param String
	 *            userId
	 * @return List<Parameter>
	 * @throws SearchException
	 * @purpose This method is used to get template list by user
	 */
	public List<String> getTemplateList(String searchName,String userId) throws SearchException {
		List<String> list = new ArrayList<>();
		try {
			String path = getTemplatePath(searchName,userId);

			File file = new File(path);
			File[] listOfFiles = file.listFiles();
			for (File file2 : listOfFiles) {
				list.add(file2.getName().substring(0, file2.getName().lastIndexOf(".")));
			}
		} catch (Exception e) {
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return list;
	}

	/**
	 * This method is usede to get Template path
	 * 
	 * @return String
	 * @throws SearchException
	 */
	private String getTemplatePath(String searchName,String userId) throws SearchException {
		try {
			
			String templatePath = "D:\\template";
			if (templatePath == null || templatePath.length() <= 0)
				throw new SearchException("\n path to save criteria template not found");
			return templatePath + File.separator + "Criteria_Template" + File.separator + userId +  File.separator + searchName;
		} catch (Exception e) {
			throw new SearchException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * This method is used to return EnumBrixCoreEntity reference
	 * 
	 * @param name
	 * @return EnumBrixCoreEntity
	 */
	private ENUM getEnumValue(String name) {
		for (ENUM b : ENUM.values()) {
			if (b.getEnumVal().equalsIgnoreCase(name)) {
				return b;
			}
		}
		for (ENUM b : ENUM.values()) {
			if (b.getId().equalsIgnoreCase(name)) {
				return b;
			}
		}
		return null;
	}

	/**
	 * This method is used to return EnumBrixCoreEntity reference
	 * 
	 * @param name
	 * @return EnumBrixCoreEntity
	 */
	private String getFilter(String name) {
		for (Filter_Type b : Filter_Type.values()) {
			if (b.getEnumVal().equalsIgnoreCase(name)) {
				return b.getEnumVal();
			}
		}
		for (Filter_Type b : Filter_Type.values()) {
			if (b.getId().equalsIgnoreCase(name)) {
				return b.getEnumVal();
			}
		}
		return null;
	}

	/**
	 * This method is used to record log when exception occurs.
	 * 
	 * @param e
	 * @param msg
	 */
	private void log(Exception e, String msg) {
		String message = "Method: " + Thread.currentThread().getStackTrace()[1].getClassName() + "."
				+ Thread.currentThread().getStackTrace()[1].getMethodName() + ":"
				+ Thread.currentThread().getStackTrace()[1].getLineNumber();
		
		logger.error(message);
	}

	/**
	 * @author 	kpramod
	 * @param	String searchName
	 * @return	List<DataTableColumn>
	 * @throws 	SearchException
	 * @purpose	This method is used to get list of column names for data table.
	 */
	public List<DataTableColumn> getListForDataTableColumnName(String searchName, String key) throws SearchException {
		List<DataTableColumn> dataTableColumns = new ArrayList<>();
		try {
			Result result = configReaderService.readTableResultConfigurations(searchName, key);
			if (result != null) {
				List<Parameter> list = result.getParameter();
				for (Parameter parameter : list) {
					DataTableColumn column = new DataTableColumn();
					column.setId(parameter.getKey());
					dataTableColumns.add(column);
				}
			}
		} catch (Exception e) {
			log(e, "Message: Exception while getting column names for data table \n "
					+ "GraphSearchProvider.getListForDataTableColumnName(String searchName)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return dataTableColumns;
	}

	@Override
	public void setRelationInfo(Relations rel_info) {
	    relationinfo = rel_info;
	    
	}
	
	/**
	 * This method is used to get Measurements related to provided tag number.
	 * 
	 * @param tagNo
	 * @param string
	 * @return List<Map<String, String>>
	 * @throws GraphSearchException
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String ,List<?>> getMeasurements(String tagNo, String authKey) throws GraphSearchException {

		Map<String ,List<?>> hashMap = new HashMap<>();
		List<String> lstStr = new ArrayList<>();
		List<Measurement> list = new ArrayList<>();
		logger.info("Getting Meaurements related to tag number ::" + tagNo);
		try {
			Measurement measurement = null;
			int count = 0;

			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;

			GraphTraversal<Vertex, Map<String, Object>> travl = gcontex.getGraph().traversal().V(tagNo).valueMap();
			
			String functionGrpIdfromDB ="" ;
			if(travl.hasNext()){
			  	Map<String, Object> values = travl.next() ;
			  	functionGrpIdfromDB = (String) values.get("webServiceID").toString();
			  	if(functionGrpIdfromDB.startsWith("[") && functionGrpIdfromDB.endsWith("]"))
			  		functionGrpIdfromDB = functionGrpIdfromDB.substring(1, functionGrpIdfromDB.length() -1);
			}
			
			if(functionGrpIdfromDB == null || "".equals(functionGrpIdfromDB)){
	    	    logger.info("No functional group Id found in Graph DB for tagNo ::::: " + tagNo);
	    	    throw new GraphSearchException("No functional group Id found in Graph DB for tagNo :::::" + tagNo);
	    	}
			
			boolean isWebServiceRquired = getIsRequired(functionGrpIdfromDB);
			if(isWebServiceRquired){
				GraphTraversal<Vertex, Vertex> gTraversal = gcontex.getGraph().traversal().V(tagNo)
						.out(getRelation(TAGNO, MEASUREMENT));
				String webServiceURL = getWebServiceUrl(functionGrpIdfromDB);
				String authString = getWebServiceUserName(functionGrpIdfromDB) + ":" + getWebServicePassword(functionGrpIdfromDB);
				String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
				lstStr.add(webServiceURL);
				lstStr.add(authStringEnc);
				while (gTraversal.hasNext()) {
					count++;
					Vertex vertex = gTraversal.next();
					measurement = new Measurement();
					// vertex.properties(ID).next().value().toString()
					measurement.setId(String.valueOf(count));
					measurement.setValue(vertex.properties("name").next().value().toString());
					measurement.setInternalId(vertex.properties(MEASUREMENTKEY).next().value().toString());
					// added as per requirement for new canvasJS 5/3/019
					measurement.setNodeId(vertex.properties(MEASUREMENTKEY).next().value().toString());
					measurement.setNodeType("MeaResult");
					JsonObject jObject = restClient(webServiceURL + "measurements/measurementkey/",
							vertex.properties(MEASUREMENTKEY).next().value().toString() + "/channels", "GET", null, authKey,
							authStringEnc);

					ResultRO<List<Map<String, Object>>> resultRO = new Gson().fromJson(jObject, ResultRO.class);
					List<Map<String, Object>> localColumns = resultRO.getData();
					int subcount = 0;
					List<LocalColumn> localColumnList = new ArrayList<>();
					if (localColumns != null) {
						for (Map<String, Object> map : localColumns) {
							subcount++;
							LocalColumn localColumn = new LocalColumn();
							localColumn.setMeaId(vertex.properties(MEASUREMENTKEY).next().value().toString());
							localColumn.setId(count + "." + subcount);
							localColumn.setActualId(map.get("channelkey").toString());
							localColumn.setValue(map.get("name").toString());
							localColumn.setUnit(map.get("unit").toString());
							localColumn.setNodeId(map.get("channelkey").toString());
							// added as per requirement for new canvasJS 5/3/019
							localColumn.setNodeType("MeaQuantity");
							localColumnList.add(localColumn);
						}
					}
					measurement.setData(localColumnList);

					list.add(measurement);
				}
			}else{
				logger.info("Web Service is configured as not required for functional group::::: " +functionGrpIdfromDB);
	    	    throw new GraphSearchException("Web Service is configured as not required for functional group:::::" +functionGrpIdfromDB);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in convertIntoTable method, Error occured while creating connection with database.");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
		hashMap.put("Measurement", list);
		hashMap.put("URL" ,lstStr);
		return hashMap;
	}	

	/**
	 * This method is used to call Rest web service.
	 * 
	 * @param baseUrl
	 * @param urlPath
	 * @param methodType
	 * @param json
	 * @param authKey
	 * @return JsonObject
	 * @throws GraphSearchException
	 */
	public JsonObject restClient(String baseUrl, String urlPath, String methodType, String json,
			String authKey,String authStringEnc) throws GraphSearchException
	{
		String output = "";
		try {

			String finalURL = baseUrl + urlPath ;

			HttpRequest request = new HttpRequest(finalURL, "GET");
			output = request.addHeader("authorization", "Basic "+ authStringEnc).getResponseAsString();
			request.close();

			URL url = new URL(baseUrl + urlPath); // ("http://localhost:8080/RESTfulExample/rest/type1");
			logger.info("Authentication Key ::" );
			logger.info("Web service URL :: " + url);

			if (output != null && output.length() > 1) {
				JsonParser parser = new JsonParser();
				return (JsonObject) parser.parse(output);
			} else {
				throw new ConnectionException("Error occured while creating connection with server.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(
					"Exception in convertIntoTable method, Error occured while creating connection with database.");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * This method is used to call authentication web service.
	 * 
	 * @param baseUrl
	 * @param urlFront
	 * @param username
	 * @param password
	 * @return String
	 * @throws GraphSearchException
	 */
	public String authenticate(String baseUrl, String urlFront, String username, String password)
			throws GraphSearchException
	{
		String str = null;
		try {
		    
			String url = baseUrl + "authentication";
			String authKey = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
			logger.info(url);
			HttpRequest request = new HttpRequest(url, "GET");
			request.addHeader("authorization", authKey).getResponseAsString();
			
			str = request.getHeader("accessToken");
			request.close();
			
		} catch (Exception e) {
		    logger.error(e);
		    logger.error(
			    "Exception in convertIntoTable method, Error occured while creating connection with database.");
		    throw new GraphSearchException(e.getMessage(), e.getCause());
		}
		return str;
	    }


	/**
	 * This method is used to get Authentication key.
	 * 
	 * @return String
	 * @throws GraphSearchException
	 */
	/*public String getAuthenticateWebService() throws GraphSearchException
	{
		String authKey = null;
		try
		{
			authKey = authenticate(mconfig.getWebServiceURL(), "authentication",
					mconfig.getWebServiceUsername(), mconfig.getWebServicePassword());
			logger.info("Authentication Key for web serivice ::" + authKey);
		}
		catch (GraphSearchException e)
		{
			logger.error(e);
			logger.error("Exception in getAuthenticateWebService method, Error occured while Getting authentication key from web service");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
		return authKey;
	}*/
	
	
	
	@Override
	public List<Map<String, Object>> getMeasurmentInfo(String meaId)
	{
			try
			{
				@SuppressWarnings("unused")
				int count = 0;
				
				WebSession session = currentSession();
				WebAppSessionContext contex = session.getSessionContext();
				GraphSessionContext gcontex = (GraphSessionContext) contex;
				
				GraphTraversal<Vertex, Vertex> gTraversal = gcontex.getGraph().traversal().V().hasLabel(MEASUREMENT).has(MEASUREMENTKEY, meaId);
				while (gTraversal.hasNext())
				{
					count++;
					
					Vertex vertex = gTraversal.next();
					 Iterator<VertexProperty<Object>> itr = vertex.properties();
					 List<Map<String,Object>> list = new ArrayList<>();
					 int srNo = 0;
					 while(itr.hasNext())
					 {
						 VertexProperty<Object> prop = itr.next();
						 String key = prop.key();
						 String value = prop.value().toString();
						 if(!key.equalsIgnoreCase("createdDate") && !key.equalsIgnoreCase(MEASUREMENTKEY) &&
								 !key.equalsIgnoreCase("webServiceID") && !key.equalsIgnoreCase("updatedDate")){
							 Map<String,Object> map = new HashMap<>();
							 map.put("Key", key);
							 map.put("Value", value);
							 map.put("Sr.no.", ++srNo);
							 list.add(map);
						 }
					 }
					 return list;
					
				}
			}
			catch (Exception e)
			{
				logger.error(e);
				logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
			}
			return null;
	}

	
    /**
	 * This method is used to get relation between two elements.
	 * 
	 * @param from
	 * @param to
	 * @return String
	 * @throws GraphSearchException
	 */
	private String getRelation(String from, String to) throws GraphSearchException
	{
		try
		{
			for (com.pvmsys.brix.graph.xml.relation.beans.Relation relation : relationinfo.getRelation())
			{
				if (relation.getFrom().equals(from) && relation.getTo().equals(to))
					return relation.getName();
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error(
					"Exception in getRelation method, Error occured while creating connection with database.");
			throw new GraphSearchException(e.getMessage(), e.getCause());
		}
	}

	
	
	public WebSession currentSession() {
	      return mSessionStorage.get();
	 }
	
	@Override
	public WebSession Login(String username,String password) throws  ConnectionException {
		String ORIENT_SERVER_URL = mconfig.getOrientServerUrl();
		WebSession websession = new WebSession();
		@SuppressWarnings("resource")
		OrientGraphFactory	fac = new OrientGraphFactory(ORIENT_SERVER_URL, username, password);
		fac.setLabelAsClassName(true);
		Graph graph = fac.getNoTx();
		WebAppSessionContext aocontex = new SessionContextProvider(graph);
		websession.setSessionContext(aocontex);
		return websession;
	 }
	
	
	private class SessionContextProvider implements GraphSessionContext
	{
		
		Graph mgraphsession =null ; 
		 public SessionContextProvider(Graph graphsession){
			 mgraphsession = graphsession;
		 }
		 
		 protected final WebSession preExecute(WebSession session) {
			  WebSession original = mSessionStorage.get();
		      mSessionStorage.set(session);
		      return original;
		 }
	    protected final void postExecute(WebSession session, WebSession original) {
		      mSessionStorage.set(original);
		     
		 }

		@Override
		public <T> T execute(WebSession session, Callable<T> command)
				throws WebAoException {
			WebSession original = preExecute(session);
		      try {
		         return command.call();
		      } catch (Exception ao) {
		            throw new WebAoException();
		      }  finally {
		         postExecute(session, original);
		      }
		}

		@Override
		public Graph getGraph() {
			return mgraphsession;
		}

		@Override
		public boolean isExpired() {
			 assert mgraphsession!= null;
		      try {
		    	mgraphsession.traversal().V().hasLabel("Dummy");
		    	 
		    	/*GraphTraversal<Vertex, Vertex>  traversal =   mgraphsession.traversal().V();
		    	traversal.select("Dummy");*/
//		    	  mgraphsession.features();
		         return false;
		      } catch (Throwable e) {
		         // any exception indicates session invalidity
		         return true;
		      }
		}
		
		   @Override public boolean logout()  {
			      if (mgraphsession != null) {
			         try {
			        	 mgraphsession.close();
			        	 mgraphsession = null ;
			        	 return true;
			         } catch (Exception e) {
			        	 return false;
			         }
			         
			      }
			      return false;
			   }
		 

	}

	/**
	 * @param lstElement
	 * @return boolean
	 */
	@Override
	public boolean importProcess(List<ElementBean> lstElement, List<RelationBean> lstRelation) throws GraphImportException {
		logger.info("<------------------------------- Executing method :: importProcess. -------------------------------------->");
		if (lstElement == null) {
			logger.error("No mapping for elements found.");
			return false;
		}
 		int taskCount = 0;
 		boolean isFunctionExist = false;
 		List<Vertex> lstProjVertex = new ArrayList<>();
		Map<String,Vertex> mapIdToVertex = new HashMap<>();
		try {
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			Helper orient = new Helper(gcontex.getGraph());
			logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% task " + taskCount + " started  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			for (ElementBean element : lstElement) {
				Vertex vertex = null;
				if(primaryAttributes.containsKey(element.getGroup())){

					String primaryAttrValue = null;
					for(ElemParameter elemParameter : element.getParameter()){
						//check whether primary attribute for vertex is present or not. 
						if(elemParameter.getPropertyKey().equals(primaryAttributes.get(element.getGroup()))){
							primaryAttrValue = elemParameter.getPropertyValue().toString();
							break;
						}
					}
					if(primaryAttrValue != null)
						vertex = orient.addVertex(element.getGroup(), primaryAttributes.get(element.getGroup()), primaryAttrValue.trim());
					else
						vertex = orient.addVertex(element.getGroup(), primaryAttributes.get(element.getGroup()), element.getLabel().trim());
				}else
					vertex = orient.addVertex(element.getGroup(), "Id",element.getLabel().trim());
				
				if(vertex != null){
					orient.updateVertex(vertex, element.getParameter());
					// Add property
					Date currentDate = new Date();
					if(!vertex.property("CreatedDate").isPresent()){
						vertex.property("CreatedDate", currentDate);
					}else
						vertex.property("UpdatedDate", currentDate);
					
					mapIdToVertex.put(element.getId(), vertex);
					
					taskCount++;
					logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Element and property task " + taskCount + " Finished  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				}
				if(element.getGroup().equalsIgnoreCase(FUNCTIONALGROUP))
					isFunctionExist = true;
				if(element.getGroup().equalsIgnoreCase(PROJECT))
					lstProjVertex.add(vertex);
			}
			taskCount = 0;
			// Add relation/edge
			for (RelationBean relation : lstRelation) {                                                              
				Vertex inverseVertex = mapIdToVertex.get(relation.getTo());
				Vertex vertex = mapIdToVertex.get(relation.getFrom());
				String relName = "unknown";
				if(relation.getLabel() != null)
					relName = relation.getLabel();
				Edge newEdge = orient.addEdge(vertex, inverseVertex, relName);
				List<RelationParameter> relationParameters = relation.getParameter();
				if(newEdge != null && relationParameters != null 
						&& !relationParameters.isEmpty()){
					relationParameters.forEach( param -> newEdge.property(param.getKey(), param.getValue()));
				}
				taskCount++;
				logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% element relation task " + taskCount + " Finished  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			}
			if(!isFunctionExist){
				Vertex functionVertex = orient.addVertex(FUNCTIONALGROUP, "roleFunction1", "UNKNOWN");
				for (Vertex vertex : lstProjVertex) {
					orient.addEdge(functionVertex, vertex, "ProjectInfo");
				}
			}
				
		} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
				throw new GraphImportException(e.getMessage(), e.getCause());
			}
		logger.info("<------------------------------- Execution Completed :: execute ------------------------------->");
		return true;
	}

	

	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String childvertex,Map<String, String> condtion,String KeyName){
		try
		{
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gTraversal = gcontex.getGraph().traversal().V().hasLabel(ParentLabel) ;
			if(Parentcondtion.containsKey("id")){
				   String id = Parentcondtion.get("id");
				   gTraversal = gcontex.getGraph().traversal().V(id).hasLabel(ParentLabel) ;
			}else{
				gTraversal = gcontex.getGraph().traversal().V().hasLabel(ParentLabel) ;
			}
			
				final GraphTraversal<Vertex, Vertex> sTraversal = gTraversal ;
				Parentcondtion.entrySet().forEach(entry -> {
					if(!entry.getKey().equalsIgnoreCase("id"))
					sTraversal.has(entry.getKey(),entry.getValue());
				});
						
				if(relation.equalsIgnoreCase("out"))
					sTraversal.out().hasLabel(childvertex);
				else
					sTraversal.in().hasLabel(childvertex);
				
				condtion.entrySet().forEach(entry -> {
					if(!entry.getKey().equalsIgnoreCase("id"))
					  sTraversal.has(entry.getKey(),entry.getValue());
				});
				
			
			
			List<Map<String,Object>> list =getMapDeatils(sTraversal,KeyName);
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String label,String KeyName){
		try
		{
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gTraversal = gcontex.getGraph().traversal().V().hasLabel(ParentLabel) ;
			if(Parentcondtion.containsKey("id")){
				   String id = Parentcondtion.get("id");
				   gTraversal = gcontex.getGraph().traversal().V(id).hasLabel(ParentLabel) ;
			}else{
				gTraversal = gcontex.getGraph().traversal().V().hasLabel(ParentLabel) ;
			}
			
				final GraphTraversal<Vertex, Vertex> sTraversal = gTraversal ;
				Parentcondtion.entrySet().forEach(entry -> {
					if(!entry.getKey().equalsIgnoreCase("id"))
					sTraversal.has(entry.getKey(),entry.getValue());
				});
						
				if(relation.equalsIgnoreCase("out"))
					gTraversal.out().hasLabel(label);
				else
					gTraversal.in().hasLabel(label);
			
			
			List<Map<String,Object>> list =getMapDeatils(gTraversal,KeyName);
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String KeyName) {
		try
		{
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gTraversal = null;
			if(Parentcondtion.containsKey("id")){
			   String id = Parentcondtion.get("id");
			   gTraversal = gcontex.getGraph().traversal().V(id).hasLabel(ParentLabel) ;
			}else{
				gTraversal = gcontex.getGraph().traversal().V().hasLabel(ParentLabel) ;
			}
			final GraphTraversal<Vertex, Vertex> sTraversal = gTraversal ;
			Parentcondtion.entrySet().forEach(entry -> {
				if(!entry.getKey().equalsIgnoreCase("id"))
				sTraversal.has(entry.getKey(),entry.getValue());
			});
					
			if(relation.equalsIgnoreCase("out"))
			    gTraversal.out();
			else
				 gTraversal.in();
			
			List<Map<String,Object>> list =getMapDeatils(gTraversal,KeyName);
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getVertiexDetails(String Label , Map<String, String> condtion,String KeyName)
	{
		try
		{
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gTraversal = null;
			if(condtion.containsKey("id")){
				   String id = condtion.get("id");
				   gTraversal = gcontex.getGraph().traversal().V(id).hasLabel(Label) ;
				}else{
					gTraversal = gcontex.getGraph().traversal().V().hasLabel(Label) ;
				}
			
			final GraphTraversal<Vertex, Vertex> sTraversal = gTraversal ;
			condtion.entrySet().forEach(entry -> {
				if(!entry.getKey().equalsIgnoreCase("id"))
					sTraversal.has(entry.getKey(),entry.getValue());
			});
			List<Map<String,Object>> list =getMapDeatils(sTraversal,KeyName);
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getVertiexDetails(String Label,String KeyName)
	{
		try
		{
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gTraversal = gcontex.getGraph().traversal().V().hasLabel(Label);
			List<Map<String,Object>> list =getMapDeatils(gTraversal,KeyName);
				
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			logger.error("Exception in getLocalColumnData method, Error occured while reading channel data.");
		}
		return null;
	}
	
	/**
	 * This method is used to get result for given parameter.
	 * 
	 * @param filterName
	 * @param parameterName
	 * @return List<Object>
	 * @throws SearchException
	 */
	public List<Map<String, Object>> search( Map<String, List<Parameter>> conditionMap,Map<String, List<Parameter>> selectparameter  ) throws SearchException {
		List<Map<String, Object>>  result = new ArrayList<>();
		try {
			
			WebSession session = currentSession();
			WebAppSessionContext contex = session.getSessionContext();
			GraphSessionContext gcontex = (GraphSessionContext) contex;
			GraphTraversal<Vertex, Vertex> gr = gcontex.getGraph().traversal().V();
			
			GraphTraversal<Vertex, Vertex> travarsal = null;
			List<String> resultelement = new ArrayList<>() ;
			GraphTraversal<Vertex, Map<String, Object>> gremlinresult = null;
			
			Set<String> setelememtRel = conditionMap.keySet();
			List<String> lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String value = (String) iterator.next();
				lstelememtRel.add(value);
			}
			
			setelememtRel = selectparameter.keySet();
			 lstelememtRel  = new ArrayList<>();
			for (Iterator<String> iterator = setelememtRel.iterator(); iterator.hasNext();)
			{
				String element = (String) iterator.next();
				if(!resultelement.contains(element))
					resultelement.add(element);
				
				if(!lstelememtRel.contains(element))
					lstelememtRel.add(element);
			}
			
			
			
			List<String> allelement = getElementList();
			String fromelement = "" ;
			
			for (Iterator<String> iterator = allelement.iterator(); iterator.hasNext();)
			{
				String eletorel = (String) iterator.next();
				if(lstelememtRel.contains(eletorel) ){
					if(travarsal == null){
						travarsal = gr.hasLabel(eletorel).as(eletorel) ;
						fromelement = eletorel ;
					}else {
						List<Relation> lstrel = getFromRelatedElementList(eletorel);
						if(lstrel!=null && lstrel.size() > 0){
							Relation relation = lstrel.get(0) ;
							fromelement =relation.getFrom();
							if(relation.getReltype().equalsIgnoreCase("out"))
					            travarsal.select(fromelement).out(relation.getName()).as(eletorel);
							else
								travarsal.select(fromelement).in(relation.getName()).as(eletorel);
						}else{
							fromelement = "" ;
						}
					}
				}
				
			}
			
			
			
		    Set<String> busskeys = conditionMap.keySet();
			
			
			for (Iterator<String> iterator = busskeys.iterator(); iterator.hasNext();){
				String keyname = (String) iterator.next();
					List<Parameter> subcond = conditionMap.get(keyname) ;
					for (Parameter parameter : subcond) {
							travarsal =travarsal.select(parameter.getBusinessobject()) ;
							travarsal =	getConditions_Value(parameter, travarsal);
						
					}
				
			}
			String first = resultelement.remove(0);
			String second = resultelement.remove(0);
			String remains[] = resultelement.toArray(new String[0]);
			if( remains.length > 0 ){
				gremlinresult =travarsal.select(first, second, remains) ;
			}
			else{
				gremlinresult =travarsal.select(first, second);
			}
			result = getResultforTable(gremlinresult,conditionMap);
			
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getResultforTable(String query)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private List<Map<String,Object>> getMapDeatils (GraphTraversal<Vertex, Vertex> gTraversal,String keyName){
		 List<Map<String,Object>> list = new ArrayList<>();
			while (gTraversal.hasNext())
			{
				Vertex vertex = gTraversal.next();
				 Iterator<VertexProperty<Object>> itr = vertex.properties();
				 Map<String,Object> map = new HashMap<>();
				 map.put(keyName, vertex.id().toString());
				 map.put("label", vertex.label());
				 while(itr.hasNext())
				 {
					 VertexProperty<Object> prop = itr.next();
					 String key = prop.key();
					 String value = prop.value().toString();
					 map.put(key, value);
				 }
				 list.add(map);
			}
		return list;
	}
	
	
	/**
	 * Method will return details of vertex (all its attributes) by provided vertex rid
	 * @author kpramod
	 * @param vertexRid
	 * @return Map<String,String>
	 * @throws SearchException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Map> getVertexDetailsToDisplay(String vertexRid) throws SearchException {
		LinkedHashMap<String, Object> vertexData = new LinkedHashMap<>();
		LinkedHashMap<String, Map<String, Object>> edges = new LinkedHashMap<>();
		Map<String, Map> vDataNEdges = new HashMap<>();
		try{
			WebSession session = currentSession();
			WebAppSessionContext appSessionContext = session.getSessionContext();
			GraphSessionContext graphContex = (GraphSessionContext) appSessionContext;
			Vertex vertex = graphContex.getGraph().vertices(vertexRid).next();
			Set<String> keys = vertex.keys();
			for(String propertyKey : keys){
				if(propertyKey.equals("selfURL") || propertyKey.equals("URL")){
					String url = "<a href='"+vertex.property(propertyKey).value().toString()+"' target='_blank'>"+vertex.property(propertyKey).value().toString()+"</a>";
					vertexData.put("URL", url);
				}else{
				vertexData.put(propertyKey, vertex.property(propertyKey).value());
			/*	if(propertyKey.equalsIgnoreCase("updateddate")){
					Date date = (Date) vertex.property(propertyKey).value();
					date.getTimezoneOffset();
					DateFormat format = new SimpleDateFormat("HH:mm");
					format.setTimeZone(TimeZone.getTimeZone("UTC+3"));
					String time = format.format(yourDate);
					System.out.println(date.getTime());
				}*/
				}
			}
//			vertexData.put("group", vertex.label());
			/*List<String> rels = new ArrayList<>();
			relationinfo.getRelation().forEach(p -> rels.add(p.getName()));
			for(String rel : rels){
				Iterator<Edge> inEdgesItr = vertex.edges(Direction.IN, rel);
				while (inEdgesItr.hasNext()) {
					LinkedHashMap<String, Object> edgeData = new LinkedHashMap<>();
					Edge edge = (Edge) inEdgesItr.next();
					edgeData.put("label", edge.label());
					edgeData.put("id", edge.id().toString());
					edgeData.put("direction", "fa fa-arrow-left");
					edgeData.put("inVertex", edge.inVertex().id().toString());
					edgeData.put("inVertexLabel", getEdgeInOutVertexName(edge.inVertex()));
					edgeData.put("outVertex", edge.outVertex().id().toString());
					edgeData.put("outVertexLabel", getEdgeInOutVertexName(edge.outVertex()));
					edges.put(edge.id().toString(), edgeData);
				}
				
				Iterator<Edge> outEdgesItr = vertex.edges(Direction.OUT, rel);
				while (outEdgesItr.hasNext()) {
					LinkedHashMap<String, Object> edgeData = new LinkedHashMap<>();
					Edge edge = (Edge) outEdgesItr.next();
					edgeData.put("label", edge.label());
					edgeData.put("id", edge.id().toString()); 
					edgeData.put("direction", "fa fa-arrow-right");
					edgeData.put("inVertex", edge.inVertex().id().toString());
					edgeData.put("inVertexLabel", getEdgeInOutVertexName(edge.inVertex()));
					edgeData.put("outVertex", edge.outVertex().id().toString());
					edgeData.put("outVertexLabel", getEdgeInOutVertexName(edge.outVertex()));
					edges.put(edge.id().toString(), edgeData);
				}
			}*/
			vDataNEdges.put("vertexData", vertexData);
			vDataNEdges.put("edges", edges);
			Map<String,String> map = new HashMap<>();               
			map.put("group",vertex.label());          //add them to the hashmap and trim whitespaces
			vDataNEdges.put("group", map);
		}catch(Exception e){
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getVertexDetailsToDisplay(String vertexRid)");
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return vDataNEdges;
	}
	
	@SuppressWarnings("unused")
	private String getEdgeInOutVertexName(Vertex inOutVertex){
		String val = "-";
		String primaryAttribute = primaryAttributes.get(inOutVertex.label());
		try {
			val = inOutVertex.value(primaryAttribute);
		} catch (Exception e) {
			logger.info("Label:"+inOutVertex.label()+" Value:"+primaryAttribute);
			log(e, "Message: No value present for property :"+primaryAttribute+" for vertex :"+inOutVertex.label());
		}
		return val;
		
		/*switch (inOutVertex.label()) {
		case PLATFORMDESIGN:
			return inOutVertex.value("LevelName");
		case PROJECT:
			return inOutVertex.value("ProjectCode");
		case ENGINESUBSYSTEM:
			return inOutVertex.value("LevelName");
		case PROTOTYPE:
			return inOutVertex.value("LevelName");
		case L3_L4_COMPONENT:
			return inOutVertex.value("LevelName");
		default:
			return inOutVertex.value("Name");
		}*/
	}

	@Override
	public List<String> getGroupElements() {
		List<String> lstElements = new ArrayList<>();
		for (Element element : elements.getElement()) {
			lstElements.add(element.getName());
		}
		return lstElements;
	}
	
	@Override
	public String getPrimaryAttribute(String groupName) {
		if(primaryAttributes.containsKey(groupName))
			return primaryAttributes.get(groupName);
		return null;
	}

	private List<ElementItem> getElementParameters(String group) {

		for (Element element : elements.getElement()) {
				if(element.getName().equals(group)){
					return element.getElementItem();
				}
		}
		return null;
	}
	
	/**
	 * Method will return tagNum vertices out edges and relation to draw on double click of tagNo vertex
	 * takes vertex rid as parameter and return result based on the same
	 *  
	 * @author kpramod
	 * @param vertexRid
	 * @return Map<String,String>
	 * @throws SearchException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Map> getOutVertexAndEdgesForTagNum(String vertexRid) throws SearchException {
		HashMap<String, Map> outVerticesOfTag = new HashMap<>();
		LinkedHashMap<String, Map<String, Object>> edges = new LinkedHashMap<>();
		Map<String, Map> tagNoOutVerticesAndEdges = new HashMap<>();
		try{
				WebSession session = currentSession();
				WebAppSessionContext appSessionContext = session.getSessionContext();
				GraphSessionContext graphContex = (GraphSessionContext) appSessionContext;
				Vertex vertex = graphContex.getGraph().vertices(vertexRid).next();
				//List<String> rels = new ArrayList<>();
				
				//relationinfo.getRelation().forEach(p -> rels.add(p.getName()));
				
				//Iterator<Vertex> outVertices = vertex.vertices(Direction.OUT);
				Iterator<Edge> inEdges = vertex.edges(Direction.OUT);
				
				while (inEdges.hasNext()) {
					
					LinkedHashMap<String, Object> edgeData = new LinkedHashMap<>();
					Edge edge = (Edge) inEdges.next();
					edgeData.put("label", edge.label());
					edgeData.put("id", edge.id().toString()); 
					edgeData.put("inVertex", edge.inVertex().id().toString());
					edgeData.put("outVertex", edge.outVertex().id().toString());
					edgeData.put("arrows", "to");
					edges.put(edge.id().toString(), edgeData);
					
					Vertex  vertextarget = edge.inVertex() ;
					String vertexlabel = vertextarget.label() ;
					String  propertyKey = primaryAttributes.get(vertexlabel) ;
					
					String valueprimery = "" ;
					if(vertextarget.property(propertyKey)!=null){
						try{
						valueprimery =(String) vertextarget.property(propertyKey).value();
						}catch (Exception e) {
						}
					}
					
					LinkedHashMap<String, Object> vertexData = new LinkedHashMap<>();
					 if(vertexlabel.equalsIgnoreCase("PowertrainSystem")){
							vertexData.put("level", "5");
						}
					 else if(vertexlabel.equalsIgnoreCase("PlatformDesign")){
							vertexData.put("level", "5");
						}
					 else if(vertexlabel.equalsIgnoreCase("EngineSubSystem")){
						vertexData.put("level", "6");
					}
					 else if(vertexlabel.equalsIgnoreCase("EngineModelCode")){
							vertexData.put("level", "7");
						}else  if(vertexlabel.equalsIgnoreCase("Measurement")){
						vertexData.put("level", "8");
					}
					else
						vertexData.put("level", "5");
					 
					vertexData.put("label", valueprimery);
					vertexData.put("group", vertexlabel);
					vertexData.put("id", vertextarget.id().toString());
					vertexData.put("title", "Name : "+valueprimery+" Label : "+vertexlabel);
					outVerticesOfTag.put(edge.inVertex().id().toString(), vertexData);
				}
				
				
				/*while (outVertices.hasNext()) {
					
						Vertex oVertex = (Vertex) outVertices.next();
						
						LinkedHashMap<String, Object> vertexData = new LinkedHashMap<>();
						vertexData.put("label", oVertex.label());
						vertexData.put("group", oVertex.label());
						vertexData.put("id", oVertex.id().toString());
						vertexData.put("title", "Name : "+ oVertex.label()+"<br>Label : "+oVertex.label());
						outVerticesOfTag.put(oVertex.id().toString(), vertexData);
						Iterator<Edge> inEdges = oVertex.edges(Direction.IN);
						
						while (inEdges.hasNext()) {
							
								LinkedHashMap<String, Object> edgeData = new LinkedHashMap<>();
								Edge edge = (Edge) inEdges.next();
								edgeData.put("label", edge.label());
								edgeData.put("id", edge.id().toString()); 
								edgeData.put("inVertex", edge.inVertex().id().toString());
								edgeData.put("outVertex", edge.outVertex().id().toString());
								edgeData.put("arrows", "to");
								edges.put(edge.id().toString(), edgeData);
					}
				}*/
				tagNoOutVerticesAndEdges.put("vertexData", outVerticesOfTag);
				tagNoOutVerticesAndEdges.put("edges", edges);
		}
		catch(Exception e){
			e.printStackTrace();
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getOutVertexAndEdgesForTagNum(String vertexRid)");
			throw new SearchException(e.getMessage(),e.getCause());
		}
		return tagNoOutVerticesAndEdges;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Map> getPreviewdVertexDetailsToDisplay(String vertId) {
		WebSession session = currentSession();
		if(session != null) {
			List<ElementBean> elementBeans = session.getElements();
			LinkedHashMap<String, Object> vertexData = new LinkedHashMap<>();
			Map<String, Map> vDataNEdges = new HashMap<>();
			String group = null ,label = null;
			List<ElemParameter> elemParameters = null;
			try{
			
				for (ElementBean elem : elementBeans) {
					if(String.valueOf(elem.getId()).equals(vertId)){
						/*//add attributes given in xml or rdf file
						for (ElemParameter param : elem.getParameter()) {
							vertexData.put(param.getKey(), param.getValue());
						}*/
						elemParameters =elem.getParameter();
						group = elem.getGroup();
						label = elem.getLabel();
						break;
					}
				}
				
				final String elemGroup = group;
				final String elemLabel = label;
				//add primary attribute and its value
				primaryAttributes.keySet().stream().filter(key -> key.equals(elemGroup))
				.forEach(attr -> vertexData.put(primaryAttributes.get(attr), elemLabel));
				
				//add remaining attributes
				if(elemParameters != null){
					//add attributes given in file
					for (ElemParameter param : elemParameters) {
						vertexData.put(param.getPropertyKey(), param.getPropertyValue());
					}
				}
				//
				vDataNEdges.put("vertexData", vertexData);
				return vDataNEdges;
			}catch(Exception e){
				log(e, "Message: Exception while executing query \n GraphSearchProvider.getPreviewdVertexDetailsToDisplay(String vertexRid)");
			}
		}
		
		return null;
	}
	
	@Override
	public String getDocumentUrl(String id) throws SearchException {
		String url = "";
		try {
			WebSession session = currentSession();
			WebAppSessionContext appSessionContext = session.getSessionContext();
			GraphSessionContext graphContex = (GraphSessionContext) appSessionContext;
			Vertex vertex = graphContex.getGraph().vertices(id).next();
			if(vertex.keys().contains("URL"))
				url = vertex.value("URL");
		} catch (Exception e) {
			log(e, "Message: Exception while executing query \n GraphSearchProvider.getDocumentUrl(String id");
			e.printStackTrace();
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return url;
	}
	
	@Override
	public List<HashMap<String,String>> getResultViewList(String searchName) throws SearchException {
		List<HashMap<String,String>> viewList = new ArrayList<>();
		try {
			List<Result> dummyresult = configReaderService.readResultConfigurations(searchName);
			for (Result result : dummyresult) {
				HashMap<String, String> map = new HashMap<>();
				map.put("type", result.getType());
				map.put("key", result.getKey());
				viewList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchException(e.getMessage(), e.getCause());
		}
		return viewList;
	}

	private boolean getIsRequired(String functionGrpIdfromDB) {
		Iterator<HodsWebService> itr = hodsWebServicesConfigList.iterator();
		while (itr.hasNext()) {
			HodsWebService hodsWebService = (HodsWebService) itr.next();
			if (hodsWebService.getId().equals(functionGrpIdfromDB)) {
				return hodsWebService.getIsImportRequired();
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean getMatchedFunGroupId(String functionGrpIdfromDB) {
		Iterator<HodsWebService> itr = hodsWebServicesConfigList.iterator();
		while (itr.hasNext()) {
			HodsWebService hodsWebService = (HodsWebService) itr.next();
			if (hodsWebService.getId().equals(functionGrpIdfromDB)) {
				return true;
			}
		}
		return false;
	}

	private String getWebServicePassword(String functionGrpIdfromDB) {
		Iterator<HodsWebService> itr = hodsWebServicesConfigList.iterator();
		while (itr.hasNext()) {
			HodsWebService hodsWebService = (HodsWebService) itr.next();
			if (hodsWebService.getId().equals(functionGrpIdfromDB)) {
				return hodsWebService.getPassword().getPassword();
			}
		}
		return "";
	}

	private String getWebServiceUserName(String functionGrpIdfromDB) {
		Iterator<HodsWebService> itr = hodsWebServicesConfigList.iterator();
		while (itr.hasNext()) {
			HodsWebService hodsWebService = (HodsWebService) itr.next();
			if (hodsWebService.getId().equals(functionGrpIdfromDB)) {
				return hodsWebService.getUsername().getUsername();
			}
		}
		return "";
	}

	private String getWebServiceUrl(String functionGrpIdfromDB) {
		Iterator<HodsWebService> itr = hodsWebServicesConfigList.iterator();
		while (itr.hasNext()) {
			HodsWebService hodsWebService = (HodsWebService) itr.next();
			if (hodsWebService.getId().equals(functionGrpIdfromDB)) {
				return hodsWebService.getURL().getBaseUrl();
			}
		}
		return "";
	}
}
