package com.tintin.trial.rabbit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.rabbitmq.RabbitMQClient;

public class RabbitVerticle extends AbstractVerticle{
    
    private static final Logger LOGGER = LogManager.getLogger(RabbitVerticle.class);

    @Override
    public void start() throws Exception {
        String rabbitExchange = "trial.exchange";
        String rabbitQueue = "trial.queue";

        RabbitMQClient rabbitMQClient=RabbitMQ.getInstance(vertx);
        rabbitMQClient.start(handler->{
        LOGGER.info("RabbitMQ Client Started");
        RabbitMQOperations rabbitOperation=new RabbitMQOperations(rabbitMQClient,vertx);
 
        rabbitOperation.createExchange(rabbitExchange);
        rabbitOperation.createQueue(rabbitQueue);
        rabbitOperation.bindQueue2Exchange(rabbitQueue, rabbitExchange);
        rabbitOperation.publish2RabbitMQ("Hello from Verticle");
        });
    }

    
}
