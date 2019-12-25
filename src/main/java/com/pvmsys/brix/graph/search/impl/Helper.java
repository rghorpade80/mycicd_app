package com.pvmsys.brix.graph.search.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.pvmsys.brix.graph.beans.ElemParameter;

public class Helper {
	private Graph graph = null;
	protected static final int L_ERROR = 1;
	protected static final int L_INFO = 2;
	protected static final int L_PARA = 3;
	protected static final int L_E_QUERY = 4;
	protected static final int L_I_QUERY = 5;
	final static Logger logger = Logger.getLogger(Helper.class);

	public Helper(Graph graph) {
		super();
		this.graph = graph;
	}


	/**
	 * This method is used to create edge between two
	 * vertices.
	 * 
	 * @param vertexFrom
	 * @param vertexTo
	 * @param relName
	 * @return 
	 */
	public Edge addEdge(Vertex vertexFrom, Vertex vertexTo, String relName) {
		Edge edge = null;
		try {
			Iterator<Edge> edgeIterator = vertexFrom.edges(Direction.OUT, relName);
			if(edgeIterator != null)
				while (edgeIterator.hasNext()) {
					edge = (Edge) edgeIterator.next();
					if(edge.inVertex().id().equals(vertexTo.id())){
						logger.info("Edge already present between vertex ::"+vertexFrom.label()+" and "+vertexTo.label());
						return edge;
					}
				}

			edge = vertexFrom.addEdge(relName, vertexTo);
			logger.info("Edge added between vertex ::"+vertexFrom.label()+" and "+vertexTo.label());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return edge;
	}
	
	
	/**
	 * This method is used for create vertex using primary property and value
	 * If vertex already exist, it will retrieve available vertex
	 * else it will create new vertex.
	 * 
	 * @param className
	 * @param primaryProperty
	 * @param propertyValue
	 * @return Vertex
	 * @throws Exception
	 */
	public Vertex addVertex(String className, String primaryProperty, String propertyValue) throws Exception {
		try {
			Vertex vertex = getVertexByProperty(className, primaryProperty, propertyValue);
			
			if (vertex == null) {
				logger.info("No Vertex available for given class Name: "+className+" for Property::"+primaryProperty);
				vertex = graph.addVertex(className);
				logger.info("[" + vertex.label() + "] \t <-- Vertex created successfully.");	
				vertex.property(primaryProperty, propertyValue);
			} 
			return vertex;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * This method is used to update vertex property.
	 * 
	 * @param vertex
	 * @param propertyValues
	 */
	public void updateVertex(Vertex vertex, List<ElemParameter> propertyValues) {
		try {
			propertyValues.forEach(property ->{
				vertex.property(property.getPropertyKey(), property.getPropertyValue());
				logger.info("Property \t [" + property.getPropertyKey() + "] \t\t is added with as value ::\t  ["
						+ property.getPropertyValue() + "] \t   for vertex :: \t [" + vertex.label() + "]");
			});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	/**
	 * This method is used to check if particular vertex is present or not
	 * using property name and value
	 * @param className
	 * @param propertyName
	 * @param propertyValue
	 * @return Vertex
	 */
	private Vertex getVertexByProperty(String className, String propertyName, Object propertyValue) {
		try {
			logger.info("Checking if is present for Vertex Label as ::"+className +" with property as ::"+propertyName +" and its value as ::"+propertyValue.toString());
			GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().hasLabel(className).has(propertyName, propertyValue);
			while (traversal.hasNext()) {
				logger.info("Vertex Found with Label as ::"+className +" with property as ::"+propertyName +" and its value as ::"+propertyValue.toString());
				return traversal.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		logger.info("No related vertex found.");
		return null;
	}
	
	
	/**
	 * To get already available vertex using vertex class Name
	 * @param className
	 * @return Vertex
	 */
	public Vertex getVertexByClassName(String className) {
		try {
			GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().hasLabel(className);
			if (traversal.hasNext()) {
				logger.info("Vertex Found with Label as ::"+className);
				return traversal.next();
			}else{
				Vertex vertex = graph.addVertex(className);
				logger.info("[" + vertex.label() + "] \t <-- Vertex created successfully.");
				return vertex;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}


	/**
	 * This method is used to clear graph database. It does not clear graph
	 * schema.
	 * 
	 * @return boolean
	 */
	public boolean clearDb() {
		try {
			logger.info("<------------------------------- Executing method :: clearDb. -------------------------------------->");
			logger.info("Clearing database...");
			GraphTraversal<Vertex, Vertex> gTraversal = graph.traversal().V();
			while (gTraversal.hasNext()) {
				Vertex vertex = gTraversal.next();
				vertex.remove();
				logger.info("Vertex removed :: \t [" + vertex.label() + "]");
			}
			logger.info("<------------------------------- Execution Completed :: clearDb ------------------------------->");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while clear DB :"+e);
			return false;
		}
	}
}