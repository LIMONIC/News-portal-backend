package com.site.admin.service.impl;

import com.site.admin.mapper.AdminUserMapper;
import com.site.admin.service.AdminUserService;
import com.site.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    public AdminUserMapper adminUserMapper;


    @Override
    public AdminUser queryAdminByUsername(String username) {

        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser admin = adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }
}
