package com.xyz.rbac.service;

import com.xyz.rbac.data.domain.CategoryPermission;
import com.xyz.rbac.data.mapper.CategoryPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryPermissionService {

    @Autowired
    CategoryPermissionMapper categoryPermissionDao;


    public List<CategoryPermission> get(int category){
        return categoryPermissionDao.get(category);
    }

    public boolean set(CategoryPermission model) {
        if (model.getId() > 0) {
            return categoryPermissionDao.update(model)>0;
        } else {
            return categoryPermissionDao.add(model)>0;
        }
    }

    public boolean delete(Integer id,Integer category) {
        return categoryPermissionDao.delete(id, category) > 0;
    }
}
