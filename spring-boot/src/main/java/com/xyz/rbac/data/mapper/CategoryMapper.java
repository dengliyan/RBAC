package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("insert into auth_category(`name`,`description`,`parent_id`,`rank`,`is_valid`) values(#{name},#{description},#{parentId},#{rank},1)")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    public Integer add(Category category);

    @Select("select * from auth_category where is_valid=1")
    public List<Category> get();
}
