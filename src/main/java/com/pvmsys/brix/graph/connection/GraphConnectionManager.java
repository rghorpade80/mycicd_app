package com.pvmsys.brix.graph.connection;

import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.structure.Graph;

public class GraphConnectionManager {

	/*
	 * public static void main(String[] args) { Graph instance = getGraph();
	 * List<Vertex> vList = instance.traversal().V().toList(); for (Vertex
	 * object : vList) { System.out.println(object.label()); } }
	 */
	private static Logger logger = Logger.getLogger(GraphConnectionManager.class);
	private static OrientGraphFactory fac = null;
//	private static Graph graph = null;
	
	/**
	 * Close graph
	 * @throws ConnectionException 
	 */
	/*public static void closeGraph() throws ConnectionException {
		if (graph != null) {
			try {
				graph.close();
			} catch (Exception e) {
				logger.error("ERROR ::::"+e);
				throw new ConnectionException(e.getMessage(),e.getCause());
			}
		}
	}*/

	/**
	 * create Graph object to interact with orient DB or return existing
	 * 
	 * @return Graph
	 * @throws ConnectionException 
	 */
	public static Graph getGraph(String ORIENT_SERVER_URL,String username,String password) throws ConnectionException {
		/**
		 * Create Orient Db connection factory......
		 */
		Graph graph = null;
		try
		{
			fac = new OrientGraphFactory(ORIENT_SERVER_URL,username,password);
			if(fac==null)
			{
				logger.error("ERROR ::::Failed to get OrientGraphFactory. OrientGraphFactory found null.");
				logger.error("ERROR ::::Please provide valid Credentials.");
			}
			fac.setLabelAsClassName(true);
			if (graph == null) {
				graph = fac.getNoTx();
			}			
		}
		catch (Exception e) {
			logger.error("ERROR ::::"+e);
			throw new ConnectionException(e.getMessage(),e.getCause());
		}
		return graph;
	}

	/**
	 * create Graph object to interact with orient DB or return existing
	 * 
	 * @return
	 */
	/*public static Graph getTxGraph() {
		if (graph == null) {
			graph = fac.getTx();
		}
		return graph;
	}*/

}
