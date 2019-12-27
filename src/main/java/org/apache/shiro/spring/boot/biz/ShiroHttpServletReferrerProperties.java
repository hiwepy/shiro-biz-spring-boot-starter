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
package org.apache.shiro.spring.boot.biz;

import org.apache.shiro.biz.web.filter.HttpServletReferrerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
@ConfigurationProperties(ShiroHttpServletReferrerProperties.PREFIX)
public class ShiroHttpServletReferrerProperties extends HttpServletReferrerProperties {
	
	public static final String PREFIX = "shiro.http.referrer";

}
