package com.xyz.rbac.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(
        validatedBy = {PatternConstraint.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPatternValid {
    boolean required() default true;

    String pattern() default "";

    String message() default "验证失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
