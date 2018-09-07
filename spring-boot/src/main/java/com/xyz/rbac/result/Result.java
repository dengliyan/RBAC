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
    public static Result EXCUTE_ERROR = new Result(500998, "操作失败");

    //系统session
    public static Result USER_NOT_LOGIN = new Result(500990, "未登录");
    public static Result USER_NOT_AUTHORIZED = new Result(500991, "未授权");
    public static Result USER_TOKEN_ILLEGAL = new Result(500992, "token无效");


    public static Result BIND_EXCEPTION=new Result(500000,"参数验证失败，%s");

    //登录
    public static Result USER_NOT_EXISTS=new Result(500010,"用户不存在");
    public static Result USER_PASSWORD_ERROR=new Result(500011,"用户密码不正确");
    public static Result USER_VERIFY_CODE_ERROR=new Result(500012,"验证码不正确");
    public static Result USER_TRY_LOGIN_LIMIT=new Result(500013,"登录次数太多");
    //修改密码
    public static Result USER_ORIGNAL_PASSWORD_ERROR=new Result(500020,"原始密码不正确");
    public static Result USER_PASSWORD_IS_NULL=new Result(500021,"密码不能为空");
    public static Result USER_PASSWORD_NOT_EQUAL=new Result(500022,"两次密码不一致");

    public Result format(Object... args) {
        Integer ret = this.ret;
        String msg = this.msg;
        return new Result(ret, String.format(msg, args));
    }
}
