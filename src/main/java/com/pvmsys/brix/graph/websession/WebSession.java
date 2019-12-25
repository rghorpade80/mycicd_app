package com.pvmsys.brix.graph.websession;


import java.util.List;
import java.util.concurrent.Callable;

import com.pvmsys.brix.graph.beans.ElementBean;
import com.pvmsys.brix.graph.beans.RelationBean;



public class WebSession {
	
	private WebAppSessionContext webAppSessionContext;
	private List<RelationBean> relationBeans;
	private List<ElementBean> elementBeans;
	@SuppressWarnings("unused")
	private List<ElementBean> elements;
	
	public WebAppSessionContext getSessionContext() {
		return webAppSessionContext;
	}

	public void setSessionContext(WebAppSessionContext webAppSessionContext) {
		this.webAppSessionContext = webAppSessionContext;
	}
	public <T> T execute(Callable<T> command) throws WebAoException{
		return webAppSessionContext.execute(this,command);
		
	}
	
	public boolean isExpired(){
		return webAppSessionContext.isExpired();
	}
	
	public boolean logout(){
		return webAppSessionContext.logout();
	}

	public List<RelationBean> getRelations() {
		return relationBeans;
	}

	public void setRelations(List<RelationBean> relationBeans) {
		this.relationBeans = relationBeans;
	}

	public List<ElementBean> getElements() {
		return elementBeans;
	}

	public void setElements(List<ElementBean> elementBeans) {
		this.elementBeans = elementBeans;
	}

	
	
}
