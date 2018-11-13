package org.apache.shiro.spring.boot;

import java.util.List;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.utils.StringUtils;
import org.apache.shiro.biz.web.filter.HttpServletRequestEscapeHtml4Filter;
import org.apache.shiro.biz.web.filter.HttpServletRequestHeaderFilter;
import org.apache.shiro.biz.web.filter.HttpServletRequestMethodFilter;
import org.apache.shiro.biz.web.filter.HttpServletRequestReferrerFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionDequeFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionExpiredFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionStatusFilter;
import org.apache.shiro.biz.web.filter.authc.listener.LogoutListener;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.boot.biz.ShiroBizFilterFactoryBean;
import org.apache.shiro.spring.boot.biz.ShiroHttpServletHeaderProperties;
import org.apache.shiro.spring.boot.biz.ShiroHttpServletReferrerProperties;
import org.apache.shiro.spring.boot.biz.authc.BizLogoutFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * <p>Shiro内置了很多默认的拦截器，比如身份验证、授权等相关的。默认拦截器可以参考org.apache.shiro.web.filter.mgt.DefaultFilter中的枚举拦截器：&nbsp;&nbsp;</p>
 * <p>自定义Filter通过@Bean注解后，被Spring Boot自动注册到了容器的Filter chain中，这样导致的结果是，所有URL都会被自定义Filter过滤，而不是Shiro中配置的一部分URL。</p>
 * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
 * http://www.jianshu.com/p/bf79fdab9c19
 */
@Configuration
@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class, ShiroHttpServletHeaderProperties.class, ShiroHttpServletReferrerProperties.class })
public class ShiroBizWebFilterConfiguration extends AbstractShiroWebFilterConfiguration {

	@Autowired
	private ShiroBizProperties bizProperties;
	
	/*
	 * 系统登录注销过滤器；默认：org.apache.shiro.spring.boot.cas.filter.CasLogoutFilter
	 */
	@Bean("logout")
	@ConditionalOnMissingBean(name = "logout")
	public FilterRegistrationBean<BizLogoutFilter> logoutFilter(List<LogoutListener> logoutListeners){
		
		FilterRegistrationBean<BizLogoutFilter> registration = new FilterRegistrationBean<BizLogoutFilter>(); 
		BizLogoutFilter logoutFilter = new BizLogoutFilter();
		
		//注销监听：实现该接口可监听账号注销失败和成功的状态，从而做业务系统自己的事情，比如记录日志
		logoutFilter.setLogoutListeners(logoutListeners);
		logoutFilter.setPostOnlyLogout(bizProperties.isPostOnlyLogout());
		//登录注销后的重定向地址：直接进入登录页面
		logoutFilter.setRedirectUrl(bizProperties.getRedirectUrl());
		
		registration.setFilter(logoutFilter);
	    registration.setEnabled(false); 
	    return registration;
	}
	
	@Bean("escapeHtml4")
	@ConditionalOnMissingBean(name = "escapeHtml4")
	public FilterRegistrationBean<HttpServletRequestEscapeHtml4Filter> escapeHtml4Filter(){
		FilterRegistrationBean<HttpServletRequestEscapeHtml4Filter> registration = new FilterRegistrationBean<HttpServletRequestEscapeHtml4Filter>();
		registration.setFilter(new HttpServletRequestEscapeHtml4Filter());
	    registration.setEnabled(false); 
	    return registration;
	}
	
	@Bean("headers")
	@ConditionalOnMissingBean(name = "headers")
	public FilterRegistrationBean<HttpServletRequestHeaderFilter> headerFilter(ShiroHttpServletHeaderProperties properties){
		
		FilterRegistrationBean<HttpServletRequestHeaderFilter> registration = new FilterRegistrationBean<HttpServletRequestHeaderFilter>();
		HttpServletRequestHeaderFilter headFilter = new HttpServletRequestHeaderFilter(properties);
		registration.setFilter(headFilter);
	    registration.setEnabled(false); 
	    return registration;
	}
	
	@Bean("methods")
	@ConditionalOnMissingBean(name = "methods")
	public FilterRegistrationBean<HttpServletRequestMethodFilter> methodFilter(ShiroHttpServletHeaderProperties properties){
		
		FilterRegistrationBean<HttpServletRequestMethodFilter> registration = new FilterRegistrationBean<HttpServletRequestMethodFilter>();
		
		HttpServletRequestMethodFilter methodFilter = new HttpServletRequestMethodFilter();
		methodFilter.setAllowedHTTPMethods(StringUtils.tokenizeToStringArray(properties.getAccessControlAllowMethods()));
		
		registration.setFilter(methodFilter);
	    registration.setEnabled(false); 
	    return registration;
	}
	
