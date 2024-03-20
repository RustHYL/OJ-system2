package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.model.entity.TestQuestion;
import com.hyl.zhanmaoj.service.TestQuestionService;
import com.hyl.zhanmaoj.mapper.TestQuestionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Alan
* @description 针对表【test_question(题目题目关联)】的数据库操作Service实现
* @createDate 2024-03-16 16:58:22
*/
@Service
public class TestQuestionServiceImpl extends ServiceImpl<TestQuestionMapper, TestQuestion>
    implements TestQuestionService{

    @Override
    public boolean removeByTestId(long testId) {
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("test_id",testId);
        List<TestQuestion> list = this.list(queryWrapper);
        for (TestQuestion testQuestion : list) {
            boolean removeById = this.removeById(testQuestion.getId());
            if (!removeById){
                return false;
            }
        }
        return true;
    }
}




