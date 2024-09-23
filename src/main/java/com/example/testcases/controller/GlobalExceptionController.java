package com.example.testcases.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

//@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        // 将堆栈信息转换为字符串
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        // 返回堆栈信息作为响应
        return new ResponseEntity<>(stackTrace, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
