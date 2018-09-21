package com.xyz.rbac.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArrayVo<T> {
    private Integer id;
    private T[] data;
}
