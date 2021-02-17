package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.data.jpa.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(access = AccessLevel.PACKAGE)
@Slf4j
public class ItemId implements Serializable {

    @Override
    public String toString() {
        return super.toString() + '{'
               + "locdate=" + locdate
               + ",seq=" + seq
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final ItemId that = (ItemId) obj;
        return Objects.equals(locdate, that.locdate)
               && Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locdate, seq);
    }

    private LocalDate locdate;

    private Integer seq;
}
