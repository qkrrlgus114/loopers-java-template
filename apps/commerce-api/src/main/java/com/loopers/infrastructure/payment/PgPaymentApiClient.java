package com.loopers.infrastructure.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PgPaymentApiClient {

    private final RestTemplate restTemplate;

    private static final String PAYMENT_INFO_WITH_TRANSACTION_KEY = "http://localhost:8082/api/v1/payments/";

    public PgPaymentInfoResponse getPaymentInfo(String transactionKey, Long memberId) {
        String url = PAYMENT_INFO_WITH_TRANSACTION_KEY + transactionKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-USER-ID", String.valueOf(memberId));

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PgPaymentInfoResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<>() {
                        }
                );


        return response.getBody();
    }
}
