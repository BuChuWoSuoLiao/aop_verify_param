package com.liao.aop_verify_param.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @BelongsPackage: com.liao.aop_verify_param.annotation
 * @Author: Liao YunYang
 * @Description: 验证拦截注解
 * @CreateTime: 2023-07-10  23:41
 * @Version: 1.0
 */
@Target({ElementType.METHOD})       // 注解写在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时触发
@Mapping
public @interface ParamInterceptor {

    /**
     * 校验参数
     *
     * @return
     */
    boolean checkParams() default false;

    /**
     * 校验登录
     *
     * @return
     */
    boolean checkLogin() default false;
}
