package com.hyl.zhanmaoj.judge.codesandbox;


import com.hyl.zhanmaoj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.hyl.zhanmaoj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.hyl.zhanmaoj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

import java.util.Scanner;

/**
 * 代码沙箱工厂 根据字符串参数指定创建代码沙箱实例
 */
public class CodeSandBoxFactory {

    /**
     * 创建代码沙箱实例
     * @param type
     * @return
     */
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
