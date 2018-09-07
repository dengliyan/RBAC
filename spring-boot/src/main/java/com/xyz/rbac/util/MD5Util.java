package com.xyz.rbac.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String s){
        return DigestUtils.md5Hex(s);
    }

    public static String pwd(String s,String salt) {
        return md5(salt.substring(0, 4) + "123456" + s + "abcefg" + salt.substring(4, 8));
    }

    public static void main(String[] args){
        System.out.println(pwd("123456","q1w2e3r4"));
    }
}
