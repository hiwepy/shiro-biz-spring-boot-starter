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
package org.apache.shiro.spring.boot.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EhCache2 配置
 * @author <a href="https://github.com/vindell">vindell</a>
 */
@ConfigurationProperties(ShiroEhCache2CacheProperties.PREFIX)
public class ShiroEhCache2CacheProperties {

	public static final String PREFIX = "shiro.cache.ehcache2";
	
    /**
     * Classpath file location of the ehcache CacheManager config file.
     */
    private String cacheManagerConfigFile = "classpath:org/apache/shiro/cache/ehcache/ehcache.xml";

	public String getCacheManagerConfigFile() {
		return cacheManagerConfigFile;
	}

	public void setCacheManagerConfigFile(String cacheManagerConfigFile) {
		this.cacheManagerConfigFile = cacheManagerConfigFile;
	}
    
}
