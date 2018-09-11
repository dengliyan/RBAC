package com.xyz.rbac.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class CategoryVo {
    private Integer id;

    @NotEmpty(message =  "分类名称不能为空")
    private String name;
    private String description;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rank;

    private Integer pid;

    public CategoryVo(){
        this.rank=new Date();
    }
}
