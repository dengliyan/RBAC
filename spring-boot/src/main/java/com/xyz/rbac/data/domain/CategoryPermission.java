package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPermission {
    private Integer categoryId;
    private Integer id;
    private String name;
    private String path;
    private Integer rank;
}
