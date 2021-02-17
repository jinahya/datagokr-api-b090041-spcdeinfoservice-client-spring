package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                SpcdeInfoServiceReactiveClient_WithoutWebClientAutowired_IT._Configuration.class,
                SpcdeInfoServiceReactiveClient.class
        }
)
@Slf4j
class SpcdeInfoServiceReactiveClient_WithoutWebClientAutowired_IT
        extends AbstractSpcdeInfoServiceClientIT<SpcdeInfoServiceReactiveClient> {

    @Import(AbstractSpcdeInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        @SpcdeInfoServiceReactiveClient.SpcdeInfoServiceWebClient
        @Bean
        WebClient spcdeInfoServiceWebClient() {
            return null;
        }

        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    void clientInstance__() {
        assertThat(clientInstance()).isNotNull();
    }

    SpcdeInfoServiceReactiveClient_WithoutWebClientAutowired_IT() {
        super(SpcdeInfoServiceReactiveClient.class);
    }
}
