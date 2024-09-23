package com.example.testcases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ognl.Ognl;
import ognl.OgnlContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping(("/api/vulns/ognl"))
public class OGNLController {
    @GetMapping("/eval")
    public String evaluateOgnlExpression(@RequestParam("exp") String expression) {
        try {
            // 创建 OGNL 上下文
            OgnlContext context = new OgnlContext();

            // 解析传入的表达式
            Object expr = Ognl.parseExpression(expression);

            // 在安全的上下文中执行表达式
            Object result = Ognl.getValue(expr, context, context.getRoot());

            if (result instanceof Process) {
                Process process = (Process) result;
                // 读取进程的输出
                return readProcessOutput(process);
            }

            // 返回执行结果
            return "Expression result: " + result.toString();
        } catch (Exception e) {
            return "Error executing expression: " + e.getMessage();
        }
    }

    private String readProcessOutput(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (Exception e) {
            return "Error reading process output: " + e.getMessage();
        }
    }
}
