package com.xyz.rbac.data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class Tree {
    public Tree() {
        this.parents = new ArrayList<Integer>();
        this.childrens=new ArrayList<Integer>();
    }
    private Integer id;
    private String name;
    private String description;
    private long rank;

    private long parentRank;

    private Integer parentId;

    private List<Integer> parents;
    private List<Integer> childrens;

    public void  addParents(Integer tree) {
        if (this.parents == null) {
            this.parents = new ArrayList<Integer>();
        }
        this.parents.add(tree);
    }
    public void  addChildrens(Integer tree) {
        if (this.childrens == null) {
            this.childrens = new ArrayList<Integer>();
        }
        if(tree!=this.id) {
            this.childrens.add(tree);
        }
    }
}
