package org.apache.shiro.spring.boot.captcha;

import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore( name = {
	"com.google.code.kaptcha.spring.boot.KaptchaAutoConfiguration"  // spring-boot-starter-kaptcha
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroKaptchaProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroKaptchaProperties.class })
public class ShiroKaptchaConfiguration extends AbstractShiroWebConfiguration {

	@Autowired
	private ShiroKaptchaProperties captchaProperties;
	
	/*@Bean
	@ConditionalOnClass({ freemarker.template.Configuration.class, freemarker.template.TemplateMethodModelEx.class,
			org.springframework.ui.freemarker.FreeMarkerConfigurationFactory.class })
	@ConditionalOnProperty(prefix = "spring.freemarker", value = "enabled", havingValue = "true")
	public CaptchaEnabled captchaEnabled() {
		return new CaptchaEnabled();
	}*/
	
	@Bean
	@ConditionalOnClass({ com.google.code.kaptcha.spring.boot.ext.KaptchaResolver.class})
	public ShiroKaptchaResolver kaptchaResolver() {
		
		ShiroKaptchaResolver kaptchaResolver = new ShiroKaptchaResolver(); 
		// 初始化参数
		kaptchaResolver.init(captchaProperties.getStoreKey(), 
				captchaProperties.getDateStoreKey(), captchaProperties.getTimeout());
		
		return kaptchaResolver;
	}
	 
}
