package com.kinnear.test.validation;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;

import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

public class OverwriteValidatorConstraintsValidatorFactory extends SpringWebConstraintValidatorFactory {
	private Map<Class<?>, ? extends ConstraintValidator<?, ?>> constraintValidatorMap;
	private WebApplicationContext context;

	public <T extends ConstraintValidator<?, ?>> OverwriteValidatorConstraintsValidatorFactory(
			WebApplicationContext context,
			final ConstraintValidator<?, ?>... validators) {
		this.context = context;
		this.constraintValidatorMap = Arrays.stream(validators)
			.collect(Collectors.toMap(OverwriteValidatorConstraintsValidatorFactory::typeOf, Function.identity()));
	}
	
	@Override
	protected WebApplicationContext getWebApplicationContext() {
		return context;
	}

	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		@SuppressWarnings("unchecked")
		T value = (T) constraintValidatorMap.get(key);
		if (value == null) {
			return super.getInstance(key);
		}
		return value;
	}
	
	private static final String ENHANCER = "$$EnhancerByMockitoWithCGLIB$$";

	public static Class<? extends Object> typeOf(Object instance) {
		Class<? extends Object> type = instance.getClass();
		while (type.getSimpleName().contains(ENHANCER)) {
			type = type.getSuperclass();
		}

		return type;
	}
}