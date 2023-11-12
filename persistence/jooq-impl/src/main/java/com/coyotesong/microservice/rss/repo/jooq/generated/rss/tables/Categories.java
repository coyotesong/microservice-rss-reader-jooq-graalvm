/*
 * This file is generated by jOOQ.
 */
package com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables;


import com.coyotesong.microservice.rss.repo.jooq.generated.rss.Keys;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.Rss;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.CategoriesRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Categories extends TableImpl<CategoriesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>rss.categories</code>
     */
    public static final Categories CATEGORIES = new Categories();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CategoriesRecord> getRecordType() {
        return CategoriesRecord.class;
    }

    /**
     * The column <code>rss.categories.id</code>.
     */
    public final TableField<CategoriesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>rss.categories.name</code>.
     */
    public final TableField<CategoriesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>rss.categories.label</code>.
     */
    public final TableField<CategoriesRecord, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>rss.categories.taxonomy_uri</code>.
     */
    public final TableField<CategoriesRecord, String> TAXONOMY_URI = createField(DSL.name("taxonomy_uri"), SQLDataType.CLOB, this, "");

    private Categories(Name alias, Table<CategoriesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Categories(Name alias, Table<CategoriesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>rss.categories</code> table reference
     */
    public Categories(String alias) {
        this(DSL.name(alias), CATEGORIES);
    }

    /**
     * Create an aliased <code>rss.categories</code> table reference
     */
    public Categories(Name alias) {
        this(alias, CATEGORIES);
    }

    /**
     * Create a <code>rss.categories</code> table reference
     */
    public Categories() {
        this(DSL.name("categories"), null);
    }

    public <O extends Record> Categories(Table<O> child, ForeignKey<O, CategoriesRecord> key) {
        super(child, key, CATEGORIES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Rss.RSS;
    }

    @Override
    public Identity<CategoriesRecord, Long> getIdentity() {
        return (Identity<CategoriesRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<CategoriesRecord> getPrimaryKey() {
        return Keys.RSS_CATEGORIES_PKEY;
    }

    @Override
    public Categories as(String alias) {
        return new Categories(DSL.name(alias), this);
    }

    @Override
    public Categories as(Name alias) {
        return new Categories(alias, this);
    }

    @Override
    public Categories as(Table<?> alias) {
        return new Categories(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Categories rename(String name) {
        return new Categories(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Categories rename(Name name) {
        return new Categories(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Categories rename(Table<?> name) {
        return new Categories(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
