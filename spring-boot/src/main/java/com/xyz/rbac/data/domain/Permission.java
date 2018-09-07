package com.xyz.rbac.data.domain;

public class Permission {
    private Integer id;
    private String name;
    private String description;
    private String router;
    private boolean isDefault;
    private Integer categoryId;
    private boolean childrenInherit;
}
