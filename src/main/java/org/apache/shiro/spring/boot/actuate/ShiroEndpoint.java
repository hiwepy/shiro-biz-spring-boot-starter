/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot.actuate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import org.apache.shiro.biz.web.Constants;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleOnlineSession;
import org.apache.shiro.session.mgt.SimpleOnlineSession.OnlineStatus;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import com.google.common.collect.Maps;

/**
 * {@link Endpoint} to expose shiro info.
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
@Endpoint(id = "shiro")
public class ShiroEndpoint {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	private final SessionDAO sessionDAO;

	public ShiroEndpoint( SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	@ReadOperation
	public ApplicationShiroSessions shiroSessions() {
		Map<Serializable, SessionDescriptor> shiroSessions = Maps.newHashMap();
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		if (sessions != null) {
			for(Session session : sessions){         

				SessionDescriptor sessionDesc = new SessionDescriptor(session.getId(), session.getHost(),
						dateFormat.format(session.getStartTimestamp()), 
						dateFormat.format(session.getLastAccessTime()),
						session.getTimeout());
	    		
	    		if(session instanceof SimpleOnlineSession) {
	    			SimpleOnlineSession onlineSession = (SimpleOnlineSession) session;
	    			sessionDesc.setStatus(onlineSession.getStatus().getInfo());
	    			sessionDesc.setUserAgent(onlineSession.getUserAgent());
	    			sessionDesc.setSystemHost(onlineSession.getSystemHost());
	    		}
	    		if(Boolean.TRUE.equals(session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY))) {
	    			sessionDesc.setStatus(OnlineStatus.FORCE_LOGOUT.getInfo());
	    		} 
	    		shiroSessions.put(session.getId(), sessionDesc);
			}
		}
		
		return new ApplicationShiroSessions(shiroSessions);
	}

	@ReadOperation
	public SessionDescriptor getSession(@Selector String sessionId) {
		Session session = this.sessionDAO.readSession(sessionId);
		if (session == null) {
			return null;
		}
		return new SessionDescriptor(session);
	}

	@WriteOperation
	public boolean forceLogout(@Selector String sessionId) {
		try {  
            Session session = this.sessionDAO.readSession(sessionId);  
            if(session != null) {  
            	if(session instanceof SimpleOnlineSession) {
        			SimpleOnlineSession onlineSession = (SimpleOnlineSession) session;
        			onlineSession.setStatus(OnlineStatus.FORCE_LOGOUT);
        		}
                session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);  
            }
            return true;
        } catch (Exception e) {/*ignore*/
        	return false;
        }  
	}
	
	/**
	 * Description of an shiro's {@link Session} sessions, primarily intended for
	 * serialization to JSON.
	 */
	public static final class ApplicationShiroSessions {

		private final Map<Serializable, SessionDescriptor> sessions;

		private ApplicationShiroSessions(Map<Serializable, SessionDescriptor> shiroSessions) {
			this.sessions = shiroSessions;
		}

		public Map<Serializable, SessionDescriptor> getSessions() {
			return this.sessions;
		}

	}
	
	/**
	 * Description of a {@link Session} session, primarily intended for serialization to JSON.
	 */
	public static class SessionDescriptor {

		/** 会话ID */
		private Serializable sessionId;
		/** 主机地址 */
		private String host;
		/** 登录时间 */
		private String startTimestamp;
		/** 最后访问时间 */
		private String lastAccessTime;
		/** 会话多久后过期（毫秒） */
		private long timeout;
		/** 用户浏览器类型 */
		protected String userAgent;
		/** 用户登录时系统IP */
		protected String systemHost;
		/** 在线状态 */
		protected String status;
		/** 已强制退出:1:是，0:否 */
		private String forceLogout;
		
		public SessionDescriptor() {
			super();
		}

		public SessionDescriptor(Serializable sessionId, String host, String startTimestamp, String lastAccessTime,
				long timeout) {
			this.sessionId = sessionId;
			this.host = host;
			this.startTimestamp = startTimestamp;
			this.lastAccessTime = lastAccessTime;
			this.timeout = timeout;
		}

		public SessionDescriptor(Session session) {
			this.sessionId = session.getId();
			this.host = session.getHost();
			this.startTimestamp = dateFormat.format(session.getStartTimestamp());
			this.lastAccessTime = dateFormat.format(session.getLastAccessTime());
			this.timeout = session.getTimeout();
			if(session instanceof SimpleOnlineSession) {
    			SimpleOnlineSession onlineSession = (SimpleOnlineSession) session;
    			this.status = onlineSession.getStatus().getInfo();
    		}
    		if(Boolean.TRUE.equals(session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY))) {
    			this.status =  OnlineStatus.FORCE_LOGOUT.getInfo();
    		}
		}

		public Serializable getSessionId() {
			return sessionId;
		}

		public void setSessionId(Serializable sessionId) {
			this.sessionId = sessionId;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getStartTimestamp() {
			return startTimestamp;
		}

		public void setStartTimestamp(String startTimestamp) {
			this.startTimestamp = startTimestamp;
		}

		public String getLastAccessTime() {
			return lastAccessTime;
		}

		public void setLastAccessTime(String lastAccessTime) {
			this.lastAccessTime = lastAccessTime;
		}

		public long getTimeout() {
			return timeout;
		}

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}

		public String getUserAgent() {
			return userAgent;
		}

		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}
		
		public String getSystemHost() {
			return systemHost;
		}

		public void setSystemHost(String systemHost) {
			this.systemHost = systemHost;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getForceLogout() {
			return forceLogout;
		}

		public void setForceLogout(String forceLogout) {
			this.forceLogout = forceLogout;
		}

	}

}
