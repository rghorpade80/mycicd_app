/**
 * 
 */
package com.pvmsys.brix.graph.session.watcher;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.pvmsys.brix.graph.websession.LoginSessionInfo;
import com.pvmsys.brix.graph.websession.WebSession;

/**
 * @author Swapnali.Suntnure
 *
 */
public class SessionWatcherImpl implements Runnable,SessionWatcher{
	
	private static ConcurrentHashMap<String, LoginSessionInfo> hmLockedInstance = new ConcurrentHashMap<String, LoginSessionInfo>();
	public long lockTimeOut = 1000*60*5;
	private static Logger logger = Logger.getLogger(SessionWatcherImpl.class);
	 public SessionWatcherImpl() {
	  
	 }
	@Override
	public void run() {
		boolean running = true;
		while (running) {
			try {
				Set<Entry<String, LoginSessionInfo>> entSet = hmLockedInstance.entrySet();
				for (Entry<String, LoginSessionInfo> entry : entSet) {
					String Key = entry.getKey();
					LoginSessionInfo lockedSession = entry.getValue();
					long initTime = lockedSession.getTime();
					if ((System.currentTimeMillis() - initTime) > lockTimeOut) {
						WebSession Lsession = lockedSession.getSession() ;
						hmLockedInstance.remove(Key);
						if(!Lsession.isExpired()){
							try {
								logger.error("Session with key " +Key+" is LogOut");
								Lsession.logout();
							} catch (Exception e) {
							}
						}
					}
				}

				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}


	}
	@Override
	public void RegisterSessionInfo(String key, WebSession session) {
		LoginSessionInfo sessioninfo = new LoginSessionInfo(System.currentTimeMillis(), session);
		hmLockedInstance.put(key, sessioninfo);
	}
	@Override
	public void setSessionInfo(String key, WebSession session) {

		LoginSessionInfo sessioninfo = null ;
		if(hmLockedInstance.containsKey(key)){
			sessioninfo =  hmLockedInstance.get(key);
			sessioninfo.setTime(System.currentTimeMillis());
			hmLockedInstance.put(key, sessioninfo);
		}else{
			sessioninfo = new LoginSessionInfo(System.currentTimeMillis(), session);
			hmLockedInstance.put(key, sessioninfo);
		}
	
	}
	@Override
	public void RemoveSessionInfo(String key, WebSession session) {

		LoginSessionInfo sessioninfo = null ;
		if(hmLockedInstance.containsKey(key)){
			sessioninfo =  hmLockedInstance.remove(key);
			long Total = System.currentTimeMillis() - sessioninfo.getStartTime();
			System.out.println("Total Session Time :"+Total);
		}
	
	}

}
