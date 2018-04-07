/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * 
 */
package org.apache.shiro.spring.boot.cache.redis;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis CacheManager Wrapper
 * @author wangjie (https://github.com/wj596)
 * @author <a href="https://github.com/vindell">vindell</a>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisCacheManager extends AbstractCacheManager {

	private RedisTemplate redisTemplate;

	public RedisCacheManager() {
	}

	public RedisCacheManager(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setRedisTimeout(String cacheName, long timeout) {
		this.redisTemplate.expire(cacheName, timeout, TimeUnit.SECONDS);
	}

	@Override
	protected Cache createCache(String cacheName) throws CacheException {
		return new RedisCache(redisTemplate.opsForHash(), cacheName);
	}

}