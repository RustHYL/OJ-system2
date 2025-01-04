package com.hyl.zhanmaojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.testSubmit.TestSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TestSubmit;

/**
* @author Alan
* @description 针对表【test_submit(试卷提交)】的数据库操作Service
* @createDate 2024-03-17 16:50:59
*/
public interface TestSubmitService extends IService<TestSubmit> {


    Wrapper<TestSubmit> getQueryWrapper(TestSubmitQueryRequest testSubmitQueryRequest);
}