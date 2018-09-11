package com.xyz.rbac.vo;

import com.xyz.rbac.validation.IsLoginName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Setter
@Getter
public class UserVo {

    @NotEmpty
    @IsLoginName
    private String name;

    @NotEmpty(message = "密码不能为空")
    private String password;


    private String code;
}
