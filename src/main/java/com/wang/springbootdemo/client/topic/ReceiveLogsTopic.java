package com.wang.springbootdemo.client.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //"*.orange.*", "*.*.rabbit", "lazy.#"
        String queueName = channel.queueDeclare().getQueue();
        String[] bindingKey = {"*.*.rabbit", "lazy.#"};
        for (String key :
                bindingKey) {
            //binding key :
            channel.queueBind(queueName, EXCHANGE_NAME, key);
        }

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);

    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }

}
