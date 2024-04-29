package com.hyl.zhanmaoj.controller;


import com.hyl.zhanmaoj.common.BaseResponse;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.common.ResultUtils;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.entity.SMS;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.service.UserService;
import com.hyl.zhanmaoj.utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证
 */
@RestController
@RequestMapping("/sms")
@Slf4j
@CrossOrigin
public class SMSController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private SMSUtil smsUtil;

    @PostMapping("/sendSMS")
    public BaseResponse<String> sendSMS(@RequestBody SMS sms, HttpServletRequest request) {
        //校验是否登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //生成发送验证码
        String code = smsUtil.generateSMS(sms.getPhoneNumber());
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "短信验证码生成失败");
        }
        String redisStr = loginUser.getId() + ":" + sms.getStatus() + ":" + sms.getPhoneNumber();
        try {
            redisTemplate.opsForValue().set(redisStr, code, 180, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "redis操作失败");
        }
        return ResultUtils.success(code);
    }

    @PostMapping("/validateSMS")
    public BaseResponse<Boolean> validateSMS(@RequestBody SMS sms, HttpServletRequest request) {
        //校验是否登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        //获取验证码
        String redisStr = userId + ":" + sms.getStatus() + ":" + sms.getPhoneNumber();
        boolean b = smsUtil.validateSMS(redisStr, sms.getCode());
        if (b){
            redisTemplate.delete(redisStr);
        }
        return ResultUtils.success(b);
    }


}
