package com.example.testcases.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vulns/leak")
public class DataLeakController {
    @GetMapping("/data")
    public Map<String, String> generateData() {
        Map<String, String> data = new HashMap<>();
        data.put("idCard", generateIdCard());
        data.put("phoneNumber", generatePhoneNumber());
        data.put("bankCard", generateBankCard());
        return data;
    }

    // 参看 https://id.dcode.top/ 生成
    private String generateIdCard() {
        // 生成假的18位身份证号码，不保证符合校验规则
//        return RandomStringUtils.randomNumeric(18);
        return "210102196906282783";
    }

    private String generatePhoneNumber() {
        // 生成假的11位手机号码，以1开头
//        return "1" + RandomStringUtils.randomNumeric(10);
        return "13846638181";
    }

    private String generateBankCard() {
        // 生成假的16位银行卡号，不保证符合校验规则
//        return RandomStringUtils.randomNumeric(16);
        return "6222028762454542";
    }
}
