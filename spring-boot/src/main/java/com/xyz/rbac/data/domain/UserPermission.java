package com.xyz.rbac.data.domain;

import java.util.Map;

public class UserPermission {
    private Integer user;
    private Map<Integer, Permission> permissions;
}
