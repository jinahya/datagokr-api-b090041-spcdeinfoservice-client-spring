package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

import static java.util.Objects.requireNonNull;

abstract class AbstractSpcdeInfoServiceClientIT<T extends AbstractSpcdeInfoServiceClient> {

    static final String SYSTEM_PROPERTY_SERVICE_KEY = "serviceKey";

    @Configuration
    static class _Configuration {

        @AbstractSpcdeInfoServiceClient.SpcdeInfoServiceServiceKey
        @Bean
        String spcdeInfoServiceServiceKey() {
            return System.getProperty(SYSTEM_PROPERTY_SERVICE_KEY);
        }

        @Bean
        Validator validator() {
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
    }

    /**
     * Creates a new instance with specified client class.
     *
     * @param clientClass the client class to test.
     * @see #clientClass
     */
    AbstractSpcdeInfoServiceClientIT(final Class<T> clientClass) {
        super();
        this.clientClass = requireNonNull(clientClass, "clientClass is null");
    }

    /**
     * The client class to test.
     */
    final Class<T> clientClass;

    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private T clientInstance;

    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private Validator validator;
}