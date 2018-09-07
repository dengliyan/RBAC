package com.xyz.rbac.data.domain;

import java.util.Date;
import java.util.List;

public class Department {
    private Integer id;
    private String name;
    private String description;
    private long rank;
    private Integer parentId;
    private List<Integer> parents;
}
