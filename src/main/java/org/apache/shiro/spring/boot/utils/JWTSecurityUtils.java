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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.JSONObject;

import io.authc.spring.boot.security.JWTUserDetails;
import io.jsonwebtoken.Claims;

public class JWTSecurityUtils {

    
    public Map<String, Object> generateClaims(JWTUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenUtil.CLAIM_KEY_USER_ID, user.getUserId());
        claims.put(JwtTokenUtil.CLAIM_KEY_ACCOUNT_ENABLED, user.isEnabled());
        claims.put(JwtTokenUtil.CLAIM_KEY_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        claims.put(JwtTokenUtil.CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        return claims;
    }

    
	public JWTUserDetails getUserFromToken(String token, String secret) {
		
		
		
		JWTUserDetails user;
		try {
			final Claims claims = JwtTokenUtils.jwtTokenUtil(secret).getClaimsFromToken(token);
			long userId = JwtTokenUtils.jwtTokenUtil(secret).getUserIdFromToken(token);
			String username = claims.getSubject();
			List roles = (List) claims.get(JwtTokenUtil.CLAIM_KEY_AUTHORITIES);
			Collection<? extends GrantedAuthority> authorities = parseArrayToAuthorities(roles);
			boolean account_enabled = (Boolean) claims.get(JwtTokenUtil.CLAIM_KEY_ACCOUNT_ENABLED);
			boolean account_non_locked = (Boolean) claims.get(JwtTokenUtil.CLAIM_KEY_ACCOUNT_NON_LOCKED);
			boolean account_non_expired = (Boolean) claims.get(JwtTokenUtil.CLAIM_KEY_ACCOUNT_NON_EXPIRED);

			user = new JWTUserDetails(userId, username, "password", account_enabled, account_non_expired, true,
					account_non_locked, authorities);
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	private List authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
		List<String> list = new ArrayList<>();
		for (GrantedAuthority ga : authorities) {
			list.add(ga.getAuthority());
		}
		return list;
	}

	private Collection<? extends GrantedAuthority> parseArrayToAuthorities(List roles) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority authority;
		for (Object role : roles) {
			authority = new SimpleGrantedAuthority(role.toString());
			authorities.add(authority);
		}
		return authorities;
	}

	public String generateRefreshToken(UserDetails userDetails) {
		JWTUserDetails user = (JWTUserDetails) userDetails;
		Map<String, Object> claims = generateClaims(user);
		// 只授于更新 token 的权限
		String roles[] = new String[] { JwtTokenUtil.ROLE_REFRESH_TOKEN };
		claims.put(JwtTokenUtil.CLAIM_KEY_AUTHORITIES, JSONObject.toJSONString(roles));
		return generateRefreshToken(user.getUsername(), claims);
	}
	
	public String generateAccessToken(UserDetails userDetails) {
		JWTUserDetails user = (JWTUserDetails) userDetails;
		Map<String, Object> claims = generateClaims(user);
		claims.put(JwtTokenUtil.CLAIM_KEY_AUTHORITIES, JSONObject.toJSONString(authoritiesToArray(user.getAuthorities())));
		return generateAccessToken(user.getUsername(), claims);
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
        JWTUserDetails user = (JWTUserDetails) userDetails;
        final long userId = getUserIdFromToken(token);
        final String username = getUsernameFromToken(token);
        // final Date created = getCreatedDateFromToken(token);
        // final Date expiration = getExpirationDateFromToken(token);
        return (userId == user.getUserId()
                && username.equals(user.getUsername())
                && !isTokenExpired(token)
                /* && !isCreatedBeforeLastPasswordReset(created, userDetails.getLastPasswordResetDate()) */
        );
    }
}
