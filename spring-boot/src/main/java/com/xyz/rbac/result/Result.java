package com.xyz.rbac.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private Result(Integer ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    private Integer ret;
    private String msg;

    public static Result NONE = new Result(0, "");
    public static Result SYSTEM_ERROR = new Result(500999, "系统异常");
    public static Result OPER_ERROR = new Result(500998, "操作失败");

    //系统session
    public static Result USER_NOT_LOGIN = new Result(500990, "未登录");
    public static Result USER_NOT_AUTHORIZED = new Result(500991, "未授权");
    public static Result USER_TOKEN_ILLEGAL = new Result(500992, "token无效");

    public static Result BIND_EXCEPTION=new Result(500000,"参数验证失败，%s");

    //登录
    public static Result USER_NOT_EXISTS=new Result(500010,"用户不存在");
    public static Result USER_PASSWORD_ERROR=new Result(500010,"用户密码不正确");


    public Result format(Object... args) {
        Integer ret = this.ret;
        String msg = this.msg;
        return new Result(ret, String.format(msg, args));
    }
}
