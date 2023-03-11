package com.ims.apigateway.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Autowired
    ConfigProperties configProperties;

    @Bean
    Queue queue() {
        return new Queue(configProperties.getLimitQueue(), false);
    }

    @Bean
    Queue loanQueue() {
        return new Queue(configProperties.getLoanQueue(), false);
    }

    @Bean
    Queue loanRepayQueue() {
        return new Queue(configProperties.getLoanRepayQueue(), false);
    }

    // spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(configProperties.getExchange());
    }

    // binding between limit queue and exchange using routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(configProperties.getRoutingKey());
    }

    // binding between loan queue and exchange using routing key
    @Bean
    public Binding loanBinding(){
        return BindingBuilder
                .bind(loanQueue())
                .to(exchange())
                .with(configProperties.getRoutingKey());
    }

    // binding between loan queue and exchange using routing key
    @Bean
    public Binding loanRepayBinding(){
        return BindingBuilder
                .bind(loanRepayQueue())
                .to(exchange())
                .with(configProperties.getRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
