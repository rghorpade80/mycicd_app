package com.pvmsys.brix.graph.beans;

public class RelationParameter {
	
	String key;
	Object value;
	
	public RelationParameter(){
		
	}
	public RelationParameter(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
