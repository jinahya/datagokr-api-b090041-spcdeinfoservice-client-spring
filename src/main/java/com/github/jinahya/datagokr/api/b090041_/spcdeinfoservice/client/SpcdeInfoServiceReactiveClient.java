package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Response;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Responses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;
import java.time.Month;
import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.fromIterable;

/**
 * A client implementation uses an instance of {@link WebClient}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see SpcdeInfoServiceClient
 */
@Lazy
@Component
@Slf4j
public class SpcdeInfoServiceReactiveClient extends AbstractSpcdeInfoServiceClient {

    /**
     * An injection qualifier for an instance of {@link WebClient}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SpcdeInfoServiceWebClient {

    }

    // -----------------------------------------------------------------------------------------------------------------
    protected static Mono<Response> handled(final Mono<Response> mono) {
        return requireNonNull(mono, "mono is null").handle((r, h) -> {
            if (!Responses.isResultSuccessful(r)) {
                h.error(new WebClientException("unsuccessful result: " + r.getHeader()) {
                });
            } else {
                h.next(r);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public SpcdeInfoServiceReactiveClient() {
        super();
    }

    // --------------------------------------------------------------------------------------------- /get24DivisionsInfo

    /**
     * Retrieves a response from {@code /get24DivisionsInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> get24DivisionsInfo(@NotNull final Year solYear, @Nullable final Month solMonth,
                                                      @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_24_DIVISIONS_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                    ;
                    ofNullable(solMonth)
                            .map(MONTH_FORMATTER::format)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(SpcdeInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Reads all responses from all pages of {@code /get24DivisionsInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> get24DivisionsInfoForAllPages(@NotNull final Year solYear,
                                                                 @Nullable final Month solMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return get24DivisionsInfo(solYear, solMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return get24DivisionsInfo(solYear, solMonth, pageNo.incrementAndGet());
                })
                ;
    }

    /**
     * Reads all items from {@code /get24DivisionsInfo} with parameters derived from specified solar month.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of items.
     * @see #get24DivisionsInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> get24DivisionsInfo(@NotNull final Year solYear, @Nullable final Month solMonth) {
        return get24DivisionsInfoForAllPages(solYear, solMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()))
                ;
    }

    // --------------------------------------------------------------------------------------------- /getAnniversaryInfo

    /**
     * Retrieves a response from {@code /getAnniversaryInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getAnniversaryInfo(@NotNull final Year solYear, @Nullable final Month solMonth,
                                                      @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_ANNIVERSARY_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                    ;
                    ofNullable(solMonth)
                            .map(MONTH_FORMATTER::format)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(SpcdeInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Reads all responses from all pages of {@code /getAnniversaryInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getAnniversaryInfoForAllPages(@NotNull final Year solYear,
                                                                 @Nullable final Month solMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getAnniversaryInfo(solYear, solMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getAnniversaryInfo(solYear, solMonth, pageNo.incrementAndGet());
                })
                ;
    }

    /**
     * Reads all items from {@code /getAnniversaryInfo} with parameters derived from specified solar month.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of items.
     * @see #getAnniversaryInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getAnniversaryInfo(@NotNull final Year solYear, @Nullable final Month solMonth) {
        return getAnniversaryInfoForAllPages(solYear, solMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()))
                ;
    }

    // -------------------------------------------------------------------------------------------------- /getHoliDeInfo

    /**
     * Retrieves a response from {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getHoliDeInfo(@NotNull final Year solYear, @Nullable final Month solMonth,
                                                 @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_HOLI_DE_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                    ;
                    ofNullable(solMonth)
                            .map(MONTH_FORMATTER::format)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(SpcdeInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Reads all responses from all pages of {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getHoliDeInfoForAllPages(@NotNull final Year solYear,
                                                            @Nullable final Month solMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getHoliDeInfo(solYear, solMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getHoliDeInfo(solYear, solMonth, pageNo.incrementAndGet());
                })
                ;
    }

    /**
     * Reads all items from {@code /getHoliDeInfo} with parameters derived from specified solar month.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of items.
     * @see #getHoliDeInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getHoliDeInfo(@NotNull final Year solYear, @Nullable final Month solMonth) {
        return getHoliDeInfoForAllPages(solYear, solMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()))
                ;
    }

    // -------------------------------------------------------------------------------------------------- /getRestDeInfo

    /**
     * Retrieves a response from {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getRestDeInfo(@NotNull final Year solYear, @Nullable final Month solMonth,
                                                 @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_REST_DE_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                    ;
                    ofNullable(solMonth)
                            .map(MONTH_FORMATTER::format)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(SpcdeInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Reads all responses from all pages of {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getRestDeInfoForAllPages(@NotNull final Year solYear,
                                                            @Nullable final Month solMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getRestDeInfo(solYear, solMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getRestDeInfo(solYear, solMonth, pageNo.incrementAndGet());
                })
                ;
    }

    /**
     * Reads all items from {@code /getRestDeInfo} with parameters derived from specified solar month.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of items.
     * @see #getRestDeInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getRestDeInfo(@NotNull final Year solYear, @Nullable final Month solMonth) {
        return getRestDeInfoForAllPages(solYear, solMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()))
                ;
    }

    // ----------------------------------------------------------------------------------------------- /getSundryDayInfo

    /**
     * Retrieves a response from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getSundryDayInfo(@NotNull final Year solYear, @Nullable final Month solMonth,
                                                    @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_SUNDRY_DAY_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                    ;
                    ofNullable(solMonth)
                            .map(MONTH_FORMATTER::format)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(SpcdeInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Reads all responses from all pages of {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getSundryDayInfoForAllPages(@NotNull final Year solYear,
                                                               @Nullable final Month solMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getSundryDayInfo(solYear, solMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getSundryDayInfo(solYear, solMonth, pageNo.incrementAndGet());
                })
                ;
    }

    /**
     * Reads all items from {@code /getSundryDayInfo} with parameters derived from specified solar month.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a flux of items.
     * @see #getSundryDayInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getSundryDayInfo(@NotNull final Year solYear, @Nullable final Month solMonth) {
        return getSundryDayInfoForAllPages(solYear, solMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()))
                ;
    }

    // ------------------------------------------------------------------------------------------------- instance fields
    @Autowired
    @SpcdeInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
