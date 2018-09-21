package com.xyz.rbac.service;


import com.xyz.rbac.cache.keys.CategoryKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.domain.Department;
import com.xyz.rbac.data.mapper.CategoryMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryDao;

    @Autowired
    RedisService redisService;



    public boolean add(Category category) {
        Category model = categoryDao.getByName(category.getName(), category.getParentId());
        if (model != null) {
            throw new BusinessException(Result.TREE_EXISTS_SAME_NAME);
        }
        if (categoryDao.add(category) > 0) {
            this.fill();
            return true;
        }
        return false;
    }

    public boolean update(Category category) {
        Category model = categoryDao.getByName(category.getName(), category.getParentId());
        if (model!=null&&model.getId() != category.getId()) {
            throw new BusinessException(Result.TREE_EXISTS_SAME_NAME);
        }
        if (categoryDao.update(category) > 0) {
            this.fill();
            return true;
        }
        return false;
    }

    public boolean delete(Integer id, Integer user) {
        if (categoryDao.delete(id, user) > 0) {
            this.fill();//同步
            return true;
        }
        return false;
    }



    public List<Category> fill() {
        List<Category> lists = categoryDao.get();
        if (lists != null && lists.size() > 0) {
            TreeUtil.build(lists);
            redisService.set(CategoryKey.TREE, "", lists);
        }
        return lists;
    }

    public List<Category> get() {
        List<Category> lists = redisService.getList(CategoryKey.TREE, "", Category.class);
        if (lists != null && lists.size() > 0) {
            return lists;
        }
        return this.fill();
    }

    public List<Category> getTreeById(Integer id) {
        List<Category> lists=new ArrayList<Category>();
        List<Category> categories = this.get();
        Map<Integer,Category> map=new HashMap<Integer,Category>();
        if (categories != null && categories.size() > 0) {
            for (Category c:categories) {
                map.put(c.getId(),c);
            }
        }
        if(!map.containsKey(id)) {
            throw new BusinessException(Result.DB_QUERY_NOT_EXISTS);
        }
        Category item=map.get(id);
        lists.add(item);
        if(item.getParents()!=null) {
            for (Integer p : item.getParents()) {
                if(map.containsKey(p)) {
                    lists.add(map.get(p));
                }
            }
        }
        return lists;
    }


    public Category get(Integer id) {
        List<Category> lists = this.get();
        if (lists != null && lists.size() > 0) {
            for (Category category: lists) {
                if(category.getId()==id){
                    return category;
                }
            }
        }
        throw new  BusinessException(Result.DB_QUERY_NOT_EXISTS);
    }
}
