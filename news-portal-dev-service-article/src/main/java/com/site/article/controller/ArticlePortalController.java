package com.site.article.controller;

import com.site.api.BaseController;
import com.site.api.controller.article.ArticleControllerApi;
import com.site.api.controller.article.ArticlePortalControllerApi;
import com.site.article.service.ArticlePortalService;
import com.site.article.service.ArticleService;
import com.site.enums.ArticleCoverType;
import com.site.enums.ArticleReviewStatus;
import com.site.enums.YesOrNo;
import com.site.grace.result.GraceJSONResult;
import com.site.grace.result.ResponseStatusEnum;
import com.site.pojo.Article;
import com.site.pojo.Category;
import com.site.pojo.bo.NewArticleBO;
import com.site.pojo.vo.AppUserVO;
import com.site.pojo.vo.ArticleDetailVO;
import com.site.pojo.vo.IndexArticleVO;
import com.site.utils.IPUtil;
import com.site.utils.JsonUtils;
import com.site.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    final static Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articlePortalService.queryIndexArticleList(keyword, category, page, pageSize);
        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {

        // START
        List<Article> list = (List<Article>)gridResult.getRows();

        // 1. Construct user id list
        // User ID de-duplication
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();

        for (Article a : list) {
            // 1.1 Construct a set for publishers
            idSet.add(a.getPublishUserId());
            // 1.2 Construct a list for article id
            idList.add(REDIS_ARTICLE_READ_COUNTS + ":" + a.getId());
        }

        // Using mget method in redis to query multiple values paired to article id.
        List<String> readCountsRedisList = redis.mget(idList);

        // 2.  Initiating remote call (restTemplate) to request user service to send over user (idSet) list
        List<AppUserVO> publisherList = getPublisherList(idSet);

//        for (AppUserVO u : publisherList) {
//            System.out.println(u.toString());
//        }

        // 3. Concat two lists and reconstruct article list
        // IndexArticleVO contains AppUserVO
        List<IndexArticleVO> indexArticleList = new ArrayList<>();

        for (int i = 0 ; i < list.size() ; i ++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article a = list.get(i);
            BeanUtils.copyProperties(a, indexArticleVO);

            // 3.1 Obtain publisher's basic info from publisherList
            AppUserVO publisher  = getUserIfPublisher(a.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 Rebuild the read counter inside the article page
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleList.add(indexArticleVO);
        }

        gridResult.setRows(indexArticleList);
        //END
        return gridResult;
    }

    // get user's basic info
    private List<AppUserVO> getPublisherList(Set idSet) {
        String userServerUrlExecute = "http://user.inews.com:8003/user/queryByIds?userIds="
                + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }

        return publisherList;
    }

    private AppUserVO getUserIfPublisher(String publisherId, List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equalsIgnoreCase(publisherId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public GraceJSONResult hotList() {
        return GraceJSONResult.ok(articlePortalService.queryHotList());
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult detail(String articleId) {
        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);

        Set<String> idSet = new HashSet<>();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);

        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }

        detailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" + articleId));
        return GraceJSONResult.ok(detailVO);
    }

    @Override
    public GraceJSONResult readArticle(String articleId, HttpServletRequest request) {

        String userIp = IPUtil.getRequestIp(request);
        // Create a key for current user Ip address and save it to redis.
        // Once a user has already read article, the read counter should not increase by re-accessing from same ip address
        redis.setnx(REDIS_ALREADY_READ + ":" + articleId + ":" + userIp, userIp); // If key exist, cannot be update.

        redis.increment(REDIS_ARTICLE_READ_COUNTS + ":" + articleId, 1);
        return GraceJSONResult.ok();
    }

}
