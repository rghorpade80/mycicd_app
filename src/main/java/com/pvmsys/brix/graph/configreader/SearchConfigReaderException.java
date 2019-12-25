package com.pvmsys.brix.graph.configreader;


/**
 * 
 * @author kpramod
 * @since 13/Jan/2019
 */
public class SearchConfigReaderException extends Exception {

    private static final long serialVersionUID = 6565773415129310697L;

    public SearchConfigReaderException(){}

	/**
	 * 
	 * @param msg
	 */
	public SearchConfigReaderException(String msg){
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public SearchConfigReaderException(String msg, Throwable cause){
		super(msg, cause);
	}

	/**
	 * 
	 * @param cause
	 */
	public SearchConfigReaderException(Throwable cause){
		super(cause);
	}
}
