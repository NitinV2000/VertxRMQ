package com.tintin.trial.Async;

import com.tintin.trial.rabbit.RabbitMQ;
import com.tintin.trial.rabbit.RabbitMQOperations;

import io.vertx.core.AbstractVerticle;
import io.vertx.rabbitmq.RabbitMQClient;


public class AsyncVerticle extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        RabbitMQClient rabbitMQClient=RabbitMQ.getInstance(vertx);
        RabbitMQOperations rabbitOperation=new RabbitMQOperations(rabbitMQClient,vertx);
        vertx.setPeriodic(5000, handler -> {
            rabbitOperation.consumeFromRabbitMQ();
        });
    }
    
}
