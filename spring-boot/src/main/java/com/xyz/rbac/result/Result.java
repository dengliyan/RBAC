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
    public static Result SYSTEM_ERROR = new Result(599999, "系统异常");
    public static Result EXCUTE_ERROR = new Result(599998, "操作失败");

    public static Result DB_EXCUTE_ERROR = new Result(599900, "数据库异常");
    public static Result DB_EXCUTE_DUPLICATE_ERROR = new Result(599901, "数据主键冲突");
    public static Result DB_QUERY_NOT_EXISTS = new Result(599902, "数据不存在");

    //系统session
    public static Result USER_NOT_LOGIN = new Result(500990, "未登录");
    public static Result USER_NOT_AUTHORIZED = new Result(500991, "未授权");
    public static Result USER_TOKEN_ILLEGAL = new Result(500992, "token无效");




    public static Result ARGUMENTS_ERROR =new Result(500980,"%s");
    public static Result ARGUMENTS_VALIDATION_EXCEPTION =new Result(500981,"参数不正确");


    //登录
    public static Result USER_NOT_EXISTS=new Result(500010,"用户不存在");
    public static Result USER_PASSWORD_ERROR=new Result(500011,"用户密码不正确");
    public static Result USER_VERIFY_CODE_ERROR=new Result(500012,"验证码不正确");
    public static Result USER_TRY_LOGIN_LIMIT=new Result(500013,"登录次数太多，请稍后再试");
    //修改密码
    public static Result USER_ORIGNAL_PASSWORD_ERROR=new Result(500020,"原始密码不正确");
    public static Result USER_PASSWORD_IS_NULL=new Result(500021,"密码不能为空");
    public static Result USER_PASSWORD_NOT_EQUAL=new Result(500022,"两次密码不一致");

    public static Result TREE_FILTER_LEVEL_DIFF=new Result(500030,"树层级不一致");
    public static Result TREE_FILTER_LEVEL_EMPTY=new Result(500031,"树层级不能为空");
    public static Result TREE_EXISTS_SAME_NAME=new Result(500032,"存在相同的名称");

    public Result format(Object... args) {
        Integer ret = this.ret;
        String msg = this.msg;
        return new Result(ret, String.format(msg, args));
    }
}
