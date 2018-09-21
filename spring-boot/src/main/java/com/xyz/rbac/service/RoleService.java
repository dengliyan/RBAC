package com.xyz.rbac.service;

import com.xyz.rbac.data.domain.Role;
import com.xyz.rbac.data.mapper.RoleMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.vo.RoleVo;
import com.xyz.rbac.vo.ui.TreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService {
    @Autowired
    RoleMapper roleDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    RoleCategoryService roleCategoryService;

    public Role get(Integer id,boolean left) {
        if (left)
            return roleDao.getById2(id);
        return roleDao.getById(id);
    }

    public List<RoleVo> get() {
        List<Role> roles=roleDao.get();
        List<RoleVo> lists=new ArrayList<RoleVo>();
        Map<Integer,RoleVo> map=new LinkedHashMap<Integer,RoleVo>();
        for (Role role:roles) {
            if (role.getParent() == 0) {
                RoleVo vo = new RoleVo();
                vo.setId(role.getId());
                vo.setName(role.getName());
                map.put(role.getId(), vo);
            }
        }
        //读取非1的
        for (Role role:roles) {
            if (role.getParent() !=0) {
                if(map.containsKey(role.getParent())){
                    RoleVo value = map.get(role.getParent());
                    RoleVo vo = new RoleVo();
                    vo.setId(role.getId());
                    vo.setName(role.getName());
                    value.add(vo);
                }
            }
        }

        for (Map.Entry<Integer,RoleVo> entry : map.entrySet()) {
            RoleVo vo=entry.getValue();
            lists.add(vo);
        }


        return lists;
    }

    @Transactional
    public boolean add(Role role) {
        if(role.getParent()>0) {
            Role r = roleDao.getById(role.getParent());
            if (r == null) {
                throw new BusinessException(Result.ARGUMENTS_ERROR.format("parent不存在"));
            }
            role.setParentName(r.getName());
        }
        Integer id = roleDao.add(role);
        if (id <= 0||role.getId()<=0) {
            throw new BusinessException(Result.DB_EXCUTE_ERROR);
        }
        if(role.getParent()==0) {
            Role r = new Role();
            r.setName("默认");
            r.setDescription(role.getName() + "角色默认权限分组");
            r.setParent(role.getId());
            Integer groupId = roleDao.add(r);//添加默认子元素
            if (groupId <= 0) {
                throw new BusinessException(Result.DB_EXCUTE_ERROR);
            }
            //设置新的数据
            role.setParent(role.getId());
            role.setParentName(role.getName());
            role.setId(r.getId());
            role.setName(r.getName());
        }
        return true;
    }

    public boolean update(Role role) {
        return roleDao.update(role)>0;
    }

    @Transactional
    public boolean delete(Role role) {
        //删除当前分组
        if (roleDao.deleteById(role.getId()) <= 0) {
            throw new BusinessException(Result.DB_EXCUTE_ERROR);
        }
        //删除当前对应的分类
        roleCategoryService.delete(role.getId());
        return true;
    }


}
