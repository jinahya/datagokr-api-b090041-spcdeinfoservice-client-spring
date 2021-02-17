package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import static java.util.Objects.requireNonNull;

@XmlType
@XmlEnum
public enum DateKind {

    /**
     * 국경일.
     */
    @XmlEnumValue("01")
    C01("01"),

    /**
     * 기념일.
     */
    @XmlEnumValue("02")
    C02("02"),

    /**
     * 24절기.
     */
    @XmlEnumValue("03")
    C03("03"),

    /**
     * 잡절.
     */
    @XmlEnumValue("04")
    C04("04");

    public static DateKind of(final String value) {
        requireNonNull(value, "value is null");
        for (final DateKind v : values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("no constant for " + value);
    }

    DateKind(final String value) {
        this.value = value;
    }

    @JsonValue
    private final String value;
}
