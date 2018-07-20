package com.wang.springbootdemo.rabbitDemo;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SenderConfiguration {

    //@Bean
    public Queue queue() {
        return new Queue("myQueue");
    }
}
