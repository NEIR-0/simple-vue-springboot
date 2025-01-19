package com.restApi.RestAPI.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Mendefinisikan Antrian
    @Bean
    public Queue transactionQueue() {
        return new Queue("transactionQueue", false);
    }
    @Bean
    public Queue userQueue() {
        return new Queue("userQueue", true);
    }
    @Bean
    public Queue messageQueue() {
        return new Queue("messageQueue", true);
    }
    @Bean
    public Queue tokenQueue() {
        return new Queue("tokenQueue", true);
    }
    @Bean
    public Queue notifQueue() {
        return new Queue("notifQueue", true);
    }

    // Mendefinisikan Exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("myExchange");
    }

    // Mendefinisikan Binding untuk transaksi
    @Bean
    public Binding bindingTransactionQueue(Queue transactionQueue, TopicExchange exchange) {
        return BindingBuilder.bind(transactionQueue).to(exchange).with("transaction.routing.key");
    }

    // Mendefinisikan Binding untuk userQueue
    @Bean
    public Binding bindingUserQueue(Queue userQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userQueue).to(exchange).with("user.routing.key");
    }

    @Bean
    public Binding bindingMessageQueue(Queue messageQueue, TopicExchange exchange) {
        return BindingBuilder.bind(messageQueue).to(exchange).with("message.routing.key");
    }

    @Bean
    public Binding bindingTokenQueue(Queue tokenQueue, TopicExchange exchange) {
        return BindingBuilder.bind(tokenQueue).to(exchange).with("token.routing.key");
    }

    @Bean
    public Binding bindingNotifQueue(Queue notifQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notifQueue).to(exchange).with("notif.routing.key");
    }
}
