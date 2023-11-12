/*
 * This file is generated by jOOQ.
 */
package com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records;


import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.FeedBody;
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
public class FeedBodyRecord extends UpdatableRecordImpl<FeedBodyRecord> implements Record7<Long, URL, String, Integer, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>rss.feed_body.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>rss.feed_body.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>rss.feed_body.url</code>.
     */
    public void setUrl(URL value) {
        set(1, value);
    }

    /**
     * Getter for <code>rss.feed_body.url</code>.
     */
    public URL getUrl() {
        return (URL) get(1);
    }

    /**
     * Setter for <code>rss.feed_body.content_type</code>.
     */
    public void setContentType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>rss.feed_body.content_type</code>.
     */
    public String getContentType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>rss.feed_body.content_length</code>.
     */
    public void setContentLength(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>rss.feed_body.content_length</code>.
     */
    public Integer getContentLength() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>rss.feed_body.charset</code>.
     */
    public void setCharset(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>rss.feed_body.charset</code>.
     */
    public String getCharset() {
        return (String) get(4);
    }

    /**
     * Setter for <code>rss.feed_body.etag</code>.
     */
    public void setEtag(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>rss.feed_body.etag</code>.
     */
    public String getEtag() {
        return (String) get(5);
    }

    /**
     * Setter for <code>rss.feed_body.body</code>.
     */
    public void setBody(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>rss.feed_body.body</code>.
     */
    public String getBody() {
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
    public Row7<Long, URL, String, Integer, String, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, URL, String, Integer, String, String, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return FeedBody.FEED_BODY.ID;
    }

    @Override
    public Field<URL> field2() {
        return FeedBody.FEED_BODY.URL;
    }

    @Override
    public Field<String> field3() {
        return FeedBody.FEED_BODY.CONTENT_TYPE;
    }

    @Override
    public Field<Integer> field4() {
        return FeedBody.FEED_BODY.CONTENT_LENGTH;
    }

    @Override
    public Field<String> field5() {
        return FeedBody.FEED_BODY.CHARSET;
    }

    @Override
    public Field<String> field6() {
        return FeedBody.FEED_BODY.ETAG;
    }

    @Override
    public Field<String> field7() {
        return FeedBody.FEED_BODY.BODY;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public URL component2() {
        return getUrl();
    }

    @Override
    public String component3() {
        return getContentType();
    }

    @Override
    public Integer component4() {
        return getContentLength();
    }

    @Override
    public String component5() {
        return getCharset();
    }

    @Override
    public String component6() {
        return getEtag();
    }

    @Override
    public String component7() {
        return getBody();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public URL value2() {
        return getUrl();
    }

    @Override
    public String value3() {
        return getContentType();
    }

    @Override
    public Integer value4() {
        return getContentLength();
    }

    @Override
    public String value5() {
        return getCharset();
    }

    @Override
    public String value6() {
        return getEtag();
    }

    @Override
    public String value7() {
        return getBody();
    }

    @Override
    public FeedBodyRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public FeedBodyRecord value2(URL value) {
        setUrl(value);
        return this;
    }

    @Override
    public FeedBodyRecord value3(String value) {
        setContentType(value);
        return this;
    }

    @Override
    public FeedBodyRecord value4(Integer value) {
        setContentLength(value);
        return this;
    }

    @Override
    public FeedBodyRecord value5(String value) {
        setCharset(value);
        return this;
    }

    @Override
    public FeedBodyRecord value6(String value) {
        setEtag(value);
        return this;
    }

    @Override
    public FeedBodyRecord value7(String value) {
        setBody(value);
        return this;
    }

    @Override
    public FeedBodyRecord values(Long value1, URL value2, String value3, Integer value4, String value5, String value6, String value7) {
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
     * Create a detached FeedBodyRecord
     */
    public FeedBodyRecord() {
        super(FeedBody.FEED_BODY);
    }

    /**
     * Create a detached, initialised FeedBodyRecord
     */
    public FeedBodyRecord(Long id, URL url, String contentType, Integer contentLength, String charset, String etag, String body) {
        super(FeedBody.FEED_BODY);

        setId(id);
        setUrl(url);
        setContentType(contentType);
        setContentLength(contentLength);
        setCharset(charset);
        setEtag(etag);
        setBody(body);
        resetChangedOnNotNull();
    }
}
