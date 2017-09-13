package org.apache.shiro.spring.boot.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;

import io.authc.spring.boot.shiro.token.JWTAuthenticationToken;
import io.authc.spring.boot.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;

public final class JWTOrFormAuthenticationFilter extends AuthenticatingFilter {

   /**
     * HTTP Authorization header, equal to <code>Authorization</code>
     */
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    public JWTOrFormAuthenticationFilter() {
        setLoginUrl(DEFAULT_LOGIN_URL);
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;

        if (isLoginRequest(request, response) || isLoggedAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }

        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return loggedIn;
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws IOException {
    	//如果是登录地址，则创建登录的Token
        if (isLoginRequest(request, response)) {
        	
            String json = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
            
            if (json != null && !json.isEmpty()) {
            	JSONObject object = JSONObject.parseObject(json);
                String username = object.getString(USER_NAME);
                String password = object.getString(PASSWORD);
                return new UsernamePasswordToken(username, password);
            }
            
        }

        if (isLoggedAttempt(request, response)) {
            String jwtToken = getAuthzHeader(request);
            if (jwtToken != null) {
                return createToken(jwtToken);
            }
        }

        return new UsernamePasswordToken();
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    protected boolean isLoggedAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        return authzHeader != null;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    public JWTAuthenticationToken createToken(String token) {
        try {
            
        	
        	Claims claims = JwtTokenUtils.getClaimsFromToken(token);
        	
        	
        	
        	JWSObject jwsObject = JWSObject.parse(token);
            String decrypted = jwsObject.getPayload().toString();
            JSONObject object = JSONObject.parseObject(decrypted);

            String userId = object.getString("sub");
            return new JWTAuthenticationToken(userId, token);
            
        } catch (ParseException ex) {
            throw new AuthenticationException(ex);
        }

    }
    
     
}
