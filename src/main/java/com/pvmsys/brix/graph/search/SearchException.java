package com.pvmsys.brix.graph.search;


/**
 * 
 * @author kpramod
 * @since 13/Jan/2019
 */
public class SearchException extends Exception {


    private static final long serialVersionUID = 1369217814038301968L;

	
	public SearchException(){}

	/**
	 * 
	 * @param msg
	 */
	public SearchException(String msg){
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public SearchException(String msg, Throwable cause){
		super(msg, cause);
	}

	/**
	 * 
	 * @param cause
	 */
	public SearchException(Throwable cause){
		super(cause);
	}

}
