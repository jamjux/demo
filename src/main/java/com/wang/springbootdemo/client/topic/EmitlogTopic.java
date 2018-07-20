package com.wang.springbootdemo.client.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitlogTopic {
    private final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //exchange:direct
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String routingKey = "lazy.l.rabbit";
        String message = getMessage(args);
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes());

        System.out.println(" [x] Sent '" + message + ".");
        channel.close();
        connection.close();
    }



    private static String getMessage(String[] strings) {
        if(strings.length < 1) {
            return "Hello World!";
        }
        return joinStrings(strings, "");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if(length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
