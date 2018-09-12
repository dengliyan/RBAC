package com.xyz.rbac.validation;

import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternConstraint implements ConstraintValidator<IsPatternValid,String> {

    private boolean required;

    private String pattern;
    @Override
    public void initialize(IsPatternValid context) {
        this.required = context.required();
        this.pattern = context.pattern();
        if (StringUtils.isEmpty(pattern)) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("验证正则未定义"));
        }
    }


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!required && StringUtils.isEmpty(s)) {
            return true;
        }
        Pattern regex = Pattern.compile(pattern);

        Matcher m = regex.matcher(s);
        System.out.println(s);
        System.out.println(pattern);
        return m.matches();
    }

}