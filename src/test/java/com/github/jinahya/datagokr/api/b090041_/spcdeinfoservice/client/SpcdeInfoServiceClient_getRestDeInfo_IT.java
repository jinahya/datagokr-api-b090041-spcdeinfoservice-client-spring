package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SpcdeInfoServiceClient_getRestDeInfo_IT extends SpcdeInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getRestDeInfo(yearMonth)")
    @Test
    void getRestDeInfo__YearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getRestDeInfo(yearMonth);
        assertThat(items)
                .isNotNull()
                //.isNotEmpty() // 특정 월에 공휴일이 없을 수도 있다.
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLocdate()).isNotNull().satisfies(d -> {
                        assertThat(Year.of(d.getYear())).isNotNull().isEqualTo(Year.from(yearMonth));
                        assertThat(d.getMonth()).isNotNull().isEqualTo(yearMonth.getMonth());
                    });
                })
        ;
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getRestDeInfo(year, executor, collection)")
    @Test
    void getRestDeInfo_Expected_Year() {
        final List<Item> items = new ArrayList<>();
        final Year year = Year.now();
        clientInstance().getRestDeInfo(year, commonPool(), items);
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLocdate()).isNotNull().satisfies(d -> {
                        assertThat(Year.from(d)).isEqualTo(year);
                    });
                })
        ;
    }
}
