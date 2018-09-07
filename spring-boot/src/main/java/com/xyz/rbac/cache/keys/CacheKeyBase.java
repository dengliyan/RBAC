package com.xyz.rbac.cache.keys;

public class CacheKeyBase implements CacheKeyPrefix {

    private String prefix;
    private Integer seconds;

    public CacheKeyBase(String prefix){
        this.prefix=prefix;
        this.seconds=0;

    }
    public CacheKeyBase(String prefix,int seconds){
        this.prefix=prefix;
        this.seconds=seconds;
    }

    @Override
    public String getKey(String key) {
        return this.getClass().getSimpleName().toLowerCase() + ":" + this.prefix + ":" + key;
    }

    @Override
    public Integer getSeconds() {
        return this.seconds;
    }
}
