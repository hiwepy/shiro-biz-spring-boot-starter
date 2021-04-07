package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.streamone.shiro.cache.RedissonShiroCacheManager;

@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnBean(RedissonClient.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "redis")
@EnableConfigurationProperties({ ShiroRedissonCacheProperties.class })
public class ShiroRedissonCacheConfiguration {

	@Bean
	@ConditionalOnMissingBean
	protected Codec codec() {
		return new StringCodec();
	}
	
	@Bean
	public CacheManager shiroCacheManager(ShiroRedissonCacheProperties properties,
			RedissonClient redisson, @Autowired(required = false) Codec codec) {
		RedissonShiroCacheManager cacheManager = new RedissonShiroCacheManager(redisson, properties.getConfigLocation(), codec);
		cacheManager.setAllowNullValues(properties.isAllowNullValues());
		cacheManager.setConfig(properties.getConfigMap());
		return cacheManager;
	}

}
