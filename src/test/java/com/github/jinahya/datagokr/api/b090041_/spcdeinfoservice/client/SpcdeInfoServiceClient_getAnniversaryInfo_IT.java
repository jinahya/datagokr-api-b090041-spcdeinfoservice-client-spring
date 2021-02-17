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
class SpcdeInfoServiceClient_getAnniversaryInfo_IT extends SpcdeInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getAnniversaryInfo(yearMonth)")
    @Test
    void getAnniversaryInfo_Expected_SolarYearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getAnniversaryInfo(yearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLocdate().getYear()).isEqualTo(yearMonth.getYear());
            assertThat(i.getLocdate().getMonth()).isNotNull().isSameAs(yearMonth.getMonth());
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getAnniversaryInfo(year, executor, collection)")
    @Test
    void getAnniversaryInfo_Expected_SolarYear() {
        final List<Item> items = new ArrayList<>();
        final Year year = Year.now();
        clientInstance().getAnniversaryInfo(year, commonPool(), items);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLocdate().getYear()).isEqualTo(year.getValue());
        });
    }
}
