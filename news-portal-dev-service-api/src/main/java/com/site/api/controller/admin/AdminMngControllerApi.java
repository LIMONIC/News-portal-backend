package com.site.api.controller.admin;

import com.site.pojo.bo.AdminLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Admin management API", tags = {"Admin management API"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {
    @ApiOperation(value = "API for hello method", notes = "API for hello method", httpMethod = "POST")
    @PostMapping("/adminLogin") // route of the method
    public Object adminLogin(@RequestBody AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response);
}
