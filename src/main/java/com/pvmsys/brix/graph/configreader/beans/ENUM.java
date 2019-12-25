package com.pvmsys.brix.graph.configreader.beans;

public enum ENUM {

	COMBOBOX ("ComboBox","1"),
	MULTICOMBO ("MultiComboBox","2"),
	TEXTBOX ("TextBox","3"),
	DATE ("Date","4"),
	DATERANGE ("DateRange","5"),
	MANUAL ("Manual","1"),
	FIXED ("Fixed","2"),
	CATALOG ("Catalog","3"),
	BUSINESSOBJECT ("BusinessObject","4"),
	TREE ("Tree"),
	TABLE ("Table"),
	SEARCHTYPE_SIMPLE("Simple","1"),
	SEARCHTYPE_ADVANCE("Advanced","2"),
	FILTER_COMBO("ComboBox","1"),
	FILTER_TEXT("TextBox","2");
	
	private String enumVal;
	
	private String id;

	ENUM(String enumVal){
		this.enumVal = enumVal;
	}
	
	ENUM(String enumVal,String id){
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
