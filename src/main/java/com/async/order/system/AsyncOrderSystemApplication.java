package com.async.order.system;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AsyncOrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncOrderSystemApplication.class, args);
    }
}
