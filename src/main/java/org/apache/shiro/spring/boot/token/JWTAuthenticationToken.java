package org.apache.shiro.spring.boot.token;

@SuppressWarnings("serial")
public class JWTAuthenticationToken extends UsernameWithoutPwdToken {

    private String token;

    public JWTAuthenticationToken(final String username, final String token) {
        super(username);
        this.token = token;
    }
    
    public JWTAuthenticationToken(final String username, final boolean rememberMe, final String token) {
        super(username, rememberMe);
        this.token = token;
    }
    
    public JWTAuthenticationToken(final String username, final boolean rememberMe, final String host,  final String token) {
    	super(username, rememberMe, host);
        this.token = token;
    }
    
    @Override
    public Object getCredentials() {
        return getToken();
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
	
}
