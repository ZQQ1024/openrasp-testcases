package com.example.testcases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/vulns/cmd")
public class CommandController {
    @GetMapping("/ping")
    public String ping(@RequestParam("address") String address) {
        StringBuilder result = new StringBuilder();
        try {
            // 构建 ping 命令
            String os = System.getProperty("os.name").toLowerCase();
            String[] pingCmdList = os.contains("win") ? new String[]{"sh", "-c", "ping -n 1 " + address} : new String[]{"sh", "-c", "ping -c 1 " + address};
            ProcessBuilder builder = new ProcessBuilder(pingCmdList);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // 执行 ping 命令
            // Runtime.getRuntime().exec 等Java 来执行系统命令时，并不是通过 shell 来执行 (Linux下)，因此如果需要用到如 pipeline
            // (|)、;、&&、|| 等 shell 特性时，需要创建 shell 来执行。这是为了防止执行危险的命令注入攻击
            // Process process = Runtime.getRuntime().exec(pingCommand);

            // 读取命令执行结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return result.toString();
            } else {
                return "Ping 命令执行失败，返回码: " + exitCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Ping 命令执行失败: " + e.getMessage();
        }
    }
}
