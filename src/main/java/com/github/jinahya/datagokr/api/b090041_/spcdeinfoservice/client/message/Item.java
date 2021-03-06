package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter.HhMmLocaTimeAdapter;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter.UuuuMmDdLocalDateAdapter;
import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter.YnBooleanAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.comparing;

/**
 * A class for binding {@code /:response/:body/:item} part.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@Slf4j
public class Item implements Serializable {

    private static final long serialVersionUID = 142085981070786610L;

    // ----------------------------------------------------------------------------------------------------- comparators

    /**
     * The comparator compares items by {@code locDate}.
     */
    public static final Comparator<Item> COMPARING_LOC_DATE = comparing(Item::getLocdate);

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public Item() {
        super();
    }

    // -------------------------------------------------------------------------------- overridden from java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{'
               + "locdate=" + locdate
               + ",seq=" + seq
               + ",dateKind=" + dateKind
               + ",isHoliday=" + isHoliday
               + ",dateName=" + dateName
               + ",kst=" + kst
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Item that = (Item) obj;
        return Objects.equals(locdate, that.locdate)
               && Objects.equals(seq, that.seq)
               && Objects.equals(dateKind, that.dateKind)
               && Objects.equals(isHoliday, that.isHoliday)
               && Objects.equals(dateName, that.dateName)
               && Objects.equals(kst, that.kst)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                locdate,
                seq,
                dateKind,
                isHoliday,
                dateName,
                kst
        );
    }

    // ------------------------------------------------------------------------------------------------------------ JAXB
    void beforeUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        // has nothing to do.
    }

    void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        // has nothing to do.
    }

    // --------------------------------------------------------------------------------------------------------- locDate

    // ------------------------------------------------------------------------------------------------------------- seq

    // -------------------------------------------------------------------------------------------------------- dateKind

    // ------------------------------------------------------------------------------------------------------- isHoliday

    // -------------------------------------------------------------------------------------------------------- dateName

    // ------------------------------------------------------------------------------------------------------------- kst

    // ------------------------------------------------------------------------------------------------ apiDiscriminator

    // ------------------------------------------------------------------------------------------------- instance fields
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty(required = true)
    @Id
    @NotNull
    @XmlJavaTypeAdapter(UuuuMmDdLocalDateAdapter.class)
    @XmlSchemaType(name = "unsignedShort")
    @XmlElement(required = true)
    private LocalDate locdate;

    @JsonProperty(required = true)
    @Id
    @Basic(optional = false)
    @Column(name = "seq", nullable = false)
    @Positive
    @NotNull
    @XmlSchemaType(name = "positiveInteger")
    @XmlElement(required = true)
    private Integer seq;

    @JsonProperty(required = true)
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "date_kind", nullable = false)
    @NotNull
    @XmlSchemaType(name = "enumeration")
    @XmlElement(required = true)
    private DateKind dateKind;

    @JsonProperty(required = true)
    @Basic(optional = false)
    @Column(name = "is_holiday", nullable = false)
    @NotNull
    @XmlJavaTypeAdapter(YnBooleanAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Boolean isHoliday;

    @JsonProperty(required = true)
    @Basic(optional = false)
    @Column(name = "date_name", nullable = false)
    @NotBlank
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String dateName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonProperty(required = true)
    @Basic
    @Column(name = "kst")
    @Nullable
    @XmlJavaTypeAdapter(HhMmLocaTimeAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private LocalTime kst;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @XmlTransient
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "api_discriminator", nullable = false)
    private ApiDiscriminator apiDiscriminator;
}
