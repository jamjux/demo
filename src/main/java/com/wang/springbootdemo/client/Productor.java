package com.wang.springbootdemo.client;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Productor {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("jerry");
        factory.setPassword("jerry");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/jerry");
        Connection conn = null;
        Channel channel = null;

        try {
            conn = factory.newConnection();
            channel = conn.createChannel();
            //一种持久的,非自动交换的“直接”类型的交换
            //具有生成名称的非持久,独占,自动删除队列
            channel.exchangeDeclare("exchangeName", "direct", true);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "exchangeName", "routingKey");

            //具有已知名称的持久，非独占，非自动删除队列
            channel.queueDeclare("queueName", true, false, false, null);

            //被动声明
            AMQP.Queue.DeclareOk response = channel.queueDeclarePassive("queueName");

            //不会等待服务器响应,它们更依赖于心跳机制来检测失败的操作
            channel.queueDeclareNoWait("queueName", true, false, false, null);

            //删除
            channel.queueDelete("name");
            //channel.queueDelete();
            //清除消息
            channel.queuePurge("queueName");

            //发布消息
            byte[] message = "nihao".getBytes();
            channel.basicPublish("exchangeName", "routingKey", null, message);
            //mandatory强制标志，预定义消息属性
            channel.basicPublish("exchangeName","routingKey",true,MessageProperties.PERSISTENT_TEXT_PLAIN,message);
            //Builder()构建消息属性
            channel.basicPublish("exchangeName","routingKey",true,new AMQP.BasicProperties.Builder()
                    .contentType("text/plain")
                    .deliveryMode(2)
                    .priority(1)
                    .userId("bob")
                    .build()
                    ,message);
            //带有header
            Map<String, Object> header = new HashMap<>();
            header.put("name","tom");
            channel.basicPublish("exchangeName","routingKey",true,new AMQP.BasicProperties.Builder()
                            .headers(header)
                            .build()
                    ,message);
            //通道线程并发

            //push
            Channel finalChannel = channel;
            channel.basicConsume("queueName",false,"consumerTag",new DefaultConsumer(finalChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    finalChannel.basicAck(deliveryTag,false);
                }
            });

            //pull
            GetResponse response1 = channel.basicGet("queueName",false);
            if(response1 != null) {
                AMQP.BasicProperties prop = response1.getProps();
                byte[] body = response1.getBody();
                long deliveryTag = response1.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag,false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
