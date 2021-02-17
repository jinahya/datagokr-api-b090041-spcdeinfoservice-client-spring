package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SpcdeInfoServiceReactiveClient_getHoliDeInfo_IT extends SpcdeInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getHoliDeInfo(solYear, solMonth)")
    @EnumSource(Month.class)
    @ParameterizedTest
    void getHoliDeInfo_Expected_YearMonth(final Month solMonth) {
        final YearMonth yearMonth = YearMonth.now();
        final Year solYear = Year.from(yearMonth);
        final Item last = clientInstance().getHoliDeInfo(solYear, solMonth)
                .doOnNext(i -> {
                    log.debug("item: {}", i);
                })
                .doOnNext(i -> {
                    assertThat(i.getLocdate()).isNotNull().satisfies(d -> {
                        assertThat(Year.from(d)).isEqualTo(solYear);
                        assertThat(d.getMonth()).isSameAs(solMonth);
                    });
                })
                .blockLast()
                ;
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getHoliDeInfo(solYear, null)")
    @Test
    void getHoliDeInfo_Expected_Year() {
        final Year solYear = Year.now();
        final Item last = clientInstance().getHoliDeInfo(solYear, null)
                .doOnNext(i -> {
                    log.debug("item: {}", i);
                })
                .doOnNext(i -> {
                    assertThat(i.getLocdate()).isNotNull().satisfies(d -> {
                        assertThat(Year.from(d)).isEqualTo(solYear);
                    });
                })
                .blockLast()
                ;
    }
}
