package com.xyz.rbac.data.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleCategoryMapper {

    @Select("select category_id from auth_role_category where role_id=#{role_id}")
    public List<Integer> get(@Param("role_id") Integer id);

    @Delete("delete from auth_role_category where role_id=#{role_id}")
    public Integer delete(@Param("role_id") Integer id);

    @Insert("insert into auth_role_category(role_id,category_id) values(#{role_id},#{category_id})")
    public Integer add(@Param("role_id") Integer role, @Param("category_id") Integer category);
}
