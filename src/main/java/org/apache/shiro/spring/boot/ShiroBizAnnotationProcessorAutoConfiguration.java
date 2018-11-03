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
package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.config.AbstractShiroAnnotationProcessorConfiguration;
import org.apache.shiro.util.StringUtils;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration"  // shiro-spring-boot-web-starter
})
@Configuration
@ConditionalOnProperty(prefix = ShiroBizAnnotationProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizAnnotationProperties.class })
public class ShiroBizAnnotationProcessorAutoConfiguration extends AbstractShiroAnnotationProcessorConfiguration {

	@Autowired
	private ShiroBizAnnotationProperties properties;
	
	@Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @Override
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		
		// 解决Shiro与Spring Aop 冲突
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = super.defaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(properties.isProxyTargetClass());
        advisorAutoProxyCreator.setExposeProxy(properties.isExposeProxy());
        advisorAutoProxyCreator.setFrozen(properties.isFrozen());
        advisorAutoProxyCreator.setOpaque(properties.isOpaque());
        
        if(properties.isUsePrefix() && StringUtils.hasText(properties.getAdvisorBeanNamePrefix())) {
        	advisorAutoProxyCreator.setUsePrefix(properties.isUsePrefix());
        	advisorAutoProxyCreator.setAdvisorBeanNamePrefix(properties.getAdvisorBeanNamePrefix());
        }
        
        return advisorAutoProxyCreator;
    }
	 
	 
}
