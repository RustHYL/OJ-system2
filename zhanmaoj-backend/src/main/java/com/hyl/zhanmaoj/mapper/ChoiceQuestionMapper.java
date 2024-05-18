package com.hyl.zhanmaoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Alan
* @description 针对表【choice_question(选择题目)】的数据库操作Mapper
* @createDate 2024-03-15 06:12:21
* @Entity com.hyl.zhanmaoj.model.entity.ChoiceQuestion
*/
public interface ChoiceQuestionMapper extends BaseMapper<ChoiceQuestion> {

    @Select("SELECT * FROM choice_question ORDER BY RAND() LIMIT #{num}")
    List<ChoiceQuestion> getRandomChoiceQuestionList(@Param("num") Integer num);

}




