package com.site.user.controller;

import com.site.api.BaseController;
import com.site.api.controller.user.HelloControllerApi;
import com.site.api.controller.user.UserControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.grace.result.ResponseStatusEnum;
import com.site.pojo.AppUser;
import com.site.pojo.bo.UpdateUserInfoBO;
import com.site.pojo.vo.AppUserVO;
import com.site.pojo.vo.UserAccountInfoVO;
import com.site.user.service.UserService;
import com.site.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        // 0. parameter cannot be null
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1. Query user info by userId
        AppUser user = getUser(userId);

        // 2. return user info
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);

        return GraceJSONResult.ok(userVO);
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId)  {

        // 0. parameter cannot be null
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1. Query user info by userId
        AppUser user = getUser(userId);

        // 2. return user info
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, accountInfoVO);

        return GraceJSONResult.ok(accountInfoVO);
    }

    private AppUser getUser(String userId) {
        // this method will be used by other code and will be extended.
        AppUser user = userService.getUser(userId);
        return user;
    }

    @Override
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO, BindingResult result) {

        // 0. Check BO
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }

        // 1. update user info
        userService.updateUserInfo(updateUserInfoBO);

        return GraceJSONResult.ok();
    }


}
