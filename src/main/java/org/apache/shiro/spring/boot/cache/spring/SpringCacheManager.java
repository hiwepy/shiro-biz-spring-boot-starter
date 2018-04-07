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
package org.apache.shiro.spring.boot.cache.spring;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * Spring Framework CacheManager Wrapper
 * @author wangjie (https://github.com/wj596)
 * @author <a href="https://github.com/vindell">vindell</a>
 */
@SuppressWarnings({"rawtypes"})
public class SpringCacheManager extends AbstractCacheManager {

	private final org.springframework.cache.CacheManager delegate;
	
	public SpringCacheManager(org.springframework.cache.CacheManager cacheManager){
		this.delegate = cacheManager;
	}
	
	@Override
	protected Cache createCache(String cacheName) throws CacheException {
		org.springframework.cache.Cache springCache = this.delegate.getCache(cacheName);
		return new SpringCache(cacheName,springCache);
	}
	
}
