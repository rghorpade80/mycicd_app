package com.pvmsys.brix.graph.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pvmsys.brix.graph.beans.ConfigurationBean;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.RelationBean;
import com.pvmsys.brix.graph.configreader.SearchConfigReaderException;
import com.pvmsys.brix.graph.configreader.beans.DataTableColumn;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.Result;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;
import com.pvmsys.brix.graph.connection.ConnectionException;
import com.pvmsys.brix.graph.websession.WebSession;
import com.pvmsys.brix.graph.xml.relation.beans.Relations;


/**
 * 
 * @author kpramod
 * @since 13/Jan/2019
 */
@Service
public interface GraphSearchService {

	
	static final String FUNCTIONALGROUP = "FunctionalGroup";
	static final String PROJECT = "Project";
	static final String TESTPLAN = "TestPlan";
	static final String ROT = "Rot";
	static final String PROTOTYPE = "Prototype";
	static final String FACILITY = "Facility";
	static final String PLATFORMDESIGN = "PlatformDesign";
	static final String POWERTRAINSYSTEM = "PowertrainSystem";
	static final String ENGINESUBSYSTEM= "EngineSubSystem";
	static final String TRANSMISSSUBSYSTEM= "TransMissSubSystem";
	static final String EDRIVESUBSYSTEM= "EDriveSubSystem";
	static final String IPUSUBSYSTEM= "IPUSubSystem";
	static final String MOTORSUBSYSTEM= "EngineSubSystem";
	static final String AWDSUBSYSTEM= "AWDSubSystem";
	static final String CHARGINGSUBSYSTEM= "ChargingSubSystem";
	static final String L3_L4_COMPONENT= "L3_L4_Component";
	static final String UNITUNDERTEST= "UnitUnderTest";
	static final String TAGNO= "DataName";
	static final String TEST = "TestPlan";
	static final String MEASUREMENT= "Measurement";
	static final String EQUIPMENT= "Equipment";
	static final String SOFTWAREDESIGN= "SoftwareDesign";
	
	
	static final String ID= "InternalId";
	static final String NAME= "Name";
	
	/** Added for HodsStep2.O changes **/
	static final String MEASUREMENTKEY_OLD = "mearesultkey";
	/** HodsStep2.O  it is changed to below above is old**/
	static final String MEASUREMENTKEY = "measurementKey";
    /**
	 * @author 	kpramod
	 * @return	List<String>
	 * @throws 	SearchException
	 * @purpose	This method is used to get all available searches.
	 */
	public List<String> getAllAvailableSearch() throws SearchException,SearchConfigReaderException;
	
	
	/**
	 * @author 	kpramod
	 * @param	filterName
	 * @param	filterType
	 * @return	SearchBean
	 * @throws 	SearchException
	 * @purpose	This method is used to get search parameters information for provided filters
	 */
	public SearchBean getSearchParametersInformation(String filterName,String filterType)throws SearchException;
	
	
	/**
	 * @author 	kpramod
	 * @param	filterName
	 * @param	parameterName
	 * @return	List<Object>
	 * @throws 	SearchException
	 * @purpose	This method is used to get data for provided search parameters
	 */
	public List<Object> getDataForParameter(String filterName,String parameterName)throws SearchException;
	
	/**
	 * @author 	kpramod
	 * @param	searchBean
	 * @param	limit
	 * @return	List<List<Parameter>>
	 * @throws 	SearchException
	 * @purpose	This method is used to search data for provided criteria and return result for table view
	 */
	List<Map<String, Object>>  getResultForTableView(SearchBean searchBean,int limit)throws SearchException;

	/**
	 * @author 	kpramod
	 * @param	searchCriteria
	 * @param	limit
	 * @param	searchName
	 * @return	List<Result>
	 * @throws 	SearchException
	 * @purpose	This method is used to search data for provided criteria and return result for tree view
	 */
	public List<Result> getResultForTreeView(SearchBean searchCriteria,  String treeName,int limit) throws SearchException;
	
	
	/**
	 * @author 	kpramod
	 * @param	searchCriteria
	 * @param	limit
	 * @return	List<List<Result>>
	 * @throws 	SearchException
	 * @purpose	This method is used to search data for provided criteria and return result for tree view
	 */
	public List<List<Result>> getResultForTreeView(SearchBean searchCriteria,int limit) throws SearchException;
	
	/**
	 * @author rohit.walvekar
	 * @param	searchCriteria
	 * @param	limit
	 * @return	List<List<Result>>
	 * @throws 	SearchException
	 * @purpose	This method is used to search data for provided criteria and return result for tree view
	 */
	public List<Map<String, Object>>  getResultForBubbleView(SearchBean searchCriteria, int limit) throws SearchException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @param	resultType
	 * @return	List<HashMap<String,String>>
	 * @throws 	SearchException
	 * @purpose	This method is used to get column configurations
	 */

	public List<HashMap<String,String>> getColumnConfigurations(String searchName,String searchKey)throws SearchException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @param	searchType
	 * @return	Map<String, Object>
	 * @throws 	SearchException
	 * @purpose	This method is used to get link and check box configuration
	 */

	public Map<String, Object> isCheckBoxReqAndIsLinkRequired(String searchName, String searchKey) throws SearchException;


