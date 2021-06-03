package com.itc.dev.devchallange.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@javax.validation.constraints.NotNull
@ReportAsSingleViolation
public @interface NotNull {
	public abstract String fieldName();

	public abstract Class<?>[] groups() default {};

	public abstract String message() default "{NotNull.message}";

	public abstract Class<? extends Payload>[] payload() default {};

}
