package com.hyl.zhanmaoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyl.zhanmaoj.model.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Alan
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2023-11-24 02:03:05
* @Entity com.hyl.zhanmaoj.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("SELECT * FROM question ORDER BY RAND() LIMIT #{num}")
    List<Question> getRandomQuestionList(@Param("num") Integer num);

}




