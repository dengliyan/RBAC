package com.xyz.rbac.service;

import com.xyz.rbac.data.mapper.RoleCategoryMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleCategoryService {

    @Autowired
    RoleCategoryMapper dao;

    public List<Integer> get(Integer role){
        return dao.get(role);
    }


    public Integer delete(Integer role){
        return dao.delete(role);
    }

    @Transactional
    public boolean set(Integer role,Integer... categories) {
        dao.delete(role);
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (dao.add(role, categories[i]) <= 0) {
                    throw new BusinessException(Result.DB_EXCUTE_ERROR);
                }
            }
        }
        return true;
    }
}
