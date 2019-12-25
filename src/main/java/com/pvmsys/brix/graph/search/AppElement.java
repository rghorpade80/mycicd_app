package com.pvmsys.brix.graph.search;

import java.util.ArrayList;
import java.util.List;

import com.pvmsys.brix.graph.xml.relation.beans.Relation;

public class AppElement
{
   public  String  elementname  ;
   public  List<Relation> relationcheck = new ArrayList<>();
	public List<Relation> getRelation()
	{
		return relationcheck;
	}
   
}
