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

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.biz.web.filter.HttpServletSessionControlFilter;
import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
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
	 * The value of “Access-Control-Allow-Origin”
	 */
	private String accessControlAllowOrigin = "*";
	/**
	 * The value of “Access-Control-Allow-Methods”
	 */
	private String accessControlAllowMethods = "PUT,POST,GET,DELETE,OPTIONS";
	/**
	 * The value of “Access-Control-Allow-Headers”
	 */
	private String accessControlAllowHeaders = "";
	/**
	 * The name of the session cache, defaults to {@link CachingSessionDAO#ACTIVE_SESSION_CACHE_NAME}.
	 */
	private String activeSessionsCacheName = CachingSessionDAO.ACTIVE_SESSION_CACHE_NAME;
	/**
	 * The cache used by this realm to store AuthorizationInfo instances associated
	 * with individual Subject principals.
	 */
	private boolean authorizationCachingEnabled = false;
	/**
	 * the name of a authorization {@link Cache} to lookup from any available
	 */
	private String authorizationCacheName = "shiro-authorizationCache";
	/**
	 * Whether authentication caching should be utilized
	 */
	private boolean authenticationCachingEnabled = false;
	/**
	 * the name of a authentication {@link Cache} to lookup from any available
	 */
	private String authenticationCacheName = "shiro-authenticationCache";
	/** 
	 * Whether to enable the authentication authorization cache 
	 */
	private boolean cachingEnabled = false;
	/**
	 * Whether to enable captcha
	 */
	private boolean captchaEnabled = false;
	/**
	 * The request parameter name of the captcha
	 */
	private String captchaParam = AbstractTrustableAuthenticatingFilter.DEFAULT_CAPTCHA_PARAM;
	/**
	 * The default permissions for authenticated role
	 */
	private Map<String /* role */, String /* permissions */> defaultRolePermissions = new LinkedHashMap<String, String>();;
	/**
	 * Enable Shiro Biz.
	 */
	private boolean enabled = false;
	/**
	 * Failure Url: Jump path when authentication fails
	 */
	private String failureUrl;
	/**
	 * filter chain
	 */
	private Map<String /* pattert */, String /* Chain names */> filterChainDefinitionMap = new LinkedHashMap<String, String>();
	/**
     * The login url to used to authenticate a user, used when redirecting users if authentication is required.
     */
	private String loginUrl = AccessControlFilter.DEFAULT_LOGIN_URL;
	/**
     * Due to browser pre-fetching, using a GET requests for logout my cause a user to be logged accidentally, for example:
     * out while typing in an address bar.  If <code>postOnlyLogout</code> is <code>true</code>. Only POST requests will cause
     * a logout to occur.
     */
    private boolean postOnlyLogout = false;
    /**
     * The URL to where the user will be redirected after logout.
     */
	private String redirectUrl = LogoutFilter.DEFAULT_REDIRECT_URL;
	/**
	 * The attribute name of Retry Times 
	 */
	private String retryTimesKeyAttribute = AbstractTrustableAuthenticatingFilter.DEFAULT_RETRY_TIMES_KEY_ATTRIBUTE_NAME;
    /** 
     * Maximum number of retry to login . 
     */
	private int retryTimesWhenAccessDenied = 3;
	/** 
	 * The data object cache name of session control filter 
	 */
	private String sessionControlCacheName = HttpServletSessionControlFilter.DEFAULT_SESSION_CONTROL_CACHE_NAME;
	/**
	 * Whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
	 */
	private boolean sessionCreationEnabled = true;
	/**
     * Global policy determining if Subject sessions may be used to persist Subject state if the Subject's Session
     * does not yet exist.
     */
    private boolean sessionStorageEnabled = true;
    /** 
     * Whether stateless session
     */
	private boolean sessionStateless = false;
	/** 
	 * Default main session timeout value, equal to {@code 30} minutes. 
	 */
	private long sessionTimeout = DEFAULT_GLOBAL_SESSION_TIMEOUT;
	/** 
	 * Default session validation interval value, equal to {@code 30} seconds. 
	 */
	private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;
	/** 
	 * Whether to open the session timer cleaner
	 */
	private boolean sessionValidationSchedulerEnabled = true;
	/** 
	 * Redirect address after successful login
	 */
	private String successUrl = AuthenticationFilter.DEFAULT_SUCCESS_URL;
	/**
     * The URL to which users should be redirected if they are denied access to an underlying path or resource,
     * {@code null} by default which will issue a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response
     * (401 Unauthorized).
     */
    private String unauthorizedUrl;
	/** 
	 * Whether to enable user unique login, if true, the last login will kick out the previous Session 
	 */
	private boolean uniqueSessin = false;
	/** 
	 * Whether use native session manager
	 */
	private boolean userNativeSessionManager = false;
	
	public ShiroBizProperties() {

		for (String ingored : DEFAULT_IGNORED) {
			filterChainDefinitionMap.put(ingored, "anon");
		}
		
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
		return cachingEnabled;
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
	
	public boolean isCaptchaEnabled() {
		return captchaEnabled;
	}

	public void setCaptchaEnabled(boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public Map<String, String> getDefaultRolePermissions() {
		return defaultRolePermissions;
	}

	public void setDefaultRolePermissions(Map<String, String> defaultRolePermissions) {
		this.defaultRolePermissions = defaultRolePermissions;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}
	
	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}
	
	/**
     * Returns the login URL used to authenticate a user.
     * <p/>
     * Most Shiro filters use this url
     * as the location to redirect a user when the filter requires authentication.  Unless overridden, the
     * {@link #DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed, which can be overridden via
     * {@link #setLoginUrl(String) setLoginUrl}.
     *
     * @return the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * Sets the login URL used to authenticate a user.
     * <p/>
     * Most Shiro filters use this url as the location to redirect a user when the filter requires
     * authentication.  Unless overridden, the {@link #DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed.
     *
     * @param loginUrl the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
	
	public boolean isPostOnlyLogout() {
		return postOnlyLogout;
	}

	public void setPostOnlyLogout(boolean postOnlyLogout) {
		this.postOnlyLogout = postOnlyLogout;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRetryTimesKeyAttribute() {
		return retryTimesKeyAttribute;
	}

	public void setRetryTimesKeyAttribute(String retryTimesKeyAttribute) {
		this.retryTimesKeyAttribute = retryTimesKeyAttribute;
	}

	public int getRetryTimesWhenAccessDenied() {
		return retryTimesWhenAccessDenied;
	}

	public void setRetryTimesWhenAccessDenied(int retryTimesWhenAccessDenied) {
		this.retryTimesWhenAccessDenied = retryTimesWhenAccessDenied;
	}

	public String getSessionControlCacheName() {
		return sessionControlCacheName;
	}

	public void setSessionControlCacheName(String sessionControlCacheName) {
		this.sessionControlCacheName = sessionControlCacheName;
	}

	/**
     * Returns {@code true} if the constructed {@code Subject} should be allowed to create a session, {@code false}
     * otherwise.  Shiro's configuration defaults to {@code true} as most applications find value in Sessions.
     *
     * @return {@code true} if the constructed {@code Subject} should be allowed to create sessions, {@code false}
     * otherwise.
     */
	public boolean isSessionCreationEnabled(){
		return sessionCreationEnabled;
	}

    /**
     * Sets whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
     *
     * @param sessionCreationEnabled whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
     */
	public void setSessionCreationEnabled(boolean sessionCreationEnabled) {
		this.sessionCreationEnabled = sessionCreationEnabled;
	}
	
	public boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}
	
	public boolean isSessionStateless() {
		return sessionStateless;
	}

	public void setSessionStateless(boolean sessionStateless) {
		this.sessionStateless = sessionStateless;
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
	
	/**
     * Returns the success url to use as the default location a user is sent after logging in.  Typically a redirect
     * after login will redirect to the originally request URL; this property is provided mainly as a fallback in case
     * the original request URL is not available or not specified.
     * <p/>
     * The default value is {@link #DEFAULT_SUCCESS_URL}.
     *
     * @return the success url to use as the default location a user is sent after logging in.
     */
    public String getSuccessUrl() {
        return successUrl;
    }

    /**
     * Sets the default/fallback success url to use as the default location a user is sent after logging in.  Typically
     * a redirect after login will redirect to the originally request URL; this property is provided mainly as a
     * fallback in case the original request URL is not available or not specified.
     * <p/>
     * The default value is {@link #DEFAULT_SUCCESS_URL}.
     *
     * @param successUrl the success URL to redirect the user to after a successful login.
     */
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    /**
     * Returns the URL to which users should be redirected if they are denied access to an underlying path or resource,
     * or {@code null} if a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response should be issued (401 Unauthorized).
     * <p/>
     * The default is {@code null}, ensuring default web server behavior.  Override this default by calling the
     * {@link #setUnauthorizedUrl(String) setUnauthorizedUrl} method with a meaningful path within your application
     * if you would like to show the user a 'nice' page in the event of unauthorized access.
     *
     * @return the URL to which users should be redirected if they are denied access to an underlying path or resource,
     *         or {@code null} if a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response should be issued (401 Unauthorized).
     */
    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    /**
     * Sets the URL to which users should be redirected if they are denied access to an underlying path or resource.
     * <p/>
     * If the value is {@code null} a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response will
     * be issued (401 Unauthorized), retaining default web server behavior.
     * <p/>
     * Unless overridden by calling this method, the default value is {@code null}.  If desired, you can specify a
     * meaningful path within your application if you would like to show the user a 'nice' page in the event of
     * unauthorized access.
     *
     * @param unauthorizedUrl the URL to which users should be redirected if they are denied access to an underlying
     *                        path or resource, or {@code null} to a ensure raw {@link HttpServletResponse#SC_UNAUTHORIZED} response is
     *                        issued (401 Unauthorized).
     */
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

	public boolean isUniqueSessin() {
		return uniqueSessin;
	}

	public void setUniqueSessin(boolean uniqueSessin) {
		this.uniqueSessin = uniqueSessin;
	}

	public boolean isUserNativeSessionManager() {
		return userNativeSessionManager;
	}

	public void setUserNativeSessionManager(boolean userNativeSessionManager) {
		this.userNativeSessionManager = userNativeSessionManager;
	}
	
}
