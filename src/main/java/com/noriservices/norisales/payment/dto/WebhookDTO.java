package com.noriservices.norisales.payment.dto;

public record WebhookDTO(String action, WebhookDataDTO data) {
}
