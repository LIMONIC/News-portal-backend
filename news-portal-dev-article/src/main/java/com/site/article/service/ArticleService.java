package com.site.article.service;

import com.site.pojo.Category;
import com.site.pojo.bo.NewArticleBO;
import com.site.utils.PagedGridResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface ArticleService {

    /*
    * Post Article
    * */
    public void createArticle(NewArticleBO newArticleBO, Category category);

    /**
     * Update scheduled articles to instant release
     */
    public void updateAppointToPublish();

    /**
     * User center - query my article list
     * @return
     */
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate,
                                              Date endDate, Integer page, Integer pageSize);

}


