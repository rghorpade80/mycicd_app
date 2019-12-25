package com.pvmsys.brix.graph.beans;

import java.util.List;

public class Measurement
{

	String id;
	boolean open = false;
	String value;
	List<LocalColumn >data;
	String internalId;
	
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
	
	
	public String getInternalId()
	{
		return internalId;
	}
	public void setInternalId(String internalId)
	{
		this.internalId = internalId;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public boolean isOpen()
	{
		return open;
	}
	public void setOpen(boolean open)
	{
		this.open = open;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public List<LocalColumn> getData()
	{
		return data;
	}
	public void setData(List<LocalColumn> data)
	{
		this.data = data;
	}
	
	
}
