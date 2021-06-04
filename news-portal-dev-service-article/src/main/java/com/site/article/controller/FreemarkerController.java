package com.site.article.controller;

import com.site.pojo.Spouse;
import com.site.pojo.Stu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("free")
public class FreemarkerController {

    final static Logger logger = LoggerFactory.getLogger(FreemarkerController.class);

    @GetMapping("/hello")
    public String hello(Model model) {

        // Define the contents that insert to the template.
        // Output string
        String stranger = "Ty";
        model.addAttribute("there", stranger);

        makeModel(model);
        // "stu" here is the address of freemarker template classpath:/templates/
        // match *.ftl
        return "stu";
    }

    private Model makeModel(Model model) {
        Stu stu = new Stu();
        stu.setUid("10010");
        stu.setUsername("inews");
        stu.setAmount(23.54f);
        stu.setAge(23);
        stu.setHaveChild(true);
        stu.setBirthday(new Date());

        Spouse spouse = new Spouse();
        spouse.setUsername("Bob");
        spouse.setAge(18);

        stu.setSpouse(spouse);

        model.addAttribute("stu", stu);
        return model;
    }

}
