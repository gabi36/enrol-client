package com.example.clientreputationapi.service.impl;

import com.example.clientreputationapi.service.ClientReputationService;
import org.springframework.stereotype.Service;

@Service
public class ClientReputationServiceImpl implements ClientReputationService {

    private static final double PAYMENT_HISTORY_WEIGHT = 0.5;
    private static final double ENGAGEMENT_HISTORY_WEIGHT = 0.5;
    private static final double FEEDBACK_WEIGHT = 0.5;
    private static final double OFFSET = 150.0;

    @Override
    public double clientReputationFormula(long cnp) {
        //These variables can be retrieved by querying an external service using a unique identifier known as the clientId,
        //which can represent the identification number of an individual.
        //The external service provides information such as paymentHistory, engagementHistory, and feedback based on the provided clientId.
        double paymentHistory = Math.random();
        double engagementHistory = Math.random();
        double feedback = Math.random();
        return calculateClientReputation(paymentHistory, engagementHistory, feedback);
    }

    private double calculateClientReputation(double paymentHistory, double engagementHistory, double feedback) {
        double weightedSum = PAYMENT_HISTORY_WEIGHT * paymentHistory + ENGAGEMENT_HISTORY_WEIGHT * engagementHistory + FEEDBACK_WEIGHT * feedback;
        return OFFSET - weightedSum * 100;
    }
}
