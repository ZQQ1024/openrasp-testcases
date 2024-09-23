package com.example.testcases;


public class RunTimeTest {

    public static void main(String[] args) {
        try {
            String[] command = {"/bin/sh", "-c", "code"};
            Runtime.getRuntime().exec(command);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
