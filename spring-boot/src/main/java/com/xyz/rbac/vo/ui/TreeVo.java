package com.xyz.rbac.vo.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TreeVo {
    private Integer id;
    private String name;
    private List<TreeVo> childrens;
    private List<Integer> parents;
}
