package com.pvmsys.brix.graph.beans;

import java.util.List;

public class ElementBean {
	
	String id;
	String group = "-";
	String label;
	List<ElemParameter> parameter;
	
	public ElementBean(){}
	
	public ElementBean(String id, String label,String value , List<ElemParameter> parameter) {
		this.id = id;
		this.group = label;
		this.label = value;
		this.parameter = parameter;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<ElemParameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<ElemParameter> parameter) {
		this.parameter = parameter;
	}
}
