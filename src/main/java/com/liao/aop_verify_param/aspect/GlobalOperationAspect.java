package com.liao.aop_verify_param.aspect;

import com.liao.aop_verify_param.annotation.GlobalInterceptor;
import com.liao.aop_verify_param.annotation.VerifyParam;
import com.liao.aop_verify_param.utils.StringTools;
import com.liao.aop_verify_param.utils.VerifyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @BelongsProject: network
 * @BelongsPackage: com.liao.networkdisk.aspect
 * @Author: YunYang Liao
 * @CreateTime: 2023-05-19  23:28
 * @Description: 全局拦截器切面
 * @Version: 1.0
 */
@Aspect // 切面
@Component("GlobalOperationAspect")
public class GlobalOperationAspect {
    private static final String[] TYPES = {"java.lang.String", "java.lang.Integer", "java.lang.Long"};

    // 定义一个切点，以注解为切点
    @Pointcut("@annotation(com.liao.aop_verify_param.annotation.GlobalInterceptor)")
    private void requestInterceptor() {
    }

    @Before("requestInterceptor()")
    private void interceptorDo(JoinPoint point) {
        try {
            // 获取注解对象
            Object target = point.getTarget();
            // 获取参数列表
            Object[] arguments = point.getArgs();
            // 获取方法对象
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (null == interceptor) {
                return;
            }

            /**
             * 校验登录
             */
            if (interceptor.checkLogin() || interceptor.checkAdmin()) {
                checkLogin(interceptor.checkAdmin());
            }

            /**
             * 校验参数
             */
            if (interceptor.checkParams()) {
                validateParams(method, arguments);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * 校验登录
     *
     * @param checkAdmin
     */
    public void checkLogin(boolean checkAdmin) {

    }

    /**
     * 参数校验
     *
     * @param m         方法对象
     * @param arguments 参数集合
     */
    private void validateParams(Method m, Object[] arguments) {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if (verifyParam == null) {
                continue;
            }
            // 基本数据类型
            if (Arrays.asList(TYPES).contains(parameter.getParameterizedType().getTypeName())) {
                checkValue(value, verifyParam);
                // 如果传递的是对象
            } else {
                checkObjValue(parameter, value);
            }
        }
    }

    /**
     * 校验对象
     * 将对象中的属性一个一个判断
     *
     * @param parameter
     * @param value     对象值
     */
    private void checkObjValue(Parameter parameter, Object value) {
        try {
            String typeName = parameter.getParameterizedType().getTypeName();
            Class classz = Class.forName(typeName);
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldVerifyParam = parameter.getAnnotation(VerifyParam.class);
                if (fieldVerifyParam == null) {
                    continue;
                }
                field.setAccessible(true);
                Object resultValue = field.get(value);
                checkValue(resultValue, fieldVerifyParam);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * 校验参数
     *
     * @param value       待校验的参数
     * @param verifyParam 注解对象
     */
    private void checkValue(Object value, VerifyParam verifyParam) {
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();

        /**
         * 校验空
         */
        if (isEmpty && verifyParam.required()) {
            throw new RuntimeException("字符串为空");
        }

        /**
         * 校验长度
         */
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length)) {
            throw new RuntimeException("数据格式错误");
        }
        /**
         * 校验正则
         */
        if (!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex(), String.valueOf(value))) {
            throw new RuntimeException("数据格式错误");
        }
    }


}
