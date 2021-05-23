package com.site.user.controller;

import com.site.api.BaseController;
import com.site.api.controller.user.PassportControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.utils.IPUtil;
import com.site.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private SMSUtils smsUtils;
    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) throws Exception {
        // obtain user ip address
        String userIp = IPUtil.getRequestIp(request);


        // 根据用户的ip进行限制，限制用户在一定时间内只能获得一次验证码
        redis.setnx120s(MOBILE_SMSCODE + ":" + userIp, userIp);

        // Generate random verification code and send SMS
        String random = (int)((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS("18903423733", random);

        // Store the verification code into redis for verification
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60); // 30 min for timeout
        return GraceJSONResult.ok();
    }
}
