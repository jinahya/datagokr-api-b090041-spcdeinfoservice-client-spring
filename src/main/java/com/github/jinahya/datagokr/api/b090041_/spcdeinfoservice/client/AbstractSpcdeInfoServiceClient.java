package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

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
     * A query parameter name for a solar day of month. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_SOL_DAY = "solDay";

    /**
     * A query parameter name for a lunar year. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_LUN_YEAR = "lunYear";

    /**
     * A query parameter name for a lunar month. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_LUN_MONTH = "lunMonth";

    /**
     * A query parameter name for a lunar day of month. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_LUN_DAY = "lunDay";

    /**
     * A query parameter name for the starting solar year. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_FROM_SOL_YEAR = "fromSolYear";

    /**
     * A query parameter name for the ending solar year. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_TO_SOL_YEAR = "toSolYear";

    /**
     * A query parameter name for the flag of leap month. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_LEAP_MONTH = "leapMonth";

//    static String queryParamValueLeapMonth(final boolean leapMonth) {
//        return leapMonth ? Item.LEAP : Item.NORMAL;
//    }

    /**
     * A query parameter name for the page number. The value is {@value}.
     */
    public static final String QUERY_PARAM_NAME_PAGE_NO = "pageNo";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The formatter for {@code solYear} and {@code lunYear}.
     */
    public static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("uuuu");

    /**
     * The formatter for {@code solMonth} and {@code lunMonth}.
     */
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");

    /**
     * The formatter for {@code solMonth} and {@code lunMonth}.
     */
    public static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    /**
     * A formatter for formatting {@link java.time.temporal.ChronoField#DAY_OF_WEEK} with {@link Locale#KOREAN KOREAN}
     * locale.
     */
    static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

    /**
     * Formats specified day-of-month value as {@code %02d}.
     *
     * @param dayOfMonth the value to format.
     * @return a formatted string.
     */
    public static String formatDay(final int dayOfMonth) {
        return format02d(dayOfMonth);
    }

    /**
     * Formats specified as {@code %02d}.
     *
     * @param parsed the value to format.
     * @return a formatted string.
     */
    static String format02d(final Integer parsed) {
        return ofNullable(parsed).map(v -> format("%1$02d", v)).orElse(null);
    }

    // TODO: Remove, unused.
    @Deprecated
    static Integer parse02d(final String formatted) {
        return ofNullable(formatted).map(Integer::parseInt).orElse(null);
    }

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
