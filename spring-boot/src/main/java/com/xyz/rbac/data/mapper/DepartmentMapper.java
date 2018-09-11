package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.domain.Department;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Insert("insert into auth_dept(`name`,`parent_id`,`rank`,`is_valid`) values(#{name},#{parentId},#{rank},1)")
    //@SelectKey(keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id()")
    public Integer add(Department department);

    @Update("update auth_dept set `name`=#{name},`parent_id`=#{parentId},`rank`=#{rank},update_date=now(),update_user=0 where id=#{id}")
    public Integer update(Department department);

    @Update("update auth_dept a ,(select count(0) cnt from auth_dept where parent_id=#{id} and is_valid=1)b set a.is_valid=-1*a.id,a.update_date=now(),a.update_user=#{user} where a.id=#{id} and b.cnt=0")
    public Integer delete(@Param("id") Integer dept_id, @Param("user") Integer user_id);

    @Select("select * from auth_dept where is_valid=1 order by rank asc")
    public List<Department> get();

    @Select("select * from auth_dept where id=#{id} order by rank asc")
    public Department getById(@Param("id") Integer dept_id);

    @Select("select * from auth_dept where name=#{name} and parent_id=#{pid} and is_valid=1")
    public Department getByName(@Param("name") String dept_name, @Param("pid") Integer parent_id);



}
