package com.xyz.rbac.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CategoryModel {
    private Integer id;
    private String name;
    private String description;
    private Date rank;
    private Integer pid;

    public CategoryModel(){
        this.rank=new Date();
    }
}
