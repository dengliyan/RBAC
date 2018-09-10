package com.xyz.rbac.cache.keys;


import java.util.Date;

public interface CacheKeyPrefix {
    public String getKey(String key);

    public Integer getSeconds();

}
