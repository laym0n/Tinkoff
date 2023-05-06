/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfo;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WebsiteInfoRecord extends UpdatableRecordImpl<WebsiteInfoRecord>
    implements Record3<Integer, Integer, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>WEBSITE_INFO.ID</code>.
     */
    public void setId(@Nullable Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>WEBSITE_INFO.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>WEBSITE_INFO.TYPE_ID</code>.
     */
    public void setTypeId(@NotNull Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>WEBSITE_INFO.TYPE_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getTypeId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>WEBSITE_INFO.LAST_UPDATE_DATE_TIME</code>.
     */
    public void setLastUpdateDateTime(@Nullable LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>WEBSITE_INFO.LAST_UPDATE_DATE_TIME</code>.
     */
    @Nullable
    public LocalDateTime getLastUpdateDateTime() {
        return (LocalDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Integer, Integer, LocalDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Integer, Integer, LocalDateTime> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return WebsiteInfo.WEBSITE_INFO.ID;
    }

    @Override
    @NotNull
    public Field<Integer> field2() {
        return WebsiteInfo.WEBSITE_INFO.TYPE_ID;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field3() {
        return WebsiteInfo.WEBSITE_INFO.LAST_UPDATE_DATE_TIME;
    }

    @Override
    @Nullable
    public Integer component1() {
        return getId();
    }

    @Override
    @NotNull
    public Integer component2() {
        return getTypeId();
    }

    @Override
    @Nullable
    public LocalDateTime component3() {
        return getLastUpdateDateTime();
    }

    @Override
    @Nullable
    public Integer value1() {
        return getId();
    }

    @Override
    @NotNull
    public Integer value2() {
        return getTypeId();
    }

    @Override
    @Nullable
    public LocalDateTime value3() {
        return getLastUpdateDateTime();
    }

    @Override
    @NotNull
    public WebsiteInfoRecord value1(@Nullable Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public WebsiteInfoRecord value2(@NotNull Integer value) {
        setTypeId(value);
        return this;
    }

    @Override
    @NotNull
    public WebsiteInfoRecord value3(@Nullable LocalDateTime value) {
        setLastUpdateDateTime(value);
        return this;
    }

    @Override
    @NotNull
    public WebsiteInfoRecord values(@Nullable Integer value1, @NotNull Integer value2, @Nullable LocalDateTime value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WebsiteInfoRecord
     */
    public WebsiteInfoRecord() {
        super(WebsiteInfo.WEBSITE_INFO);
    }

    /**
     * Create a detached, initialised WebsiteInfoRecord
     */
    @ConstructorProperties({"id", "typeId", "lastUpdateDateTime"})
    public WebsiteInfoRecord(
        @Nullable Integer id,
        @NotNull Integer typeId,
        @Nullable LocalDateTime lastUpdateDateTime
    ) {
        super(WebsiteInfo.WEBSITE_INFO);

        setId(id);
        setTypeId(typeId);
        setLastUpdateDateTime(lastUpdateDateTime);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised WebsiteInfoRecord
     */
    public WebsiteInfoRecord(ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos.WebsiteInfo value) {
        super(WebsiteInfo.WEBSITE_INFO);

        if (value != null) {
            setId(value.getId());
            setTypeId(value.getTypeId());
            setLastUpdateDateTime(value.getLastUpdateDateTime());
            resetChangedOnNotNull();
        }
    }
}