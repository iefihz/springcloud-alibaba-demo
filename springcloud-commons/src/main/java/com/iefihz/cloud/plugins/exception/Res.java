package com.iefihz.cloud.plugins.exception;

import java.io.Serializable;

/**
 * 接口返回值包装类（尽可能使用静态方法，便于控制异常码与异常信息的对应关系）
 * @param <T>  返回数据具体类型
 *
 * @author He Zhifei
 * @date 2020/6/6 13:47
 */
public class Res<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应信息，用来说明响应情况
     */
    private String message;

    /**
     * 响应的具体数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private Res() {
    }

    /**
     * 构造器私有化，对外提供静态方法
     * @param code
     * @param message
     * @param data
     */
    private Res(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Res<T> success() {
        return new Res<T>(ResCode.SUCCESS.getCode(), ResCode.SUCCESS.getMessage(), null);
    }

    public static <T> Res<T> successMessage(String message) {
        return new Res<T>(ResCode.SUCCESS.getCode(), message, null);
    }

    public static <T> Res<T> successData(T data) {
        return new Res<T>(ResCode.SUCCESS.getCode(), ResCode.SUCCESS.getMessage(), data);
    }

    public static <T> Res<T> success(String message, T data) {
        return new Res<T>(ResCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Res<T> fail() {
        return new Res(ResCode.FAILED.getCode(), ResCode.FAILED.getMessage(), null);
    }

    public static <T> Res<T> failMessage(String message) {
        return new Res(ResCode.FAILED.getCode(), message, null);
    }

    public static <T> Res<T> failData(T data) {
        return new Res(ResCode.FAILED.getCode(), ResCode.FAILED.getMessage(), data);
    }

    public static <T> Res<T> fail(ResCode resCode) {
        return new Res(resCode.getCode(), resCode.getMessage(), null);
    }

    public static <T> Res<T> fail(int code, String message) {
        return new Res(code, message, null);
    }
}
