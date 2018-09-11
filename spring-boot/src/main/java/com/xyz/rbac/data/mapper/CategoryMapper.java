package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("insert into auth_category(`name`,`description`,`parent_id`,`rank`,`is_valid`) values(#{name},#{description},#{parentId},#{rank},1)")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id()")
    public Integer add(Category category);

    @Update("update auth_category set `name`=#{name},`description`=#{description},`parent_id`=#{parentId},`rank`=#{rank},update_date=now(),update_user=0 where id=#{id}")
    public Integer update(Category category);

    @Update("update auth_category a ,(select count(0) cnt from auth_category where parent_id=#{id} and is_valid=1)b set a.is_valid=-1*a.id,a.update_date=now(),a.update_user=#{user} where a.id=#{id} and b.cnt=0")
    public Integer delete(@Param("id") Integer category_id, @Param("user") Integer user_id);

    @Select("select * from auth_category where is_valid=1 order by rank asc")
    public List<Category> get();

    @Select("select * from auth_category where id=#{id} order by rank asc")
    public Category getById(@Param("id") Integer id);


    @Select("select * from auth_category where name=#{name} and parent_id=#{pid} and is_valid=1")
    public Category getByName(@Param("name") String dept_name, @Param("pid") Integer parent_id);
}



