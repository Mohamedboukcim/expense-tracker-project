package com.project.expensetracker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BUDGET_EXCHANGE = "budget.exchange";
    public static final String BUDGET_ALERT_QUEUE = "budget.alert.queue";
    public static final String BUDGET_ALERT_ROUTING_KEY = "budget.alert";

    @Bean
    public TopicExchange budgetExchange() {
        return new TopicExchange(BUDGET_EXCHANGE);
    }

    @Bean
    public Queue budgetAlertQueue() {
        return new Queue(BUDGET_ALERT_QUEUE, true);
    }

    @Bean
    public Binding budgetAlertBinding(Queue budgetAlertQueue, TopicExchange budgetExchange) {
        return BindingBuilder.bind(budgetAlertQueue)
                .to(budgetExchange)
                .with(BUDGET_ALERT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}