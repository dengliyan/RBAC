package com.xyz.rbac.exception;

import com.xyz.rbac.result.Result;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    private Result result;

    public  BusinessException(Result result) {
        this.result = result;
    }
}

