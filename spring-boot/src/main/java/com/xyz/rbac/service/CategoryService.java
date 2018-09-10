package com.xyz.rbac.service;


import com.xyz.rbac.cache.keys.CategoryKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.mapper.CategoryMapper;
import com.xyz.rbac.model.UITree;
import com.xyz.rbac.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    RedisService redisService;



    public void add(Category category) {
        //添加到数据库
        categoryMapper.add(category);
        //同步到缓存中
        this.fill();

    }

    public List<Category> fill() {
        List<Category> lists = categoryMapper.get();
        if (lists != null && lists.size() > 0) {
            TreeUtil.build(lists);
            redisService.set(CategoryKey.TREE, "", lists);
        }
        return  lists;
    }

    public List<Category> get() {
        List<Category> lists = redisService.getList(CategoryKey.TREE, "", Category.class);
        if (lists != null && lists.size() > 0) {
            return lists;
        }
        return this.fill();
    }


}
