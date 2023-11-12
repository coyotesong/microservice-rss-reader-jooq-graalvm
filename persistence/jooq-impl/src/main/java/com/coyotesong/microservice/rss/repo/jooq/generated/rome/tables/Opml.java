/*
 * This file is generated by jOOQ.
 */
package com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables;


import com.coyotesong.microservice.rss.repo.jooq.bindings.LocalDateTimeToInstantBinding;
import com.coyotesong.microservice.rss.repo.jooq.bindings.VarcharToUrlBinding;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.Keys;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.Rome;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.records.OpmlRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.Instant;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Opml extends TableImpl<OpmlRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>rome.opml</code>
     */
    public static final Opml OPML = new Opml();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OpmlRecord> getRecordType() {
        return OpmlRecord.class;
    }

    /**
     * The column <code>rome.opml.id</code>.
     */
    public final TableField<OpmlRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>rome.opml.timestamp</code>.
     */
    public final TableField<OpmlRecord, Instant> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field(DSL.raw("(now())::timestamp(6) without time zone"), SQLDataType.LOCALDATETIME)), this, "", new LocalDateTimeToInstantBinding());

    /**
     * The column <code>rome.opml.url</code>.
     */
    public final TableField<OpmlRecord, java.net.URL> URL = createField(DSL.name("url"), SQLDataType.CLOB, this, "", new VarcharToUrlBinding());

    /**
     * The column <code>rome.opml.created_date</code>.
     */
    public final TableField<OpmlRecord, Instant> CREATED_DATE = createField(DSL.name("created_date"), SQLDataType.LOCALDATETIME(0), this, "", new LocalDateTimeToInstantBinding());

    /**
     * The column <code>rome.opml.docs</code>.
     */
    public final TableField<OpmlRecord, String> DOCS = createField(DSL.name("docs"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.encoding</code>.
     */
    public final TableField<OpmlRecord, String> ENCODING = createField(DSL.name("encoding"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.feed_type</code>.
     */
    public final TableField<OpmlRecord, String> FEED_TYPE = createField(DSL.name("feed_type"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.modified_date</code>.
     */
    public final TableField<OpmlRecord, Instant> MODIFIED_DATE = createField(DSL.name("modified_date"), SQLDataType.LOCALDATETIME(0), this, "", new LocalDateTimeToInstantBinding());

    /**
     * The column <code>rome.opml.owner_email</code>.
     */
    public final TableField<OpmlRecord, String> OWNER_EMAIL = createField(DSL.name("owner_email"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.owner_name</code>.
     */
    public final TableField<OpmlRecord, String> OWNER_NAME = createField(DSL.name("owner_name"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.owner_id</code>.
     */
    public final TableField<OpmlRecord, String> OWNER_ID = createField(DSL.name("owner_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.stylesheet</code>.
     */
    public final TableField<OpmlRecord, String> STYLESHEET = createField(DSL.name("stylesheet"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rome.opml.title</code>.
     */
    public final TableField<OpmlRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB, this, "");

    private Opml(Name alias, Table<OpmlRecord> aliased) {
        this(alias, aliased, null);
    }

    private Opml(Name alias, Table<OpmlRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>rome.opml</code> table reference
     */
    public Opml(String alias) {
        this(DSL.name(alias), OPML);
    }

    /**
     * Create an aliased <code>rome.opml</code> table reference
     */
    public Opml(Name alias) {
        this(alias, OPML);
    }

    /**
     * Create a <code>rome.opml</code> table reference
     */
    public Opml() {
        this(DSL.name("opml"), null);
    }

    public <O extends Record> Opml(Table<O> child, ForeignKey<O, OpmlRecord> key) {
        super(child, key, OPML);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Rome.ROME;
    }

    @Override
    public Identity<OpmlRecord, Long> getIdentity() {
        return (Identity<OpmlRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<OpmlRecord> getPrimaryKey() {
        return Keys.ROME_OPML_PKEY;
    }

    @Override
    public Opml as(String alias) {
        return new Opml(DSL.name(alias), this);
    }

    @Override
    public Opml as(Name alias) {
        return new Opml(alias, this);
    }

    @Override
    public Opml as(Table<?> alias) {
        return new Opml(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Opml rename(String name) {
        return new Opml(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Opml rename(Name name) {
        return new Opml(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Opml rename(Table<?> name) {
        return new Opml(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Instant, java.net.URL, Instant, String, String, String, Instant, String, String, String, String, String> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function13<? super Long, ? super Instant, ? super java.net.URL, ? super Instant, ? super String, ? super String, ? super String, ? super Instant, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function13<? super Long, ? super Instant, ? super java.net.URL, ? super Instant, ? super String, ? super String, ? super String, ? super Instant, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
