package com.project.expensetracker.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.project.expensetracker.config.RabbitConfig.BUDGET_ALERT_QUEUE;

@Component
@Slf4j
public class BudgetAlertConsumer {

    @RabbitListener(queues = BUDGET_ALERT_QUEUE)
    public void handleBudgetAlert(BudgetAlertMessage message) {
        log.warn("""
                ALERTE BUDGET DÉPASSÉ
                Utilisateur : {}
                Catégorie   : {}
                Mois        : {}
                Budget      : {}
                Dépensé     : {}
                """,
                message.userEmail(),
                message.categoryName(),
                message.month(),
                message.budgetLimit(),
                message.totalSpent());
    }
}