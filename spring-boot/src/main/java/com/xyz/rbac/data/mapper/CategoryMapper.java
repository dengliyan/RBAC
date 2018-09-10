package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("insert into auth_category(`name`,`description`,`parent_id`,`rank`,`is_valid`) values(#{name},#{description},#{parentId},#{rank},1)")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id()")
    public Integer add(Category category);

    @Update("update auth_category set `name`=#{name},`description`=#{description},`parent_id`=#{parentId},`rank`=#{rank} where id=#{id}")
    public Integer update(Category category);

    //@Update("update auth_category set is_valid=-1*`id` where id=#{id}")
    //public Integer update(@Param("id") Integer category_id);

    @Select("select * from auth_category where is_valid=1 order by rank asc")
    public List<Category> get();


}
