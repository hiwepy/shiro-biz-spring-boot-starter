package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnBean(IRedisManager.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "redis")
@EnableConfigurationProperties({ ShiroRedisCacheProperties.class })
public class ShiroRedisCacheConfiguration {
	
	@Bean
	public CacheManager shiroCacheManager(ShiroRedisCacheProperties properties,
			IRedisManager redisManager) {
		
		RedisCacheManager cacheManager = new RedisCacheManager();
		
		cacheManager.setExpire(properties.getExpire()); 
		cacheManager.setKeyPrefix(properties.getKeyPrefix());
		//cacheManager.setKeySerializer(keySerializer);
		cacheManager.setPrincipalIdFieldName(properties.getPrincipalIdFieldName());
		cacheManager.setRedisManager(redisManager);
		//cacheManager.setValueSerializer(valueSerializer);
		
		return cacheManager;
	}

}
