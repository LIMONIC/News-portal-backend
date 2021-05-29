package com.site.admin.controller;

import com.site.admin.service.FriendLinkService;
import com.site.api.BaseController;
import com.site.api.controller.admin.FriendLinkControllerApi;
import com.site.grace.result.GraceJSONResult;
import com.site.pojo.bo.SaveFriendLinkBO;
import com.site.pojo.mo.FriendLinkMO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;


@RestController
public class FriendLinkMngController extends BaseController implements FriendLinkControllerApi {

//    final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);

    @Autowired
    private FriendLinkService friendLinkService;

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(@Valid SaveFriendLinkBO saveFriendLinkBO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }

        // copy saveFriendLinkBO to saveFriendLinkMO
        FriendLinkMO saveFriendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, saveFriendLinkMO);
        saveFriendLinkMO.setCreateTime(new Date());
        saveFriendLinkMO.setUpdateTime(new Date());

        friendLinkService.saveOrUpdateFriendLink(saveFriendLinkMO);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFriendLinkList() {

        return GraceJSONResult.ok(friendLinkService.queryAllFriendList());
    }

    @Override
    public GraceJSONResult delete(String linkId) {
        friendLinkService.deleteFriendList(linkId);
        return GraceJSONResult.ok();
    }
}
