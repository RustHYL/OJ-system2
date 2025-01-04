package com.hyl.zhanmaojbackendserviceclient.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.test.MyTestQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestJoinRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.Test;
import com.hyl.zhanmaojbackendmodel.model.entity.TestUser;
import com.hyl.zhanmaojbackendmodel.model.vo.MyTestVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Alan
* @description 针对表【test(试卷)】的数据库操作Service
* @createDate 2024-03-14 22:42:51
*/
public interface TestService extends IService<Test> {


    QueryWrapper<Test> getQueryWrapper(TestQueryRequest testQueryRequest);

    boolean joinTest(TestJoinRequest testJoinRequest);

    Page<MyTestVO> getMyTestVOPage(Page<TestUser> testUserPage, HttpServletRequest request, MyTestQueryRequest myTestQueryRequest);
}
