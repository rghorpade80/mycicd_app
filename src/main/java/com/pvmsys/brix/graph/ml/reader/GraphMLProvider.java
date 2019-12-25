package com.pvmsys.brix.graph.ml.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pvmsys.brix.graph.beans.ElemParameter;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.RelationBean;
import com.pvmsys.brix.graph.beans.RelationParameter;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
@Service
public class GraphMLProvider implements GraphMLService {
	
	private static Logger logger = Logger.getLogger(GraphMLProvider.class);
	@Override
	public Map<String,List<?>> getGraphMLElements(String filePath) throws Exception {
		
		Map<String,List<?>> map = null;
		try {
			Graph graph = readGraphMLFile(filePath);
			
			//read all nodes
			List<ElementBean> lstElements = getNodes(graph);
			//read all edges
			List<RelationBean> lstRelations = getEdges(graph);
  
			map = new HashMap<String, List<?>>();
			map.put("elementList", lstElements);
			map.put("relationList", lstRelations);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	    return map;
	}
	/**
	 * to get nodes from file
	 * @param graph
	 * @return
	 * @throws Exception 
	 */
	private List<ElementBean> getNodes(Graph graph) throws Exception{
		List<ElementBean> lstElements = null;
		try {
			Iterable<Vertex> vertices = graph.getVertices();
			Iterator<Vertex> verticesIterator = vertices.iterator();
			lstElements = new ArrayList<>();
			while (verticesIterator.hasNext()) {
 
			  Vertex vertex = verticesIterator.next();
			  ElementBean elementBean = new ElementBean();
			  elementBean.setId(vertex.getId().toString().trim());
			  elementBean.setLabel(vertex.getId().toString().trim());
			  if(vertex.getProperty("labels") != null && vertex.getProperty("labels").toString().contains(":") ){
				  elementBean.setGroup(vertex.getProperty("labels").toString().replaceAll(":",""));
			  }else if(vertex.getProperty("labels") != null)
				  elementBean.setGroup(vertex.getProperty("labels").toString());

			  List<ElemParameter> lstElemPara = new ArrayList<>();
			  vertex.getPropertyKeys().forEach( key -> {
				  ElemParameter elemParameter = new ElemParameter();
				  elemParameter.setPropertyKey(key);
				  elemParameter.setPropertyValue(vertex.getProperty(key));
				  lstElemPara.add(elemParameter);
			  });
			  elementBean.setParameter(lstElemPara);
			  lstElements.add(elementBean);
			}
		} catch (Exception e) {
			String msg = "Exception while reading vertex from graph ml file :"+e.getMessage();
			logger.error(msg);
			throw new Exception(msg,e.getCause());
		}
	    return lstElements;
	}
	/**
	 * to get edges from file
	 * @param graph
	 * @return
	 * @throws Exception 
	 */
	private List<RelationBean> getEdges(Graph graph) throws Exception{
		List<RelationBean> lstRelations = null;
		try {
			Iterable<Edge> edges =  graph.getEdges();
			Iterator<Edge> edgesIterator = edges.iterator();
			lstRelations = new ArrayList<>();
			while(edgesIterator.hasNext()){
				Edge edge = edgesIterator.next();
				RelationBean relationBean = new RelationBean();
				relationBean.setLabel(edge.getLabel());
				relationBean.setTo(edge.getVertex(Direction.IN).getId().toString());
				relationBean.setFrom(edge.getVertex(Direction.OUT).getId().toString());
				relationBean.setArrows("to");
				//for edge attributes
				List<RelationParameter> relParams = new ArrayList<>();
				edge.getPropertyKeys().forEach(key -> {
					RelationParameter relationParameter = new RelationParameter();
					relationParameter.setKey(key);
					relationParameter.setValue(edge.getProperty(key));
					relParams.add(relationParameter);
				});
				//add 'graphMLId' as property for edge
				RelationParameter relationParameter = new RelationParameter();
				relationParameter.setKey("graphMLId");
				relationParameter.setValue(edge.getId());
				relParams.add(relationParameter);
				relationBean.setParameter(relParams);

				//add 'label' as property for edge
				RelationParameter relParameter = new RelationParameter();
				relParameter.setKey("label");
				relParameter.setValue(edge.getLabel());
				relParams.add(relParameter);
				relationBean.setParameter(relParams);
				
				lstRelations.add(relationBean);
			}
		} catch (Exception e) {
			String msg = "Exception while reading edges from graph ml file :"+e.getMessage();
			logger.error(msg);
			throw new Exception(msg,e.getCause());
		}
	    return lstRelations;
	}
	/**
	 * to read GraphML file
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	private Graph readGraphMLFile(String filePath) throws Exception{
		Graph graph = new TinkerGraph();
	    GraphMLReader reader = new GraphMLReader(graph);
	    InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			reader.inputGraph(is);
		} catch (FileNotFoundException e) {
			String msg = "[FileNotFoundException]Exception while reading graph ml file :"+e.getMessage();
			logger.error(msg);
			throw new Exception(msg,e.getCause());
		} catch (IOException e) {
			String msg = "[IOException]Exception while reading graph ml file :"+e.getMessage();
			logger.error(msg);
			throw new Exception(msg,e.getCause());
		}
		return graph;
	}
}
