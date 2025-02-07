package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.testSubmit.TestSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TestSubmit;
import com.hyl.zhanmaojbackendmodel.model.enums.TestSubmitStatusEnum;
import com.hyl.zhanmaojbackendquestionservice.mapper.TestSubmitMapper;
import com.hyl.zhanmaojbackendquestionservice.service.TestSubmitService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.hyl.zhanmaojbackendcommon.common.DateUtils.convertStringToDate;


/**
* @author Alan
* @description 针对表【test_submit(试卷提交)】的数据库操作Service实现
* @createDate 2024-03-17 16:50:59
*/
@Service
public class TestSubmitServiceImpl extends ServiceImpl<TestSubmitMapper, TestSubmit>
    implements TestSubmitService {


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




