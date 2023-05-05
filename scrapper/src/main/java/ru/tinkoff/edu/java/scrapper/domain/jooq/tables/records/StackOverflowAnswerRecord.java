/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records;

import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.StackOverflowAnswer;

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
public class StackOverflowAnswerRecord extends UpdatableRecordImpl<StackOverflowAnswerRecord>
    implements Record4<Integer, String, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>STACK_OVERFLOW_ANSWER.ID</code>.
     */
    public void setId(@NotNull Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STACK_OVERFLOW_ANSWER.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STACK_OVERFLOW_ANSWER.USER_NAME</code>.
     */
    public void setUserName(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STACK_OVERFLOW_ANSWER.USER_NAME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 40)
    @NotNull
    public String getUserName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STACK_OVERFLOW_ANSWER.LAST_EDIT_DATE_TIME</code>.
     */
    public void setLastEditDateTime(@NotNull LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>STACK_OVERFLOW_ANSWER.LAST_EDIT_DATE_TIME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getLastEditDateTime() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>STACK_OVERFLOW_ANSWER.WEBSITE_INFO_ID</code>.
     */
    public void setWebsiteInfoId(@NotNull Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>STACK_OVERFLOW_ANSWER.WEBSITE_INFO_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getWebsiteInfoId() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record2<Integer, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Integer, String, LocalDateTime, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row4<Integer, String, LocalDateTime, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return StackOverflowAnswer.STACK_OVERFLOW_ANSWER.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return StackOverflowAnswer.STACK_OVERFLOW_ANSWER.USER_NAME;
    }

    @Override
    @NotNull
    public Field<LocalDateTime> field3() {
        return StackOverflowAnswer.STACK_OVERFLOW_ANSWER.LAST_EDIT_DATE_TIME;
    }

    @Override
    @NotNull
    public Field<Integer> field4() {
        return StackOverflowAnswer.STACK_OVERFLOW_ANSWER.WEBSITE_INFO_ID;
    }

    @Override
    @NotNull
    public Integer component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUserName();
    }

    @Override
    @NotNull
    public LocalDateTime component3() {
        return getLastEditDateTime();
    }

    @Override
    @NotNull
    public Integer component4() {
        return getWebsiteInfoId();
    }

    @Override
    @NotNull
    public Integer value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUserName();
    }

    @Override
    @NotNull
    public LocalDateTime value3() {
        return getLastEditDateTime();
    }

    @Override
    @NotNull
    public Integer value4() {
        return getWebsiteInfoId();
    }

    @Override
    @NotNull
    public StackOverflowAnswerRecord value1(@NotNull Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public StackOverflowAnswerRecord value2(@NotNull String value) {
        setUserName(value);
        return this;
    }

    @Override
    @NotNull
    public StackOverflowAnswerRecord value3(@NotNull LocalDateTime value) {
        setLastEditDateTime(value);
        return this;
    }

    @Override
    @NotNull
    public StackOverflowAnswerRecord value4(@NotNull Integer value) {
        setWebsiteInfoId(value);
        return this;
    }

    @Override
    @NotNull
    public StackOverflowAnswerRecord values(
        @NotNull Integer value1,
        @NotNull String value2,
        @NotNull LocalDateTime value3,
        @NotNull Integer value4
    ) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached StackOverflowAnswerRecord
     */
    public StackOverflowAnswerRecord() {
        super(StackOverflowAnswer.STACK_OVERFLOW_ANSWER);
    }

    /**
     * Create a detached, initialised StackOverflowAnswerRecord
     */
    @ConstructorProperties({"id", "userName", "lastEditDateTime", "websiteInfoId"})
    public StackOverflowAnswerRecord(
        @NotNull Integer id,
        @NotNull String userName,
        @NotNull LocalDateTime lastEditDateTime,
        @NotNull Integer websiteInfoId
    ) {
        super(StackOverflowAnswer.STACK_OVERFLOW_ANSWER);

        setId(id);
        setUserName(userName);
        setLastEditDateTime(lastEditDateTime);
        setWebsiteInfoId(websiteInfoId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised StackOverflowAnswerRecord
     */
    public StackOverflowAnswerRecord(ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos.StackOverflowAnswer value) {
        super(StackOverflowAnswer.STACK_OVERFLOW_ANSWER);

        if (value != null) {
            setId(value.getId());
            setUserName(value.getUserName());
            setLastEditDateTime(value.getLastEditDateTime());
            setWebsiteInfoId(value.getWebsiteInfoId());
            resetChangedOnNotNull();
        }
    }
}
