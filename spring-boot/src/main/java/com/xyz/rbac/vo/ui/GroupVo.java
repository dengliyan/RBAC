package com.xyz.rbac.vo.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GroupVo {
    private String name;
    private List<GroupItemVo> options;
}
