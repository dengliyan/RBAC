package com.xyz.rbac.service;

import com.xyz.rbac.cache.keys.DepartmentKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.Department;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.data.mapper.DepartmentMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.util.TreeUtil;
import com.xyz.rbac.vo.ui.GroupVo;
import com.xyz.rbac.vo.ui.GroupItemVo;
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



    public List<GroupVo> getDeptWithUserByOptionGroup(){
        List<User > users=userService.get();
        List<Department> departments=this.fill();
        List<GroupVo> options=new ArrayList<GroupVo>();

        Map<Integer,List<GroupItemVo>> map=new HashMap<Integer,List<GroupItemVo>>();
        Map<Integer,Department> dpts=new HashMap<Integer,Department>();
        //建立部门字典
        for (Department department:departments) {
            dpts.put(department.getId(), department);
        }
        //根据用户建立
        for (User user:users) {
            //读取用户的字典，按
            GroupItemVo vo = new GroupItemVo();
            vo.setValue(user.getId());
            vo.setName(user.getName());
            vo.setVal(user.getDeptId());
            Integer key = -1;
            if (dpts.containsKey(user.getDeptId())) {
                Department dpt = dpts.get(user.getDeptId());
                List<Integer> parents = dpt.getParents();
                //生成名字
                Integer length = (parents != null && parents.size() > 0) ? parents.size() : 0;
                String[] names = new String[length+1];
                names[0] = dpt.getName();
                //System.out.println(names[0]);
                if (parents != null && parents.size() > 0) {
                    //names[0] = dpt.getName();
                    for (int i = 0; i < length ; i++) {
                        if (dpts.containsKey(parents.get(i))) {
                            names[i+1] = dpts.get(parents.get(i)).getName();
                        }
                    }
                }
                if (names.length > 0) {
                    ArrayUtils.reverse(names);
                    vo.setText(org.apache.commons.lang3.StringUtils.join(names, " / "));
                }
                //
                key = length == 0 ? dpt.getId() : dpt.getParents().get(length - 1);
            }
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<GroupItemVo>());
            }
            List<GroupItemVo> values = map.get(key);
            values.add(vo);
        }
        //根据部门生成对应的数据
        for (Department department:departments) {
            if(department.getParentId()==0) {
                GroupVo optionGroupVo = new GroupVo();
                optionGroupVo.setName(department.getName());
                if(map.containsKey(department.getId())) {
                    optionGroupVo.setOptions(map.get(department.getId()));
                    map.remove(department.getId());
                    options.add(optionGroupVo);
                }
            }
        }
        //未分组
        if(!map.keySet().isEmpty()) {
            GroupVo optionGroupVo = new GroupVo();
            optionGroupVo.setName("未分组");
            List<GroupItemVo> list=new ArrayList<GroupItemVo>();
            for (Integer key : map.keySet()) {
                list.addAll(map.get(key));
            }
            optionGroupVo.setOptions(list);
            options.add(optionGroupVo);
        }
        return options;
    }
}
