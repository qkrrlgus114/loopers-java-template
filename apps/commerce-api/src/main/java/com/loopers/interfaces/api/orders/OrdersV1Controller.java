package com.loopers.interfaces.api.orders;

import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.facade.OrdersFacade;
import com.loopers.application.orders.result.OrdersRegisterInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrdersV1Controller {

    private final OrdersFacade ordersFacade;

    /*
     * 주문하기
     * */
    @PostMapping
    public OrdersRegisterInfoResult placeOrder(@RequestBody PlaceOrderCommand command) {
        return ordersFacade.placeOrder(command);
    }
}
