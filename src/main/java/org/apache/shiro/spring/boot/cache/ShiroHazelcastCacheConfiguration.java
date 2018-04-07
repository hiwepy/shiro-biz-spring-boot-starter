package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.hazelcast.cache.HazelcastCacheManager;
import org.apache.shiro.spring.boot.cache.hazelcast.HazelcastConfig;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(com.hazelcast.core.Hazelcast.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "spring")
@EnableConfigurationProperties({ HazelcastConfig.class })
public class ShiroHazelcastCacheConfiguration {

	@Bean
	public CacheManager shiroCacheManager(HazelcastConfig config) {

		HazelcastCacheManager cacheManager = new HazelcastCacheManager();

		cacheManager.setConfig(config);

		return cacheManager;
	}

}
