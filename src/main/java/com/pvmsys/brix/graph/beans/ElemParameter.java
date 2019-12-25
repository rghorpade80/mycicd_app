package com.pvmsys.brix.graph.beans;

public class ElemParameter {

	String propertyKey;
	Object propertyValue;
	String dataType;
	
	public ElemParameter(String propertyKey, Object propertyValue){
		this.propertyKey = propertyKey;
		this.propertyValue = propertyValue;
	}
	
	public ElemParameter() {
	}

	public String getPropertyKey() {
		return propertyKey;
	}
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	public Object getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
