package com.hyl.zhanmaoj.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.constant.CommonConstant;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.questionsbumit.QuestionSubmitQueryRequest;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitQueryRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.*;
import com.hyl.zhanmaoj.model.enums.QuestionSubmitStatusEnum;
import com.hyl.zhanmaoj.model.enums.TestSubmitStatusEnum;
import com.hyl.zhanmaoj.model.vo.ChoiceTestVO;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseTestVO;
import com.hyl.zhanmaoj.service.*;
import com.hyl.zhanmaoj.mapper.TestSubmitMapper;
import com.hyl.zhanmaoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hyl.zhanmaoj.common.DateUtils.convertStringToDate;

/**
* @author Alan
* @description 针对表【test_submit(试卷提交)】的数据库操作Service实现
* @createDate 2024-03-17 16:50:59
*/
@Service
public class TestSubmitServiceImpl extends ServiceImpl<TestSubmitMapper, TestSubmit>
    implements TestSubmitService{

    private static final Gson GSON = new Gson();

    @Resource
    private TrueOrFalseSubmitService trueOrFalseSubmitService;

    @Resource
    private ChoiceQuestionSubmitService choiceQuestionSubmitService;


    @Override
    public Wrapper<TestSubmit> getQueryWrapper(TestSubmitQueryRequest testSubmitQueryRequest) {
        QueryWrapper<TestSubmit> queryWrapper = new QueryWrapper<>();
        if (testSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Integer status = testSubmitQueryRequest.getStatus();
        Long testId = testSubmitQueryRequest.getTestId();
        Long userId = testSubmitQueryRequest.getUserId();
        Date createTime = testSubmitQueryRequest.getCreateTime();
        String sortField = testSubmitQueryRequest.getSortField();
        String sortOrder = testSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(testId), "testId", testId);
        queryWrapper.eq(TestSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




