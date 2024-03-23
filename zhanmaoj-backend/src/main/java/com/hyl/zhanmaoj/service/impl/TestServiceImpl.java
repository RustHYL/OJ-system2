package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hyl.zhanmaoj.constant.CommonConstant;
import com.hyl.zhanmaoj.model.dto.test.TestJoinRequest;
import com.hyl.zhanmaoj.model.dto.test.TestQueryRequest;
import com.hyl.zhanmaoj.model.entity.Test;
import com.hyl.zhanmaoj.model.entity.TestUser;
import com.hyl.zhanmaoj.model.enums.TestStatusEnum;
import com.hyl.zhanmaoj.service.TestService;
import com.hyl.zhanmaoj.mapper.TestMapper;
import com.hyl.zhanmaoj.service.TestUserService;
import com.hyl.zhanmaoj.service.UserService;
import com.hyl.zhanmaoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hyl.zhanmaoj.common.DateUtils.convertStringToDate;

/**
* @author Alan
* @description 针对表【test(试卷)】的数据库操作Service实现
* @createDate 2024-03-14 22:42:51
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
    implements TestService{


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
        if (test == null || !test.getPassword().equals(password)) {
            return false;
        }
        return true;

    }
}




