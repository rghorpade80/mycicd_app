package com.pvmsys.brix.graph.beans;



import java.io.Serializable;


public class ResultRO<T> implements Serializable {

	private static final long serialVersionUID = 2841770107177469775L;

	private int returncode;
	
	private String message;
	
	private T data;
	
	private Integer startAt =0;
	
	private Integer maxResults =0;
	
	private Integer total =0;
	
	
	
	/**
	 * @return the returnCode
	 */
	public int getReturnCode() {
		return returncode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(int returnCode) {
		this.returncode = returnCode;
	}

	/**
	 * @return the startAt
	 */
	public int getStartAt() {
		return startAt;
	}

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public String getMessage() {
	    return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}

	
	private static <E> ResultRO<E> createOutput(int returnCode, String message, E data) {
		ResultRO<E> out = new ResultRO<E>();
		out.setReturnCode(returnCode);
		out.setMessage(message);
		out.setData(data);
		return out;
	}
	
	public static <E> ResultRO<E> createSuccess() {
	return createOutput(1, null, null);
	}
	public static <E> ResultRO<E> createSuccess(E result) {
		return createOutput(1, null, result);
	}
	public static <E> ResultRO<E> createSuccess(String message) {
		return createOutput(1, message, null);
	}
	public static <E> ResultRO<E> createSuccess(String message, E result) {
		return createOutput(1, message, result);
	}
	
	public static <E> ResultRO<E> createError() {
		return createOutput(3, null, null);
	}

		public static <E> ResultRO<E> createError(E result) {
		return createOutput(3, null, result);
	}
	
	public static <E> ResultRO<E> createError(String message) {
		return createOutput(3, message, null);
	}

    public static <E> ResultRO<E> createError(String message, E result) {
		return createOutput(3, message, result);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
