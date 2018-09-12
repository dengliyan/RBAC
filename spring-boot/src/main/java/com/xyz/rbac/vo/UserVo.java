package com.xyz.rbac.vo;

import com.xyz.rbac.validation.IsPatternValid;
import com.xyz.rbac.validation.Regex;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
public class UserVo {

    private Integer id;

    @NotEmpty(message = "用户姓名不能为空")
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @IsPatternValid(pattern = Regex.EMAIL_PATTERN, message = "邮箱不正确")
    private String email;


    @NotEmpty(message = "手机号码不能为空")
    @IsPatternValid(pattern = Regex.MOBILE_PATTERN, message = "手机号码不正确")
    private String mobile;

    private Integer dept;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "密码不能为空")
    private String password2;

    private List<Integer> depts;
}
