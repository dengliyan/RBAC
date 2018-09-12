package com.xyz.rbac.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    private static final Random random=new Random();

    public static  String salt(int length){
        char[] chars=new char[length];
        for (int i = 0,len=length; i < len; i++) {
            if (random.nextInt(2) == 0) {
                chars[i] = (char) (97 + random.nextInt(26));

            } else {
                chars[i] = (char) (48 + random.nextInt(10));
            }
        }
        return  new String(chars);
    }


    public static void main(String[] args){
        System.out.println("salt="+salt(8));
        System.out.println("salt="+salt(8));
        System.out.println("salt="+salt(8));
        System.out.println("salt="+salt(8));
        System.out.println("salt="+salt(8));
        System.out.println("salt="+salt(8));
    }
}
