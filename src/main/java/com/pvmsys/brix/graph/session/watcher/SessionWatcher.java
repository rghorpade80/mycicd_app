/**
 * 
 */
package com.pvmsys.brix.graph.session.watcher;

import com.pvmsys.brix.graph.websession.WebSession;

/**
 * @author Swapnali.Suntnure
 *
 */
public interface SessionWatcher {
	public void RegisterSessionInfo(String key ,WebSession session);
	public void setSessionInfo(String key ,WebSession session);
	public void RemoveSessionInfo(String key ,WebSession session);
}
