package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.model.entity.TestUser;
import com.hyl.zhanmaoj.service.TestUserService;
import com.hyl.zhanmaoj.mapper.TestUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Alan
* @description 针对表【test_user(试卷题目关联)】的数据库操作Service实现
* @createDate 2024-03-18 19:50:01
*/
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser>
    implements TestUserService{

}




