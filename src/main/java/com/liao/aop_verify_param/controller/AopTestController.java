package com.liao.aop_verify_param.controller;

import com.liao.aop_verify_param.annotation.ParamInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsPackage: com.liao.aop_verify_param.controller
 * @Author: Liao YunYang
 * @Description:
 * @CreateTime: 2023-07-11  00:10
 * @Version: 1.0
 */
@RestController
public class AopTestController {
    @RequestMapping("/test/{id}")
    @ParamInterceptor(checkParams = true)
    public String test(@PathVariable("id") int id) {
        return "test";
    }

}
