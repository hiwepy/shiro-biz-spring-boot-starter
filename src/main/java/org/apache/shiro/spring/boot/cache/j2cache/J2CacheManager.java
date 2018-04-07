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


import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.springframework.beans.factory.InitializingBean;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;

public class J2CacheManager extends AbstractCacheManager implements Initializable, Destroyable, InitializingBean {
	
	protected CacheChannel channel;

	public J2CacheManager() {
	}
	
	public J2CacheManager(CacheChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public void init() throws ShiroException {
		//do nothing
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (channel == null) {
			channel = J2Cache.getChannel();
		}
	}

	@Override
	public void destroy() throws Exception {
		if (channel != null) {
			channel.close();
		}
	}

	@Override
	protected J2CacheWrapper<Object> createCache(String name) throws CacheException {
		return new J2CacheWrapper<Object>(name, channel);
	}

}
