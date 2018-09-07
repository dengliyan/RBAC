package com.xyz.rbac.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginNameConstraint implements ConstraintValidator<IsLoginName,String> {

    private boolean required;

    @Override
    public void initialize(IsLoginName context) {
        this.required = context.required();
    }

    public static final Pattern MOBILE_PATTERN = Pattern.compile("^1\\d{10}$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+(.\\w+)+@\\w+(.\\w+)+$");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!required && StringUtils.isEmpty(s)) {
            return true;
        }
        Matcher m = MOBILE_PATTERN.matcher(s);
        if (m.matches()) {
            return true;
        }
        m = EMAIL_PATTERN.matcher(s);
        return m.matches();
    }

}
