package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendmodel.model.entity.TestUser;
import com.hyl.zhanmaojbackendquestionservice.mapper.TestUserMapper;
import com.hyl.zhanmaojbackendquestionservice.service.TestUserService;
import com.hyl.zhanmaojbackendserviceclient.service.UserFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Alan
* @description 针对表【test_user(试卷题目关联)】的数据库操作Service实现
* @createDate 2024-03-18 19:50:01
*/
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser>
    implements TestUserService {


    @Resource
    private UserFeignClient userFeignClient;


}




