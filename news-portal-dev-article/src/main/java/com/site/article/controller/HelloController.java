package com.site.article.controller;

import com.site.api.controller.user.HelloControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redis;
    public Object hello() {

        logger.debug("debug: hello!");
        logger.info("debug: hello!");
        logger.warn("debug: hello!");
        logger.error("debug: hello!");

//        return "hello";
//        return INEWSJSONResult.ok("hello"); // value can be specified inside ok method
        return GraceJSONResult.ok();
    }

    @GetMapping("/redis")
    public Object redis() {
        redis.set("age", "18");
        return GraceJSONResult.ok(redis.get("age"));
    }

}
