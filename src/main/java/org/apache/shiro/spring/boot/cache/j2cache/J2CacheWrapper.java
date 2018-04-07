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
package org.apache.shiro.spring.boot.cache.j2cache;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;

/**
 * 封装j2cache为shiro的Cache接口
 */
@SuppressWarnings("unchecked")
public class J2CacheWrapper<V> implements Cache<String, V> {
	
	protected String region;
	
	protected CacheChannel channel;
	
	public J2CacheWrapper(String region, CacheChannel channel) {
		this.region = region;
		this.channel = channel;
	}

	public V get(String  key) throws CacheException {
		CacheObject val = this.channel.get(region, key);
		if (val == null)
			return null;
		return (V) val.getValue();
	}

	public V put(String key, V value) throws CacheException {
		this.channel.set(region, key, value);
		return null;
	}

	public V remove(String key) throws CacheException {
		this.channel.evict(region, key);
		return null;
	}

	public void clear() throws CacheException {
		this.channel.clear(region);
	}

	public int size() {
		return this.channel.keys(region).size();
	}

	public Set<String> keys() {
		return new HashSet<String>(this.channel.keys(region));
	}

	public Collection<V> values() {
		List<V> list = new ArrayList<V>();
		for (String k : keys()) {
			list.add(get(k));
		}
		return list;
	}

}
