package com.site.api;

import com.site.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {
    @Autowired
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Value("${website.domain-name}")
    public String DOMAIN_NAME;
    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;

    // get the error info from BO
    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error: errorList) {
            //Some properties that corresponds to a validation error
            String field = error.getField();
            // error message
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }

    public void setCookie (HttpServletRequest request, HttpServletResponse response,
                           String cookieName, String cookieValue, Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
//            Cookie cookie = new Cookie(cookieName, cookieValue);
//            cookie.setMaxAge(maxAge);
//            cookie.setDomain("inews.com");
//            cookie.setPath("/");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setCookieValue (HttpServletRequest request, HttpServletResponse response,
        String cookieName, String cookieValue, Integer maxAge) {
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(maxAge);
//          cookie.setDomain("inews.com");
            cookie.setDomain(DOMAIN_NAME);
            cookie.setPath("/");
            response.addCookie(cookie);
    }

}
