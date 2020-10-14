# shiro-biz-spring-boot-starter
shiro starter for spring boot

### 说明


 > 基于 Shiro 的 Spring Boot Starter 实现


1. Apache Shiro是一个强大且易用的Java安全框架,执行身份验证、授权、密码学和会话管理。使用Shiro的易于理解的API,您可以快速、轻松地获得任何应用程序,从最小的移动应用程序到最大的网络和企业应用程序。
2. shiro-biz-spring-boot-starter 是在引用 [shiro-spring-boot-starter](http://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-starter "shiro-spring-boot-starter")、[shiro-spring-boot-web-starter](http://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-web-starter "shiro-spring-boot-web-starter") 的基础上整合 [shiro-biz](https://github.com/vindell/shiro-biz "shiro-biz") 的 Spring Boot 整合；
3. 完成了基于Shiro的权限的控制


### Maven

``` xml
<dependency>
	<groupId>com.github.hiwepy</groupId>
	<artifactId>shiro-biz-spring-boot-starter</artifactId>
	<version>1.0.7.RELEASE</version>
</dependency>
```
	

### 配置参考

 > application.yml
 
``` yml
################################################################################################################  
###Shiro 权限控制基本配置：  
################################################################################################################
shiro:
  annotations: 
    enabled: true
    proxy-target-class: true
  authentication-caching-enabled: false
  authentication-cache-name: SHIRO-AUTHC
  authorization-caching-enabled: false 
  authorization-cache-name: SHIRO-AUTHZ
  caching-enabled: false
  cache:
    type: ehcache
  enabled: true
  kaptcha:
    enabled: true
    retry-times-when-access-denied: 3
  failure-url: /error
  http:
    header:
      access-control-allow-methods: PUT,POST,GET,DELETE,OPTIONS
  jwt:
    enabled: true
  login-url: /authz/login/slogin
  redirect-url: /authz/login/index
  success-url: /index
  session-creation-enabled: false
  session-validation-scheduler-enabled: false
  session-validation-interval: 20000
  session-stateless: true
  session-storage-enabled: false
  session-timeout: 1800000
  unauthorized-url: /error
  user-native-session-manager: false
  web: 
    enabled: true
  filter-chain-definition-map: 
    '[/]' : anon
    '[/**/favicon.ico]' : anon
    '[/webjars/**]' : anon
    '[/assets/**]' : anon
    '[/error*]' : anon
    '[/logo/**]' : anon
    '[/swagger-ui.html**]' : anon
    '[/swagger-resources/**]' : anon
    '[/v2/**]' : anon
    '[/kaptcha*]' : anon
    '[/admin]' : anon
    '[/admin/assets/**]' : anon
    '[/admin/applications]' : anon
    '[/admin/applications/**]' : anon
    '[/admin/notifications]' : anon
    '[/admin/notifications/**]' : anon
    '[/admin/instances]' : anon
    '[/admin/instances/**]' : anon
    '[/sockets/**]' : anon
    '[/expiry]' : cros,withinExpiry
    '[/authz/login/slogin]' : cros,authc
    '[/logout]' : logout
```

### Sample

[https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-shiro-biz](https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-shiro-biz "spring-boot-sample-shiro-biz")

### 参考资料

http://shiro.apache.org/documentation.html

http://jinnianshilongnian.iteye.com/blog/2018398