package com.pvmsys.brix.graph.beans;

public class LocalColumn
{

	String id;
	String value;
	String actualId;
	String meaId;
	String unit;
	
	// added as per requirement for new canvasJS 5/3/019
	String nodeId;
	String nodeType;
	
	
	// added as per requirement for new canvasJS 5/3/019
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	
	public String getMeaId()
	{
		return meaId;
	}
	public void setMeaId(String meaId)
	{
		this.meaId = meaId;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getActualId()
	{
		return actualId;
	}
	public void setActualId(String actualId)
	{
		this.actualId = actualId;
	}
	public String getUnit() {
	    return unit;
	}
	public void setUnit(String unit) {
	    this.unit = unit;
	}
	
	
}
