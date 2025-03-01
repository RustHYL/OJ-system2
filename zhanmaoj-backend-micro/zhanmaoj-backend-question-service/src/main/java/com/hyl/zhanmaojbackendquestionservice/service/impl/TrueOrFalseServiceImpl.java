package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.exception.ThrowUtils;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse.TrueOrFalseQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse.TrueOrFalseQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TestQuestion;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalse;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalseSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionTypeEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.TrueOrFalseAnswerEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseTestDetailVO;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseTitleVO;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseVO;
import com.hyl.zhanmaojbackendmodel.model.vo.UserVO;
import com.hyl.zhanmaojbackendquestionservice.mapper.TrueOrFalseMapper;
import com.hyl.zhanmaojbackendquestionservice.service.TestQuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseService;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseSubmitService;
import com.hyl.zhanmaojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.hyl.zhanmaojbackendcommon.common.DateUtils.convertStringToDate;


/**
* @author Alan
* @description 针对表【true_or_false(判断题目)】的数据库操作Service实现
* @createDate 2024-03-14 22:43:01
*/
@Service
public class TrueOrFalseServiceImpl extends ServiceImpl<TrueOrFalseMapper, TrueOrFalse>
    implements TrueOrFalseService {

    @Resource
    private UserFeignClient userFeignClient;


    @Resource
    private TrueOrFalseMapper trueOrFalseMapper;

    @Resource
    @Lazy
    private TrueOrFalseSubmitService trueOrFalseSubmitService;
    
    @Resource
    private TestQuestionService testQuestionService;
    /**
     * 校验题目是否合法
     * @param trueOrFalse
     * @param add
     */
    @Override
    public void validTrueOrFalse(TrueOrFalse trueOrFalse, boolean add) {
        if (trueOrFalse == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = trueOrFalse.getTitle();
        String tags = trueOrFalse.getTags();
        Integer answer = trueOrFalse.getAnswer();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank( title, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (TrueOrFalseAnswerEnum.getEnumByValue(answer) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案格式不符合要求");
        }
    }
    @Override
    public QueryWrapper<TrueOrFalse> getQueryWrapper(TrueOrFalseQueryRequest trueOrFalseQueryRequest) {
        QueryWrapper<TrueOrFalse> queryWrapper = new QueryWrapper<>();
        if (trueOrFalseQueryRequest == null) {
            return queryWrapper;
        }
        Long id = trueOrFalseQueryRequest.getId();
        String title = trueOrFalseQueryRequest.getTitle();
        List<String> tags = trueOrFalseQueryRequest.getTags();
        Long userId = trueOrFalseQueryRequest.getUserId();
        String sortField = trueOrFalseQueryRequest.getSortField();
        String sortOrder = trueOrFalseQueryRequest.getSortOrder();
        Date createTime = trueOrFalseQueryRequest.getCreateTime();
        Date updateTime = trueOrFalseQueryRequest.getUpdateTime();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public List<TrueOrFalse> getRandomTrueOrFalseList(Integer num) {
        return trueOrFalseMapper.getRandomTrueOrFalseList(num);
    }

    @Override
    public QueryWrapper<TrueOrFalse> getQueryWrapper(TrueOrFalseQueryAdminRequest trueOrFalseQueryRequest) {
        QueryWrapper<TrueOrFalse> queryWrapper = new QueryWrapper<>();
        if (trueOrFalseQueryRequest == null) {
            return queryWrapper;
        }
        Long id = trueOrFalseQueryRequest.getId();
        String title = trueOrFalseQueryRequest.getTitle();
        String tags = trueOrFalseQueryRequest.getTags();
        Long userId = trueOrFalseQueryRequest.getUserId();
        String sortField = trueOrFalseQueryRequest.getSortField();
        String sortOrder = trueOrFalseQueryRequest.getSortOrder();
        Date createTime = trueOrFalseQueryRequest.getCreateTime();
        Date updateTime = trueOrFalseQueryRequest.getUpdateTime();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(tags), "tags", tags);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public TrueOrFalseVO getTrueOrFalseVO(TrueOrFalse trueOrFalse, HttpServletRequest request) {
        TrueOrFalseVO trueOrFalseVO = TrueOrFalseVO.objToVo(trueOrFalse);
        // 1. 关联查询用户信息
        Long userId = trueOrFalse.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        UserVO userVO = userFeignClient.getUserVO(user);
        trueOrFalseVO.setUserVO(userVO);
        return trueOrFalseVO;
    }

    @Override
    public Page<TrueOrFalseVO> getTrueOrFalseVOPage(Page<TrueOrFalse> trueOrFalsePage, HttpServletRequest request) {
        List<TrueOrFalse> trueOrFalseList = trueOrFalsePage.getRecords();
        Page<TrueOrFalseVO> trueOrFalseVOPage = new Page<>(trueOrFalsePage.getCurrent(), trueOrFalsePage.getSize(), trueOrFalsePage.getTotal());
        if (CollectionUtils.isEmpty(trueOrFalseList)) {
            return trueOrFalseVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = trueOrFalseList.stream().map(TrueOrFalse::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<TrueOrFalseVO> trueOrFalseVOList = trueOrFalseList.stream().map(trueOrFalse -> {
            TrueOrFalseVO trueOrFalseVO = TrueOrFalseVO.objToVo(trueOrFalse);
            Long userId = trueOrFalse.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            trueOrFalseVO.setUserVO(userFeignClient.getUserVO(user));
            return trueOrFalseVO;
        }).collect(Collectors.toList());
        trueOrFalseVOPage.setRecords(trueOrFalseVOList);
        return trueOrFalseVOPage;
    }

    @Override
    public List<TrueOrFalseTestDetailVO> getTrueOrFalseTestDetailList(long testId) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", testId);
        queryWrapper.eq("type", QuestionTypeEnum.TRUE_OR_FALSE.getValue());
        List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOList = new ArrayList<>();
        List<TestQuestion> testQuestionList = testQuestionService.list(queryWrapper);
        if (CollectionUtils.isEmpty(testQuestionList)) {
            return trueOrFalseTestDetailVOList;
        }
        testQuestionList.forEach(testQuestion -> {
            TrueOrFalseTestDetailVO trueOrFalseTestDetailVO = new TrueOrFalseTestDetailVO();
            TrueOrFalse trueOrFalse = this.getById(testQuestion.getQuestionId());
            BeanUtils.copyProperties(trueOrFalse, trueOrFalseTestDetailVO);
            trueOrFalseTestDetailVO.setScore(testQuestion.getScore());
            trueOrFalseTestDetailVO.setAnswer(0);
            trueOrFalseTestDetailVOList.add(trueOrFalseTestDetailVO);
        });

        return trueOrFalseTestDetailVOList;
    }

    @Override
    public List<TrueOrFalseTestDetailVO> getTrueOrFalseWrongTestDetailList(long testId, HttpServletRequest request) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", testId);
        queryWrapper.eq("type", QuestionTypeEnum.TRUE_OR_FALSE.getValue());
        List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOList = new ArrayList<>();
        List<TestQuestion> testQuestionList = testQuestionService.list(queryWrapper);
        if (CollectionUtils.isEmpty(testQuestionList)) {
            return trueOrFalseTestDetailVOList;
        }
        testQuestionList.forEach(testQuestion -> {
            TrueOrFalseTestDetailVO trueOrFalseTestDetailVO = new TrueOrFalseTestDetailVO();
            TrueOrFalse trueOrFalse = this.getById(testQuestion.getQuestionId());
            BeanUtils.copyProperties(trueOrFalse, trueOrFalseTestDetailVO);
            trueOrFalseTestDetailVO.setScore(testQuestion.getScore());
            //答案
            QueryWrapper<TrueOrFalseSubmit> trueOrFalseSubmitQueryWrapper = new QueryWrapper<>();
            trueOrFalseSubmitQueryWrapper.eq("questionId", testQuestion.getQuestionId());
            trueOrFalseSubmitQueryWrapper.eq("userId", userFeignClient.getLoginUser(request).getId());
            trueOrFalseSubmitQueryWrapper.eq("testId", testId);
            TrueOrFalseSubmit one = trueOrFalseSubmitService.getOne(trueOrFalseSubmitQueryWrapper);
            if (one == null) {
                trueOrFalseTestDetailVO.setAnswer(0);
            }else {
                trueOrFalseTestDetailVO.setAnswer(one.getAnswer());
            }
            trueOrFalseTestDetailVOList.add(trueOrFalseTestDetailVO);
        });

        return trueOrFalseTestDetailVOList;
    }

    @Override
    public List<TrueOrFalseTitleVO> getTrueOrFalseTitleList(long testId) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", testId);
        queryWrapper.eq("type", QuestionTypeEnum.CHOICE_QUESTION.getValue());
        List<TrueOrFalseTitleVO> trueOrFalseTitleVOList = new ArrayList<>();
        trueOrFalseTitleVOList = testQuestionService.list(queryWrapper).stream().map(testQuestion -> {
            Long questionId = testQuestion.getQuestionId();
            TrueOrFalse trueOrFalse = this.getById(questionId);
            if (trueOrFalse == null) {
                return null;
            }
            TrueOrFalseTitleVO trueOrFalseTitleVO = new TrueOrFalseTitleVO();
            BeanUtils.copyProperties(trueOrFalse, trueOrFalseTitleVO);
            trueOrFalseTitleVO.setType(QuestionTypeEnum.TRUE_OR_FALSE.getValue());
            return trueOrFalseTitleVO;
        }).collect(Collectors.toList());
        return trueOrFalseTitleVOList;
    }


}




