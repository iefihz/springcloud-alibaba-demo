package com.iefihz.cloud.plugins.exception;

/**
 * 自定义异常类CustomException
 *
 * @author He Zhifei
 * @date 2020/6/6 13:50
 */
public class CustomException extends RuntimeException {

    private int code;

    private String message;

    /**
     * 使用时，务必使用异常码-异常信息配对使用，方便后续处理
     * @param resCode
     */
    public CustomException(ResCode resCode) {
        super(resCode.getMessage());
        this.code = resCode.getCode();
        this.message = resCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
