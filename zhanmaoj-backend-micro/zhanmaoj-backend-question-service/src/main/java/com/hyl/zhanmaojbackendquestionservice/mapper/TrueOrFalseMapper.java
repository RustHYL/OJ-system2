package com.hyl.zhanmaojbackendquestionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Alan
* @description 针对表【true_or_false(判断题目)】的数据库操作Mapper
* @createDate 2024-03-14 22:43:01
* @Entity com.hyl.zhanmaoj.model.entity.TrueOrFalse
*/
public interface TrueOrFalseMapper extends BaseMapper<TrueOrFalse> {

    @Select("SELECT * FROM true_or_false ORDER BY RAND() LIMIT #{num}")
    List<TrueOrFalse> getRandomTrueOrFalseList(@Param("num") Integer num);

}




