package com.wang.springbootdemo.rabbitDemo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "myQueue")
public class Receiver {

    @RabbitHandler
    public void processMessage(String content) {
        System.out.println("Receiver: " + content);
    }
}
