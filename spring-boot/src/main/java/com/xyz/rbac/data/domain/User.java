package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {

    public static final String COOK_NAME_TOKEN="token";

    public static final String REQUEST_CONTEXT_USER="REQUEST_CONTEXT_USER";

    private Integer id;
    private String name;
    private String email;
    private String password;
    private String salt;
    private String mobile;
    private Date createDate;
    private Date lastLoginDate;
    private String lastLoignIp;
    private Boolean isValid;
    private String token;
    private Integer deptId;
}
