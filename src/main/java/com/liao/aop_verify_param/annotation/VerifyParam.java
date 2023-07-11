package com.liao.aop_verify_param.annotation;

import com.liao.aop_verify_param.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验参数的注解定义
 */
@Target({ElementType.PARAMETER, ElementType.FIELD}) // 可以定义在字段和形参上
@Retention(RetentionPolicy.RUNTIME) // 启动的时候生效
public @interface VerifyParam {
    /**
     * 校验最小长度
     *
     * @return
     */
    int min() default -1;

    /**
     * 校验最大长度
     *
     * @return
     */
    int max() default -1;

    /**
     * 校验正则
     *
     * @return
     */
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;

    /**
     * 是否校验
     * 必填项
     *
     * @return
     */
    boolean required() default false;

}
