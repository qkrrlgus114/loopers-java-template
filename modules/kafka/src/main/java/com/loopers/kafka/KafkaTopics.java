package com.loopers.kafka;

public class KafkaTopics {
    public static final String CATALOG_EVENTS = "catalog-events";
    public static final String ORDER_EVENTS = "order-events";
    public static final String DLQ_TOPIC = "dead-letter-queue";

    private KafkaTopics() {
        throw new IllegalStateException("Constants class");
    }
}
