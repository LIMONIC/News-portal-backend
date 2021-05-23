package com.site.api.controller.user;

import com.site.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Api(value = "User Login / Sign-up", tags = {"controller for user Login / Sign-up"})
@RequestMapping("passport")
public interface PassportControllerApi {

    @ApiOperation(value = "Get SMS verification code", notes = "Get SMS verification code", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    // HttpServletRequest 获取 ip地址
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) throws Exception;
}
