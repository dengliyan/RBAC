package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Insert("insert into auth_role(name,description,create_date,is_valid) values(#{name},#{description},now(),1)")
    public Integer add(Role role);

    @Insert("update auth_role set name=#{name},description=#{description},update_date=now() where id=#{id}")
    public Integer update(Role role);

    @Insert("select * from auth_role where is_valid=1")
    public List<Role> get();

    @Insert("select * from auth_role where id=#{id} is_valid=1")
    public Integer getById(@Param("id") Integer id);



    @Delete("delete from auth_role where id=#{id}")
    public Integer deleteById(@Param("id") Integer id);
}
