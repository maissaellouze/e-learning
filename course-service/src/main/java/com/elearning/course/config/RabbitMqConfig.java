package com.elearning.course.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMqConfig {

    public static final String COURSE_EXCHANGE = "course-exchange";
    public static final String MODULE_CREATED_QUEUE = "module-created-queue";
    public static final String MODULE_CREATED_ROUTING_KEY = "module.created";

    @Bean
    public DirectExchange courseExchange() {
        return new DirectExchange(COURSE_EXCHANGE, true, false);
    }

    @Bean
    public Queue moduleCreatedQueue() {
        return new Queue(MODULE_CREATED_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue moduleCreatedQueue, DirectExchange courseExchange) {
        return BindingBuilder.bind(moduleCreatedQueue)
                .to(courseExchange)
                .with(MODULE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
