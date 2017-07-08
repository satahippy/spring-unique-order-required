package com.github.satahippy.spring.unique_order;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniqueOrderRequiredConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public static UniqueOrderRequiredBeanPostProcessor uniqueOrderRequiredProcessor() {
		return new UniqueOrderRequiredBeanPostProcessor();
	}
}
