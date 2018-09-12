package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.UserRole;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Delete("delete from auth_user_role where user_id=#{user_id}")
    public boolean deleteByUser(@Param("user_id") Integer user_id);

    @Insert("insert into auth_user_role(user_id,role_id) values(#{user},#{role})")
    public boolean insert(@Param("user") Integer user, @Param("role") Integer role);

    @Select("select * from auth_user_role where user_id=#{user_id}")
    public List<UserRole> getByUser(@Param("user") Integer user);
}
