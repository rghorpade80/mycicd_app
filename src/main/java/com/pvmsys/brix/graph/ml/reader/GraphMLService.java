package com.pvmsys.brix.graph.ml.reader;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public interface GraphMLService {

	/**
	 * method get list of vertex and edges from GraphML file
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	public Map<String,List<?>> getGraphMLElements(String filePath) throws  Exception;
}
