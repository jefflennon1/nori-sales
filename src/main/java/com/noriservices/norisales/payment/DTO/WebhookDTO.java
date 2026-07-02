package com.noriservices.norisales.payment.DTO;

public record WebhookDTO(String action, WebhookDataDTO data) {
}
