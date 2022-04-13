package org.apache.shiro.spring.boot;

import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.biz.authc.DefaultAuthenticationFailureHandler;
import org.apache.shiro.biz.authz.permission.BitAndWildPermissionResolver;
import org.apache.shiro.biz.authz.permission.DefaultRolePermissionResolver;
import org.apache.shiro.biz.realm.AuthorizingRealmListener;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Configuration
@AutoConfigureBefore({ShiroBizWebAutoConfiguration.class, ShiroAutoConfiguration.class})
@ConditionalOnProperty(name = ShiroBizProperties.PREFIX, matchIfMissing = true)
@EnableConfigurationProperties({ ShiroBizProperties.class })
public class ShiroBizAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Realm 执行监听：实现该接口可监听认证失败和成功的状态，从而做业务系统自己的事情，比如记录日志
	 */
	@Bean("realmListeners")
	@ConditionalOnMissingBean(name = "realmListeners")
	public List<AuthorizingRealmListener> realmListeners() {

		List<AuthorizingRealmListener> realmListeners = new ArrayList<AuthorizingRealmListener>();

		Map<String, AuthorizingRealmListener> beansOfType = getApplicationContext()
				.getBeansOfType(AuthorizingRealmListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, AuthorizingRealmListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				realmListeners.add(ite.next().getValue());
			}
		}

		return realmListeners;
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
	protected DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler() {
		return new DefaultAuthenticationFailureHandler();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
