/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
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

import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroRememberMeManagerCookieProperties.PREFIX)
public class ShiroRememberMeManagerCookieProperties {

	public static final String PREFIX = "shiro.rememberMeManager.cookie";
	
	/**
     * The number of seconds in one week (= 60 * 60 * 24 * 7).
     */
	private static final Integer ONE_WEEK = 60 * 60 * 24 * 7;
	// 默认记住我cookie加密秘钥
	private static final String DEFAULT_REMEMBERME_SECRETKEY = "1a2b5c8e6c9e5g2s";
	
    // RememberMe Cookie info

    private String name = CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME;

    private int maxAge = ONE_WEEK;

    private String domain;

    private String path;

    private boolean secure = false;
    
    private String secretKey = DEFAULT_REMEMBERME_SECRETKEY;// rememberMe秘钥

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
