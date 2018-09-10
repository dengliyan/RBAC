package com.xyz.rbac.cache.keys;

import org.springframework.util.StringUtils;

import java.security.Timestamp;
import java.util.Date;

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
        if(StringUtils.isEmpty(key)) {
            return this.getClass().getSimpleName().toLowerCase() + ":" + this.prefix;
        }
        return this.getClass().getSimpleName().toLowerCase() + ":" + this.prefix + ":" + key;
    }

    @Override
    public Integer getSeconds() {
        return this.seconds;
    }
}
