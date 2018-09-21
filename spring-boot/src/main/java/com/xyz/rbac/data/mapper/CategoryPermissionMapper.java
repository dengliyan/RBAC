package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.CategoryPermission;
import com.xyz.rbac.validation.IsLoginName;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryPermissionMapper {

    @Insert("insert into auth_category_permission(category_id,name,path,rank) values(#{categoryId},#{name},#{path},#{rank})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id()")
    public int add(CategoryPermission model);

    @Update("update auth_category_permission set name=#{name},path=#{path},rank=#{rank} where id=#{id} and category_id=#{categoryId}")
    public int update(CategoryPermission model);


    @Delete("delete from auth_category_permission where id=#{id} and category_id=#{category_id}")
    public int delete(@Param("id") Integer id,@Param("category_id") Integer category_id);

    @Select("select * from auth_category_permission where category_id=#{category_id}")
    public List<CategoryPermission> get(@Param("category_id") Integer category_id);
}
