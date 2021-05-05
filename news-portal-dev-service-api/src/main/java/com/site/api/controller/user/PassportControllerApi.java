package com.site.api.controller.user;

import com.site.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "User Login / Sign-up", tags = {"controller for user Login / Sign-up"})
public interface PassportControllerApi {

    @ApiOperation(value = "Get SMS verification code", notes = "Get SMS verification code", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode() throws Exception;
}
