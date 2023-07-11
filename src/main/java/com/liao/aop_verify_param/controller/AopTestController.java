package com.liao.aop_verify_param.controller;

import com.liao.aop_verify_param.annotation.GlobalInterceptor;
import com.liao.aop_verify_param.annotation.VerifyParam;
import com.liao.aop_verify_param.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @GlobalInterceptor(checkParams = true)
    public String test(@VerifyParam(required = true) @PathVariable("id") Integer id,
                       @VerifyParam(required = true) @RequestBody User user) {
        return id + "";
    }

}
