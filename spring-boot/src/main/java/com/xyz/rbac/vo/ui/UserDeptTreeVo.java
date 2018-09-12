package com.xyz.rbac.vo.ui;

import com.xyz.rbac.data.domain.Tree;
import com.xyz.rbac.data.domain.User;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDeptTreeVo {
    public UserDeptTreeVo(Integer id,String name,boolean user) {
        if(user) {
            this.id = "u" + "_" + id;
        }else {
            this.id = id.toString();
        }
        this.value=id;
        this.user = user;
        this.name=name;
        this.childrens = new ArrayList<UserDeptTreeVo>();
        this.parents = new ArrayList<String>();
    }

    public UserDeptTreeVo(Integer id,String name) {
        this.id = id.toString();
        this.name=name;
        this.value = id;
        this.user = false;
        this.childrens = new ArrayList<UserDeptTreeVo>();
        this.parents = new ArrayList<String>();
    }

    private String id;

    public Integer getValue() {
        return value;
    }


    private Integer value;
    private String name;
    private boolean user;

    private List<UserDeptTreeVo> childrens;
    private List<String> parents;

    public String getId() {
        return id;
    }



    public String getName() {
        return name;
    }



    public boolean isUser() {
        return user;
    }


    public List<UserDeptTreeVo> getChildrens() {
        return childrens != null && childrens.size() > 0 ? childrens : null;
    }

    public void setChildren(UserDeptTreeVo vo) {
        this.childrens.add(vo);
    }

    public List<String> getParents() {
        return parents != null && parents.size() > 0 ? parents : null;
    }

    public void setParent(String item) {
        this.parents.add(item);
    }

    //将tree和user合并
    public static List<UserDeptTreeVo> merge(List<TreeVo> deptTrees,Map<Integer,List<User>> userMap) {
        List<UserDeptTreeVo> lists = new ArrayList<UserDeptTreeVo>();
        if (deptTrees != null && deptTrees.size() > 0) {
            for (TreeVo tree : deptTrees) {
                UserDeptTreeVo vo = new UserDeptTreeVo(tree.getId(), tree.getName());
                //
                if (tree.getParents() != null && tree.getParents().size() > 0) {
                    for (Integer p : tree.getParents()) {
                        vo.setParent(p.toString());
                    }
                }
                if (userMap.containsKey(tree.getId())) {
                    List<User> users = userMap.get(tree.getId());
                    if (users != null && users.size() > 0) {
                        for (User user : users) {
                            UserDeptTreeVo c = new UserDeptTreeVo(user.getId(), user.getName(), true);
                            vo.setChildren(c);
                        }
                    }
                    userMap.remove(tree.getId());
                }
                if (tree.getChildrens() != null && tree.getChildrens().size() > 0) {
                    List<UserDeptTreeVo> items = merge(tree.getChildrens(), userMap);
                    if (items != null && items.size() > 0) {
                        for (UserDeptTreeVo item : items) {
                            vo.setChildren(item);
                        }
                    }
                }
                lists.add(vo);
            }
        }
        return lists;
    }



}