	/**
	 * @author 	kpramod
	 * @param	filterName
	 * @return	List<String>
	 * @throws 	SearchException
	 * @purpose	This method is used to get tree name list.
	 */
	public List<String> getTreeNameList(String filterName)throws SearchException;
	
	
	/**
	 * @author 	kpramod
	 * @param	parameters
	 * @param	String SavedFilterName
	 * @param	searchName
	 * @param	String userId
	 * @throws 	SearchException
	 * @throws DuplicateTemplateException 
	 * @purpose	This method is used to create template for search criteria.
	 */
	public void  saveUserFilter(List<Parameter> parameters,String SavedFilterName,String searchName,String userId)throws SearchException;
	
	
	
	/**
	 * @author 	kpramod
	 * @param	String SavedFilterName
	 * @param	searchName
	 * @param	String userId
	 * @return	List<Map<String,Object>>
	 * @throws 	SearchException
	 * @purpose	This method is used to get parameters from template which is saved by user as default criteria parameter
	 */
	public List<Map<String,Object>>  getTemplateDetails(String SavedFilterName,String searchName,String userId)throws SearchException;


	/**
	 * @author 	kpramod
	 * @param	String searchName
	 * @param	String userId
	 * @return	List<String>
	 * @throws 	SearchException
	 * @purpose	This method is used to get template list by user
	 */
	public List<String> getTemplateList(String searchName,String userId)throws SearchException;


	/**
	 * @author 	kpramod
	 * @param	String searchName
	 * @throws 	SearchException
	 * @purpose	This method is used to get template list by user
	 */
	public List<DataTableColumn> getListForDataTableColumnName(String searchName, String key)throws SearchException;
	
	/**
	 * 
	 * @param relinfo
	 */
	public void setRelationInfo(Relations relinfo);

	/**
	 * 
	 * @param config
	 */
	public void setConfiguration(ConfigurationBean config);
	
	/**
	 * 
	 * @throws GraphSearchException
	 */
	public void initMapping() throws GraphSearchException;
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws ConnectionException
	 */
	public WebSession Login(String username, String password) throws ConnectionException;
	
	
	/**
	 * This method is used to get Measurements related to provided tag number.
	 * 
	 * @param tagNo
	 * @param string 
	 * @return List<Map<String, String>> 
	 * @throws GraphSearchException 
	 */
	public Map<String, List<?>> getMeasurements(String tagNo, String authKey) throws GraphSearchException;

	
	/**
	 * 
	 * @param meaId
	 * @return
	 */
	public List<Map<String, Object>> getMeasurmentInfo(String meaId);
	
	
	/**
	 * 
	 * @param Label
	 * @param condtion
	 * @param KeyName
	 * @return
	 */
	public List<Map<String, Object>>  getVertiexDetails(String Label,Map<String, String> condtion,String KeyName) ;
	/**
	 * 
	 * @param ParentLabel
	 * @param Parentcondtion
	 * @param relation
	 * @param KeyName
	 * @return
	 */
	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String KeyName) ;
	/**
	 * 
	 * @param Label
	 * @param KeyName
	 * @return
	 */
	public List<Map<String, Object>>  getVertiexDetails(String Label,String KeyName) ;
	
	/**
	 * 
	 * @param ParentLabel
	 * @param Parentcondtion
	 * @param relation
	 * @param element
	 * @param KeyName
	 * @return
	 */
	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String element,String KeyName) ;

	/**
	 * @param ParentLabel
	 * @param Parentcondtion
	 * @param relation
	 * @param element
	 * @param condtion
	 * @param KeyName
	 * @return
	 */
	public List<Map<String, Object>>  getVertiexDetails(String ParentLabel,Map<String, String> Parentcondtion,String relation,String element,Map<String, String> condtion,String KeyName) ;
	
	/**
	 * @author kpramod
	 * @param searchBean
	 * @param treeName
	 * @param limit
	 * @return
	 * @throws SearchException 
	 */
	public Map<String, List<Map<String, String>>> getDataForTreeBubbleView(SearchBean searchBean, String treeName, int limit) throws SearchException;

	/**
	 * Method will return details of vertex (all its attributes) by provided vertex rid
	 * @author kpramod
	 * @param vertexRid
	 * @return Map<String,String>
	 * @throws SearchException
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,Map> getVertexDetailsToDisplay(String vertexRid)throws SearchException;
	
	/**
	 * Method will import all xmi and rdf import data
	 * 
	 * @param lstElement
	 * @throws GraphImportException
	 * @return
	 */
	public boolean importProcess(List<ElementBean> lstElement, List<RelationBean> lstRelation) throws GraphImportException;
	/**
	 * method will return all elements
	 * @return
	 */
	public List<String> getGroupElements();
	
	/**
	 * method will return primary attribute
	 * @param groupName
	 * @return
	 */
	public String getPrimaryAttribute(String groupName);
	
//	public void readCSV(String CSVPath);

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
	public Map<String,Map> getOutVertexAndEdgesForTagNum(String vertexRid)throws SearchException;

	/**
	 * to get the attributes of vertex 
	 * 
	 * @author Swapnali.Suntnure
	 * @param vertId 
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public Map<String, Map> getPreviewdVertexDetailsToDisplay(String vertId);
	
	/**
	 * to get the document url 
	 * @author rohit.walvekar
	 * @param id 
	 * @return
	 * @throws SearchException 
	 */
	public String getDocumentUrl(String id) throws SearchException;
	
	/**
	 * to get result view names 
	 * @author rohit.walvekar
	 * @param id 
	 * @return
	 * @throws SearchException 
	 */
	public List<HashMap<String,String>> getResultViewList(String searchName) throws SearchException;
}
