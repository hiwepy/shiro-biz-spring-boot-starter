package org.apache.shiro.spring.boot;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ ThirdpartClassConfig.class })
@Configuration
public @interface EnableThirdpartClass {
    
	String param() default "";
    
}