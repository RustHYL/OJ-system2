package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.test.MyTestQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestJoinRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.Test;
import com.hyl.zhanmaojbackendmodel.model.entity.TestUser;
import com.hyl.zhanmaojbackendmodel.model.enums.TestStatusEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.MyTestVO;
import com.hyl.zhanmaojbackendquestionservice.mapper.TestMapper;
import com.hyl.zhanmaojbackendquestionservice.service.TestService;
import com.hyl.zhanmaojbackendquestionservice.service.TestUserService;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hyl.zhanmaojbackendcommon.common.DateUtils.convertStringToDate;


/**
* @author Alan
* @description 针对表【test(试卷)】的数据库操作Service实现
* @createDate 2024-03-14 22:42:51
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
    implements TestService {


    @Resource
    private TestUserService testUserService;

    @Resource
    private UserService userService;

    private ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    @Override
    public QueryWrapper<Test> getQueryWrapper(TestQueryRequest testQueryRequest) {
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
        if (testQueryRequest == null) {
            return queryWrapper;
        }
        Long id = testQueryRequest.getId();
        String title = testQueryRequest.getTitle();
        Integer status = testQueryRequest.getStatus();
        String content = testQueryRequest.getContent();
        Long userId = testQueryRequest.getUserId();
        Date createTime = testQueryRequest.getCreateTime();
        Date updateTime = testQueryRequest.getUpdateTime();
        String sortField = testQueryRequest.getSortField();
        String sortOrder = testQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotEmpty(title), "title", title);
        queryWrapper.like(StringUtils.isNotEmpty(content), "content", content);
        queryWrapper.eq(TestStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public boolean joinTest(TestJoinRequest testJoinRequest) {
        Long testId = testJoinRequest.getTestId();
        String password = testJoinRequest.getPassword();
        Test test = this.getById(testId);
        if (test == null || (test.getStatus().equals(TestStatusEnum.ENCRYPTION.getValue()) && StringUtils.isBlank(password))) {
            return false;
        }
        if ((test.getStatus().equals(TestStatusEnum.ENCRYPTION.getValue()) && !test.getPassword().equals(password))) {
            return false;
        }
        return true;

    }

    @Override
    public Page<MyTestVO> getMyTestVOPage(Page<TestUser> testUserPage, HttpServletRequest request, MyTestQueryRequest myTestQueryRequest) {
        List<TestUser> testUserList = testUserPage.getRecords();
        Page<MyTestVO> myTestVOPage = new Page<>(testUserPage.getCurrent(), testUserPage.getSize(), testUserPage.getTotal());
        if (CollectionUtils.isEmpty(testUserList)){
            return myTestVOPage;
        }
        List<Test> testList = testUserList.stream().map(testUser -> this.getById(testUser.getTestId())).collect(Collectors.toList());
        //填充信息
        List<MyTestVO> myTestVOList = testList.stream().map(test -> {
            MyTestVO myTestVO = new MyTestVO();
            BeanUtils.copyProperties(test, myTestVO);
            QueryWrapper<TestUser> queryWrapper = new QueryWrapper();
            queryWrapper.eq("userId", userService.getLoginUser(request).getId());
            queryWrapper.eq("testId", test.getId());
            TestUser testUser = testUserService.getOne(queryWrapper);
            if (testUser != null) {
                myTestVO.setScore(testUser.getScore());
            } else {
                myTestVO.setScore(0);
            }
            return myTestVO;
        }).collect(Collectors.toList());
        String title = myTestQueryRequest.getTitle();
        if (title != null){
            myTestVOList = myTestVOList.stream().filter(myTestVO -> myTestVO.getTitle() != null && myTestVO.getTitle().contains(title))
                    .collect(Collectors.toList());
        }
        myTestVOPage.setRecords(myTestVOList);
        return myTestVOPage;
    }


}




