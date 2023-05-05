/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq.DefaultSchema;
import ru.tinkoff.edu.java.scrapper.domain.jooq.Keys;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.GithubCommitRecord;

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
public class GithubCommit extends TableImpl<GithubCommitRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>GITHUB_COMMIT</code>
     */
    public static final GithubCommit GITHUB_COMMIT = new GithubCommit();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<GithubCommitRecord> getRecordType() {
        return GithubCommitRecord.class;
    }

    /**
     * The column <code>GITHUB_COMMIT.SHA</code>.
     */
    public final TableField<GithubCommitRecord, String> SHA =
        createField(DSL.name("SHA"), SQLDataType.VARCHAR(40).nullable(false), this, "");

    /**
     * The column <code>GITHUB_COMMIT.WEBSITE_INFO_ID</code>.
     */
    public final TableField<GithubCommitRecord, Integer> WEBSITE_INFO_ID =
        createField(DSL.name("WEBSITE_INFO_ID"), SQLDataType.INTEGER.nullable(false), this, "");

    private GithubCommit(Name alias, Table<GithubCommitRecord> aliased) {
        this(alias, aliased, null);
    }

    private GithubCommit(Name alias, Table<GithubCommitRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>GITHUB_COMMIT</code> table reference
     */
    public GithubCommit(String alias) {
        this(DSL.name(alias), GITHUB_COMMIT);
    }

    /**
     * Create an aliased <code>GITHUB_COMMIT</code> table reference
     */
    public GithubCommit(Name alias) {
        this(alias, GITHUB_COMMIT);
    }

    /**
     * Create a <code>GITHUB_COMMIT</code> table reference
     */
    public GithubCommit() {
        this(DSL.name("GITHUB_COMMIT"), null);
    }

    public <O extends Record> GithubCommit(Table<O> child, ForeignKey<O, GithubCommitRecord> key) {
        super(child, key, GITHUB_COMMIT);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<GithubCommitRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    @Override
    @NotNull
    public List<ForeignKey<GithubCommitRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_2B);
    }

    private transient GithubInfo _githubInfo;

    /**
     * Get the implicit join path to the <code>PUBLIC.GITHUB_INFO</code> table.
     */
    public GithubInfo githubInfo() {
        if (_githubInfo == null) {
            _githubInfo = new GithubInfo(this, Keys.CONSTRAINT_2B);
        }

        return _githubInfo;
    }

    @Override
    @NotNull
    public GithubCommit as(String alias) {
        return new GithubCommit(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public GithubCommit as(Name alias) {
        return new GithubCommit(alias, this);
    }

    @Override
    @NotNull
    public GithubCommit as(Table<?> alias) {
        return new GithubCommit(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubCommit rename(String name) {
        return new GithubCommit(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubCommit rename(Name name) {
        return new GithubCommit(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubCommit rename(Table<?> name) {
        return new GithubCommit(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<String, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super String, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super String, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
