package com.site.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.site.api.service.BaseService;
import com.site.enums.Sex;
import com.site.enums.UserStatus;
import com.site.exception.GraceException;
import com.site.grace.result.ResponseStatusEnum;
import com.site.pojo.AppUser;
import com.site.pojo.Fans;
import com.site.pojo.bo.UpdateUserInfoBO;
import com.site.user.mapper.AppUserMapper;
import com.site.user.mapper.FansMapper;
import com.site.user.service.MyFanService;
import com.site.user.service.UserService;
import com.site.utils.*;
import org.checkerframework.checker.units.qual.A;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class MyFanServiceImpl extends BaseService implements MyFanService {

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Override
    public boolean isFollowThisWriter(String writerId, String fanId) {

        Fans fan = new Fans();
        fan.setFanId(fanId);
        fan.setWriterId(writerId);

        int count = fansMapper.selectCount(fan);

        return count > 0 ? true : false;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {

        // Obtain fan info
        AppUser fanInfo = userService.getUser(fanId);

        String fanPkId = sid.nextShort();
        Fans fans = new Fans();
        fans.setId(fanPkId);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);

        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());

        fansMapper.insert(fans);

        // Redis: increment the number of followers
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        // Redis: increment the number of following for current user
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {

        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);

        fansMapper.delete(fans);

        // Redis: decrement the number of followers
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId, -1);
        // Redis: decrement the number of following for current user
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, -1);
    }

    @Override
    public PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize) {

        Fans fans = new Fans();
        fans.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Fans> list = fansMapper.select(fans);


        return setterPagedGrid(list, page);
    }
}
