package com.loopers.infrastructure.payment;

import com.loopers.application.payment.PaymentGatewayPort;
import com.loopers.domain.payment.CardType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayRestPort implements PaymentGatewayPort {

    private final RestTemplate restTemplate;

    private static final String PAYMENT_API_URL = "http://localhost:8082/api/v1/payments";

    @Retry(name = "pg-api", fallbackMethod = "paymentFallback")
    @CircuitBreaker(name = "pg-api")
    public PgPaymentResponse requestPayment(String orderKey,
                                            CardType cardType,
                                            String cardNo,
                                            Long amount,
                                            Long memberId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-USER-ID", String.valueOf(memberId));

        HttpEntity<PaymentRequest> httpEntity =
                new HttpEntity<>(PaymentRequest.of(orderKey, cardType, cardNo, amount), headers);

        // 실제 결제 API 호출
        ResponseEntity<PgPaymentResponse> response = restTemplate.
                exchange(PAYMENT_API_URL, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    private PgPaymentResponse paymentFallback(String orderKey, CardType cardType, String cardNo, BigDecimal amount,
                                              Long memberId, Throwable t) {
        if (t instanceof io.github.resilience4j.circuitbreaker.CallNotPermittedException) {
            log.error("CurcuitBreaker 열림 : {}", t.getMessage());
            return new PgPaymentResponse(
                    new PgPaymentResponse.Meta("FAIL", "TIMEOUT", "PG 호출이 지연되었습니다.(서킷브레이커 발동)"),
                    new PgPaymentResponse.Data(null, "CIRCUIT_BREAKER_OPEN", t.getMessage())
            );
        } else {
            log.error("Retry 시도 : {}", t.getMessage());
            return new PgPaymentResponse(
                    new PgPaymentResponse.Meta("FAIL", "RETRY_FAIL", "PG 호출이 실패했습니다.(재시도 실패)"),
                    new PgPaymentResponse.Data(null, "RETRY_FAIL", t.getMessage())
            );
        }
    }

}