	@Bean("referrers")
	@ConditionalOnMissingBean(name = "referrers")
	public FilterRegistrationBean<HttpServletRequestReferrerFilter> referrerFilter(ShiroHttpServletReferrerProperties properties){
		
		FilterRegistrationBean<HttpServletRequestReferrerFilter> registration = new FilterRegistrationBean<HttpServletRequestReferrerFilter>();
		HttpServletRequestReferrerFilter referrerFilter = new HttpServletRequestReferrerFilter(properties);
		registration.setFilter(referrerFilter);
	    registration.setEnabled(false); 
	    return registration;
	}
	
	/*
	 * 默认的Session在线状态过滤器 ：解决回话被强制登出问题
	 */
	@Bean("sessionStatus")
	@ConditionalOnMissingBean(name = "sessionStatus")
	public FilterRegistrationBean<HttpServletSessionStatusFilter> sessionOnlineFilter(SessionManager sessionManager){
		
		FilterRegistrationBean<HttpServletSessionStatusFilter> registration = new FilterRegistrationBean<HttpServletSessionStatusFilter>();
		
		HttpServletSessionStatusFilter sessionOnlineFilter = new HttpServletSessionStatusFilter();
		sessionOnlineFilter.setLoginUrl(bizProperties.getLoginUrl());
		sessionOnlineFilter.setSessionManager(sessionManager);
		
		registration.setFilter(sessionOnlineFilter);
	    registration.setEnabled(false); 
	    return registration;
	}
	
	/*
	 * 默认的Session控制实现，解决唯一登录问题
	 */
	@Bean("sessionDeque")
	@ConditionalOnMissingBean(name = "sessionDeque")
	public FilterRegistrationBean<HttpServletSessionDequeFilter> sessionDequeFilter(CacheManager cacheManager, SessionManager sessionManager){
		
		FilterRegistrationBean<HttpServletSessionDequeFilter> registration = new FilterRegistrationBean<HttpServletSessionDequeFilter>();
		
		HttpServletSessionDequeFilter sessionDequeFilter = new HttpServletSessionDequeFilter() {

			@Override
			protected String getSessionDequeCacheKey(Object principal) {
				ShiroPrincipal sp = (ShiroPrincipal) principal;
				return sp.getUserid();
			}
			
		};
		
		sessionDequeFilter.setCacheManager(cacheManager);
		sessionDequeFilter.setKickoutFirst(bizProperties.isKickoutFirst());
		sessionDequeFilter.setSessionDequeCacheName(bizProperties.getSessionDequeCacheName());
		sessionDequeFilter.setSessionManager(sessionManager);
		sessionDequeFilter.setSessionMaximumKickout(bizProperties.getSessionMaximumKickout());
		sessionDequeFilter.setRedirectUrl(bizProperties.getRedirectUrl());
		
		registration.setFilter(sessionDequeFilter);
		
	    registration.setEnabled(false); 
	    return registration;
	}
	
	/*
	 * 默认的Session过期过滤器 ：解决Ajax请求期间会话过期异常处理
	 */
	@Bean("sessionExpired")
	@ConditionalOnMissingBean(name = "sessionExpired")
	public FilterRegistrationBean<HttpServletSessionExpiredFilter> sessionExpiredFilter(){
		
		FilterRegistrationBean<HttpServletSessionExpiredFilter> registration = new FilterRegistrationBean<HttpServletSessionExpiredFilter>();
		registration.setFilter(new HttpServletSessionExpiredFilter());
		
	    registration.setEnabled(false); 
	    return registration;
	}

	
	@Bean
    @ConditionalOnMissingBean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
		
		ShiroFilterFactoryBean filterFactoryBean = new ShiroBizFilterFactoryBean();
        
		//登录地址：会话不存在时访问的地址
        filterFactoryBean.setLoginUrl(bizProperties.getLoginUrl());
  		//系统主页：登录成功后跳转路径
  		filterFactoryBean.setSuccessUrl(bizProperties.getSuccessUrl());
  		//异常页面：无权限时的跳转路径
  		filterFactoryBean.setUnauthorizedUrl(bizProperties.getUnauthorizedUrl());
  		
  		//必须设置 SecurityManager
 		filterFactoryBean.setSecurityManager(securityManager);
 		//拦截规则
 		filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
      
 		return filterFactoryBean;
        
    }

    @Bean(name = "filterShiroFilterRegistrationBean")
    @ConditionalOnMissingBean(name = "filterShiroFilterRegistrationBean")
    protected FilterRegistrationBean<AbstractShiroFilter> filterShiroFilterRegistrationBean() throws Exception {

        FilterRegistrationBean<AbstractShiroFilter> filterRegistrationBean = new FilterRegistrationBean<AbstractShiroFilter>();
        filterRegistrationBean.setFilter((AbstractShiroFilter) shiroFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);

        return filterRegistrationBean;
    }

}
