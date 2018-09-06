package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.boot.cache.redis.RedisCacheManager;
import org.apache.shiro.spring.boot.cache.spring.SpringCacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "spring")
public class ShiroSpringCacheConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	public CacheManager shiroCacheManager() {
		// 默认使用Spring CacheManager
		org.springframework.cache.CacheManager springCacheManager = getApplicationContext().getBean(org.springframework.cache.CacheManager.class);
		// EhCache
		if (null != springCacheManager && springCacheManager instanceof org.springframework.cache.ehcache.EhCacheCacheManager) {
			EhCacheManager ehCacheManager = new EhCacheManager();
			ehCacheManager.setCacheManager(((org.springframework.cache.ehcache.EhCacheCacheManager) springCacheManager).getCacheManager());
			return ehCacheManager;
		}
		// Redis
		if (null != springCacheManager && springCacheManager instanceof org.springframework.data.redis.cache.RedisCacheManager) {
			RedisConnectionFactory redisConnectionFactory = getApplicationContext().getBean(RedisConnectionFactory.class);
			GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
			RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
			redisTemplate.setConnectionFactory(redisConnectionFactory);
			redisTemplate.setKeySerializer(jsonSerializer);
			redisTemplate.setHashKeySerializer(jsonSerializer);
			redisTemplate.setBeanClassLoader(this.getClass().getClassLoader());
			redisTemplate.afterPropertiesSet();
			return new RedisCacheManager(redisTemplate);
		}
		// OTHER
		return new SpringCacheManager(springCacheManager);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
