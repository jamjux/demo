package com.wang.springbootdemo.rabbitDemo;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RabbitConfig {

    public static String QUEUE_NAME = "myQueue";
    public static String EXCHANGE_NAME = "myQueue";


//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
//        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
//        cachingConnectionFactory.setUsername("");
//
//        return cachingConnectionFactory;
//    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("hh");
    }

    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }


}