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
package org.apache.shiro.spring.boot.biz;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;

import org.apache.shiro.biz.spring.ShiroFilterProxyFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;
@SuppressWarnings("rawtypes")
public class ShiroBizFilterFactoryBean extends ShiroFilterProxyFactoryBean implements ApplicationContextAware  {
	
	private ApplicationContext applicationContext;
	
	//过滤器链：实现对路径规则的拦截过滤
	@Override
	public Map<String, Filter> getFilters() {
		
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();

		Map<String, FilterRegistrationBean> beansOfType = getApplicationContext().getBeansOfType(FilterRegistrationBean.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, FilterRegistrationBean>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, FilterRegistrationBean> entry = ite.next();
				if (entry.getValue().getFilter() instanceof AccessControlFilter) {
					filters.put(entry.getKey(), entry.getValue().getFilter());
				}
			}
		}
		
		filters.putAll(super.getFilters());
		
		return filters;
		
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}