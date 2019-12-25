package com.pvmsys.brix.graph.beans;

import java.util.LinkedList;
import java.util.List;

public class Table
{

	List<Row> rows = new LinkedList<>();

	public void addRow(Row row)
	{
		rows.add(row);
	}
	public Table()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Table(List<Row> rows)
	{
		super();
		this.rows = rows;
	}

	public List<Row> getRows()
	{
		return rows;
	}

	public void setRows(List<Row> rows)
	{
		this.rows = rows;
	}

}
