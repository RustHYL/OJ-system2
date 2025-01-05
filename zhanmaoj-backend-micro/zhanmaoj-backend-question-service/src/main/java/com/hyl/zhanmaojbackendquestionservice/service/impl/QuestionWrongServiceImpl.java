package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.questionWrong.QuestionWrongQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionWrongVO;
import com.hyl.zhanmaojbackendquestionservice.mapper.QuestionWrongMapper;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionWrongService;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hyl.zhanmaojbackendcommon.common.DateUtils.convertStringToDate;


/**
* @author Alan
* @description 针对表【question_wrong(错题表)】的数据库操作Service实现
* @createDate 2024-03-26 13:25:34
*/
@Service
public class QuestionWrongServiceImpl extends ServiceImpl<QuestionWrongMapper, QuestionWrong>
    implements QuestionWrongService {

    @Resource
    private UserService userService;

    @Override
    public QueryWrapper<QuestionWrong> getQueryWrapper(QuestionWrongQueryRequest questionWrongQueryRequest) {
        QueryWrapper<QuestionWrong> queryWrapper = new QueryWrapper<>();
        if (questionWrongQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionWrongQueryRequest.getId();
        String language = questionWrongQueryRequest.getLanguage();
        Long questionId = questionWrongQueryRequest.getQuestionId();
        Long userId = questionWrongQueryRequest.getUserId();
        Long submitId = questionWrongQueryRequest.getSubmitId();
        String title = questionWrongQueryRequest.getTitle();
        List<String> tags = questionWrongQueryRequest.getTags();
        Date createTime = questionWrongQueryRequest.getCreateTime();
        Date updateTime = questionWrongQueryRequest.getUpdateTime();
        String sortField = questionWrongQueryRequest.getSortField();
        String sortOrder = questionWrongQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(submitId), "submitId", submitId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<QuestionWrongVO> getQuestionWrongVOPage(Page<QuestionWrong> questionWrongPage, HttpServletRequest request) {
        List<QuestionWrong> questionWrongList = questionWrongPage.getRecords();
        Page<QuestionWrongVO> questionWrongVOPage = new Page<>(questionWrongPage.getCurrent(), questionWrongPage.getSize(), questionWrongPage.getTotal());
        if (CollectionUtils.isEmpty(questionWrongList)) {
            return questionWrongVOPage;
        }
        // 填充信息
        List<QuestionWrongVO> questionWrongVOList = questionWrongList.stream().map(questionWrong -> {
            QuestionWrongVO questionWrongVO = QuestionWrongVO.objToVo(questionWrong);
            return questionWrongVO;
        }).collect(Collectors.toList());
        questionWrongVOPage.setRecords(questionWrongVOList);
        return questionWrongVOPage;
    }
}




