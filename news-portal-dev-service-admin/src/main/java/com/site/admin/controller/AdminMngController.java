package com.site.admin.controller;

import com.site.admin.service.AdminUserService;
import com.site.api.controller.admin.AdminMngControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.grace.result.ResponseStatusEnum;
import com.site.pojo.AdminUser;
import com.site.pojo.bo.AdminLoginBO;
import com.site.utils.RedisOperator;
import com.site.api.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;


@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

//    final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);

    @Autowired
    private RedisOperator redis;

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {

        // 0. Verify user name and password are not empty
        if (StringUtils.isBlank(adminLoginBO.getUsername())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isBlank(adminLoginBO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }

        // 1. Query admin info
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());

        // 2. Check if admin exist
        if (admin == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        // 3. Check if password match
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
        if (isPwdMatch) {
            doLoginSettings(admin, request, response);
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }


    /**
     * Set basic info for admin after successful login
     * @param admin
     * @param request
     * @param response
     */
    private void doLoginSettings (AdminUser admin,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        // Generate token，Save it into redis
        String uniqueToken = UUID.randomUUID().toString().trim();
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(), uniqueToken);

        // Write token and user info into cookie

        setCookie(request, response, "atoken", uniqueToken, COOKIE_MONTH);
        setCookie(request, response, "aid", admin.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", admin.getAdminName(), COOKIE_MONTH);

    }
}
