package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from auth_users where `id`=#{id}")
    public User getById(@Param("id") Integer id);

    @Select("select * from auth_users where `email`=#{email}")
    public User getByEmail(@Param("email") String email);

    @Select("select * from auth_users where `mobile`=#{mobile}")
    public User getByMobile(@Param("mobile") String mobile);

    @Select("select count(0) from auth_users where is_valid=1")
    public Integer getAllListCount();

    @Select("select * from auth_users where is_valid=1 order by createDate desc limit #{skip},#{size}")
    public List<User> getAllList(@Param("skip") Integer skip, @Param("size") Integer size);

    @Insert("insert into auth_users() values()")
    public Integer insert(User user);

    @Insert("insert into auth_users() values()")
    public Integer update(Integer id, String password);
}
