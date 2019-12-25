package com.pvmsys.brix.graph.websession;

import java.util.concurrent.Callable;

public interface WebAppSessionContext {
	public <T> T execute(WebSession session,Callable<T> command) throws WebAoException;
	public boolean isExpired();
	boolean logout();
}
