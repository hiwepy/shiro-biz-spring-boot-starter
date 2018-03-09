/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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


import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.boot.cache.j2cache.J2CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.oschina.j2cache.CacheChannel;

@Configuration
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass({ net.oschina.j2cache.CacheChannel.class, org.apache.shiro.cache.CacheManager.class })
@ConditionalOnProperty(prefix = "spring.cache", value = "type", havingValue = "none")
public class ShiroJ2CacheAutoConfiguration {
	
	@Bean
	public CacheManager shiroCacheManager(@Autowired(required = false) CacheChannel channel) {
		return new J2CacheManager(channel);
	}
	
}
