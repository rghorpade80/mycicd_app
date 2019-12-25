package com.pvmsys.brix.graph.beans;

import java.util.List;


public class ConfigurationBean
{

	private String orientServerUrl;
	private String mappingXmlName;
	private List<String> optionalNodes;
	private List<String> optionalNodesTable;
	
	public ConfigurationBean(String orientServerUrl, String mappingXmlName)
	{
		super();
		this.orientServerUrl = orientServerUrl;
		this.mappingXmlName = mappingXmlName;
	}

	
	public String getOrientServerUrl()
	{
		return orientServerUrl;
	}

	public void setOrientServerUrl(String orientServerUrl)
	{
		this.orientServerUrl = orientServerUrl;
	}

	public String getMappingXmlName()
	{
		return mappingXmlName;
	}

	public void setMappingXmlName(String mappingXmlName)
	{
		this.mappingXmlName = mappingXmlName;
	}


	public List<String> getOptionalNodes() {
		return optionalNodes;
	}


	public void setOptionalNodes(List<String> optionalNodes) {
		this.optionalNodes = optionalNodes;
	}


	public List<String> getOptionalNodesTable() {
		return optionalNodesTable;
	}


	public void setOptionalNodesTable(List<String> optionalNodesTable) {
		this.optionalNodesTable = optionalNodesTable;
	}

}
