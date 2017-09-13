package io.jsonwebtoken.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = JwtSecurtyProperties.PREFIX)
public class JwtSecurtyProperties extends JwtProperties {

	public static final String PREFIX = "spring.security.jwt";

}
