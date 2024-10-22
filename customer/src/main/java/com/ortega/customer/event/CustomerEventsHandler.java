package com.ortega.customer.event;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventsHandler {

    @EventHandler
    public void on(CustomerCreatedEvent event) {
        log.info("Execute action CustomerEventsHandler {}", event);
    }

}
