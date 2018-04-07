package org.apache.shiro.spring.boot;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.boot.biz.ShiroRememberMeManagerCookieProperties;
import org.apache.shiro.spring.boot.biz.ShiroSessionManagerCookieProperties;
import org.apache.shiro.spring.boot.biz.ShiroSessionManagerProperties;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(name = { "org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class, ShiroRememberMeManagerCookieProperties.class,
		ShiroSessionManagerProperties.class, ShiroSessionManagerCookieProperties.class })
public class ShiroBizWebAutoConfiguration extends AbstractShiroWebConfiguration {

	@Autowired
	private ShiroBizProperties bizProperties;
	@Autowired
	private ShiroRememberMeManagerCookieProperties rememberMeProperties;
	@Autowired
	private ShiroSessionManagerProperties sessionManagerProperties;
	@Autowired
	private ShiroSessionManagerCookieProperties sessionManagerCookieProperties;

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

    @Bean
	@Override
    protected SessionManager sessionManager() {
        if (sessionManagerProperties.isUserNative()) {
        	
        	 DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
             webSessionManager.setSessionIdCookieEnabled(sessionManagerProperties.isSessionIdCookieEnabled());
             webSessionManager.setSessionIdUrlRewritingEnabled(sessionManagerProperties.isSessionIdUrlRewritingEnabled());
             webSessionManager.setSessionIdCookie(sessionCookieTemplate());
             
             webSessionManager.setSessionFactory(sessionFactory());
             webSessionManager.setSessionDAO(sessionDAO());
             webSessionManager.setDeleteInvalidSessions(sessionManagerProperties.isDeleteInvalidSessions());

             webSessionManager.setCacheManager(cacheManager);
             
             return webSessionManager;
             
        }
        return new ServletContainerSessionManager();
    }
	
	@Override
	protected Cookie sessionCookieTemplate() {
		return buildCookie(sessionManagerCookieProperties.getName(), sessionManagerCookieProperties.getMaxAge(),
				sessionManagerCookieProperties.getPath(), sessionManagerCookieProperties.getDomain(),
				sessionManagerCookieProperties.isSecure());
	}

	@Override
	protected Cookie rememberMeCookieTemplate() {
		return buildCookie(rememberMeProperties.getName(), rememberMeProperties.getMaxAge(),
				rememberMeProperties.getPath(), rememberMeProperties.getDomain(), rememberMeProperties.isSecure());
	}

}
