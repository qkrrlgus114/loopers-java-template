package com.loopers.application.payment.processor;

import com.loopers.domain.payment.PaymentType;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PaymentProcessorRegistry {
    private final Map<PaymentType, PaymentProcessor> map;

    public PaymentProcessorRegistry(List<PaymentProcessor> processors) {
        this.map = processors.stream().collect(Collectors.toMap(PaymentProcessor::supports, p -> p));
    }

    public PaymentProcessor get(PaymentType type) {
        return Optional.ofNullable(map.get(type))
                .orElseThrow(() -> new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 결제 타입입니다: " + type));
    }
}
