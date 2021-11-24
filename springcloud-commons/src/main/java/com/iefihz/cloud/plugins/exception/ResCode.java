package com.iefihz.cloud.plugins.exception;

/**
 * 异常码（只需新增异常信息，抛出自定义的通用异常即可）
 *
 * @author He Zhifei
 * @date 2020/6/6 13:45
 */
public enum ResCode {

    SUCCESS(10000, "操作成功"),

    FAILED(-10000, "操作失败"),

    VALIDATE_FAILED(-10001, "参数校验失败"),

    MISSING_REQUEST_BODY(-10002, "缺失请求体"),

    NEED_RELOGIN(-20000, "请重新登录"),

    NEED_REFRESH_TOKEN(-20001, "请重新获取access_token"),

    ERROR(-50000, "未知错误"),

    UNAUTHORIZED(-51000, "权限不足"),

    INCORRECT_CREDENTIALS(-51001, "密码不正确");

    private int code;

    private String message;

    ResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}