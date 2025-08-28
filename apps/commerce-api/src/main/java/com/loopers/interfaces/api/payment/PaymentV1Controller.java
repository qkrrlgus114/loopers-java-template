package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.command.PaymentCallbackCommand;
import com.loopers.application.payment.facade.PaymentFacade;
import com.loopers.interfaces.api.orders.dto.PaymentCallbackDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentV1Controller {

    private final PaymentFacade paymentFacade;

    /*
     * 결제 콜백 API
     * */
    @PostMapping("/callback")
    public void paymentCallback(@RequestBody PaymentCallbackDTO paymentCallbackDTO) {
        // 트랜잭션키로 결제 정보 조회
        paymentFacade.processCallbackPayment(
                new PaymentCallbackCommand(
                        paymentCallbackDTO.transactionKey(),
                        paymentCallbackDTO.orderId()
                )
        );

        // 외부 데이터플랫폼으로 결과 전송(?)
        // 솔직히 이 부분 잘 이해가 되지 않습니다. 흑
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
