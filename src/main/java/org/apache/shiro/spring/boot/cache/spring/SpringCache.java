/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
package org.apache.shiro.spring.boot.cache.spring;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * Spring Cache Wrapper
 * @author wangjie (https://github.com/wj596)
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
@SuppressWarnings({ "unchecked" })
public class SpringCache<K, V> implements Cache<K, V> {

	private final String cacheName;
	private final org.springframework.cache.Cache delegator;

	public SpringCache(String cacheName, org.springframework.cache.Cache cache) {
		this.cacheName = cacheName;
		this.delegator = cache;
	}

	@Override
	public void clear() throws CacheException {
		this.delegator.clear();
	}

	@Override
	public V get(K key) throws CacheException {
		ValueWrapper wrapper = this.delegator.get(key);
		return wrapper == null ? null : (V) wrapper.get();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		this.delegator.put(key, value);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		V v = this.get(key);
		this.delegator.evict(key);
		return v;
	}

	@Override
	public Set<K> keys() {
		throw new UnsupportedOperationException(" not supported ");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException(" not supported ");
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException(" not supported ");
	}

	@Override
	public String toString() {
		return "cacheName:" + this.cacheName;
	}

}
