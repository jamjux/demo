package com.wang.springbootdemo.rabbitDemo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class Sender {

    @Autowired
    private Queue queue;
    @Autowired
    private AmqpTemplate template;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {

        String message = "helloll";
        template.convertAndSend(queue.getName(), message);

        System.out.println("Sender: " + message + LocalTime.now());
    }

}


