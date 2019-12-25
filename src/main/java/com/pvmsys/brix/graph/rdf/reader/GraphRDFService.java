package com.pvmsys.brix.graph.rdf.reader;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface GraphRDFService {
	/**
	 * to read the rdf file and retrieve elements ,attributes & relations
	 * @param path - path where file is present
	 * @return map of elements and relations
	 * @throws Exception 
	 */
	public Map<String, List<?>> getRdfData(String path) throws  Exception;

}
