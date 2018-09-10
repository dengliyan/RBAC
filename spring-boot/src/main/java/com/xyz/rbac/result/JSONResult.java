package com.xyz.rbac.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JSONResult<T> {
    private Integer ret;
    private String msg;
    private T data;

    private JSONResult(Result result){
        this.ret=result.getRet();
        this.msg=result.getMsg();
    }

    private JSONResult(T data){
        this.ret=0;
        this.msg="";
        this.data=data;
    }

    public static JSONResult SUCCESS=new JSONResult(Result.NONE);

    public static JSONResult FAILURE=new JSONResult(Result.EXCUTE_ERROR);

    public static <T> JSONResult<T> success(T data){
        return  new JSONResult<T>(data);
    }
    public static <T> JSONResult<T> error(Result result) {
        if (result != null) {
            return new JSONResult<T>(result);
        }
        return null;
    }
}
