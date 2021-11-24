package com.iefihz.cloud.plugins.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标记的Controller，将进行异常的增强处理
 * 异常的增强：把异常信息封装到Rs里
 *
 * @author He Zhifei
 * @date 2020/6/6 13:30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionAdvise {
}
