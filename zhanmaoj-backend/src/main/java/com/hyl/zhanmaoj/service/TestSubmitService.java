package com.hyl.zhanmaoj.service;

import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.TestSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.entity.User;

/**
* @author Alan
* @description 针对表【test_submit(试卷提交)】的数据库操作Service
* @createDate 2024-03-17 16:50:59
*/
public interface TestSubmitService extends IService<TestSubmit> {

    long doTestSubmit(TestSubmitAddRequest testSubmitAddRequest, User loginUser);
}