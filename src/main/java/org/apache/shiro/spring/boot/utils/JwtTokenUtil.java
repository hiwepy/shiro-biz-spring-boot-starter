/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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
package io.authc.spring.boot.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

class JwtTokenUtil {

	public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
	public static final String CLAIM_KEY_USER_ID = "user_id";
	public static final String CLAIM_KEY_AUTHORITIES = "scope";
	public static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";
	public static final String CLAIM_KEY_ACCOUNT_NON_LOCKED = "non_locked";
	public static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "non_expired";

	private final String secret;
	private final SignatureAlgorithm signatureAlgorithm;
	private Long access_token_expiration = -1L;
	private Long refresh_token_expiration = -1L;

	public JwtTokenUtil(String secret) {
		this.secret = secret;
		this.signatureAlgorithm = SignatureAlgorithm.HS256;
	}

	public JwtTokenUtil(String secret, Long access_token_expiration) {
		this(secret, SignatureAlgorithm.HS256, access_token_expiration);
	}

	public JwtTokenUtil(String secret, SignatureAlgorithm signatureAlgorithm, Long access_token_expiration) {
		this.secret = secret;
		this.signatureAlgorithm = signatureAlgorithm;
		this.access_token_expiration = access_token_expiration;
	}

	public JwtTokenUtil(String secret, Long access_token_expiration, Long refresh_token_expiration) {
		this(secret, SignatureAlgorithm.HS256, access_token_expiration, refresh_token_expiration);
	}

	public JwtTokenUtil(String secret, SignatureAlgorithm signatureAlgorithm, Long access_token_expiration,
			Long refresh_token_expiration) {
		this.secret = secret;
		this.signatureAlgorithm = signatureAlgorithm;
		this.access_token_expiration = access_token_expiration;
		this.refresh_token_expiration = refresh_token_expiration;
	}

	public long getUserIdFromToken(String token) {
		long userId;
		try {
			final Claims claims = getClaimsFromToken(token);
			userId = (Long) claims.get(CLAIM_KEY_USER_ID);
		} catch (Exception e) {
			userId = 0;
		}
		return userId;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = claims.getIssuedAt();
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			// 解析jwt串 :其中parseClaimsJws验证jwt字符串失败可能会抛出异常，需要捕获异常
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody(); // 得到body后我们可以从body中获取我们需要的信息
		} catch (Exception e) {
			// jwt 解析错误
			claims = null;
		}
		return claims;
	}

	public Date generateExpirationDate(long expiration) {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public Claims parseJWT(String jsonWebToken, String base64Security) {
		try {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
					.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		} catch (Exception ex) {
			return null;
		}
	}

	public String createJWT(String name, String userId, String role, String audience, String issuer, long TTLMillis,
			String base64Security) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// 生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").claim("role", role).claim("unique_name", name)
				.claim("userid", userId).setIssuer(issuer).setAudience(audience)
				.signWith(signatureAlgorithm, signingKey);
		// 添加Token过期时间
		if (TTLMillis >= 0) {
			long expMillis = nowMillis + TTLMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}

	public String generateAccessToken(String subject, Map<String, Object> claims) {
		return generateToken(subject, claims, access_token_expiration);
	}

	public String generateRefreshToken(String subject, Map<String, Object> claims) {
		return generateToken(subject, claims, refresh_token_expiration);
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token));
	}

	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			refreshedToken = generateAccessToken(claims.getSubject(), claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	private String generateToken(String subject, Map<String, Object> claims, long expiration) {
		return Jwts.builder().setClaims(claims).setSubject(subject) // 设置主题
				.setId(UUID.randomUUID().toString()).setIssuedAt(new Date())
				.setExpiration(generateExpirationDate(expiration)).compressWith(CompressionCodecs.DEFLATE)
				.signWith(signatureAlgorithm, secret) // 设置算法（必须）
				.compact();
	}

}
