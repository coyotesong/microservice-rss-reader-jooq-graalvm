/*
 * This file is generated by jOOQ.
 */
package com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records;


import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Images;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import java.net.URL;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class ImagesRecord extends UpdatableRecordImpl<ImagesRecord> implements Record7<Long, String, URL, String, Integer, Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>rss.images.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>rss.images.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>rss.images.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>rss.images.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>rss.images.url</code>.
     */
    public void setUrl(URL value) {
        set(2, value);
    }

    /**
     * Getter for <code>rss.images.url</code>.
     */
    public URL getUrl() {
        return (URL) get(2);
    }

    /**
     * Setter for <code>rss.images.description</code>.
     */
    public void setDescription(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>rss.images.description</code>.
     */
    public String getDescription() {
        return (String) get(3);
    }

    /**
     * Setter for <code>rss.images.height</code>.
     */
    public void setHeight(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>rss.images.height</code>.
     */
    public Integer getHeight() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>rss.images.width</code>.
     */
    public void setWidth(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>rss.images.width</code>.
     */
    public Integer getWidth() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>rss.images.link</code>.
     */
    public void setLink(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>rss.images.link</code>.
     */
    public String getLink() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, URL, String, Integer, Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, String, URL, String, Integer, Integer, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Images.IMAGES.ID;
    }

    @Override
    public Field<String> field2() {
        return Images.IMAGES.TITLE;
    }

    @Override
    public Field<URL> field3() {
        return Images.IMAGES.URL;
    }

    @Override
    public Field<String> field4() {
        return Images.IMAGES.DESCRIPTION;
    }

    @Override
    public Field<Integer> field5() {
        return Images.IMAGES.HEIGHT;
    }

    @Override
    public Field<Integer> field6() {
        return Images.IMAGES.WIDTH;
    }

    @Override
    public Field<String> field7() {
        return Images.IMAGES.LINK;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public URL component3() {
        return getUrl();
    }

    @Override
    public String component4() {
        return getDescription();
    }

    @Override
    public Integer component5() {
        return getHeight();
    }

    @Override
    public Integer component6() {
        return getWidth();
    }

    @Override
    public String component7() {
        return getLink();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public URL value3() {
        return getUrl();
    }

    @Override
    public String value4() {
        return getDescription();
    }

    @Override
    public Integer value5() {
        return getHeight();
    }

    @Override
    public Integer value6() {
        return getWidth();
    }

    @Override
    public String value7() {
        return getLink();
    }

    @Override
    public ImagesRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ImagesRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public ImagesRecord value3(URL value) {
        setUrl(value);
        return this;
    }

    @Override
    public ImagesRecord value4(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public ImagesRecord value5(Integer value) {
        setHeight(value);
        return this;
    }

    @Override
    public ImagesRecord value6(Integer value) {
        setWidth(value);
        return this;
    }

    @Override
    public ImagesRecord value7(String value) {
        setLink(value);
        return this;
    }

    @Override
    public ImagesRecord values(Long value1, String value2, URL value3, String value4, Integer value5, Integer value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ImagesRecord
     */
    public ImagesRecord() {
        super(Images.IMAGES);
    }

    /**
     * Create a detached, initialised ImagesRecord
     */
    public ImagesRecord(Long id, String title, URL url, String description, Integer height, Integer width, String link) {
        super(Images.IMAGES);

        setId(id);
        setTitle(title);
        setUrl(url);
        setDescription(description);
        setHeight(height);
        setWidth(width);
        setLink(link);
        resetChangedOnNotNull();
    }
}
