package com.site.user.controller;

import com.site.api.controller.user.HelloControllerApi;
import com.site.api.controller.user.PassportControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private SMSUtils smsUtils;
    @Override
    public GraceJSONResult getSMSCode() throws Exception {
        String random = "123456";

        smsUtils.sendSMS("18903423733", random);
        return GraceJSONResult.ok();
    }
}
