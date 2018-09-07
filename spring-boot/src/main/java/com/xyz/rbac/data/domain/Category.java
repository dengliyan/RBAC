package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Category {
    private Integer id;
    private String name;
    private String description;
    private long rank;
    private long parentRank;
    private Integer parentId;
    private List<Integer> parents;
}
