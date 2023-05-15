/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records;

import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfoType;

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
public class WebsiteInfoTypeRecord extends UpdatableRecordImpl<WebsiteInfoTypeRecord>
    implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>WEBSITE_INFO_TYPE.ID</code>.
     */
    public void setId(@Nullable Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>WEBSITE_INFO_TYPE.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>WEBSITE_INFO_TYPE.NAME</code>.
     */
    public void setName(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>WEBSITE_INFO_TYPE.NAME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 255)
    @NotNull
    public String getName() {
        return (String) get(1);
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
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return WebsiteInfoType.WEBSITE_INFO_TYPE.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return WebsiteInfoType.WEBSITE_INFO_TYPE.NAME;
    }

    @Override
    @Nullable
    public Integer component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getName();
    }

    @Override
    @Nullable
    public Integer value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getName();
    }

    @Override
    @NotNull
    public WebsiteInfoTypeRecord value1(@Nullable Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public WebsiteInfoTypeRecord value2(@NotNull String value) {
        setName(value);
        return this;
    }

    @Override
    @NotNull
    public WebsiteInfoTypeRecord values(@Nullable Integer value1, @NotNull String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WebsiteInfoTypeRecord
     */
    public WebsiteInfoTypeRecord() {
        super(WebsiteInfoType.WEBSITE_INFO_TYPE);
    }

    /**
     * Create a detached, initialised WebsiteInfoTypeRecord
     */
    @ConstructorProperties({"id", "name"})
    public WebsiteInfoTypeRecord(@Nullable Integer id, @NotNull String name) {
        super(WebsiteInfoType.WEBSITE_INFO_TYPE);

        setId(id);
        setName(name);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised WebsiteInfoTypeRecord
     */
    public WebsiteInfoTypeRecord(ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos.WebsiteInfoType value) {
        super(WebsiteInfoType.WEBSITE_INFO_TYPE);

        if (value != null) {
            setId(value.getId());
            setName(value.getName());
            resetChangedOnNotNull();
        }
    }
}
