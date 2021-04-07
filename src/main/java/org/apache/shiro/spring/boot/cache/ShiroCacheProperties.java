/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

@ConfigurationProperties(ShiroCacheProperties.PREFIX)
public class ShiroCacheProperties {

	public static final String PREFIX = "shiro.cache";

	/*
	 * ============================= Shiro Cache  ============================
	 */
	
	/**
	 * 缓存类型：memory、spring、ehcache、hazelcast、j2cache、redis、redisson
	 */
	private String type = "memory";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
