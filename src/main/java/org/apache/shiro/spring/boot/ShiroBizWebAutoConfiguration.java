package org.apache.shiro.spring.boot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.biz.authc.pam.DefaultModularRealmAuthenticator;
import org.apache.shiro.biz.realm.PrincipalRealmListener;
import org.apache.shiro.biz.web.filter.authc.listener.LoginListener;
import org.apache.shiro.biz.web.filter.authc.listener.LogoutListener;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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

@Configuration
@AutoConfigureBefore(name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class})
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
	
	@Bean
    @ConditionalOnMissingBean
    @Override
    protected SubjectFactory subjectFactory() {
        return super.subjectFactory();
    }
	
	@Override
	protected Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new DefaultModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(authenticationStrategy());
        return authenticator;
    }

	@Bean
	@Override
	protected SessionManager sessionManager() {
		SessionManager sessionManager = super.sessionManager();
		if (sessionManager instanceof DefaultWebSessionManager) {
			DefaultWebSessionManager webSessionManager = (DefaultWebSessionManager) sessionManager;
			webSessionManager.setCacheManager(cacheManager);
			return webSessionManager;

		}
		return sessionManager;
	}
	
	@Bean
	@Override
	protected SessionsSecurityManager securityManager(List<Realm> realms) {
		return super.securityManager(realms);
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
	

	/*@Bean
    @ConditionalOnMissingBean(name = "sessionCookieTemplate")
	@Override
	protected Cookie sessionCookieTemplate() {
		if (sessionManagerCookieProperties != null && StringUtils.hasText(sessionManagerCookieProperties.getName())
				&& sessionManagerCookieProperties.getMaxAge() > 0) {
			return buildCookie(sessionManagerCookieProperties.getName(), sessionManagerCookieProperties.getMaxAge(),
					sessionManagerCookieProperties.getPath(), sessionManagerCookieProperties.getDomain(),
					sessionManagerCookieProperties.isSecure());
		}
		return super.sessionCookieTemplate();
	}

	@Bean
    @ConditionalOnMissingBean(name = "rememberMeCookieTemplate")
	@Override
	protected Cookie rememberMeCookieTemplate() {
		if (rememberMeProperties != null && StringUtils.hasText(rememberMeProperties.getName())
				&& rememberMeProperties.getMaxAge() > 0) {
			return buildCookie(rememberMeProperties.getName(), rememberMeProperties.getMaxAge(),
					rememberMeProperties.getPath(), rememberMeProperties.getDomain(), rememberMeProperties.isSecure());
		}
		return super.rememberMeCookieTemplate();

	}
*/
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
