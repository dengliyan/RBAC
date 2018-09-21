package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.Date;

@Getter
@Setter
public class Role {
    private Integer id;
    private String name;
    private String description;
    private Integer parent;
    private String parentName;
}
