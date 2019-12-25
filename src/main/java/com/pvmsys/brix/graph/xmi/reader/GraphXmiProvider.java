package com.pvmsys.brix.graph.xmi.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.pvmsys.brix.graph.beans.ElemParameter;
import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.RelationBean;
import com.pvmsys.brix.graph.search.GraphImportException;

import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.handler.HandlerEnum;
import gov.nih.nci.ncicb.xmiinout.handler.XmiException;
import gov.nih.nci.ncicb.xmiinout.handler.XmiHandlerFactory;
import gov.nih.nci.ncicb.xmiinout.handler.XmiInOutHandler;

@Service
public class GraphXmiProvider implements GraphXmiService {

	private static Logger logger = Logger.getLogger(GraphXmiProvider.class);
	XmiInOutHandler handler = null;
	/*
	@Autowired
	GraphSearchService graphSearchService;*/
	
	@Override
	public Map<String,List<?>> getElements(String filePath) throws Exception {
		
		Map<String,ElementBean> elementMap = new HashMap<>();
		List<ElementBean> lstElements = new ArrayList<>();
		List<RelationBean> lstRelations = new ArrayList<>();
		Map<String, List<?>> map = null;
		try {
			UMLModel model = readXMIFile(filePath);
			if(model.getPackages() != null){
				for (UMLPackage pkg : model.getPackages()) {
					if(pkg.getClasses() != null){
						for (UMLClass umlClass : pkg.getClasses()) {
							ElementBean element = getElement(umlClass);
							elementMap.put(umlClass.getTaggedValue("ea_localid").getValue(),element);
							lstElements.add(element);
						}
					}
				}
			}
			
			if(model.getAssociations() != null){
				for (UMLAssociation association : model.getAssociations()) {
					RelationBean relationBean = null;;
					try {
						relationBean = getRelation(association, elementMap);
					} catch (Exception e) {
						throw e;
					}
					lstRelations.add(relationBean);
				}
			}
			map = new HashMap<String, List<?>>();
			map.put("elementList", lstElements);
			map.put("relationList", lstRelations);
			/*Gson gson = new Gson();
			  System.out.println(gson.toJson(lstElements));
			System.out.println(gson.toJson(lstRelations));*/
			return map;
		}catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * to get element[class] and its attributes
	 * @param cls
	 * @return
	 * @throws Exception 
	 */
	private ElementBean getElement(UMLClass cls) throws Exception{
		List<ElemParameter> lstElemParameters = new ArrayList<>();
		ElementBean elemBean = new ElementBean();
		try {
			if(cls.getStereotype() != null && !cls.getStereotype().isEmpty())
				elemBean.setGroup(cls.getStereotype());
			else
				elemBean.setGroup("-");
			elemBean.setLabel(cls.getName());
			elemBean.setId(cls.getTaggedValue("ea_localid").getValue());
			
			/*List<ElementItem> elementItems = getElementItem(elemBean.getGroup());
			if(elementItems != null) {*/
				//iterate attributes 
				if(cls.getAttributes() != null){
					
					cls.getAttributes().forEach(attr ->{
						ElemParameter elemPara = new ElemParameter();
						elemPara.setPropertyKey(attr.getName());
						if( attr.getTaggedValue(attr.getName())!= null)
							elemPara.setPropertyValue(attr.getTaggedValue(attr.getName()).getValue());
						else
							elemPara.setPropertyValue("-");
						lstElemParameters.add(elemPara);
					
						/*elementItems.forEach(item -> {
							if(item.equals(attr.getName())){
								elemPara.setKey(attr.getName());
								if( attr.getTaggedValue(attr.getName())!= null)
									elemPara.setValue(attr.getTaggedValue(attr.getName()).getValue());
								else
									elemPara.setValue("-");
								elemPara.setDataType(item.getdataType());
								lstElemParameters.add(elemPara);
							}
						});*/
						
					});
				}
//			}
			
			elemBean.setParameter(lstElemParameters);
		} catch (Exception e) {
			logger.error(e);
//			logger.error("Exception while getting element from XML file");
			throw new Exception(e.getMessage(), e.getCause());
		}
		return elemBean;
	}
	/**
	 * to get relation between elements
	 * @param association
	 * @param elementMap
	 * @return
	 * @throws Exception
	 */
	private RelationBean getRelation(UMLAssociation association, Map<String,ElementBean> elementMap) throws Exception{
		RelationBean relationBean = new RelationBean();
		try {
			if(association.getAssociationEnds() != null){
				relationBean.setFrom(association.getTaggedValue("ea_sourceID").getValue());
				relationBean.setTo(association.getTaggedValue("ea_targetID").getValue());
				if(association.getTaggedValue("stereotype") != null)
					relationBean.setLabel(association.getTaggedValue("stereotype").getValue());
				/*else{
					// relation name is mandatory
					//throw new GraphImportException(e.getMessage(), e.getCause());
					throw new Exception("relation name not present from source node :" +relationBean.getFrom() +
							" to target node :"+relationBean.getTo());
				}*/
				relationBean.setArrows("to");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e.getCause());
			throw new Exception(e.getMessage(), e.getCause());
		}
		return relationBean;
	}
	/**
	 * To read XML file 
	 * @param filePath
	 * @return -uml model
	 * @throws Exception
	 */
	private UMLModel readXMIFile(String filePath) throws Exception{
		try {
			
			File fXmlFile = new File(filePath);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			if(!doc.getDocumentElement().getAttribute("xmi.version").equals("1.1")) {
				throw new GraphImportException("XMI version must be 1.1");
			}
			handler = XmiHandlerFactory.getXmiHandler(HandlerEnum.EADefault);
			handler.load(filePath);
			UMLModel model = handler.getModel();
			return model;
		
		} catch (XmiException e){
			String msg = "Error while reading XMI file "+e.getMessage();
			logger.error(msg,e);
			throw new Exception(msg, e.getCause());
		} catch (IOException e) {
			String msg = "IO exception while reading XMI file"+e.getMessage();
			logger.error(msg,e);
			throw new Exception(msg, e.getCause());
		}  catch (Exception e) {
			logger.error(e);
			String msg = "exception while reading XMI file "+e.getMessage();
			logger.error(msg,e);
			throw new Exception(msg, e.getCause());
		}
	}

}
