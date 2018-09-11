package com.xyz.rbac.cache.keys;

public class DepartmentKey extends CacheKeyBase {

    public DepartmentKey(String prefix)  {
        super(prefix);
    }

    public static DepartmentKey TREE = new DepartmentKey("tree");
}
