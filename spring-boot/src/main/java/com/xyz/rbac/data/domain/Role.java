package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Role {
    private Integer id;
    private String name;
    private String description;
    private Date createDate;
    private Date updateDate;
    private Integer createUser;
    private Integer updateUser;
}
