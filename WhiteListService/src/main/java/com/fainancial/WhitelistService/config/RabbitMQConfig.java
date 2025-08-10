package com.fainancial.WhitelistService.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fainancial.WhitelistService.constants.WhitelistConstants.UPLOAD_COMPLETED_QUEUE;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.WHITELIST_COMPLETED_QUEUE;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue uploadCompletedQueue() {
        return new Queue(UPLOAD_COMPLETED_QUEUE, true);
    }

    @Bean
    public Queue whitelistCompletedQueue() {
        return new Queue(WHITELIST_COMPLETED_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}