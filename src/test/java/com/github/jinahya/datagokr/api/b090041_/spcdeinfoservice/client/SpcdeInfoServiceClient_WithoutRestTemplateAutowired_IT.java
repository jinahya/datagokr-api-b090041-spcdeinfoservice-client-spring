package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        classes = {
                SpcdeInfoServiceClient_WithoutRestTemplateAutowired_IT._Configuration.class,
                SpcdeInfoServiceClient.class
        }
)
@Slf4j
class SpcdeInfoServiceClient_WithoutRestTemplateAutowired_IT
        extends AbstractSpcdeInfoServiceClientIT<SpcdeInfoServiceClient> {

    @Import(AbstractSpcdeInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        @SpcdeInfoServiceClient.SpcdeInfoServiceRestTemplate
        @Bean
        RestTemplate spcdeInfoServiceRestTemplate() {
            return null;
        }

        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    void clientInstanceRestTemplate_NonNull_() {
        assertThat(clientInstance().restTemplate()).isNotNull();
    }

    @Test
    void clientInstanceRestTemplateRootUri_NonBlank_() {
        assertThat(clientInstance().restTemplateRootUri()).isNotBlank();
    }

    SpcdeInfoServiceClient_WithoutRestTemplateAutowired_IT() {
        super(SpcdeInfoServiceClient.class);
    }
}
