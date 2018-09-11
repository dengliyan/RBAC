package com.xyz.rbac.data.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Category extends Tree {

    private String description;
}
