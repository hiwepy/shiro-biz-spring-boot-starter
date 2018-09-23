package org.apache.shiro.spring.boot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.biz.authc.pam.DefaultModularRealmAuthenticator;
import org.apache.shiro.biz.authz.permission.BitAndWildPermissionResolver;
import org.apache.shiro.biz.authz.permission.DefaultRolePermissionResolver;
import org.apache.shiro.biz.realm.PrincipalRealmListener;
import org.apache.shiro.biz.session.DefaultSessionListener;
import org.apache.shiro.biz.session.mgt.SimpleOnlineSessionFactory;
import org.apache.shiro.biz.session.mgt.eis.SequenceSessionIdGenerator;
import org.apache.shiro.biz.web.filter.authc.listener.LoginListener;
import org.apache.shiro.biz.web.filter.authc.listener.LogoutListener;
import org.apache.shiro.biz.web.mgt.SessionCreationEnabledSubjectFactory;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.AbstractNativeSessionManager;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;

@Configuration
@AutoConfigureBefore(name = { 
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class })
public class ShiroBizWebAutoConfiguration extends AbstractShiroWebConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private ShiroBizProperties bizProperties;

	/**
	 * 登录监听：实现该接口可监听账号登录失败和成功的状态，从而做业务系统自己的事情，比如记录日志
	 */
	@Bean("loginListeners")
	@ConditionalOnMissingBean(name = "loginListeners")
	public List<LoginListener> loginListeners() {

		List<LoginListener> loginListeners = new ArrayList<LoginListener>();
		Map<String, LoginListener> beansOfType = getApplicationContext().getBeansOfType(LoginListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, LoginListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				loginListeners.add(ite.next().getValue());
			}
		}

		return loginListeners;
	}

	/**
	 * Realm 执行监听：实现该接口可监听认证失败和成功的状态，从而做业务系统自己的事情，比如记录日志
	 */
	@Bean("realmListeners")
	@ConditionalOnMissingBean(name = "realmListeners")
	public List<PrincipalRealmListener> realmListeners() {

		List<PrincipalRealmListener> realmListeners = new ArrayList<PrincipalRealmListener>();

		Map<String, PrincipalRealmListener> beansOfType = getApplicationContext()
				.getBeansOfType(PrincipalRealmListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, PrincipalRealmListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				realmListeners.add(ite.next().getValue());
			}
		}

		return realmListeners;
	}

	/**
	 * 注销监听：实现该接口可监听账号注销失败和成功的状态，从而做业务系统自己的事情，比如记录日志
	 */
	@Bean("logoutListeners")
	@ConditionalOnMissingBean(name = "logoutListeners")
	public List<LogoutListener> logoutListeners() {

		List<LogoutListener> logoutListeners = new ArrayList<LogoutListener>();

		Map<String, LogoutListener> beansOfType = getApplicationContext().getBeansOfType(LogoutListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, LogoutListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				logoutListeners.add(ite.next().getValue());
			}
		}

		return logoutListeners;
	}

	@Bean("sessionListeners")
	@ConditionalOnMissingBean(name = "sessionListeners")
	public List<SessionListener> sessionListeners() {
		List<SessionListener> sessionListeners = Lists.newLinkedList();
		Map<String, SessionListener> beansOfType = getApplicationContext().getBeansOfType(SessionListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, SessionListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				sessionListeners.add(ite.next().getValue());
			}
		}
		DefaultSessionListener defSessionListener = new DefaultSessionListener();
		sessionListeners.add(defSessionListener);
		return sessionListeners;
	}

	@Bean("authenticationListeners")
	@ConditionalOnMissingBean(name = "authenticationListeners")
	public List<AuthenticationListener> authenticationListeners() {
		List<AuthenticationListener> authenticationListeners = Lists.newLinkedList();
		Map<String, AuthenticationListener> beansOfType = getApplicationContext().getBeansOfType(AuthenticationListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, AuthenticationListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				authenticationListeners.add(ite.next().getValue());
			}
		}
		return authenticationListeners;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PermissionResolver permissionResolver() {
		return new BitAndWildPermissionResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RolePermissionResolver rolePermissionResolver(ShiroBizProperties bizProperties) {
		DefaultRolePermissionResolver permissionResolver = new DefaultRolePermissionResolver();
		permissionResolver.setDefaultRolePermissions(bizProperties.getDefaultRolePermissions());
		return permissionResolver; 
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CredentialsMatcher credentialsMatcher() {
		return new AllowAllCredentialsMatcher();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected Authenticator authenticator() {
		ModularRealmAuthenticator authenticator = new DefaultModularRealmAuthenticator();
		authenticator.setAuthenticationStrategy(authenticationStrategy());
		authenticator.setAuthenticationListeners(authenticationListeners());
		return authenticator;
	}
    
	@Bean
	@ConditionalOnMissingBean
	protected SessionIdGenerator sessionIdGenerator() {
		return new SequenceSessionIdGenerator();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SessionDAO sessionDAO() {
		// 缓存存在的时候使用外部Session管理器
		if (cacheManager != null) {
			CachingSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
			sessionDAO.setCacheManager(cacheManager);
			sessionDAO.setActiveSessionsCacheName(bizProperties.getActiveSessionsCacheName());
			sessionDAO.setSessionIdGenerator(new SequenceSessionIdGenerator());
			return sessionDAO;
		}
		return new MemorySessionDAO();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SubjectDAO subjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return subjectDAO;
    }

	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SessionStorageEvaluator sessionStorageEvaluator() {
		DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		// 无状态逻辑情况下不持久化session
		sessionStorageEvaluator.setSessionStorageEnabled(bizProperties.isSessionStorageEnabled());
		return sessionStorageEvaluator;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SessionFactory sessionFactory() {
		return new SimpleOnlineSessionFactory();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SessionManager sessionManager() {
		SessionManager sessionManager = super.sessionManager();
		if (sessionManager instanceof AbstractSessionManager) {
			AbstractSessionManager absSessionManager = (AbstractSessionManager) sessionManager;
			absSessionManager.setGlobalSessionTimeout(bizProperties.getSessionTimeout());
		}
		if (sessionManager instanceof AbstractNativeSessionManager) {
			AbstractNativeSessionManager sManager = (AbstractNativeSessionManager) sessionManager;
			sManager.setSessionListeners(sessionListeners());
		}
		if (sessionManager instanceof AbstractValidatingSessionManager) {
			AbstractValidatingSessionManager sManager = (AbstractValidatingSessionManager) sessionManager;
			sManager.setSessionValidationInterval(bizProperties.getSessionValidationInterval());
			sManager.setSessionValidationSchedulerEnabled(bizProperties.isSessionValidationSchedulerEnabled());
		}
		
		if (sessionManager instanceof DefaultSessionManager) {
			DefaultSessionManager defSessionManager = (DefaultSessionManager) sessionManager;
			if (cacheManager != null) {
				defSessionManager.setCacheManager(cacheManager);
			}
			defSessionManager.setSessionDAO(sessionDAO());
			return defSessionManager;
		}
		return sessionManager;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SessionsSecurityManager securityManager(List<Realm> realms) {

		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

		securityManager.setAuthenticator(authenticator());
		securityManager.setAuthorizer(authorizer());
		if (cacheManager != null) {
			securityManager.setCacheManager(cacheManager);
		}
		securityManager.setEventBus(eventBus);
		securityManager.setRealms(realms);
		securityManager.setRememberMeManager(rememberMeManager());
		securityManager.setSessionManager(sessionManager());
		securityManager.setSubjectDAO(subjectDAO());
		securityManager.setSubjectFactory(subjectFactory());
		return securityManager;

	}
	
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected SubjectFactory subjectFactory() {
		return new SessionCreationEnabledSubjectFactory(bizProperties.isSessionCreationEnabled());
	}

	/**
	 * 责任链定义 ：定义Shiro的逻辑处理责任链
	 */
	@Bean
	@ConditionalOnMissingBean
	@Override
	protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		Map<String /* pattert */, String /* Chain names */> pathDefinitions = bizProperties
				.getFilterChainDefinitionMap();
		if (MapUtils.isNotEmpty(pathDefinitions)) {
			chainDefinition.addPathDefinitions(pathDefinitions);
			return chainDefinition;
		}
		chainDefinition.addPathDefinition("/logout", "logout");
		chainDefinition.addPathDefinition("/**", "authc");
		return chainDefinition;
	}

	/*
	 * @Bean
	 * 
	 * @ConditionalOnMissingBean(name = "sessionCookieTemplate")
	 * 
	 * @Override protected Cookie sessionCookieTemplate() { if
	 * (sessionManagerCookieProperties != null &&
	 * StringUtils.hasText(sessionManagerCookieProperties.getName()) &&
	 * sessionManagerCookieProperties.getMaxAge() > 0) { return
	 * buildCookie(sessionManagerCookieProperties.getName(),
	 * sessionManagerCookieProperties.getMaxAge(),
	 * sessionManagerCookieProperties.getPath(),
	 * sessionManagerCookieProperties.getDomain(),
	 * sessionManagerCookieProperties.isSecure()); } return
	 * super.sessionCookieTemplate(); }
	 * 
	 * @Bean
	 * 
	 * @ConditionalOnMissingBean(name = "rememberMeCookieTemplate")
	 * 
	 * @Override protected Cookie rememberMeCookieTemplate() { if
	 * (rememberMeProperties != null &&
	 * StringUtils.hasText(rememberMeProperties.getName()) &&
	 * rememberMeProperties.getMaxAge() > 0) { return
	 * buildCookie(rememberMeProperties.getName(), rememberMeProperties.getMaxAge(),
	 * rememberMeProperties.getPath(), rememberMeProperties.getDomain(),
	 * rememberMeProperties.isSecure()); } return super.rememberMeCookieTemplate();
	 * 
	 * }
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public ShiroBizProperties getBizProperties() {
		return bizProperties;
	}

	public void setBizProperties(ShiroBizProperties bizProperties) {
		this.bizProperties = bizProperties;
	}

}
