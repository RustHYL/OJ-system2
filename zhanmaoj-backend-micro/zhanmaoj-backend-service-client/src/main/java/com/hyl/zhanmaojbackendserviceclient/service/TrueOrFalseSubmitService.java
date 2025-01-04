package com.hyl.zhanmaojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalseSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseSubmitVO;

/**
* @author Alan
* @description 针对表【true_or_false_submit(判断题提交信息)】的数据库操作Service
* @createDate 2024-03-15 05:44:58
*/
public interface TrueOrFalseSubmitService extends IService<TrueOrFalseSubmit> {

    /**
     * 题目提交信息
     *
     * @param trueOrFalseSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doTrueOrFalseSubmit(TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param trueOrFalseSubmitQueryRequest
     * @return
     */
    QueryWrapper<TrueOrFalseSubmit> getQueryWrapper(TrueOrFalseSubmitQueryRequest trueOrFalseSubmitQueryRequest);

    /**
     * 获取查询条件
     *
     * @param trueOrFalseSubmitQueryAdminRequest
     * @return
     */
    QueryWrapper<TrueOrFalseSubmit> getQueryWrapper(TrueOrFalseSubmitQueryAdminRequest trueOrFalseSubmitQueryAdminRequest);

    /**
     * 获取题目封装
     *
     * @param trueOrFalseSubmit
     * @param loginUser
     * @return
     */
    TrueOrFalseSubmitVO getTrueOrFalseSubmitVO(TrueOrFalseSubmit trueOrFalseSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param trueOrFalseSubmitPage
     * @param loginUser
     * @return
     */
    Page<TrueOrFalseSubmitVO> getTrueOrFalseSubmitVOPage(Page<TrueOrFalseSubmit> trueOrFalseSubmitPage, User loginUser);

}
