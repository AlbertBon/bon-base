package com.bon.common.domain.enums;

/**
 * Enum 响应类型
 */
public enum ExceptionType {
    SUCCESS("00", "成功"),
    REQUEST_ERROR("100002", "请求信息错误"),
    DATA_ERROR("100003", "数据错误"),

    //登录模块
    LOGIN_AUTHORITY_ERROR("100004", "对不起，您没有该权限"),
    USERNAME_OR_PASSWORD_ERROR("100005","用户名或密码错误"),
    VALIDATE_CODE_ERROR("100006","验证码错误"),
    EXPIRED_ERROR("100007","登录信息过期,请重新登录"),
    USERNAME_NULL_PASSWORD_ERROR("100008","用户获取失败"),
    PASSWORD_NULL_ERROR("100009","用户密码不能为空"),

    //系统异常
    SYSTEM_ERROR("100001", "网络异常"),
    CUSTOMER_ERROR("200001","程序运行异常"),
    REDIS_ERROR("200002","redis连接异常"),
    OTHER_ERROR("200003","其他异常"),
    NULL_ERROR("200004","数据为空错误"),;

    private String code;
    private String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


