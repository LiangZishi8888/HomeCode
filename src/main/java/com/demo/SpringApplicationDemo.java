package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("/com.demo")
@SpringBootApplication
public class SpringApplicationDemo {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationDemo.class);
    }
}
