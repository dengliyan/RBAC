package com.xyz.rbac.data.mapper;

import com.xyz.rbac.data.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Insert("insert into auth_role(name,description,create_date,is_valid,parent) values(#{name},#{description},now(),1,#{parent})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id()")
    public Integer add(Role role);

    @Insert("update auth_role set name=#{name},description=#{description},update_date=now() where id=#{id}")
    public Integer update(Role role);

    @Select("select * from auth_role where is_valid=1 order by create_date asc")
    public List<Role> get();

    @Select("select * from auth_role where id=#{id} and is_valid=1")
    public Role getById(@Param("id") Integer id);

    @Select("select a.* ,b.`name` as parent_name from auth_role a left join auth_role b on a.parent=b.id where a.id=#{id} and a.is_valid=1;")
    public Role getById2(@Param("id") Integer id);



    @Delete("delete from auth_role where id=#{id}")
    public Integer deleteById(@Param("id") Integer id);
}
