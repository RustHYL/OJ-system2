package com.hyl.zhanmaoj.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms")
public class SMSModel {
    //账户id
    private String accountId;
    //账户token
    private String authToken;
    //app Id
    private String appId;
}
