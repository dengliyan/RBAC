package com.xyz.rbac.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UITree {
    private Integer id;
    private String name;
    private List<UITree> children;
    private List<Integer> parent;
}
