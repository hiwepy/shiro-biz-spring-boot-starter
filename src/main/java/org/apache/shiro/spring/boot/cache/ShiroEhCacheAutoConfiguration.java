package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass({net.sf.ehcache.CacheManager.class, org.apache.shiro.cache.CacheManager.class })
@ConditionalOnProperty(prefix = "spring.cache", value = "type", havingValue = "ehcache")
public class ShiroEhCacheAutoConfiguration {
 
	@Bean
	public CacheManager shiroCacheManager(org.springframework.cache.CacheManager cacheManager) {
		// 强制类型转换
		EhCacheCacheManager ehCacheCacheManager = (EhCacheCacheManager) cacheManager;
		// 构造Shiro的CacheManager
		EhCacheManager shiroCacheManager = new EhCacheManager();
		// 给 CacheManager设置值
		shiroCacheManager.setCacheManager(ehCacheCacheManager.getCacheManager());
		return shiroCacheManager;
	}
	 
}
