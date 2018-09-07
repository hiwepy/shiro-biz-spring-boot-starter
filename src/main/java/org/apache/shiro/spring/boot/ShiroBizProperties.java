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
package org.apache.shiro.spring.boot;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.biz.web.filter.authc.KickoutSessionControlFilter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroBizProperties.PREFIX)
public class ShiroBizProperties {

	public static final String PREFIX = "shiro";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	protected static final long MILLIS_PER_SECOND = 1000;
	protected static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	protected static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/**
	 * Default main session timeout value, equal to {@code 30} minutes.
	 */
	public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT = 30 * MILLIS_PER_MINUTE;
	/**
	 * Default session validation interval value, equal to {@code 30} seconds.
	 */
	private static final long DEFAULT_SESSION_VALIDATION_INTERVAL = 30 * MILLIS_PER_SECOND;

	public static final List<String> DEFAULT_IGNORED = Arrays.asList("/**/favicon.ico", "/assets/**", "/webjars/**");

	/*
	 * ============================== Shiro Basic =================================
	 */

	/**
	 * Enable Shiro.
	 */
	private boolean enabled = false;

	private String accessControlAllowOrigin = "*";
	private String accessControlAllowMethods = "PUT,POST,GET,DELETE,OPTIONS";
	private String accessControlAllowHeaders = "*";
	
	/**
	 * The name of the session cache, defaults to
	 * {@link CachingSessionDAO#ACTIVE_SESSION_CACHE_NAME}.
	 */
	private String activeSessionsCacheName = CachingSessionDAO.ACTIVE_SESSION_CACHE_NAME;
	/**
	 * The cache used by this realm to store AuthorizationInfo instances associated
	 * with individual Subject principals.
	 */
	private boolean authorizationCachingEnabled;
	private String authorizationCacheName;
	
	private boolean authenticationCachingEnabled;
	private String authenticationCacheName;
	/** 是否启用认证授权缓存 */
	private boolean cachingEnabled;
	/** 登录地址：会话不存在时访问的地址 */
	private String loginUrl = "/login.jsp";
	/** 重定向地址：会话注销后的重定向地址 */
	private String redirectUrl = "/";
	/** 系统主页：登录成功后跳转路径 */
	private String successUrl;
	/** 未授权页面：无权限时的跳转路径 */
	private String unauthorizedUrl;
	/** 是否启用用户唯一登陆，如果为true则最后一次登陆会踢出前面的Session */
	private boolean uniqueSessin;
	/** 异常页面：认证失败时的跳转路径 */
	private String failureUrl;
	
	/** Session控制过滤器使用的缓存数据对象名称 */
	private String sessionControlCacheName = KickoutSessionControlFilter.DEFAULT_SESSION_CONTROL_CACHE_NAME;
	/**
     * Global policy determining if Subject sessions may be used to persist Subject state if the Subject's Session
     * does not yet exist.
     */
    private boolean sessionStorageEnabled = true;
	/** Default main session timeout value, equal to {@code 30} minutes. */
	private long sessionTimeout = DEFAULT_GLOBAL_SESSION_TIMEOUT;
	/** Default session validation interval value, equal to {@code 30} seconds. */
	private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;
	/** 是否开启session定时清理任务 */
	private boolean sessionValidationSchedulerEnabled = true;
	/** If Session Stateless */
	private boolean sessionStateless = false;
	
	private Map<String /* pattert */, String /* Chain names */> filterChainDefinitionMap = new LinkedHashMap<String, String>();

	public ShiroBizProperties() {

		for (String ingored : DEFAULT_IGNORED) {
			filterChainDefinitionMap.put(ingored, "anon");
		}
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAccessControlAllowOrigin() {
		return accessControlAllowOrigin;
	}

	public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
		this.accessControlAllowOrigin = accessControlAllowOrigin;
	}

	public String getAccessControlAllowMethods() {
		return accessControlAllowMethods;
	}

	public void setAccessControlAllowMethods(String accessControlAllowMethods) {
		this.accessControlAllowMethods = accessControlAllowMethods;
	}

	public String getAccessControlAllowHeaders() {
		return accessControlAllowHeaders;
	}

