package com.noriservices.norisales.payment.mercadopago;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.noriservices.norisales.shared.exception.PaymentProviderException;
import org.springframework.stereotype.Component;

@Component
public class MercadoPagoGateway {

    private static final String APPROVED_STATUS = "approved";

    public boolean isPaymentApproved(String externalPaymentId) {
        try {
            PaymentClient client = new PaymentClient();

            com.mercadopago.resources.payment.Payment payment =
                    client.get(Long.parseLong(externalPaymentId));

            return APPROVED_STATUS.equals(payment.getStatus());

        } catch (MPException | MPApiException ex) {
            throw new PaymentProviderException(
                    "Failed to retrieve payment from Mercado Pago",
                    ex
            );
        }
    }
}
