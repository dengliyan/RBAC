package com.xyz.rbac.vo;

import com.xyz.rbac.data.domain.Category;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Setter
@Getter
public class CategoryVo {
    private Integer id;

    @NotEmpty(message = "分类名称不能为空")
    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rank;

    private Integer pid;

    private String path;

    private String icon;

    private Boolean childrenOnly;

    private Boolean inMenu;

    public CategoryVo() {
        this.rank = new Date();
    }


    public Category convert(){
        Category category=new Category();
        category.setId(this.id);
        category.setName(this.name);
        category.setIcon(this.icon);
        category.setDescription(this.description);
        category.setParentId(this.pid);
        category.setRank(this.rank.getTime());
        category.setPath(StringUtils.isEmpty(this.path)?"":this.path.toLowerCase());
        category.setChildrenOnly(this.childrenOnly);
        category.setInMenu(this.inMenu);
        return category;
    }
}
