package com.tintin.trial.rabbit;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQConsumer;

public class RabbitMQOperations {
	RabbitMQClient rabbitMQClient;
	Vertx vertx;
	
	public RabbitMQOperations(RabbitMQClient rabbitMQClient,Vertx vertx){
		this.rabbitMQClient=rabbitMQClient;
		this.vertx=vertx;
		rabbitMQClient.start(resultHandler->{
			if(resultHandler.succeeded()) {
				System.out.println("succedded");
			}else {
				System.out.println("failed");
			}
		});
	}
	
	public void createExchange(String exchangeName) {
		rabbitMQClient.exchangeDeclare("trial.exchange", "topic", true, false, resultHandler->{
			if(resultHandler.succeeded()) {
				System.out.println("created successfully");
			}else {
				System.out.println("failed");
				resultHandler.cause().printStackTrace();
			}
		});
	}
	
	
	public void createQueue(String queueName) {
		rabbitMQClient.queueDeclare("trial.queue", true, false, false, resultHandler->{
			if(resultHandler.succeeded()) {
				System.out.println("queue created successfully");
			}else {
				System.out.println("failed");
				resultHandler.cause().printStackTrace();
			}
		});
	}
	
	public void bindQueue2Exchange(String queueName,String exchangeName) {
		rabbitMQClient.queueBind("trial.queue", "trial.exchange", "#", resultHandler->{
			if(resultHandler.succeeded()) {
				System.out.println("bind created successfully");
			}else {
				System.out.println("failed");
				resultHandler.cause().printStackTrace();
			}
		});
	}
	
	public void publish2RabbitMQ(String request) {
		Buffer buffer = Buffer.buffer(request);
		System.out.println(buffer.toString());
		rabbitMQClient.basicPublish("trial.exchange", "#", buffer,
			   publishResultHandler -> {
				 if (publishResultHandler.succeeded()) {
					System.out.println("message published to queue");
				 } else {
					System.out.println("failed");
					publishResultHandler.cause().printStackTrace();
				 }
			   });
	}

	public void consumeFromRabbitMQ(){
		rabbitMQClient.basicConsumer("trial.queue", rabbitMQConsumerAsyncResult -> {
            if (rabbitMQConsumerAsyncResult.succeeded()) {
            //   System.out.println("RabbitMQ consumer created !");
              RabbitMQConsumer mqConsumer = rabbitMQConsumerAsyncResult.result();
              mqConsumer.handler(message -> {
                System.out.println("Got message: " + message.body().toString());
              });
            } else {
              rabbitMQConsumerAsyncResult.cause().printStackTrace();
            }
          });
	}
}