	public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
		this.accessControlAllowHeaders = accessControlAllowHeaders;
	}

	public String getActiveSessionsCacheName() {
		return activeSessionsCacheName;
	}

	public void setActiveSessionsCacheName(String activeSessionsCacheName) {
		this.activeSessionsCacheName = activeSessionsCacheName;
	}

	public String getAuthorizationCacheName() {
		return authorizationCacheName;
	}

	public void setAuthorizationCacheName(String authorizationCacheName) {
		this.authorizationCacheName = authorizationCacheName;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}

	public boolean isUniqueSessin() {
		return uniqueSessin;
	}

	public void setUniqueSessin(boolean uniqueSessin) {
		this.uniqueSessin = uniqueSessin;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public String getSessionControlCacheName() {
		return sessionControlCacheName;
	}

	public void setSessionControlCacheName(String sessionControlCacheName) {
		this.sessionControlCacheName = sessionControlCacheName;
	}

	public boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}

	public long getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public long getSessionValidationInterval() {
		return sessionValidationInterval;
	}

	public void setSessionValidationInterval(long sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

	public boolean isSessionValidationSchedulerEnabled() {
		return sessionValidationSchedulerEnabled;
	}

	public void setSessionValidationSchedulerEnabled(boolean sessionValidationSchedulerEnabled) {
		this.sessionValidationSchedulerEnabled = sessionValidationSchedulerEnabled;
	}
	
	public boolean isSessionStateless() {
		return sessionStateless;
	}

	public void setSessionStateless(boolean sessionStateless) {
		this.sessionStateless = sessionStateless;
	}

	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	/**
	 * Returns {@code true} if authorization caching should be utilized if a
	 * {@link CacheManager} has been
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager) configured},
	 * {@code false} otherwise.
	 * <p/>
	 * The default value is {@code true}.
	 *
	 * @return {@code true} if authorization caching should be utilized,
	 *         {@code false} otherwise.
	 */
	public boolean isAuthorizationCachingEnabled() {
		return isCachingEnabled() && authorizationCachingEnabled;
	}

	/**
	 * Sets whether or not authorization caching should be utilized if a
	 * {@link CacheManager} has been
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager) configured},
	 * {@code false} otherwise.
	 * <p/>
	 * The default value is {@code true}.
	 *
	 * @param authenticationCachingEnabled
	 *            the value to set
	 */
	public void setAuthorizationCachingEnabled(boolean authenticationCachingEnabled) {
		this.authorizationCachingEnabled = authenticationCachingEnabled;
		if (authenticationCachingEnabled) {
			setCachingEnabled(true);
		}
	}

	/**
	 * Returns the name of a {@link Cache} to lookup from any available
	 * {@link #getCacheManager() cacheManager} if a cache is not explicitly
	 * configured via {@link #setAuthenticationCache(org.apache.shiro.cache.Cache)}.
	 * <p/>
	 * This name will only be used to look up a cache if authentication caching is
	 * {@link #isAuthenticationCachingEnabled() enabled}.
	 * <p/>
	 * <b>WARNING:</b> Only set this property if safe caching conditions apply, as
	 * documented at the top of this page in the class-level JavaDoc.
	 *
	 * @return the name of a {@link Cache} to lookup from any available
	 *         {@link #getCacheManager() cacheManager} if a cache is not explicitly
	 *         configured via
	 *         {@link #setAuthenticationCache(org.apache.shiro.cache.Cache)}.
	 * @see #isAuthenticationCachingEnabled()
	 * @since 1.2
	 */
	public String getAuthenticationCacheName() {
		return this.authenticationCacheName;
	}

	/**
	 * Sets the name of a {@link Cache} to lookup from any available
	 * {@link #getCacheManager() cacheManager} if a cache is not explicitly
	 * configured via {@link #setAuthenticationCache(org.apache.shiro.cache.Cache)}.
	 * <p/>
	 * This name will only be used to look up a cache if authentication caching is
	 * {@link #isAuthenticationCachingEnabled() enabled}.
	 *
	 * @param authenticationCacheName
	 *            the name of a {@link Cache} to lookup from any available
	 *            {@link #getCacheManager() cacheManager} if a cache is not
	 *            explicitly configured via
	 *            {@link #setAuthenticationCache(org.apache.shiro.cache.Cache)}.
	 * @see #isAuthenticationCachingEnabled()
	 * @since 1.2
	 */
	public void setAuthenticationCacheName(String authenticationCacheName) {
		this.authenticationCacheName = authenticationCacheName;
	}

	/**
	 * Returns {@code true} if authentication caching should be utilized if a
	 * {@link CacheManager} has been
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager) configured},
	 * {@code false} otherwise.
	 * <p/>
	 * The default value is {@code true}.
	 *
	 * @return {@code true} if authentication caching should be utilized,
	 *         {@code false} otherwise.
	 */
	public boolean isAuthenticationCachingEnabled() {
		return this.authenticationCachingEnabled && isCachingEnabled();
	}

	/**
	 * Sets whether or not authentication caching should be utilized if a
	 * {@link CacheManager} has been
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager) configured},
	 * {@code false} otherwise.
	 * <p/>
	 * The default value is {@code false} to retain backwards compatibility with
	 * Shiro 1.1 and earlier.
	 * <p/>
	 * <b>WARNING:</b> Only set this property to {@code true} if safe caching
	 * conditions apply, as documented at the top of this page in the class-level
	 * JavaDoc.
	 *
	 * @param authenticationCachingEnabled
	 *            the value to set
	 */
	public void setAuthenticationCachingEnabled(boolean authenticationCachingEnabled) {
		this.authenticationCachingEnabled = authenticationCachingEnabled;
		if (authenticationCachingEnabled) {
			setCachingEnabled(true);
		}
	}

	/**
	 * Returns {@code true} if caching should be used if a {@link CacheManager} has
	 * been {@link #setCacheManager(org.apache.shiro.cache.CacheManager)
	 * configured}, {@code false} otherwise.
	 * <p/>
	 * The default value is {@code true} since the large majority of Realms will
	 * benefit from caching if a CacheManager has been configured. However,
	 * memory-only realms should set this value to {@code false} since they would
	 * manage account data in memory already lookups would already be as efficient
	 * as possible.
	 *
	 * @return {@code true} if caching will be globally enabled if a
	 *         {@link CacheManager} has been configured, {@code false} otherwise
	 */
	public boolean isCachingEnabled() {
		return cachingEnabled && !sessionStateless;
	}

	/**
	 * Sets whether or not caching should be used if a {@link CacheManager} has been
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager) configured}.
	 *
	 * @param cachingEnabled
	 *            whether or not to globally enable caching for this realm.
	 */
	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}

}
