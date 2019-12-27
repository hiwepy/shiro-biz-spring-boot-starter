package org.apache.shiro.spring.boot.template.method;


import java.util.List;

import org.apache.shiro.spring.boot.captcha.ShiroKaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 提供Freemarker模板判断是否启用验证码功能
 * @author <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class CaptchaEnabled implements TemplateMethodModelEx {

	@Autowired
	private ShiroKaptchaProperties properties;
	
	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		return new SimpleScalar(Boolean.toString(properties.isEnabled()));
	}

}
