package org.apache.shiro.spring.boot.template.method;


import java.util.List;

import org.apache.shiro.spring.boot.ShiroBizProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 
 * @className	： ValidateCaptcha
 * @description	： 提供Freemarker模板判断是否启用验证码功能
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月8日 上午11:54:31
 * @version 	V1.0
 */
public class ValidateCaptcha implements TemplateMethodModelEx {

	@Autowired
	private ShiroBizProperties properties;
	
	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		return new SimpleScalar(Boolean.toString(properties.isValidateCaptcha()));
	}

}
