package com.pvmsys.brix.graph.beans;

public class Row
{
	String from;
	String to;
	String relation;
	String attribute;
	String condition;
	Object value;
	
	public Row()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Row(String from, String to, String relation, String attribute, String condition, Object value)
	{
		super();
		this.from = from;
		this.to = to;
		this.relation = relation;
		this.attribute = attribute;
		this.condition = condition;
		this.value = value;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getRelation()
	{
		return relation;
	}

	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	public String getAttribute()
	{
		return attribute;
	}

	public void setAttribute(String attribute)
	{
		this.attribute = attribute;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
	
	
}
