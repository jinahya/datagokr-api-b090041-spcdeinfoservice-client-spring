package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HhMmLocaTimeAdapter extends FormattedTemporalAdapter<LocalTime> {

    public static final DateTimeFormatter LOCAL_TIME_HHMM_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    public HhMmLocaTimeAdapter() {
        super(LocalTime.class, LOCAL_TIME_HHMM_FORMATTER, LocalTime::from);
    }
}
