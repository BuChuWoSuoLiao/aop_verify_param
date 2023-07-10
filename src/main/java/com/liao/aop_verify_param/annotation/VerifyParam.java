package com.liao.aop_verify_param.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @BelongsPackage: com.liao.aop_verify_param.annotation
 * @Author: Liao YunYang
 * @Description: 用户获取参数的注解
 * @CreateTime: 2023-07-10  23:45
 * @Version: 1.0
 */
@Target({ElementType.FIELD})  // 可以将注解定义在字段上
public @interface VerifyParam {
    /**
     * 最小长度
     * 默认值为 -1
     *
     * @return
     */
    int minLen() default -1;

    /**
     * 最大长度
     * 默认值为 -1
     *
     * @return
     */
    int maxLen() default -1;

    /**
     * 是否校验参数
     * 必填项
     *
     * @return
     */
    boolean whetherCheck() default false;
}
