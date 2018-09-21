package com.xyz.rbac.vo;

import com.xyz.rbac.data.domain.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleVo {
    private Integer id;
    private String name;
    private List<RoleVo> groups;

    public void add(RoleVo vo){
        if(this.groups==null){
            this.groups=new ArrayList<RoleVo>();
        }
        this.groups.add(vo);
    }
}
