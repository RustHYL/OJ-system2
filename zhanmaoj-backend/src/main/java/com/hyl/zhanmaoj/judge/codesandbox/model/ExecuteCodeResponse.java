package com.hyl.zhanmaoj.judge.codesandbox.model;

import com.hyl.zhanmaoj.model.dto.questionsbumit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    private Integer status;

    private JudgeInfo judgeInfo;

}
