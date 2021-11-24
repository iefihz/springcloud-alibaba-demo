package com.iefihz.cloud.plugins.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author He Zhifei
 * @date 2020/8/24 19:53
 */
@RestControllerAdvice(/*basePackages = {"com.iefihz.controller"}, */
        annotations = {ExceptionAdvise.class})
public class GlobalControllerExceptionAdvice extends AbstractControllerExceptionAdvice {

    //方便其他系统（或配置指定异常对应的异常码）使用，异常与返回结果描述信息对应
    static {
//        // shiro认证
//        builder.put(IncorrectCredentialsException.class, ResCode.INCORRECT_CREDENTIALS);
//        // shiro鉴权
//        builder.put(UnauthorizedException.class, ResCode.UNAUTHORIZED);
    }
}
