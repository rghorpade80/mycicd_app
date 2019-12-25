package com.pvmsys.brix.graph.beans;

import java.util.List;

public class RelationBean {

	String from;
	String to;
	String arrows;
	String label;
	List<RelationParameter> parameter;
	
	public List<RelationParameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<RelationParameter> parameter) {
		this.parameter = parameter;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getArrows() {
		return arrows;
	}
	public void setArrows(String arrows) {
		this.arrows = arrows;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
