package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.boot.cache.spring.SpringCacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnBean({org.springframework.cache.CacheManager.class})
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "spring")
public class ShiroSpringCacheConfiguration {
	
	@Bean
	public CacheManager shiroCacheManager(org.springframework.cache.CacheManager cacheManager) {
		return new SpringCacheManager(cacheManager);
	}
	
}
