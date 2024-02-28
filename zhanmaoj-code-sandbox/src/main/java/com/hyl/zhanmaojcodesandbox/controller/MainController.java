package com.hyl.zhanmaojcodesandbox.controller;

import com.hyl.zhanmaojcodesandbox.JavaNativeCodeSandbox;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class MainController {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "maowulang";

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    //todo 可以做code message等的封装
    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String authHeader = httpServletRequest.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            httpServletResponse.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null){
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }
}

