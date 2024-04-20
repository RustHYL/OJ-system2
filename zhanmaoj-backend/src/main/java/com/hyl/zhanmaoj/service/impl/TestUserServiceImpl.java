package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.model.entity.Test;
import com.hyl.zhanmaoj.model.entity.TestUser;
import com.hyl.zhanmaoj.model.vo.MyTestVO;
import com.hyl.zhanmaoj.service.TestService;
import com.hyl.zhanmaoj.service.TestUserService;
import com.hyl.zhanmaoj.mapper.TestUserMapper;
import com.hyl.zhanmaoj.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Alan
* @description 针对表【test_user(试卷题目关联)】的数据库操作Service实现
* @createDate 2024-03-18 19:50:01
*/
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser>
    implements TestUserService{


    @Resource
    private UserService userService;


}




