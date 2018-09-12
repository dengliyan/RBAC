package com.xyz.rbac.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo {
    private String name;
    private String email;
    private String phone;
    private Integer dept;
    private String password;
}
