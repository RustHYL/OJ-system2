package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.constant.CommonConstant;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.exception.ThrowUtils;
import com.hyl.zhanmaoj.model.dto.trueorfalse.TrueOrFalseQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalse.TrueOrFalseQueryRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import com.hyl.zhanmaoj.model.entity.TrueOrFalse;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.enums.TrueOrFalseAnswerEnum;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseVO;
import com.hyl.zhanmaoj.model.vo.UserVO;
import com.hyl.zhanmaoj.service.TrueOrFalseService;
import com.hyl.zhanmaoj.mapper.TrueOrFalseMapper;
import com.hyl.zhanmaoj.service.UserService;
import com.hyl.zhanmaoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hyl.zhanmaoj.common.DateUtils.convertStringToDate;

/**
* @author Alan
* @description 针对表【true_or_false(判断题目)】的数据库操作Service实现
* @createDate 2024-03-14 22:43:01
*/
@Service
public class TrueOrFalseServiceImpl extends ServiceImpl<TrueOrFalseMapper, TrueOrFalse>
    implements TrueOrFalseService{

    @Resource
    private UserService userService;


    @Resource
    private TrueOrFalseMapper trueOrFalseMapper;
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
        String content = trueOrFalse.getContent();
        String tags = trueOrFalse.getTags();
        Integer answer = trueOrFalse.getAnswer();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank( content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
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
        String content = trueOrFalseQueryRequest.getContent();
        List<String> tags = trueOrFalseQueryRequest.getTags();
        Long userId = trueOrFalseQueryRequest.getUserId();
        String sortField = trueOrFalseQueryRequest.getSortField();
        String sortOrder = trueOrFalseQueryRequest.getSortOrder();
        Date createTime = trueOrFalseQueryRequest.getCreateTime();
        Date updateTime = trueOrFalseQueryRequest.getUpdateTime();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
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
        String content = trueOrFalseQueryRequest.getContent();
        String tags = trueOrFalseQueryRequest.getTags();
        Long userId = trueOrFalseQueryRequest.getUserId();
        String sortField = trueOrFalseQueryRequest.getSortField();
        String sortOrder = trueOrFalseQueryRequest.getSortOrder();
        Date createTime = trueOrFalseQueryRequest.getCreateTime();
        Date updateTime = trueOrFalseQueryRequest.getUpdateTime();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
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
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
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
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<TrueOrFalseVO> trueOrFalseVOList = trueOrFalseList.stream().map(trueOrFalse -> {
            TrueOrFalseVO trueOrFalseVO = TrueOrFalseVO.objToVo(trueOrFalse);
            Long userId = trueOrFalse.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            trueOrFalseVO.setUserVO(userService.getUserVO(user));
            return trueOrFalseVO;
        }).collect(Collectors.toList());
        trueOrFalseVOPage.setRecords(trueOrFalseVOList);
        return trueOrFalseVOPage;
    }


}




