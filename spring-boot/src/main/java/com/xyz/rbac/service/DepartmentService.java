package com.xyz.rbac.service;

import com.xyz.rbac.cache.keys.DepartmentKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.Department;
import com.xyz.rbac.data.domain.Tree;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.data.mapper.DepartmentMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.util.TreeUtil;
import com.xyz.rbac.vo.ui.GroupVo;
import com.xyz.rbac.vo.ui.GroupItemVo;
import com.xyz.rbac.vo.ui.TreeVo;
import com.xyz.rbac.vo.ui.UserDeptTreeVo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    DepartmentMapper departmentDao;

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;


    public List<Department> fill() {
        List<Department> lists = departmentDao.get();
        if (lists != null && lists.size() > 0) {
            TreeUtil.build(lists);
            redisService.set(DepartmentKey.TREE, "", lists);
        }
        return lists;
    }

    public List<Department> get() {
        List<Department> lists = redisService.getList(DepartmentKey.TREE, "", Department.class);
        if (lists != null && lists.size() > 0) {
            return lists;
        }
        return this.fill();
    }

    public Department get(Integer id) {
        List<Department> lists = redisService.getList(DepartmentKey.TREE, "", Department.class);
        if (lists != null && lists.size() > 0) {
            for (Department dept :lists) {
                if(dept.getId()==id){
                    return dept;
                }
            }
        }
        throw new BusinessException(Result.DB_QUERY_NOT_EXISTS);
    }

    public boolean add(Department department) {
        Department model = departmentDao.getByName(department.getName(), department.getParentId());
        if (model != null) {
            throw new BusinessException(Result.TREE_EXISTS_SAME_NAME);
        }
        if (departmentDao.add(department) > 0) {
            this.fill();
            return true;
        }
        return false;
    }

    public boolean update(Department department) {
        Department model = departmentDao.getByName(department.getName(), department.getParentId());
        if (model!=null&&model.getId() != department.getId()) {
            throw new BusinessException(Result.TREE_EXISTS_SAME_NAME);
        }
        if (departmentDao.update(department) > 0) {
            this.fill();
            return true;
        }
        return false;
    }

    public boolean delete(Integer id, Integer user) {
        if (departmentDao.delete(id, user) > 0) {
            this.fill();//同步
            return true;
        }
        return false;

    }



    public List<UserDeptTreeVo> getDeptAndUserTree() {
        //读取当前部门
        List<Department> departments = this.fill();
        //生成部门树
        List<TreeVo> trees = TreeUtil.tree(departments);
        //读取所有的用户
        List<User> users = userService.get();
        Map<Integer, List<User>> userMap = new HashMap<Integer, List<User>>();
        for (User user : users) {
            Integer dept = user.getDeptId();
            if (!userMap.containsKey(dept)) {
                userMap.put(dept, new ArrayList<User>());
            }
            List<User> values = userMap.get(dept);
            values.add(user);
        }

        List<UserDeptTreeVo> list= UserDeptTreeVo.merge(trees, userMap);
        //判断当前是否存在未处理的
        if(userMap.keySet()!=null&&userMap.keySet().size()>0) {
            UserDeptTreeVo vo = new UserDeptTreeVo(-999,"其他部门");
            for (Integer key:userMap.keySet()) {
                List<User> values = userMap.get(key);
                if(values!=null&&values.size()>0){
                    for (User user:values) {
                        vo.setChildren(new UserDeptTreeVo(user.getId(), user.getName()+" ("+user.getEmail()+")", true));
                    }
                }
            }
            list.add(vo);
        }

        return list;
    }

}
