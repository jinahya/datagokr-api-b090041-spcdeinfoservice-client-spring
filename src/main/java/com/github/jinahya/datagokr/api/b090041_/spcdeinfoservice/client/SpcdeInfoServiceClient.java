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
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Responses.requireResultSuccessful;
import static java.util.Optional.ofNullable;
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
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @Valid @NotNull Response get24DivisionsInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_24_DIVISIONS_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear);
        ofNullable(solMonth)
                .map(MONTH_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses of all pages from {@code /get24DivisionsInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of responses.
     * @see #get24DivisionsInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> get24DivisionsInfoForAllPages(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
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
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of items.
     * @see #get24DivisionsInfoForAllPages(Year, Month)
     */
    public @NotEmpty List<@Valid @NotNull Item> get24DivisionsInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
        return get24DivisionsInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // --------------------------------------------------------------------------------------------- /getAnniversaryInfo

    /**
     * Retrieves a response from {@code /getAnniversaryInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @Valid @NotNull Response getAnniversaryInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_ANNIVERSARY_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear);
        ofNullable(solMonth)
                .map(MONTH_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
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
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of responses.
     * @see #getAnniversaryInfoForAllPages(Year, Month)
     */
    public @NotNull List<@Valid @NotNull Response> getAnniversaryInfoForAllPages(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
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
     * Reads all items from {@code /getAnniversaryInfo} for specified year and month.
     *
     * @param solYear  the year for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth the month for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of items.
     * @see #getAnniversaryInfoForAllPages(Year, Month)
     */
    public @NotEmpty List<@Valid @NotNull Item> getAnniversaryInfo(@NotNull final Year solYear,
                                                                   @Nullable final Month solMonth) {
        return getAnniversaryInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // -------------------------------------------------------------------------------------------------- /getHoliDeInfo

    /**
     * Retrieves a response from {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @Valid @NotNull Response getHoliDeInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_HOLI_DE_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear);
        ofNullable(solMonth)
                .map(MONTH_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses of all pages from {@code /getHoliDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of responses.
     * @see #getHoliDeInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getHoliDeInfoForAllPages(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
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
     * Reads all items from {@code /getHoliDeInfo} for specified year and month.
     *
     * @param solYear  the year for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth the month for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of items.
     * @see #getHoliDeInfo(Year, Month, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getHoliDeInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
        return getHoliDeInfoForAllPages(solYear, solMonth)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // -------------------------------------------------------------------------------------------------- /getRestDeInfo

    /**
     * Retrieves a response from {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @NotNull Response getRestDeInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_REST_DE_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear);
        ofNullable(solMonth)
                .map(MONTH_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_MONTH, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses of all pages from {@code /getRestDeInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of responses.
     * @see #getRestDeInfo(Year, Month, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getRestDeInfoForAllPages(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
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
     * Retrieves all items from {@code /getRestDeInfo} for specified year and month.
     *
     * @param solYear  the year for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth the month for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @return a list of items.
     * @see #getRestDeInfoForAllPages(Year, Month)
     */
    public @NotEmpty List<@Valid @NotNull Item> getRestDeInfo(
            @NotNull final Year solYear, @Nullable final Month solMonth) {
        return getRestDeInfoForAllPages(solYear, solMonth)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // ----------------------------------------------------------------------------------------------- /getSundryDayInfo

    /**
     * Retrieves a response from {@code /getSundryDayInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
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
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}; {@code null} for a whole year.
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
