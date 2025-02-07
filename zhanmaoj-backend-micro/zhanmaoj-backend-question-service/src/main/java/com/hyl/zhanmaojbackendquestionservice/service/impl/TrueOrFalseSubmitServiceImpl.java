package com.hyl.zhanmaojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.constant.CommonConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.utils.SqlUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalse;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalseSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.enums.StatusEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.TrueOrFalseAnswerEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseSubmitVO;
import com.hyl.zhanmaojbackendquestionservice.mapper.TrueOrFalseSubmitMapper;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseService;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseSubmitService;
import com.hyl.zhanmaojbackendserviceclient.service.UserFeignClient;
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
* @description 针对表【true_or_false_submit(判断题提交信息)】的数据库操作Service实现
* @createDate 2024-03-15 05:44:58
*/
@Service
public class TrueOrFalseSubmitServiceImpl extends ServiceImpl<TrueOrFalseSubmitMapper, TrueOrFalseSubmit>
    implements TrueOrFalseSubmitService {

    @Resource
    private TrueOrFalseService trueOrFalseService;

    @Resource
    private UserFeignClient userFeignClient;




    /**
     * 提交问题
     *
     * @param trueOrFalseSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doTrueOrFalseSubmit(TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest, User loginUser) {
        //校验答案是否合法
        Integer answer = trueOrFalseSubmitAddRequest.getAnswer();
//        TrueOrFalseAnswerEnum answerEnum = TrueOrFalseAnswerEnum.getEnumByValue(answer);
//        if (answerEnum == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案不合法");
//        }
        // 判断实体是否存在，根据类别获取实体
        long trueOrFalseId = trueOrFalseSubmitAddRequest.getQuestionId();
        TrueOrFalse trueOrFalse = trueOrFalseService.getById(trueOrFalseId);
        if (trueOrFalse == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交问题
        long userId = loginUser.getId();
        // 每个用户串行提交问题
        TrueOrFalseSubmit trueOrFalseSubmit = new TrueOrFalseSubmit();
        trueOrFalseSubmit.setUserId(userId);
        trueOrFalseSubmit.setQuestionId(trueOrFalseId);
        Long testId = trueOrFalseSubmitAddRequest.getTestId();
        if (testId != null){
            trueOrFalseSubmit.setTestId(testId);
        }
        trueOrFalseSubmit.setAnswer(answer);
        //判题
        trueOrFalseSubmit.setStatus(StatusEnum.WAITING.getValue());
        boolean save = this.save(trueOrFalseSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long id = trueOrFalseSubmit.getId();
        //执行判题服务
        Integer questionAnswer = trueOrFalseService.getById(trueOrFalseId).getAnswer();
        if (TrueOrFalseAnswerEnum.getEnumByValue(answer) != null && questionAnswer.equals(answer) && !Objects.equals(TrueOrFalseAnswerEnum.getEnumByValue(answer), TrueOrFalseAnswerEnum.NULL)) {
            trueOrFalseSubmit.setStatus(StatusEnum.TRUE.getValue());
        } else {
            trueOrFalseSubmit.setStatus(StatusEnum.FALSE.getValue());
        }
        boolean judge = this.updateById(trueOrFalseSubmit);
        if (!judge) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据更新失败");
        }
        return id;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param trueOrFalseSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<TrueOrFalseSubmit> getQueryWrapper(TrueOrFalseSubmitQueryRequest trueOrFalseSubmitQueryRequest) {
        QueryWrapper<TrueOrFalseSubmit> queryWrapper = new QueryWrapper<>();
        if (trueOrFalseSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Integer status = trueOrFalseSubmitQueryRequest.getStatus();;
        Long trueOrFalseId = trueOrFalseSubmitQueryRequest.getQuestionId();
        Long userId = trueOrFalseSubmitQueryRequest.getUserId();
        Long testId = trueOrFalseSubmitQueryRequest.getTestId();
        String sortField = trueOrFalseSubmitQueryRequest.getSortField();
        String sortOrder = trueOrFalseSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(trueOrFalseId), "questionId", trueOrFalseId);
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
     * @param trueOrFalseSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<TrueOrFalseSubmit> getQueryWrapper(TrueOrFalseSubmitQueryAdminRequest trueOrFalseSubmitQueryRequest) {
        QueryWrapper<TrueOrFalseSubmit> queryWrapper = new QueryWrapper<>();
        if (trueOrFalseSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long id = trueOrFalseSubmitQueryRequest.getId();
        Integer status = trueOrFalseSubmitQueryRequest.getStatus();;
        Long trueOrFalseId = trueOrFalseSubmitQueryRequest.getQuestionId();
        Long userId = trueOrFalseSubmitQueryRequest.getUserId();
        Long testId = trueOrFalseSubmitQueryRequest.getTestId();
        Date createTime = trueOrFalseSubmitQueryRequest.getCreateTime();
        Date updateTime = trueOrFalseSubmitQueryRequest.getUpdateTime();
        String sortField = trueOrFalseSubmitQueryRequest.getSortField();
        String sortOrder = trueOrFalseSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(id != null, "id", id);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(trueOrFalseId), "questionId", trueOrFalseId);
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
    public TrueOrFalseSubmitVO getTrueOrFalseSubmitVO(TrueOrFalseSubmit trueOrFalseSubmit, User loginUser) {
        TrueOrFalseSubmitVO trueOrFalseSubmitVO = TrueOrFalseSubmitVO.objToVo(trueOrFalseSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != trueOrFalseSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
            trueOrFalseSubmitVO.setAnswer(null);
        }
        return trueOrFalseSubmitVO;
    }

    @Override
    public Page<TrueOrFalseSubmitVO> getTrueOrFalseSubmitVOPage(Page<TrueOrFalseSubmit> trueOrFalseSubmitPage, User loginUser) {
        List<TrueOrFalseSubmit> trueOrFalseSubmitList = trueOrFalseSubmitPage.getRecords();
        Page<TrueOrFalseSubmitVO> trueOrFalseSubmitVOPage = new Page<>(trueOrFalseSubmitPage.getCurrent(), trueOrFalseSubmitPage.getSize(), trueOrFalseSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(trueOrFalseSubmitList)) {
            return trueOrFalseSubmitVOPage;
        }
        List<TrueOrFalseSubmitVO> trueOrFalseSubmitVOList = trueOrFalseSubmitList.stream()
                .map(trueOrFalseSubmit -> getTrueOrFalseSubmitVO(trueOrFalseSubmit, loginUser))
                .collect(Collectors.toList());
        trueOrFalseSubmitVOPage.setRecords(trueOrFalseSubmitVOList);
        return trueOrFalseSubmitVOPage;
    }



}




