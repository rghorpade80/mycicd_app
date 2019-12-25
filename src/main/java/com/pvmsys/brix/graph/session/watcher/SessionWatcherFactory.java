package com.pvmsys.brix.graph.session.watcher;

public class SessionWatcherFactory {

	private static SessionWatcher sessionWatcher;
	
	private SessionWatcherFactory(){
		
	}
	
	public static SessionWatcher getSessionWatcher(){
		if(sessionWatcher == null)
			sessionWatcher = new SessionWatcherImpl();
		return sessionWatcher;
	}

}
