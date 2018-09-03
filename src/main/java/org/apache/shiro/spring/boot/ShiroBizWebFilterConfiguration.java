package org.apache.shiro.spring.boot;

import java.util.List;

import org.apache.shiro.biz.web.filter.HttpServletSessionControlFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionExpiredFilter;
import org.apache.shiro.biz.web.filter.authc.listener.LogoutListener;
import org.apache.shiro.spring.boot.biz.ShiroBizFilterFactoryBean;
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
 * 默认拦截器
 * <p>Shiro内置了很多默认的拦截器，比如身份验证、授权等相关的。默认拦截器可以参考org.apache.shiro.web.filter.mgt.DefaultFilter中的枚举拦截器：&nbsp;&nbsp;</p>
 * <table style="border-collapse: collapse; border: 1px; width: 100%; table-layout: fixed;" class="aa" cellspacing="0" cellpadding="0" border="1">
 *	  <tbody>
 *	  	<tr>
 *			<td style="padding: 0cm 5.4pt 0cm 5.4pt; width: 150px;">
 *			<p class="MsoNormal">默认拦截器名</p>
 *			</td>
 *			<td style="padding: 0cm 5.4pt 0cm 5.4pt; width: 215px;">
 *			<p class="MsoNormal">拦截器类</p>
 *			</td>
 *			<td style="padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">说明（括号里的表示默认值）</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal"><strong>身份验证相关的</strong></p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">authc</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authc</p>
 *			<p class="MsoNormal">.FormAuthenticationFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">基于表单的拦截器；如“/**=authc”，如果没有登录会跳到相应的登录页面登录；主要属性：usernameParam：表单提交的用户名参数名（ username）； &nbsp;passwordParam：表单提交的密码参数名（password）； rememberMeParam：表单提交的密码参数名（rememberMe）；&nbsp; loginUrl：登录页面地址（/login.jsp）；successUrl：登录成功后的默认重定向地址； failureKeyAttribute：登录失败后错误信息存储key（shiroLoginFailure）；</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">authcBasic</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authc</p>
 *			<p class="MsoNormal">.BasicHttpAuthenticationFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">Basic HTTP身份验证拦截器，主要属性： applicationName：弹出登录框显示的信息（application）；</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">logout</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authc</p>
 *			<p class="MsoNormal">.LogoutFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址（/）;示例“/logout=logout”</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">user</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authc</p>
 *			<p class="MsoNormal">.UserFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">用户拦截器，用户已经身份验证/记住我登录的都可；示例“/**=user”</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">anon</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authc</p>
 *			<p class="MsoNormal">.AnonymousFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤；示例“/static/**=anon”</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal"><strong>授权相关的</strong></p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">roles</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authz</p>
 *			<p class="MsoNormal">.RolesAuthorizationFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">角色授权拦截器，验证用户是否拥有所有角色；主要属性： loginUrl：登录页面地址（/login.jsp）；unauthorizedUrl：未授权后重定向的地址；示例“/admin/**=roles[admin]”</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">perms</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authz</p>
 *			<p class="MsoNormal">.PermissionsAuthorizationFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">权限授权拦截器，验证用户是否拥有所有权限；属性和roles一样；示例“/user/**=perms["user:create"]”</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">port</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authz</p>
 *			<p class="MsoNormal">.PortFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">端口拦截器，主要属性：port（80）：可以通过的端口；示例“/test= port[80]”，如果用户访问该页面是非80，将自动将请求端口改为80并重定向到该80端口，其他路径/参数等都一样</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">rest</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authz</p>
 *			<p class="MsoNormal">.HttpMethodPermissionFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">rest风格拦截器，自动根据请求方法构建权限字符串（GET=read, POST=create,PUT=update,DELETE=delete,HEAD=read,TRACE=read,OPTIONS=read, MKCOL=create）构建权限字符串；示例“/users=rest[user]”，会自动拼出“user:read,user:create,user:update,user:delete”权限字符串进行权限匹配（所有都得匹配，isPermittedAll）；</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">ssl</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.authz</p>
 *			<p class="MsoNormal">.SslFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">SSL拦截器，只有请求协议是https才能通过；否则自动跳转会https端口（443）；其他和port拦截器一样；</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal"><strong>其他</strong></p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">&nbsp;</p>
 *			</td>
 *		</tr>
 *		<tr>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">noSessionCreation</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">org.apache.shiro.web.filter.session</p>
 *			<p class="MsoNormal">.NoSessionCreationFilter</p>
 *			</td>
 *			<td style=" padding: 0cm 5.4pt 0cm 5.4pt;">
 *			<p class="MsoNormal">不创建会话拦截器，调用 subject.getSession(false)不会有什么问题，但是如果 subject.getSession(true)将抛出 DisabledSessionException异常；</p>
 *			</td>
 *		</tr>
 *	  </tbody>
 * </table>
 * 自定义Filter通过@Bean注解后，被Spring Boot自动注册到了容器的Filter chain中，这样导致的结果是，所有URL都会被自定义Filter过滤，而不是Shiro中配置的一部分URL。
 * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
 * http://www.jianshu.com/p/bf79fdab9c19
 */
@Configuration
@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class })
public class ShiroBizWebFilterConfiguration extends AbstractShiroWebFilterConfiguration {

	@Autowired
	private ShiroBizProperties bizProperties;
	
	/**
	 * 系统登录注销过滤器；默认：org.apache.shiro.spring.boot.cas.filter.CasLogoutFilter
	 */
	@Bean("logout")
	@ConditionalOnMissingBean(name = "logout")
	public FilterRegistrationBean<BizLogoutFilter> logoutFilter(List<LogoutListener> logoutListeners){
		
		FilterRegistrationBean<BizLogoutFilter> registration = new FilterRegistrationBean<BizLogoutFilter>(); 
		BizLogoutFilter logoutFilter = new BizLogoutFilter();
		
		//登录注销后的重定向地址：直接进入登录页面
		logoutFilter.setRedirectUrl(bizProperties.getRedirectUrl());
		registration.setFilter(logoutFilter);
		//注销监听：实现该接口可监听账号注销失败和成功的状态，从而做业务系统自己的事情，比如记录日志
		logoutFilter.setLogoutListeners(logoutListeners);
	    
	    registration.setEnabled(false); 
	    return registration;
	}
	
	/**
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
	
	/**
	 * 默认的Session控制实现，解决唯一登录问题
	 */
	@Bean("sessionControl")
	@ConditionalOnMissingBean(name = "sessionControl")
	public FilterRegistrationBean<HttpServletSessionControlFilter> sessionControlFilter(){
		
		FilterRegistrationBean<HttpServletSessionControlFilter> registration = new FilterRegistrationBean<HttpServletSessionControlFilter>();
		registration.setFilter(new HttpServletSessionControlFilter() {

			@Override
			protected String getSessionControlCacheKey(Object principal) {
				return bizProperties.getSessionControlCacheName();
			}
			
		});
		
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
