package com.hyl.zhanmaoj.utils;

import cn.hutool.captcha.generator.RandomGenerator;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.entity.SMSModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
public class SMSUtil {

    @Autowired
    private SMSModel smsModel;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 生成短信验证码
     */
    public String generateSMS(String phoneNumber) {
        //生成环境请求地址
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号，登录网站后再控制台首页看到开发者账号ACCOUNT_SID
        String accountSid = smsModel.getAccountId();
        //token
        String authToken = smsModel.getAuthToken();
        //appId
        //使用管理控制台中一创建应用的APPID
        String appId = smsModel.getAppId();
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSid, authToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = phoneNumber;
        String templateId = "1";
        //四位随机数
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        String code = randomGenerator.generate();
        Long expireTime = 3l;
        String[] dates = {code, expireTime.toString()};
        // 发送
        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, dates);
        // 结果
        if ("000000".equals(result.get("statusCode"))) {
            return code;
        } else {
            return null;
        }
    }

    /**
     * 校验短信验证码
     */
    public boolean validateSMS(String redisStr, String smsCode) {
        Object value;
        System.out.println("redisStr=" + redisStr);
        try {
            value = redisTemplate.opsForValue().get(redisStr);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "redis操作失败");
        }
        if (ObjectUtils.isEmpty(value)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期");
        }
        String code = (String) value;
        if (StringUtils.equals(code, smsCode)) {
            return true;
        }
        return false;
    }
}
