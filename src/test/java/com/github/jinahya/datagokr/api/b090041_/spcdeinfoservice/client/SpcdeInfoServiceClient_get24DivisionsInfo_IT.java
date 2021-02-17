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
class SpcdeInfoServiceClient_get24DivisionsInfo_IT extends SpcdeInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("get24DivisionsInfo(yearMonth)")
    @Test
    void get24DivisionsInfo_Expected_YearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        final List<Item> items = clientInstance().get24DivisionsInfo(yearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLocdate().getYear()).isEqualTo(yearMonth.getYear());
            assertThat(i.getLocdate().getMonth()).isNotNull().isSameAs(yearMonth.getMonth());
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("get24DivisionsInfo(year, executor, collection)")
    @Test
    void get24DivisionsInfo_Expected_Year() {
        final List<Item> items = new ArrayList<>();
        final Year year = Year.now();
        clientInstance().get24DivisionsInfo(year, commonPool(), items);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().hasSize(24).allSatisfy(i -> {
            assertThat(i.getLocdate().getYear()).isEqualTo(year.getValue());
        });
    }
}
