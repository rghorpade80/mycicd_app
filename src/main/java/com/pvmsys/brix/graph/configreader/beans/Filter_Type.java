package com.pvmsys.brix.graph.configreader.beans;

public enum Filter_Type {

	FILTER_COMBO("ComboBox","1"),
	FILTER_TEXT("TextBox","2");
	
	private String enumVal;
	
	private String id;

	Filter_Type(String enumVal){
		this.enumVal = enumVal;
	}
	
	Filter_Type(String enumVal,String id){
		this.enumVal = enumVal;
		this.id = id;
	}


	public String getEnumVal() {
		return enumVal;
	}
	
	public String getId() {
		return id;
	}
}
