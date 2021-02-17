package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public final class ItemResources {

    public static Stream<Item> items() {
        return ResponseResources.responses().flatMap(r -> r.getBody().getItems().stream());
    }

    private ItemResources() {
        throw new AssertionError("instantiation is not allowed");
    }
}