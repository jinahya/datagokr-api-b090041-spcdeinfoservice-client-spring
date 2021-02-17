package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UuuuMmDdLocalDateAdapter extends FormattedTemporalAdapter<LocalDate> {

    public static final DateTimeFormatter LOCAL_DATE_UUUUMMDD_FORMATTER = DateTimeFormatter.ofPattern("uuuuMMdd");

    public UuuuMmDdLocalDateAdapter() {
        super(LocalDate.class, LOCAL_DATE_UUUUMMDD_FORMATTER, LocalDate::from);
    }
}
