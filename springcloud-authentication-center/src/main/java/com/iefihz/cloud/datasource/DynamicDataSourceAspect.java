package com.iefihz.cloud.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 动态数据源切面
 *
 * @author He Zhifei
 * @date 2020/11/22 21:00
 */
@Component
@Aspect
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(com.iefihz.cloud.datasource.DynamicDataSource) || @within(com.iefihz.cloud.datasource.DynamicDataSource)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            DynamicDataSource dynamicDataSource = null;
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                dynamicDataSource = ((MethodSignature) signature).getMethod().getAnnotation(DynamicDataSource.class);
            }
            if (dynamicDataSource == null) {
                dynamicDataSource = joinPoint.getTarget().getClass().getAnnotation(DynamicDataSource.class);
            }
            if (dynamicDataSource != null) DynamicRoutingDataSource.setDataSource(dynamicDataSource.value().name());
            result = joinPoint.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            DynamicRoutingDataSource.clear();
            return result;
        }
    }
}
