package com.xyz.rbac.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {LoginNameConstraint.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLoginName {
    boolean required() default true;

    String message() default "登录名为手机号或邮箱";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
