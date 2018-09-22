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

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.biz.authc.token.CaptchaAuthenticationToken;
import org.apache.shiro.biz.utils.WebUtils;
import org.apache.shiro.biz.web.filter.authc.captcha.CaptchaResolver;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

/**
 * 验证JCaptcha生成的验证码
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class ShiroJCaptchaCaptchaResolver implements CaptchaResolver {

	@Override
	public boolean validCaptcha(ServletRequest request, CaptchaAuthenticationToken token)
			throws AuthenticationException {
		return SimpleImageCaptchaServlet.validateResponse(WebUtils.toHttp(request), token.getCaptcha());
	}

}
