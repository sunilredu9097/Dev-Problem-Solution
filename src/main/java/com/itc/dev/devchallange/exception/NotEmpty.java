package com.itc.dev.devchallange.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@javax.validation.constraints.NotEmpty
@NotNull
@ReportAsSingleViolation
public @interface NotEmpty {
	public abstract String fieldName();

	public abstract Class<?>[] groups() default {};

	public abstract String message() default "{NotEmpty.message}";

	public abstract Class<? extends Payload>[] payload() default {};
}
