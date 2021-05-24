package com.site.api.controller.user;

import com.site.grace.result.GraceJSONResult;
import com.site.pojo.bo.RegisterLoginBO;
import com.site.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "Controller for user information", tags = {"Controller for user information"})
@RequestMapping("user")
public interface UserControllerApi {

    @ApiOperation(value = "Get user basic info", notes = "Get user basic info", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    // HttpServletRequest 获取 ip地址
    public GraceJSONResult getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "Get user account info", notes = "Get user account info", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    // HttpServletRequest 获取 ip地址
    public GraceJSONResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "Update user info", notes = "Update user info", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    // BO: event from browser. Business Object.
    // result is bind to result
    // @RequestBody means the JSON passed from front end is match with the Object here
    public GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);


}
