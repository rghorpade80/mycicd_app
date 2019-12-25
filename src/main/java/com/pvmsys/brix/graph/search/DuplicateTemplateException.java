/*******************************************************************************
  * @(#) Id: SearchException.java
  * ***************************************************************************
  * Copyright(C) IASYS Technology Solutions Pvt Ltd. All Rights Reserved.
  * ****************************************************************************
  *  IASYS Technology Solutions Pvt Ltd                                       
  *  25/5 Rajiv Gandhi Infotech Park, Hinjewadi, Phase III, Tal-Mulsi,Pune-411057.
  *  Phone: +91-9623447255, Internet: https://www.iasys.co.in  
  * **************************************************************************/
package com.pvmsys.brix.graph.search;

/***************************************************************
 * This class is used as custom exception for Search Configuration Service
 ***************************************************************/

/**
 * @author gonkar
 * @version 1.0
 * @created 27-Nov-2018
 */
public class DuplicateTemplateException extends Exception {

	private static final long serialVersionUID = 3606170353373847941L;
	
	public DuplicateTemplateException(){}

	/**
	 * 
	 * @param msg
	 */
	public DuplicateTemplateException(String msg){
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public DuplicateTemplateException(String msg, Throwable cause){
		super(msg, cause);
	}

	/**
	 * 
	 * @param cause
	 */
	public DuplicateTemplateException(Throwable cause){
		super(cause);
	}

}