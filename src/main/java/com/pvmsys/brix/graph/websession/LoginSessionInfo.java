package com.pvmsys.brix.graph.websession;
public class LoginSessionInfo{
			
			private long startTime ;
			private long time;
			private WebSession session ;
			
			public LoginSessionInfo(long time,WebSession session){
				this.startTime = time;
				this.time = time ;
				this.session = session ;
			}
			/**
			 * @return the time
			 */
			public long getStartTime() {
				return startTime;
			}
			
			public long getTime() {
				return time;
			}
			
			
			/**
			 * @param time the time to set
			 */
			public void setTime(long time) {
				this.time = time;
			}
			/**
			 * @return the session
			 */
			public WebSession getSession() {
				return session;
			}
			
		}