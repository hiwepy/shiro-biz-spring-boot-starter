package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.boot.cache.redis.RedisCacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "redis")
@SuppressWarnings("rawtypes")
public class ShiroRedisCacheConfiguration {

	@Bean
	public CacheManager shiroCacheManager(RedisTemplate redisTemplate) {
		return new RedisCacheManager(redisTemplate);
	}

}
