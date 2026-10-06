package com.project.expensetracker.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.project.expensetracker.config.RabbitConfig.BUDGET_ALERT_ROUTING_KEY;
import static com.project.expensetracker.config.RabbitConfig.BUDGET_EXCHANGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class BudgetAlertProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendAlert(BudgetAlertMessage message) {
        log.info("Publication d'une alerte de budget pour la catégorie {} ({})",
                message.categoryName(), message.userEmail());
        rabbitTemplate.convertAndSend(BUDGET_EXCHANGE, BUDGET_ALERT_ROUTING_KEY, message);
    }
}