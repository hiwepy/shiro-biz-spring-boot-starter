package io.authc.spring.boot.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @className	： JwtTokenUtils
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月13日 下午7:14:57
 * @version 	V1.0
 */
public class JwtTokenUtils {

	protected static ConcurrentMap<String /* Key */, JwtTokenUtil> COMPLIED_UTILS = new ConcurrentHashMap<String, JwtTokenUtil>();

	public static JwtTokenUtil jwtTokenUtil(String secret) {
		return jwtTokenUtil(secret, SignatureAlgorithm.HS256, -1L, -1L);
	}
	
	public static JwtTokenUtil jwtTokenUtil(String secret, SignatureAlgorithm signatureAlgorithm) {
		return jwtTokenUtil(secret, signatureAlgorithm, -1L, -1L);
	}
	
	public static JwtTokenUtil jwtTokenUtil(String secret, SignatureAlgorithm signatureAlgorithm,
			Long access_token_expiration, Long refresh_token_expiration) {
		String key = new StringBuilder(secret).append(".").append(signatureAlgorithm.getValue()).append(".")
				.append(access_token_expiration).append(".").append(refresh_token_expiration).toString();
		if (StringUtils.isNotEmpty(key)) {
			JwtTokenUtil ret = COMPLIED_UTILS.get(key);
			if (ret != null) {
				return ret;
			}
			ret = new JwtTokenUtil(secret, signatureAlgorithm, access_token_expiration, refresh_token_expiration);
			JwtTokenUtil existing = COMPLIED_UTILS.putIfAbsent(key, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}

}
