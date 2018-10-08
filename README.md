# spring-boot-starter-shiro-biz
shiro starter for spring boot

### 说明


 > 基于 Shiro 的 Spring Boot Starter 实现


1. Apache Shiro是一个强大且易用的Java安全框架,执行身份验证、授权、密码学和会话管理。使用Shiro的易于理解的API,您可以快速、轻松地获得任何应用程序,从最小的移动应用程序到最大的网络和企业应用程序。
2. spring-boot-starter-shiro-biz 是在引用 [shiro-spring-boot-starter](http://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-starter "shiro-spring-boot-starter")、[shiro-spring-boot-web-starter](http://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-web-starter "shiro-spring-boot-web-starter") 的基础上整合 [shiro-biz](https://github.com/vindell/shiro-biz "shiro-biz") 的 Spring Boot 整合；
3. 完成了基于Shiro的权限的控制


### Maven

	<dependency>
		<groupId>com.github.vindell</groupId>
		<artifactId>spring-boot-starter-shiro-biz</artifactId>
		<version>1.0.0.RELEASE</version>
	</dependency>

### 配置参考

 > application.yml

	################################################################################################################  
	###Shiro 权限控制基本配置：  
	################################################################################################################
	shiro:
	  enabled: true
	  validate-captcha: false
	  login-url: /authz/login
	  redirect-url: /authz/index
	  success-url: /index
	  unauthorized-url: /error
	  failure-url: /error
	  annotations: 
	    enabled: true
	  web: 
	    enabled: true
	  filter-chain-definition-map: 
	    / : anon
	    /*favicon.ico : anon
	    /webjars/** : anon
	    /assets/** : anon
	    /html/** : anon
	    /error* : anon
	    /logo/** : anon
	    /kaptcha* : anon
	    /sockets/** : anon
	    /logout : logout
	    /index : sessionExpired,sessionControl,authc
	    /** : sessionExpired,sessionControl,authc

### Sample

[https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-shiro-biz](https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-shiro-biz "spring-boot-sample-shiro-biz")

### 参考资料

http://shiro.apache.org/documentation.html

http://jinnianshilongnian.iteye.com/blog/2018398