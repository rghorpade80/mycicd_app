package com.pvmsys.brix.graph.configreader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.naming.ConfigurationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pvmsys.brix.graph.configreader.SearchConfigReaderException;
import com.pvmsys.brix.graph.configreader.SearchConfigReaderService;
import com.pvmsys.brix.graph.configreader.beans.Column;
import com.pvmsys.brix.graph.configreader.beans.Criteria;
import com.pvmsys.brix.graph.configreader.beans.ENUM;
import com.pvmsys.brix.graph.configreader.beans.Layout;
import com.pvmsys.brix.graph.configreader.beans.Parameter;
import com.pvmsys.brix.graph.configreader.beans.Result;
import com.pvmsys.brix.graph.configreader.beans.Row;
import com.pvmsys.brix.graph.configreader.beans.SearchBean;
import com.pvmsys.brix.graph.configreader.beans.SearchConfigurations;

/**
 * 
 * @author kpramod
 * @since 13/Jan/2019
 * 
 * This class contains method to read search related configurations from
 * SearchConfigurations.xml file
 */

@Service
public class SearchConfigReaderImpl implements SearchConfigReaderService{

    private String searchConfigXMLFileName = "SearchConfigurations.xml";
    private SearchConfigurations SEARCH_CONFIGURATIONS;
    private static Logger logger = Logger.getLogger(SearchConfigReaderImpl.class);
    
    
    public SearchConfigReaderImpl() throws ConfigurationException {
	readSearchConfigurationFile();
    }
    
    
    private void readSearchConfigurationFile() throws ConfigurationException {
	
	InputStream inputStream = null;
	try {
			final String dir = System.getProperty("user.dir");
			File file = new File(dir+"/config/"+searchConfigXMLFileName);
			inputStream = new FileInputStream(file);
	    	JAXBContext jaxbContext = JAXBContext.newInstance(SearchConfigurations.class);
	    	Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	    	Source source = new StreamSource(inputStream);
	    	JAXBElement<SearchConfigurations> searchConfigRoot = unmarshaller.unmarshal(source,SearchConfigurations.class);
	    	SEARCH_CONFIGURATIONS = searchConfigRoot.getValue();
	}
	catch(Exception e){
	    logger.error("Problem while reading search configurations");
	    throw new ConfigurationException();
	}
	finally{
	    if(inputStream != null){
		try {
		    inputStream.close();
		    inputStream = null;
		} catch (IOException e) {
		    logger.error("Exception while closing inputStream");
		    logger.error(e);
		}
	    }
	}
	
	logger.info("SearchConfigReaderImpl.readSearchConfigurationile()");
    }
    
    
    @Override
    public List<SearchBean> readAllSearchConfigurations() throws SearchConfigReaderException {
	List<SearchBean> listOfSearchType = null;
	try{
		listOfSearchType = SEARCH_CONFIGURATIONS.getSearch();
	}
	catch (Exception e) {
	    logger.error(e);
	    logger.error("Message: Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	logger.info("SearchConfigReaderImpl.readAllSearchConfigurations()");
	return listOfSearchType;
    }

    
    @Override
    public SearchBean readSearchConfigurations(String searchName) throws SearchConfigReaderException {
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	logger.debug("Parameter : searchName "+searchName);
	SearchBean searchType = null;
	try {
		searchType = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
	}
	catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return searchType;
    }

    
    @Override
    public List<Parameter> readSearchCriteriaParameters(String searchName, ENUM enumType)
	    throws SearchConfigReaderException {
	
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	List<Parameter> parameters = null;
	try {
		    SearchBean searchType = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
		    
        	    if (searchType != null) {
        		
        		// 1 - FOR SIMPLE , 2 - For Advance
        		if (Integer.parseInt(enumType.getId().toString()) == 1) {
        		    
        		    parameters = searchType.getCriteria().getParameter().stream().filter(
        			    x -> x.getSearchtype().toUpperCase().trim().contains(enumType.getId().toUpperCase().trim()))
        			    .collect(Collectors.toList());
        		} else {
        		    parameters = searchType.getCriteria().getParameter();
        		}
        	    }
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return parameters;
    }

     
    @Override
    public Layout readLayoutInformation(String searchName) throws SearchConfigReaderException {
	
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	Layout layoutType = null;
	try {
		SearchBean searchType  = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
		if(searchType!=null){
			layoutType = getLayOut(searchType);
		}
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return layoutType;
    }

    
    @Override
    public List<Result> readResultConfigurations(String searchName) throws SearchConfigReaderException {
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	List<Result> resultTypes = null;
	try {
		SearchBean searchType  = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
		if(searchType!=null)
			resultTypes = searchType.getResults().getResult();
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return resultTypes;
    }
    

    @Override
    public Result readTableResultConfigurations(String searchName, String key) throws SearchConfigReaderException {
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	Result resultTypes = null;
	try {
		SearchBean searchType  = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
		if(searchType!=null){
			resultTypes = searchType.getResults().getResult().stream().filter(x -> x.getKey()
					.equalsIgnoreCase(key)).findFirst().orElse(null);
		}
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return resultTypes;
    }

    
    @Override
    public Result readTreeResultConfigurations(String searchName) throws SearchConfigReaderException {
	
	Objects.requireNonNull(searchName, "Invalid  Search Criteria");
	Result resultTypes = null;
	try {
		SearchBean searchType  = SEARCH_CONFIGURATIONS.getSearch().stream().filter(x -> x.getKey()
				.equalsIgnoreCase(searchName)).findFirst().orElse(null);
		if(searchType!=null){
			resultTypes = searchType.getResults().getResult().stream().filter(x -> x.getType()
					.equalsIgnoreCase("Tree")).findFirst().orElse(null);
		}
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return resultTypes;
    }

    
    @Override
    public List<String> getAllAvailableSearch() throws SearchConfigReaderException {
	List<String> lstOfAvailableSearch = null;
	try {
		lstOfAvailableSearch  = SEARCH_CONFIGURATIONS.getSearch().stream().map(SearchBean::getKey).collect(Collectors.toList());
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return lstOfAvailableSearch;
    }
    

    @Override
    public Layout getLayOut(SearchBean searchType) throws SearchConfigReaderException {
	
	Layout layout = new Layout();
	try {
	    Criteria criteria = searchType.getCriteria();
	    int rowCount = criteria.getNoofrows();
	    int columnCount = criteria.getNoofcolumns();
	    List<Row> rows = new ArrayList<>();
	    List<Column> columns = null;
	    for (int i = 1; i <= rowCount; i++) {
		columns = new ArrayList<>();
		Row row = new Row();
		row.setRowid("r" + i);
		row.setRownumber(i);
		for (int j = 1; j <= columnCount; j++) {
		    Column column = new Column();
		    column.setColumnnumber(j);
		    column.setColumnid("r" + i + "c" + j);
		    Parameter parameter = criteria.getParameter().stream()
			    .filter(pre -> pre.getLayoutid().equalsIgnoreCase(column.getColumnid())).findFirst()
			    .orElse(null);

		    if (parameter != null) {
			if (parameter.getColumncount() != null) {
			    if (Integer.parseInt(parameter.getColumncount()) > 1) {
				column.setIsmergedcolumn(true);
				column.setMergewithcolumn(parameter.getColumncount());
			    } else {
				column.setIsmergedcolumn(false);
				column.setMergewithcolumn("0");
			    }
			} else {
			    column.setIsmergedcolumn(false);
			    column.setMergewithcolumn("0");
			}
		    } else {
			column.setIsmergedcolumn(false);
			column.setMergewithcolumn("0");
		    }

		    columns.add(column);
		}
		row.setColumn(columns);
		rows.add(row);
	    }
	    layout.setColumnwidth(criteria.getColumnwidth());
	    layout.setNoofcolumns(criteria.getNoofcolumns());
	    layout.setNoofrows(criteria.getNoofrows());
	    layout.setRow(rows);
	} catch (Exception e) {
	    logger.error(e);
	    logger.error("Problem while reading search configurations");
	    throw new SearchConfigReaderException(e.getMessage(), e.getCause());
	}
	return layout;
    }

   
    
   

    
}
