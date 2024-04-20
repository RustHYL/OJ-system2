package com.hyl.zhanmaoj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaoj.model.entity.TestUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.vo.MyTestVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Alan
* @description 针对表【test_user(试卷题目关联)】的数据库操作Service
* @createDate 2024-03-18 19:50:01
*/
public interface TestUserService extends IService<TestUser> {


}
