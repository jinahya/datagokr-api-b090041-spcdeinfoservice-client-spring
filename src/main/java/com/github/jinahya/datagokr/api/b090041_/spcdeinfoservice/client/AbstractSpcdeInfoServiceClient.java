package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.format.DateTimeFormatter;

/**
 * An abstract parent class for client classes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public abstract class AbstractSpcdeInfoServiceClient {

    // ------------------------------------------------------------------------------------------------ urls \ constants
    static final String BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService";

    /**
     * The base url for development environment. The value is {@value}.
     */
    public static final String BASE_URL_DEVELOPMENT = BASE_URL;

    /**
     * The value url for production environment. The value is {@value}.
     */
    public static final String BASE_URL_PRODUCTION = BASE_URL_DEVELOPMENT;

    // --------------------------------------------------------------------------------------------- methods \ constants

    /**
     * A path segment for {@code /get24DivisionsInfo}. The value is {@value}.
     */
    public static final String PATH_SEGMENT_GET_24_DIVISIONS_INFO = "get24DivisionsInfo";

    /**
     * A path segment for {@code getAnniversaryInfo}. The value is {@value}.
     */
    public static final String PATH_SEGMENT_GET_ANNIVERSARY_INFO = "getAnniversaryInfo";

    /**
     * A path segment for {@code /getHolidayInfo}. The value is {@value}.
     */
    public static final String PATH_SEGMENT_GET_HOLI_DE_INFO = "getHoliDeInfo";

    /**
     * A path segment for {@code /getRestDeInfo} uri. The value is {@value}.
     */
    public static final String PATH_SEGMENT_GET_REST_DE_INFO = "getRestDeInfo";

    /**
     * A path segment for {@code /getSundryDayInfo}. The value is {@value}.
     */
    public static final String PATH_SEGMENT_GET_SUNDRY_DAY_INFO = "getSundryDayInfo";

    // ------------------------------------------------------------------------------------------ parameters \ constants

    /**
     * A query parameter name for assigned service key. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_SERVICE_KEY = "ServiceKey";

    /**
     * A query parameter name for a solar year. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_SOL_YEAR = "solYear";

    /**
     * A query parameter name for a solar month. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_SOL_MONTH = "solMonth";

    /**
     * A query parameter name for the page number. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_PAGE_NO = "pageNo";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The formatter for {@link #QUERY_PARAM_NAME_SOL_MONTH}.
     */
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");

    // -------------------------------------------------------------------------------------------- injection qualifiers

    /**
     * An injection qualifier for the {@code serviceKey} provided by service provider.
     *
     * @see AbstractSpcdeInfoServiceClient#QUERY_PARAM_NAME_SERVICE_KEY
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SpcdeInfoServiceServiceKey {

    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    protected AbstractSpcdeInfoServiceClient() {
        super();
    }

    // ------------------------------------------------------------------------------------------------- instance fields

    /**
     * A value for {@link #QUERY_PARAM_NAME_SERVICE_KEY}.
     */
    @SpcdeInfoServiceServiceKey
    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(value = AccessLevel.PROTECTED)
    private String serviceKey;
}
