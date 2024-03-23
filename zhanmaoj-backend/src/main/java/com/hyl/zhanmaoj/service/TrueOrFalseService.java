package com.hyl.zhanmaoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaoj.model.dto.question.QuestionQueryRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalse.TrueOrFalseQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalse.TrueOrFalseQueryRequest;
import com.hyl.zhanmaoj.model.entity.Question;
import com.hyl.zhanmaoj.model.entity.TrueOrFalse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.vo.QuestionVO;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseTestDetailVO;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseTitleVO;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Alan
* @description 针对表【true_or_false(判断题目)】的数据库操作Service
* @createDate 2024-03-14 22:43:01
*/
public interface TrueOrFalseService extends IService<TrueOrFalse> {

    void validTrueOrFalse(TrueOrFalse trueOrFalse, boolean add);

    /**
     * 获取查询条件
     *
     * @param trueOrFalseQueryRequest
     * @return
     */
    QueryWrapper<TrueOrFalse> getQueryWrapper(TrueOrFalseQueryRequest trueOrFalseQueryRequest);

    /**
     * 获取查询条件
     *
     * @param trueOrFalseQueryAdminRequest
     * @return
     */
    QueryWrapper<TrueOrFalse> getQueryWrapper(TrueOrFalseQueryAdminRequest trueOrFalseQueryAdminRequest);

    /**
     * 随机获取n条数据
     * @param num
     * @return
     */
    List<TrueOrFalse> getRandomTrueOrFalseList(Integer num);

    /**
     * 获取题目封装
     *
     * @param trueOrFalse
     * @param request
     * @return
     */
    TrueOrFalseVO getTrueOrFalseVO(TrueOrFalse trueOrFalse, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param trueOrFalsePage
     * @param request
     * @return
     */
    Page<TrueOrFalseVO> getTrueOrFalseVOPage(Page<TrueOrFalse> trueOrFalsePage, HttpServletRequest request);

    /**
     * 获取试卷判断题题目信息列表
     * @param testId
     * @return
     */
    List<TrueOrFalseTestDetailVO> getTrueOrFalseTestDetailList(long testId);

    /**
     * 获取题目id type title列表
     * @param testId
     * @return
     */
    List<TrueOrFalseTitleVO> getTrueOrFalseTitleList(long testId);
}
