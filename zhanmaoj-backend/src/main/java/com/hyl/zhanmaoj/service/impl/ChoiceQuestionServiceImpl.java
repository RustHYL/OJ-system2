package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.constant.CommonConstant;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.exception.ThrowUtils;
import com.hyl.zhanmaoj.model.dto.choicequestion.ChoiceQuestionQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.choicequestion.ChoiceQuestionQueryRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import com.hyl.zhanmaoj.model.entity.TestQuestion;
import com.hyl.zhanmaoj.model.entity.TrueOrFalse;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.enums.QuestionTypeEnum;
import com.hyl.zhanmaoj.model.enums.TrueOrFalseAnswerEnum;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionTestDetailVO;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionTitleVO;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionVO;
import com.hyl.zhanmaoj.model.vo.UserVO;
import com.hyl.zhanmaoj.service.ChoiceQuestionService;
import com.hyl.zhanmaoj.mapper.ChoiceQuestionMapper;
import com.hyl.zhanmaoj.service.TestQuestionService;
import com.hyl.zhanmaoj.service.UserService;
import com.hyl.zhanmaoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.hyl.zhanmaoj.common.DateUtils.convertStringToDate;

/**
* @author Alan
* @description 针对表【choice_question(选择题目)】的数据库操作Service实现
* @createDate 2024-03-15 06:12:21
*/
@Service
public class ChoiceQuestionServiceImpl extends ServiceImpl<ChoiceQuestionMapper, ChoiceQuestion>
    implements ChoiceQuestionService{

    @Resource
    private UserService userService;

    @Resource
    private ChoiceQuestionMapper choiceQuestionMapper;

    @Resource
    private TestQuestionService testQuestionService;

    /**
     * 校验题目是否合法
     * @param choiceQuestion
     * @param add
     */
    @Override
    public void validChoiceQuestion(ChoiceQuestion choiceQuestion, boolean add) {
        if (choiceQuestion == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = choiceQuestion.getTitle();
        String tags = choiceQuestion.getTags();
        Integer answer = choiceQuestion.getAnswer();
        String optionA = choiceQuestion.getOptionA();
        String optionB = choiceQuestion.getOptionB();
        String optionC = choiceQuestion.getOptionC();
        String optionD = choiceQuestion.getOptionD();
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
        if (StringUtils.isNotBlank(optionA) && optionA.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选项A过长");
        }
        if (StringUtils.isNotBlank(optionB) && optionB.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选项B过长");
        }
        if (StringUtils.isNotBlank(optionC) && optionC.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选项C过长");
        }
        if (StringUtils.isNotBlank(optionD) && optionD.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选项D过长");
        }

    }

    @Override
    public List<ChoiceQuestion> getRandomChoiceQuestionList(Integer num) {
        return choiceQuestionMapper.getRandomChoiceQuestionList(num);
    }

    @Override
    public QueryWrapper<ChoiceQuestion> getQueryWrapper(ChoiceQuestionQueryRequest choiceQuestionQueryRequest) {
        QueryWrapper<ChoiceQuestion> queryWrapper = new QueryWrapper<>();
        if (choiceQuestionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = choiceQuestionQueryRequest.getId();
        String title = choiceQuestionQueryRequest.getTitle();
        List<String> tags = choiceQuestionQueryRequest.getTags();
        Long userId = choiceQuestionQueryRequest.getUserId();
        String sortField = choiceQuestionQueryRequest.getSortField();
        String sortOrder = choiceQuestionQueryRequest.getSortOrder();
        Date createTime = choiceQuestionQueryRequest.getCreateTime();
        Date updateTime = choiceQuestionQueryRequest.getUpdateTime();
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
    public QueryWrapper<ChoiceQuestion> getQueryWrapper(ChoiceQuestionQueryAdminRequest choiceQuestionQueryRequest) {
        QueryWrapper<ChoiceQuestion> queryWrapper = new QueryWrapper<>();
        if (choiceQuestionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = choiceQuestionQueryRequest.getId();
        String title = choiceQuestionQueryRequest.getTitle();
        String tags = choiceQuestionQueryRequest.getTags();
        Long userId = choiceQuestionQueryRequest.getUserId();
        String sortField = choiceQuestionQueryRequest.getSortField();
        String sortOrder = choiceQuestionQueryRequest.getSortOrder();
        Date createTime = choiceQuestionQueryRequest.getCreateTime();
        Date updateTime = choiceQuestionQueryRequest.getUpdateTime();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(tags), "tags", tags);
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ChoiceQuestionVO getChoiceQuestionVO(ChoiceQuestion choiceQuestion, HttpServletRequest request) {
        ChoiceQuestionVO choiceQuestionVO = ChoiceQuestionVO.objToVo(choiceQuestion);
        // 1. 关联查询用户信息
        Long userId = choiceQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        choiceQuestionVO.setUserVO(userVO);
        return choiceQuestionVO;
    }

    @Override
    public Page<ChoiceQuestionVO> getChoiceQuestionVOPage(Page<ChoiceQuestion> choiceQuestionPage, HttpServletRequest request) {
        List<ChoiceQuestion> choiceQuestionList = choiceQuestionPage.getRecords();
        Page<ChoiceQuestionVO> choiceQuestionVOPage = new Page<>(choiceQuestionPage.getCurrent(), choiceQuestionPage.getSize(), choiceQuestionPage.getTotal());
        if (CollectionUtils.isEmpty(choiceQuestionList)) {
            return choiceQuestionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = choiceQuestionList.stream().map(ChoiceQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<ChoiceQuestionVO> choiceQuestionVOList = choiceQuestionList.stream().map(choiceQuestion -> {
            ChoiceQuestionVO choiceQuestionVO = ChoiceQuestionVO.objToVo(choiceQuestion);
            Long userId = choiceQuestion.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            choiceQuestionVO.setUserVO(userService.getUserVO(user));
            return choiceQuestionVO;
        }).collect(Collectors.toList());
        choiceQuestionVOPage.setRecords(choiceQuestionVOList);
        return choiceQuestionVOPage;
    }

    @Override
    public List<ChoiceQuestionTestDetailVO> getChoiceQuestonTestDetailList(long testId) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", testId);
        queryWrapper.eq("type", QuestionTypeEnum.CHOICE_QUESTION.getValue());
        List<ChoiceQuestionTestDetailVO> choiceQuestionTestDetailVOList = new ArrayList<>();
        List<TestQuestion> testQuestionList = testQuestionService.list(queryWrapper);
        if (CollectionUtils.isEmpty(testQuestionList)) {
            return choiceQuestionTestDetailVOList;
        }
        testQuestionList.forEach(testQuestion -> {
            ChoiceQuestionTestDetailVO choiceQuestionTestDetailVO = new ChoiceQuestionTestDetailVO();
            ChoiceQuestion choiceQuestion = this.getById(testQuestion.getQuestionId());
            BeanUtils.copyProperties(choiceQuestion, choiceQuestionTestDetailVO);
            choiceQuestionTestDetailVO.setScore(testQuestion.getScore());
            choiceQuestionTestDetailVO.setAnswer(0);
            choiceQuestionTestDetailVOList.add(choiceQuestionTestDetailVO);
        });

        return choiceQuestionTestDetailVOList;
    }

    @Override
    public List<ChoiceQuestionTitleVO> getChoiceQuestonTitleList(long testId) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", testId);
        queryWrapper.eq("type", QuestionTypeEnum.CHOICE_QUESTION.getValue());
        List<ChoiceQuestionTitleVO> choiceQuestionTitleVOList = new ArrayList<>();
        choiceQuestionTitleVOList = testQuestionService.list(queryWrapper).stream().map(testQuestion -> {
            Long questionId = testQuestion.getQuestionId();
            ChoiceQuestion choiceQuestion = this.getById(questionId);
            if (choiceQuestion == null) {
                return null;
            }
            ChoiceQuestionTitleVO choiceQuestionTitleVO = new ChoiceQuestionTitleVO();
            BeanUtils.copyProperties(choiceQuestion, choiceQuestionTitleVO);
            choiceQuestionTitleVO.setType(QuestionTypeEnum.CHOICE_QUESTION.getValue());
            return choiceQuestionTitleVO;
        }).collect(Collectors.toList());
        return choiceQuestionTitleVOList;
    }

}




