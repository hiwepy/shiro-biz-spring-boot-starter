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
package org.apache.shiro.spring.boot.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroRedissonCacheProperties.PREFIX)
public class ShiroRedissonCacheProperties {

	public static final String PREFIX = "shiro.cache.redisson";

	private boolean allowNullValues = true;

    private String configLocation;

    private Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();

	public boolean isAllowNullValues() {
		return allowNullValues;
	}

	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public Map<String, CacheConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, CacheConfig> configMap) {
		this.configMap = configMap;
	}
    
}
