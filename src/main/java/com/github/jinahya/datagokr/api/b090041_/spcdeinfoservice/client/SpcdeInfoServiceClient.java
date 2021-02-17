package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;
import java.net.URI;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Responses.requireResultSuccessful;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

/**
 * A client implementation uses an instance of {@link RestTemplate}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see SpcdeInfoServiceReactiveClient
 */
@Lazy
@Component
@Slf4j
public class SpcdeInfoServiceClient extends AbstractSpcdeInfoServiceClient {

    /**
     * An injection qualifier for an instance of {@link RestTemplate}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SpcdeInfoServiceRestTemplate {

    }

    /**
     * An injection qualifier for injecting a custom {@code root-uri} in non-spring-boot environments.
     *
     * @deprecated Just for non-spring-boot environments.
     */
    @Deprecated
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SpcdeInfoServiceRestTemplateRootUri {

    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the body of specified response entity while validating it.
     *
     * @param responseEntity the response entity.
     * @return the body of the {@code responseEntity}.
     */
    protected static @NotNull Response unwrap(@NotNull final ResponseEntity<Response> responseEntity) {
        Objects.requireNonNull(responseEntity, "responseEntity is null");
        final HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RestClientException("unsuccessful response status: " + statusCode);
        }
        final Response response = responseEntity.getBody();
        if (response == null) {
            throw new RestClientException("no entity body received");
        }
        return requireResultSuccessful(response, h -> new RestClientException("unsuccessful result: " + h));
    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public SpcdeInfoServiceClient() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    private void onPostConstruct() {
        rootUri = restTemplate.getUriTemplateHandler().expand("/");
        if (restTemplateRootUri != null) {
            log.info("custom root uri specified: {}", restTemplateRootUri);
            rootUri = URI.create(restTemplateRootUri);
        }
    }

    // --------------------------------------------------------------------------------------------- /get24DivisionsInfo

    /**
     * Retrieves a response from {@code /get24DivisionsInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response get24DivisionsInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_24_DIVISIONS_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /get24DivisionsInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @return a list of responses.
     * @see #get24DivisionsInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> get24DivisionsInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth) {
        final List<Response> result = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = get24DivisionsInfo(solYear, solMonth, pageNo);
            result.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return result;
    }

    /**
     * Reads all items from {@code /get24DivisionsInfo} for specified month in solar.
     *
     * @param solarYearMonth the solar month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                       #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of items.
     * @see #get24DivisionsInfo(Year, Month, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> get24DivisionsInfo(@NotNull final YearMonth solarYearMonth) {
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        return get24DivisionsInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified solar year.
     *
     * @param year       the solar year.
     * @param executor   an executor for concurrently execute {@link #get24DivisionsInfo(YearMonth)} for each {@link
     *                   Month} in {@code year}.
     * @param collection a collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #get24DivisionsInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T get24DivisionsInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> get24DivisionsInfo(v), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll)
        ;
        return collection;
    }


    // --------------------------------------------------------------------------------------------- /getAnniversaryInfo

    /**
     * Retrieves a response from {@code /getAnniversaryInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response getAnniversaryInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_ANNIVERSARY_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getAnniversaryInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @return a list of responses.
     * @see #getAnniversaryInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getAnniversaryInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth) {
        final List<Response> result = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getAnniversaryInfo(solYear, solMonth, pageNo);
            result.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return result;
    }

    /**
     * Reads all items from {@code /getAnniversaryInfo} for specified month in solar.
     *
     * @param solarYearMonth the solar month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                       #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of items.
     * @see #getAnniversaryInfo(Year, Month, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getAnniversaryInfo(@NotNull final YearMonth solarYearMonth) {
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        return getAnniversaryInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified solar year.
     *
     * @param year       the solar year.
     * @param executor   an executor for concurrently execute {@link #getAnniversaryInfo(YearMonth)} for each {@link
     *                   Month} in {@code year}.
     * @param collection a collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getAnniversaryInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getAnniversaryInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> getAnniversaryInfo(v), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll)
        ;
        return collection;
    }


    // -------------------------------------------------------------------------------------------------- /getHoliDeInfo

    /**
     * Retrieves a response from {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response getHoliDeInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_HOLI_DE_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @return a list of responses.
     * @see #getHoliDeInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getHoliDeInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth) {
        final List<Response> result = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getHoliDeInfo(solYear, solMonth, pageNo);
            result.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return result;
    }

    /**
     * Reads all items from {@code /getHoliDeInfo} for specified month in solar.
     *
     * @param solarYearMonth the solar month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                       #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of items.
     * @see #getHoliDeInfo(Year, Month, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getHoliDeInfo(@NotNull final YearMonth solarYearMonth) {
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        return getHoliDeInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified solar year.
     *
     * @param year       the solar year.
     * @param executor   an executor for concurrently execute {@link #getHoliDeInfo(YearMonth)} for each {@link Month}
     *                   in {@code year}.
     * @param collection a collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getHoliDeInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getHoliDeInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> getHoliDeInfo(v), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll)
        ;
        return collection;
    }


    // -------------------------------------------------------------------------------------------------- /getRestDeInfo

    /**
     * Retrieves a response from {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     * @see #getRestDeInfoForAllPages(Year, Month)
     * @see #getRestDeInfo(YearMonth)
     * @see #getRestDeInfo(Year, Executor, Collection)
     */
    public @NotNull Response getRestDeInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_REST_DE_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear)
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @return a list of responses.
     * @see #getRestDeInfo(Year, Month, Integer)
     * @see #getRestDeInfo(YearMonth)
     * @see #getRestDeInfo(Year, Executor, Collection)
     */
    public @NotNull List<@Valid @NotNull Response> getRestDeInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth) {
        final List<Response> responses = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getRestDeInfo(solYear, solMonth, pageNo);
            responses.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return responses;
    }

    /**
     * Retrieves all items from {@code /getRestDeInfo} with parameters derived from specified month in lunar calendar.
     *
     * @param yearMonth the year-month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                  #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of items.
     * @see #getRestDeInfo(Year, Month, Integer)
     * @see #getRestDeInfoForAllPages(Year, Month)
     * @see #getRestDeInfo(Year, Executor, Collection)
     */
    public @NotEmpty List<@Valid @NotNull Item> getRestDeInfo(@NotNull final YearMonth yearMonth) {
        final Year solYear = Year.from(yearMonth);
        final Month solMonth = Month.from(yearMonth);
        return getRestDeInfoForAllPages(solYear, solMonth)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified year and adds them to specified collection.
     *
     * @param year       the lunar year.
     * @param executor   an executor for concurrently executing {@link #getRestDeInfo(YearMonth)} for each {@link Month}
     *                   in {@code year}.
     * @param collection the collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getRestDeInfoForAllPages(Year, Month)
     * @see #getRestDeInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getRestDeInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> getRestDeInfo(v), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll)
        ;
        return collection;
    }

    // ----------------------------------------------------------------------------------------------- /getSundryDayInfo

    /**
     * Retrieves a response from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response getSundryDayInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth, @Positive @Nullable Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SUNDRY_DAY_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear);
        ofNullable(solMonth)
                .map(MONTH_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode()
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @return a list of responses.
     */
    public @NotNull List<@Valid @NotNull Response> getSundryDayInfoForAllPages(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
        final List<Response> responses = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getSundryDayInfo(solYear, solMonth, pageNo);
            responses.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return responses;
    }

    /**
     * Reads all items from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of items.
     * @see #getSundryDayInfoForAllPages(Year, Month)
     */
    public @NotNull List<@Valid @NotNull Item> getSundryDayInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
        return getSundryDayInfoForAllPages(solYear, solMonth)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param year a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @return a list of items.
     * @see #getSundryDayInfoForAllPages(Year, Month)
     */
    public @NotNull List<@Valid @NotNull Item> getSundryDayInfo(@NotNull final Year year) {
        return getSundryDayInfoForAllPages(year, null)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a uri builder built from the {@code rootUri}.
     *
     * @return a uri builder built from the {@code rootUri}.
     */
    protected UriComponentsBuilder uriBuilderFromRootUri() {
        return UriComponentsBuilder.fromUri(rootUri);
    }

    // ------------------------------------------------------------------------------------------------- instance fields
    @SpcdeInfoServiceRestTemplate
    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private RestTemplate restTemplate;

    /**
     * A custom root uri value for non-spring-boot environments.
     */
    @SpcdeInfoServiceRestTemplateRootUri
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String restTemplateRootUri;

    /**
     * The root uri expanded with {@code '/'} from {@code restTemplate.uriTemplateHandler}.
     */
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private URI rootUri;
}
