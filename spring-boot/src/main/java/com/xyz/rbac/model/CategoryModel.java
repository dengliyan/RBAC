package com.xyz.rbac.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class CategoryModel {
    private Integer id;

    @NotEmpty(message =  "分类名称不能为空")
    private String name;
    private String description;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rank;

    private Integer pid;

    public CategoryModel(){
        this.rank=new Date();
    }
}
