package com.site.api.controller.article;

import com.site.grace.result.GraceJSONResult;
import com.site.pojo.bo.NewArticleBO;
import com.site.pojo.bo.SaveCategoryBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "Article controller", tags = {"Article controller"})
@RequestMapping("article")
public interface ArticleControllerApi {

    @PostMapping("createArticle")
    @ApiOperation(value = "User posts an article", notes = "User posts an article", httpMethod = "POST")
    public GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO,
                                                BindingResult result);

//    @PostMapping("queryMyList")
//    @ApiOperation(value = "Query all articles posted by current user", notes = "Query all articles posted by current user", httpMethod = "POST")
//    public GraceJSONResult queryMyList(@RequestParam String userId, @RequestParam String keyword, @RequestParam Integer status, @RequestParam Date startDate,
//                                       @RequestParam Date endDate, @RequestParam Integer page, @RequestParam Integer pageSize);

    @PostMapping("queryMyList")
    @ApiOperation(value = "查询用户的所有文章列表", notes = "查询用户的所有文章列表", httpMethod = "POST")
    public GraceJSONResult queryMyList(@RequestParam  String userId,
                                       @RequestParam String keyword,
                                       @RequestParam(value = "status") Integer status,
                                       @RequestParam Date startDate,
                                       @RequestParam Date endDate,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize);

}
