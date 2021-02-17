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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SpcdeInfoServiceClient_getAnniversaryInfo_IT extends SpcdeInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getAnniversaryInfo(solYear, solMonth)")
    @ParameterizedTest
    @EnumSource(Month.class)
    @SuppressWarnings({"java:S5841"})
    void getAnniversaryInfo_Expected_YearMonth(final Month solMonth) {
        final Year solYear = Year.now();
        final List<Item> items = clientInstance().getAnniversaryInfo(solYear, solMonth);
        assertThat(items)
                .isNotNull()
                //.isNotEmpty() // 특정 월에 없을 수도 있다.
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLocdate())
                            .isNotNull()
                            .satisfies(d -> {
                                assertThat(Year.from(d)).isEqualTo(solYear);
                                assertThat(d.getMonth()).isSameAs(solMonth);
                            });
                    assertThat(i.getApiDiscriminator()).isNotNull().isSameAs(ApiDiscriminator.GET_ANNIVERSARY_INFO);
                });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getAnniversaryInfo(solYear, null)")
    @Test
    void getAnniversaryInfo_Expected_Year() {
        final Year solYear = Year.now();
        final List<Item> items = clientInstance().getAnniversaryInfo(solYear, null);
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLocdate())
                            .isNotNull()
                            .satisfies(d -> {
                                assertThat(Year.from(d)).isEqualTo(solYear);
                            });
                    assertThat(i.getApiDiscriminator()).isNotNull().isSameAs(ApiDiscriminator.GET_ANNIVERSARY_INFO);
                });
    }
}
