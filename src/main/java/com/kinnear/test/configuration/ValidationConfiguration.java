package com.kinnear.test.configuration;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidationConfiguration {
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
	    MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
	    methodValidationPostProcessor.setValidator(validator);
	    return methodValidationPostProcessor;
	}

}
