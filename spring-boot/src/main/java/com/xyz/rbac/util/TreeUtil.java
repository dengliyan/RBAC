package com.xyz.rbac.util;

import com.xyz.rbac.data.domain.Tree;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.vo.ui.TreeVo;
import com.xyz.rbac.result.Result;

import java.util.*;

public class TreeUtil {

    public static <T extends Tree> void build(List<T> trees) {
        Map<Integer, T> map = new HashMap<Integer, T>();
        for (T tree : trees) {
            map.put(tree.getId(), tree);
        }
        //开始遍历，生成一个树的父级
        for (T tree : trees) {
            Integer id = tree.getId();
            //取父级
            Integer parentId = tree.getParentId();
            //父级列表，防止数据错误，出现死循环
            HashSet<Integer> hash = new HashSet<Integer>();
            //找到元素
            boolean setParent = true;

            while (parentId != 0 && map.containsKey(parentId) && map.containsKey(id) && !hash.contains(parentId)) {
                Tree item = map.get(parentId);
                tree.addParents(item.getId()); //添加父
                if (setParent) {
                    item.addChildrens(tree.getId());//添加子
                    tree.setParentRank(item.getParentRank());
                    setParent = false;
                }
                hash.add(parentId);
                parentId = item.getParentId();//
            }
        }
    }

    public static <T extends Tree> List<TreeVo> tree(List<T> trees, Integer... selector) {
        List<TreeVo> list = new ArrayList<TreeVo>();
        //转化成MAP，以加快处理速度
        Map<Integer, T> map = new HashMap<Integer, T>();
        for (T item : trees) {
            map.put(item.getId(), item);
        }
        HashSet<Integer> hash = new HashSet<Integer>();
        Integer level = -1;
        if (selector != null && selector.length > 0) {
            for (Integer id : selector) {
                if (map.containsKey(id)) {
                    Tree data = map.get(id);
                    //必须在同一层级
                    if (level == -1) {
                        level = data.getParentId();
                    } else {
                        if (level != data.getParentId()) {
                            throw new BusinessException(Result.TREE_FILTER_LEVEL_DIFF);
                        }
                    }
                    hash.add(id);
                }
            }
        }
        if (hash.size() == 0) {
            for (T item : trees) {
                if (item.getParentId() == 0 || item.getParents() == null || item.getParents().size() == 0) {
                    hash.add(item.getId());
                }
            }
        }
        for (T item : trees) {
            if (hash.contains(item.getId())) {
                //创建一个UI对象
                TreeVo ui = new TreeVo();
                ui.setId(item.getId());
                ui.setName(item.getName());
                ui.setParents(item.getParents());
                //设置UI对象的子元素
                ui.setChildrens(tree(map, item));
                list.add(ui);
            }
        }
        return list;
    }

    private static <T extends Tree> List<TreeVo> tree(Map<Integer, T> map, T item) {
        if(!map.containsKey(item.getId())) {
            return null;
        }
        List<TreeVo> list=new ArrayList<TreeVo>();
        //
        List<Integer> childrens=item.getChildrens();
        if(childrens==null||childrens.size()==0) {
            return null;
        }

        //遍历所有的子
        for(Integer c :childrens){
            System.out.println("遍历所有的子 "+c);
            if(map.containsKey(c)) {
                TreeVo ui=new TreeVo();
                T data=map.get(c);
                ui.setId(c);
                ui.setName(data.getName());
                ui.setChildrens(tree(map,data));
                ui.setParents(data.getParents());
                list.add(ui);
            }
        }
        return list;
    }
}
