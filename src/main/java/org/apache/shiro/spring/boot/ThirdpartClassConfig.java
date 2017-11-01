package org.apache.shiro.spring.boot;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

@Configuration
@EnableConfigurationProperties(ThirdpartClassProperties.class)
public class ThirdpartClassConfig implements ImportAware, BeanClassLoaderAware {

	@Autowired
	private ThirdpartClassProperties demoProperties;
	Logger logger = LoggerFactory.getLogger(getClass());
	private ClassLoader classLoader;
	private String param;

	@PostConstruct
	public void init() {
		logger.info("######DemoConfig init####### param is {} demoProperties is {}", param, demoProperties.toString());
	}

	@Override
	public void setImportMetadata(AnnotationMetadata annotationMetadata) {
		Map<String, Object> enableAttrMap = annotationMetadata
				.getAnnotationAttributes(EnableThirdpartClass.class.getName());
		AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
		if (enableAttrs == null) {
			// search parent classes
			Class<?> currentClass = ClassUtils.resolveClassName(annotationMetadata.getClassName(), classLoader);
			for (Class<?> classToInspect = currentClass; classToInspect != null; classToInspect = classToInspect
					.getSuperclass()) {
				EnableThirdpartClass enableDemo = AnnotationUtils.findAnnotation(classToInspect,
						EnableThirdpartClass.class);
				if (enableDemo == null) {
					continue;
				}
				enableAttrMap = AnnotationUtils.getAnnotationAttributes(enableDemo);
				enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
			}
		}
		this.param = enableAttrs.getString("param");
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}
