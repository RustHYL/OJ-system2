package com.hyl.zhanmaoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hyl.zhanmaoj.model.dto.test.TestQueryRequest;
import com.hyl.zhanmaoj.model.entity.Test;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
* @author Alan
* @description 针对表【test(试卷)】的数据库操作Service
* @createDate 2024-03-14 22:42:51
*/
public interface TestService extends IService<Test> {


    QueryWrapper<Test> getQueryWrapper(TestQueryRequest testQueryRequest);

    boolean joinTest(Long testId, String password);
}
