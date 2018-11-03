/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
package org.apache.shiro.spring.boot.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroKaptchaProperties.PREFIX)
public class ShiroKaptchaProperties {

	public static final String PREFIX = "shiro.kaptcha";

	protected static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;
	
	/*
	 * ============================= Shiro Basic Captcha  ============================
	 */

	/**
	 * Enable Shiro Captcha.
	 */
	private boolean enabled = false;
	/** 失败重试次数：超出限制后要求输入验证码 . */
	private int retryTimesWhenAccessDenied = 3;
	/**
	 * 验证码缓存的key
	 */
	private String storeKey;
	/**
	 * 验证码创建时间缓存的key
	 */
	private String dateStoreKey;
	/**
	 * 验证码有效期；单位（毫秒），默认 60000
	 */
	private long timeout = DEFAULT_CAPTCHA_TIMEOUT;
	/**
	 * 验证码缓存名称
	 */
	private String cacheKey;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getRetryTimesWhenAccessDenied() {
		return retryTimesWhenAccessDenied;
	}

	public void setRetryTimesWhenAccessDenied(int retryTimesWhenAccessDenied) {
		this.retryTimesWhenAccessDenied = retryTimesWhenAccessDenied;
	}

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

	public String getDateStoreKey() {
		return dateStoreKey;
	}

	public void setDateStoreKey(String dateStoreKey) {
		this.dateStoreKey = dateStoreKey;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
}
