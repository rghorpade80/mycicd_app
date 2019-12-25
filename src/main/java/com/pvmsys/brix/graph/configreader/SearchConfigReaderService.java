package com.pvmsys.brix.graph.configreader;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pvmsys.brix.graph.configreader.beans.ENUM;
import com.pvmsys.brix.graph.configreader.beans.Layout;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.Result;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;

/**
 * 
 * @author kpramod
 * @since 13/Jan/2019
 */

@Service
public interface SearchConfigReaderService {

    
    /**
	 * @author	kpramod
	 * @param
	 * @return	List<SearchBean>
	 * @throws 	SearchException
	 * @purpose This method is used to read all search configurations
	 */
	public List<SearchBean> readAllSearchConfigurations() throws SearchConfigReaderException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @return	SearchBean
	 * @throws 	SearchException
	 * @purpose	This method is used to read search configuration using given search criteria
	 */
	public SearchBean readSearchConfigurations(String searchName)throws SearchConfigReaderException;

	/**
	 * @author	kpramod
	 * @param	searchName,searchType	
	 * @return	List<Parameter>
	 * @throws 	SearchException
	 * @purpose	This method is used to read search criteria parameters using provided name and type
	 */
	public List<Parameter> readSearchCriteriaParameters(String searchName, ENUM searchType)throws SearchConfigReaderException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @return	Layout
	 * @throws 	SearchException
	 * @purpose	This method is used to read layout information using search name.
	 */
	public Layout readLayoutInformation(String searchName)throws SearchConfigReaderException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @return	List<Result>
	 * @throws 	SearchException
	 * @purpose	This method is used to read and return result configurations using search name.
	 */
	public List<Result> readResultConfigurations(String searchName)throws SearchConfigReaderException;

	/**
	 * @author	kpramod
	 * @param	searchName
	 * @return	Result
	 * @throws 	SearchException
	 * @purpose	This method is used to read configuration for table using search name
	 */
	public Result readTableResultConfigurations(String searchName, String key)throws SearchConfigReaderException;

	/**
	 * @author 	kpramod
	 * @param	searchName
	 * @return	Result
	 * @throws 	SearchException
	 * @purpose	This method is used to read result configurations.
	 */
	public Result readTreeResultConfigurations(String searchName)throws SearchConfigReaderException;
	
	
	/**
	 * @author 	kpramod
	 * @param	
	 * @return	List<String>
	 * @throws 	SearchException
	 * @purpose	This method is used to get all available search.
	 */
	public List<String> getAllAvailableSearch()throws SearchConfigReaderException;

	
	/**
	 * @author 	kpramod
	 * @param searchType
	 * @return Layout
	 * @throws SearchConfigReaderException
	 */
	public Layout getLayOut(SearchBean searchType) throws SearchConfigReaderException;
	
}
