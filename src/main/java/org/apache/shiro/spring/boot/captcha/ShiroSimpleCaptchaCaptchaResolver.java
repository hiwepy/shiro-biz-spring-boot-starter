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
package org.apache.shiro.spring.boot.captcha;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.biz.authc.exception.IncorrectCaptchaException;
import org.apache.shiro.biz.authc.exception.InvalidCaptchaException;
import org.apache.shiro.biz.authc.token.CaptchaAuthenticationToken;
import org.apache.shiro.biz.utils.WebUtils;
import org.apache.shiro.biz.web.filter.authc.captcha.CaptchaResolver;

import nl.captcha.Captcha;

/**
 * 验证SimpleCaptcha生成的验证码
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class ShiroSimpleCaptchaCaptchaResolver implements CaptchaResolver {

	@Override
	public boolean validCaptcha(ServletRequest request, CaptchaAuthenticationToken token)
			throws AuthenticationException {
		// 验证码无效
		if(StringUtils.isEmpty(token.getCaptcha())) {
			throw new IncorrectCaptchaException();
		}
		try {
			
			Captcha captcha = (Captcha) WebUtils.toHttp(request).getSession().getAttribute(Captcha.NAME);  
			if(captcha == null) {
				throw new InvalidCaptchaException();
			}
			return captcha.isCorrect(token.getCaptcha());
		} catch (Exception e) {
			throw new IncorrectCaptchaException(e);
		}
	}

}
