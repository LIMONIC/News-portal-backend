package com.site.api.controller.admin;

import com.site.grace.result.GraceJSONResult;
import com.site.pojo.bo.AdminLoginBO;
import com.site.pojo.bo.NewAdminBO;
import com.site.pojo.bo.SaveFriendLinkBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "Manage friendship links on homepage", tags = {"Controller for managing friendship links on homepage"})
@RequestMapping("friendLinkMng")
public interface FriendLinkControllerApi {
    @ApiOperation(value = "API for adding / modifying friendship links", notes = "API for adding / modifying friendship links", httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink") // route of the method
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO, BindingResult result);

    @ApiOperation(value = "API for querying the list of friendship links", notes = "API for querying the list of friendship links", httpMethod = "POST")
    @PostMapping("/getFriendLinkList") // route of the method
    public GraceJSONResult getFriendLinkList();

    @ApiOperation(value = "API for deleting friendship link", notes = "API for deleting friendship link", httpMethod = "POST")
    @PostMapping("/delete") // route of the method
    public GraceJSONResult delete(@RequestParam String linkId);
}