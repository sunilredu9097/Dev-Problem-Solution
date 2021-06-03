package com.itc.dev.devchallange.helper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IDValidator.class})
@Documented
public @interface ValidIdNumber {
	String message() default "This is not a Valid SA ID Number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
