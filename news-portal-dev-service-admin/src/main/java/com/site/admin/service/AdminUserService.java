package com.site.admin.service;

import com.site.pojo.AdminUser;

public interface AdminUserService {

    /*
    * Get admin user's info
    * */
    public AdminUser queryAdminByUsername(String username);

}
