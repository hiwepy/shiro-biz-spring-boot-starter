package io.jsonwebtoken.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = JwtShiroProperties.PREFIX)
public class JwtShiroProperties extends JwtProperties {

	public static final String PREFIX = "spring.shiro.jwt";
	
}