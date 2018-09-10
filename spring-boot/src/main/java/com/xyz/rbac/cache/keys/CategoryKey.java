package com.xyz.rbac.cache.keys;

public class CategoryKey extends CacheKeyBase {

    public CategoryKey(String prefix) {
        super(prefix);
    }

    public static CategoryKey TREE = new CategoryKey("tree");
}
