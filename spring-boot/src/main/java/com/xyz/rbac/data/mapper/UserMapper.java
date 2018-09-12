package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from auth_user where `id`=#{id} and is_valid=1")
    public User getById(@Param("id") Integer id);

    @Select("select * from auth_user where `email`=#{email}")
    public User getByEmail(@Param("email") String email);

    @Select("select * from auth_user where `mobile`=#{mobile}")
    public User getByMobile(@Param("mobile") String mobile);

    @Select("select count(0) from auth_user where is_valid=1")
    public Integer getAllListCount();

    @Select("select * from auth_user where is_valid=1")
    public List<User> get();

    @Insert("insert into auth_user(name,email,mobile,password,salt,create_date,dept_id,is_valid) values(#{name},#{email},#{mobile},#{password},#{salt},now(),#{deptId},1)")
    public Integer add(User user);

    @Insert("insert into auth_user() values()")
    public Integer update(Integer id, String password);


    @Update("update auth_user set dept_id=#{dept_id} where id=#{user_id}")
    public Integer updateDeptById(@Param("user_id") Integer user_id, @Param("dept_id") Integer dept_id);

}
