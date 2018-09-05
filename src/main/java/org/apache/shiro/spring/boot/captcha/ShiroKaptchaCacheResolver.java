package org.apache.shiro.spring.boot.captcha;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.biz.authc.exception.IncorrectCaptchaException;
import org.apache.shiro.biz.authc.token.CaptchaAuthenticationToken;
import org.apache.shiro.biz.web.filter.authc.captcha.CaptchaResolver;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.spring.boot.ShiroBizProperties;
import org.apache.shiro.web.util.WebUtils;

import com.google.code.kaptcha.spring.boot.ext.KaptchaResolver;
import com.google.code.kaptcha.spring.boot.ext.exception.KaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.ext.exception.KaptchaTimeoutException;
import com.google.code.kaptcha.util.Config;

public class ShiroKaptchaCacheResolver implements KaptchaResolver, CaptchaResolver {

	/**
	 * Name of the session attribute that holds the Captcha name. Only used
	 * internally by this implementation.
	 */
	public static final String CAPTCHA_SESSION_ATTRIBUTE_NAME = ShiroKaptchaCacheResolver.class.getName() + ".Captcha";
	public static final String CAPTCHA_DATE_SESSION_ATTRIBUTE_NAME = ShiroKaptchaCacheResolver.class.getName() + ".Captcha_DATE";

	/**
     * 验证码在Session中存储值的key
     */
	private String captchaStoreKey = CAPTCHA_SESSION_ATTRIBUTE_NAME;
	/**
     * 验证码创建时间在Session中存储值的key
     */
	private String captchaDateStoreKey = CAPTCHA_SESSION_ATTRIBUTE_NAME;
	/**
     * 验证码有效期；单位（毫秒），默认 60000
     */
	private long captchaTimeout = ShiroBizProperties.DEFAULT_CAPTCHA_TIMEOUT;
	
	private Cache<String, Object> captchaCache;
	
	@Override
	public void init(Config config ) {
		if(StringUtils.isNoneEmpty(captchaStoreKey)) {
			this.captchaStoreKey = config.getSessionKey();
		}
		if(StringUtils.isNoneEmpty(captchaDateStoreKey)) {
			this.captchaDateStoreKey = config.getSessionDate();
		}
	}
	
	@Override
	public void init(String captchaStoreKey, String captchaDateStoreKey, long captchaTimeout) {
		if(StringUtils.isNoneEmpty(captchaStoreKey)) {
			this.captchaStoreKey = captchaStoreKey;
		}
		if(StringUtils.isNoneEmpty(captchaDateStoreKey)) {
			this.captchaDateStoreKey = captchaDateStoreKey;
		}
		if(captchaTimeout > 0) {
			this.captchaTimeout = captchaTimeout;
		}
	}
	
	/**
	 * Shiro 验证码接口扩展实现
	 */
	@Override
	public boolean validCaptcha(ServletRequest request, CaptchaAuthenticationToken token)
			throws IncorrectCaptchaException {
		try {
			return this.validCaptcha(WebUtils.toHttp(request), token.getCaptcha());
		} catch (KaptchaIncorrectException e) {
			throw new IncorrectCaptchaException(e);
		} catch (KaptchaTimeoutException e) {
			throw new IncorrectCaptchaException(e);
		}
	}

	/**
	 * Kaptcha 验证码接口扩展实现
	 */
	@Override
	public boolean validCaptcha(HttpServletRequest request, String capText)
			throws KaptchaIncorrectException, KaptchaTimeoutException {
		
		// 验证码无效
		if(StringUtils.isEmpty(capText)) {
			throw new KaptchaIncorrectException();
		}
		
		// 历史验证码无效
		String sessionCapText = (String) getCaptchaCache().get(getCaptchaStoreKey());
		if(StringUtils.isEmpty(sessionCapText)) {
			throw new KaptchaIncorrectException();
		}
		// 检查验证码是否过期
		Date sessionCapDate = (Date) getCaptchaCache().get(getCaptchaDateStoreKey());
		if(new Date().getTime() - sessionCapDate.getTime()  > getCaptchaTimeout()) {
			throw new KaptchaTimeoutException();
		}
		
		return StringUtils.equalsIgnoreCase(sessionCapText, capText);
	}

	/**
	 * Kaptcha 验证码接口扩展实现
	 */
	@Override
	public void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate) {
		
		// store the text in the cache
		getCaptchaCache().put( getCaptchaStoreKey(), (StringUtils.isNotEmpty(capText) ? capText : null));

		// store the date in the cache so that it can be compared
		// against to make sure someone hasn't taken too long to enter  their kaptcha
		getCaptchaCache().put( getCaptchaDateStoreKey(), (capDate != null ? capDate : new Date()));

	}
	
	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}
	
	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}

	public Cache<String, Object> getCaptchaCache() {
		return captchaCache;
	}

	public void setCaptchaCache(Cache<String, Object> captchaCache) {
		this.captchaCache = captchaCache;
	}
	
}