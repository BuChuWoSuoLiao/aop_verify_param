package com.liao.aop_verify_param.aspect;

import com.liao.aop_verify_param.annotation.ParamInterceptor;
import com.liao.aop_verify_param.annotation.VerifyParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @BelongsPackage: com.liao.aop_verify_param.aspect
 * @Author: Liao YunYang
 * @Description: ParamInterceptor的切面
 * @CreateTime: 2023-07-10  23:53
 * @Version: 1.0
 */
@Aspect // 切面
@Component("ParamOperationAspect")
public class ParamOperationAspect {

    private static final String[] TYPES = {"java.lang.String"};

    // 定义一个切点
    @Pointcut("@annotation(com.liao.aop_verify_param.annotation.ParamInterceptor)")
    private void VerifyParamInterceptor() {
    }

    @Before("VerifyParamInterceptor()")
    private void VerifyParamInterceptor(JoinPoint point) {
        try {
            Object target = point.getTarget();  // 获取一个目标对象
            Object[] args = point.getArgs();    // 获取切点的参数

            // 获取方法名字
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            ParamInterceptor interceptor = method.getAnnotation(ParamInterceptor.class);

            if (null == interceptor) {
                return;
            }

            /**
             * 校验参数
             */
            if (interceptor.checkParams()) {
                checkParams(method, args);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private void checkParams(Method m, Object[] arguments) {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {   // 循环判断
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

    private void checkObjValue(Parameter parameter, Object value) {
        try {
            String typeName = parameter.getParameterizedType().getTypeName();
            Class classz = Class.forName(typeName);
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldVerifyParam = field.getAnnotation(VerifyParam.class);
                if (fieldVerifyParam == null) {
                    continue;
                }
                field.setAccessible(true);
                Object resultValue = field.get(value);
                checkValue(resultValue, fieldVerifyParam);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 校验参数
     *
     * @param value
     * @param verifyParam
     */
    private void checkValue(Object value, VerifyParam verifyParam) {
        Boolean isEmpty = value == null;
        Integer length = value == null ? 0 : value.toString().length();

        /**
         * 校验空
         */
        if (isEmpty && verifyParam.whetherCheck()) {
            // throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        /**
         * 校验长度
         */
        if (!isEmpty && (verifyParam.maxLen() != -1 && verifyParam.maxLen() < length || verifyParam.minLen() != -1 && verifyParam.minLen() > length)) {
            // throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

}
