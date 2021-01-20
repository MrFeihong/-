package com.baidu.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-jobs.xml");
        System.in.read();
    }
}
