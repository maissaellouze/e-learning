package com.elearning.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String COURSE_EXCHANGE = "course-exchange";
    public static final String NOTIFICATION_QUEUE = "notification-queue";
    public static final String MODULE_CREATED_ROUTING_KEY = "module.created";

    @Bean
    public DirectExchange courseExchange() {
        return new DirectExchange(COURSE_EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange courseExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(courseExchange)
                .with(MODULE_CREATED_ROUTING_KEY);
    }
}
