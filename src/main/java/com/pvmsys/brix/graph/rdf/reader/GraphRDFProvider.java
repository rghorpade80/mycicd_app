package com.pvmsys.brix.graph.rdf.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pvmsys.brix.graph.beans.ElemParameter;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.RelationBean;

@Service
public class GraphRDFProvider implements GraphRDFService {

	private static Logger logger = Logger.getLogger(GraphRDFProvider.class);
	static Long id;

	@Override
	public Map<String,List<?>> getRdfData(String inputFileName) throws Exception {

		List<RelationBean> lstRelation = new ArrayList<>();
		List<ElementBean> lstelement = new ArrayList<>();
		Map<String,Long> idMap = new HashMap<>();
		id = 0l;
		Map<String, List<?>> map = null;
		try {
			 
			//get the model
			Model model = readRDFFile(inputFileName);
			
			ResIterator resIter = model.listSubjects();
			//iterate all subjects
			while(resIter.hasNext()){
				Resource subject = resIter.next(); //get the subject

				if(!idMap.containsKey(subject.toString()))
					idMap.put(subject.toString(), ++id);

				ElementBean element = new ElementBean();
				element.setId(idMap.get(subject.toString()).toString());
				element.setLabel(subject.getLocalName());
				String[]  group = subject.getNameSpace().split("/");
				element.setGroup(group[group.length-1]);

				StmtIterator stmtIterator = subject.listProperties();
				List<ElemParameter> lstElemPara = new ArrayList<>();
				
				while(stmtIterator.hasNext()){
					Statement statement = stmtIterator.next();
					Property  predicate = statement.getPredicate();   // get the predicate
					RDFNode   object   = statement.getObject();		// get the object
					if(object != null && predicate != null){

						if(object instanceof Resource ){
							//relation
							RelationBean relationBean = new RelationBean();
							relationBean.setLabel(predicate.getLocalName());
							if(!idMap.containsKey(object.toString()))
								idMap.put(object.toString(), ++id);
							relationBean.setFrom(idMap.get(subject.toString()).toString());
							relationBean.setTo(idMap.get(object.toString()).toString());
							relationBean.setArrows("to");
							lstRelation.add(relationBean);

						}else if(object instanceof Literal){
							ElemParameter elemParameter = new ElemParameter();
							elemParameter.setPropertyKey(predicate.getLocalName());
							elemParameter.setPropertyValue(object.toString());
							lstElemPara.add(elemParameter);
						}
					}
				}
				element.setParameter(lstElemPara);
				lstelement.add(element);
			}

			map = new HashMap<String, List<?>>();
			map.put("elementList", lstelement);
			map.put("relationList", lstRelation);
		}catch (Exception e) {
//			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception(e.getMessage() ,e.getCause());
		}
		/*Gson gson = new Gson();
		System.out.println( gson.toJson(lstRelation));
		System.out.println(gson.toJson(lstelement));*/
		return map;
	}
	/**
	 * To read RDF file 
	 * @param filePath
	 * @return -Model
	 * @throws Exception
	 */
	private Model readRDFFile(String filePath) throws Exception{
		// create an empty model
		Model model = null;
		try {
			model = ModelFactory.createDefaultModel();

			InputStream in = FileManager.get().open( filePath );
			if (in == null) {
				throw new IllegalArgumentException( "File: " + filePath + " not found");
			}

			// read the RDF/XML file
			model.read(in, "");
		} catch (Exception e) {
			String msg = "Exception while reading rdf file :" +e.getMessage();
			logger.error(msg);
			throw new Exception(msg,e.getCause());
		}
		return model;

	}
}
