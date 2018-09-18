package com.xyz.rbac.service;

import com.xyz.rbac.data.domain.Role;
import com.xyz.rbac.data.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleMapper roleDao;

    public List<Role> get() {
        return roleDao.get();
    }

    public boolean add(Role role) {
        return roleDao.add(role) > 0;
    }

    public boolean update(Role role) {
        return roleDao.update(role)>0;
    }

    public boolean delete(Integer id) {
        return roleDao.deleteById(id)>0;
    }
}
