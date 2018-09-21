package com.xyz.rbac.vo;

import com.xyz.rbac.data.domain.CategoryPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryPermissionVo {
    private Integer id;
    private CategoryPermission[] data;
}
