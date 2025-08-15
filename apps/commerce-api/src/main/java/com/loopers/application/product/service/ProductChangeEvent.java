package com.loopers.application.product.service;

import org.springframework.context.ApplicationEvent;

public class ProductChangeEvent extends ApplicationEvent {
    public ProductChangeEvent(Long id) {
        super(id);
    }
}
