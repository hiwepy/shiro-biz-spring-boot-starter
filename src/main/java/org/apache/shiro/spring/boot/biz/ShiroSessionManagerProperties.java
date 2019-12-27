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
package org.apache.shiro.spring.boot.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroSessionManagerProperties.PREFIX)
public class ShiroSessionManagerProperties {

	public static final String PREFIX = "shiro.sessionManager";
	
    private boolean sessionIdCookieEnabled = true;
    
    private boolean sessionIdUrlRewritingEnabled = true;
    
    protected boolean deleteInvalidSessions = true;
    
	public boolean isSessionIdCookieEnabled() {
		return sessionIdCookieEnabled;
	}

	public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
		this.sessionIdCookieEnabled = sessionIdCookieEnabled;
	}

	public boolean isSessionIdUrlRewritingEnabled() {
		return sessionIdUrlRewritingEnabled;
	}

	public void setSessionIdUrlRewritingEnabled(boolean sessionIdUrlRewritingEnabled) {
		this.sessionIdUrlRewritingEnabled = sessionIdUrlRewritingEnabled;
	}

	public boolean isDeleteInvalidSessions() {
		return deleteInvalidSessions;
	}

	public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
		this.deleteInvalidSessions = deleteInvalidSessions;
	}

}
