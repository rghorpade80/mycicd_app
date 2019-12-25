package com.pvmsys.brix.graph.xmi.reader;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface GraphXmiService {

	/**
	 * This method is to get elements and relations from the XML file
	 * @param filePath - path of xml file
	 * @return Map
	 * @throws Exception
	 * 
	 */
	public Map<String, List<?>> getElements(String filePath) throws Exception;
}
