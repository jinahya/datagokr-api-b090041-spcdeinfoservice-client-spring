package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.Month;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SpcdeInfoServiceReactiveClient_getRestDeInfo_IT extends SpcdeInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getRestDeInfo(solYear, solMonth)")
    @EnumSource(Month.class)
    @ParameterizedTest
    void getRestDeInfo_Expected_YearMonth(final Month solMonth) {
        final Year solYear = Year.now();
        final Item last = clientInstance().getRestDeInfo(solYear, solMonth)
                .doOnNext(i -> {
                    assertThat(i.getLocdate())
                            .isNotNull()
                            .satisfies(d -> {
                                assertThat(Year.from(d)).isEqualTo(solYear);
                                assertThat(d.getMonth()).isSameAs(solMonth);
                            });
                    assertThat(i.getIsHoliday()).isEqualTo(Boolean.TRUE);
                    assertThat(i.getApiDiscriminator()).isNotNull().isSameAs(ApiDiscriminator.GET_REST_DE_INFO);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getRestDeInfo(solYear, null)")
    @Test
    void getRestDeInfo_Expected_Year() {
        final Year solYear = Year.now();
        final Item last = clientInstance().getRestDeInfo(solYear, null)
                .doOnNext(i -> {
                    assertThat(i.getLocdate())
                            .isNotNull()
                            .satisfies(d -> {
                                assertThat(Year.from(d)).isEqualTo(solYear);
                            });
                    assertThat(i.getIsHoliday()).isEqualTo(Boolean.TRUE);
                    assertThat(i.getApiDiscriminator()).isNotNull().isSameAs(ApiDiscriminator.GET_REST_DE_INFO);
                })
                .blockLast();
    }
}
