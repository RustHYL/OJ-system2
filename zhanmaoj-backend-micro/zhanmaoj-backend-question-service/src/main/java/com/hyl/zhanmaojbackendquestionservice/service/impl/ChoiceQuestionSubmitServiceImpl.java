package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.ChoiceQuestion;
import com.hyl.zhanmaojbackendmodel.model.entity.ChoiceQuestionSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.enums.ChoiceQuestionAnswerEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.StatusEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.ChoiceQuestionSubmitVO;
import com.hyl.zhanmaojbackendquestionservice.mapper.ChoiceQuestionSubmitMapper;
import com.hyl.zhanmaojbackendquestionservice.service.ChoiceQuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.ChoiceQuestionSubmitService;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hyl.zhanmaojbackendcommon.common.DateUtils.convertStringToDate;


/**
* @author Alan
* @description 针对表【choice_question_submit(选择题提交信息)】的数据库操作Service实现
* @createDate 2024-03-15 05:45:06
*/
@Service
public class ChoiceQuestionSubmitServiceImpl extends ServiceImpl<ChoiceQuestionSubmitMapper, ChoiceQuestionSubmit>
    implements ChoiceQuestionSubmitService {


    @Resource
    private ChoiceQuestionService choiceQuestionService;

    @Resource
    private UserService userService;




    /**
     * 提交问题
     *
     * @param choiceQuestionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doChoiceQuestionSubmit(ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest, User loginUser) {
        //校验答案是否合法
        Integer answer = choiceQuestionSubmitAddRequest.getAnswer();
//        ChoiceQuestionAnswerEnum answerEnum = ChoiceQuestionAnswerEnum.getEnumByValue(answer);
//        if (answerEnum == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案不合法");
//        }
        // 判断实体是否存在，根据类别获取实体
        long choiceQuestionId = choiceQuestionSubmitAddRequest.getQuestionId();
        ChoiceQuestion choiceQuestion = choiceQuestionService.getById(choiceQuestionId);
        if (choiceQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交问题
        long userId = loginUser.getId();
        // 每个用户串行提交问题
        ChoiceQuestionSubmit choiceQuestionSubmit = new ChoiceQuestionSubmit();
        choiceQuestionSubmit.setUserId(userId);
        choiceQuestionSubmit.setQuestionId(choiceQuestionId);
        Long testId = choiceQuestionSubmitAddRequest.getTestId();
        if (testId != null) {
            choiceQuestionSubmit.setTestId(testId);
        }
        choiceQuestionSubmit.setAnswer(answer);
        //判题
        choiceQuestionSubmit.setStatus(StatusEnum.WAITING.getValue());
        boolean save = this.save(choiceQuestionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long id = choiceQuestionSubmit.getId();
        //执行判题服务
        Integer questionAnswer = choiceQuestionService.getById(choiceQuestionId).getAnswer();
        if (ChoiceQuestionAnswerEnum.getEnumByValue(answer) != null &&questionAnswer.equals(answer) && !Objects.equals(ChoiceQuestionAnswerEnum.getEnumByValue(answer), ChoiceQuestionAnswerEnum.OPTION_NULL)) {
            choiceQuestionSubmit.setStatus(StatusEnum.TRUE.getValue());
        } else {
            choiceQuestionSubmit.setStatus(StatusEnum.FALSE.getValue());
        }
        boolean judge = this.updateById(choiceQuestionSubmit);
        if (!judge) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据更新失败");
        }
        return id;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param choiceQuestionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ChoiceQuestionSubmit> getQueryWrapper(ChoiceQuestionSubmitQueryRequest choiceQuestionSubmitQueryRequest) {
        QueryWrapper<ChoiceQuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (choiceQuestionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Integer status = choiceQuestionSubmitQueryRequest.getStatus();;
        Long choiceQuestionId = choiceQuestionSubmitQueryRequest.getQuestionId();
        Long userId = choiceQuestionSubmitQueryRequest.getUserId();
        Long testId = choiceQuestionSubmitQueryRequest.getTestId();
        String sortField = choiceQuestionSubmitQueryRequest.getSortField();
        String sortOrder = choiceQuestionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(choiceQuestionId), "questionId", choiceQuestionId);
        queryWrapper.like(ObjectUtils.isNotEmpty(testId), "testId", testId);
        queryWrapper.eq(StatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param choiceQuestionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ChoiceQuestionSubmit> getQueryWrapper(ChoiceQuestionSubmitQueryAdminRequest choiceQuestionSubmitQueryRequest) {
        QueryWrapper<ChoiceQuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (choiceQuestionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long id = choiceQuestionSubmitQueryRequest.getId();
        Integer status = choiceQuestionSubmitQueryRequest.getStatus();;
        Long choiceQuestionId = choiceQuestionSubmitQueryRequest.getQuestionId();
        Long userId = choiceQuestionSubmitQueryRequest.getUserId();
        Long testId = choiceQuestionSubmitQueryRequest.getTestId();
        Date createTime = choiceQuestionSubmitQueryRequest.getCreateTime();
        Date updateTime = choiceQuestionSubmitQueryRequest.getUpdateTime();
        String sortField = choiceQuestionSubmitQueryRequest.getSortField();
        String sortOrder = choiceQuestionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(id != null, "id", id);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(choiceQuestionId), "questionId", choiceQuestionId);
        queryWrapper.like(ObjectUtils.isNotEmpty(testId), "testId", userId);
        queryWrapper.eq(StatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.between(createTime != null, "createTime", convertStringToDate(createTime, "00:00:00"), convertStringToDate(createTime, "23:59:59"));
        queryWrapper.between(updateTime != null, "updateTime", convertStringToDate(updateTime, "00:00:00"), convertStringToDate(updateTime, "23:59:59"));
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ChoiceQuestionSubmitVO getChoiceQuestionSubmitVO(ChoiceQuestionSubmit choiceQuestionSubmit, User loginUser) {
        ChoiceQuestionSubmitVO choiceQuestionSubmitVO = ChoiceQuestionSubmitVO.objToVo(choiceQuestionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != choiceQuestionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            choiceQuestionSubmitVO.setAnswer(null);
        }
        return choiceQuestionSubmitVO;
    }

    @Override
    public Page<ChoiceQuestionSubmitVO> getChoiceQuestionSubmitVOPage(Page<ChoiceQuestionSubmit> choiceQuestionSubmitPage, User loginUser) {
        List<ChoiceQuestionSubmit> choiceQuestionSubmitList = choiceQuestionSubmitPage.getRecords();
        Page<ChoiceQuestionSubmitVO> choiceQuestionSubmitVOPage = new Page<>(choiceQuestionSubmitPage.getCurrent(), choiceQuestionSubmitPage.getSize(), choiceQuestionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(choiceQuestionSubmitList)) {
            return choiceQuestionSubmitVOPage;
        }
        List<ChoiceQuestionSubmitVO> choiceQuestionSubmitVOList = choiceQuestionSubmitList.stream()
                .map(choiceQuestionSubmit -> getChoiceQuestionSubmitVO(choiceQuestionSubmit, loginUser))
                .collect(Collectors.toList());
        choiceQuestionSubmitVOPage.setRecords(choiceQuestionSubmitVOList);
        return choiceQuestionSubmitVOPage;
    }

}




