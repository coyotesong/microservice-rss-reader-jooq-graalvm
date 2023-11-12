/*
 * Copyright (c) 2023. Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.microservice.rss.service.rome;

import com.coyotesong.microservice.rss.internal.ConversionUtilities;
import com.coyotesong.microservice.rss.model.*;
import com.coyotesong.microservice.rss.service.XmlAwareHttpResponse;
import com.coyotesong.microservice.rss.service.XmlAwareHttpService;
import com.coyotesong.microservice.rss.service.RssService;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOpmlToOpmlConverter;
import com.coyotesong.microservice.rss.service.rome.converters.SyndFeedToFeedConverter;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.input.JDOMParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Implementation of RssService using ROME library
 */
public class RssServiceImpl implements RssService {
    private static final Logger LOG = LoggerFactory.getLogger(RssServiceImpl.class);

    private final XmlAwareHttpService httpService;
    private final RomeOpmlToOpmlConverter opmlConverter;
    private final SyndFeedToFeedConverter feedConverter;

    public RssServiceImpl(XmlAwareHttpService httpService) {
        this(httpService, RomeOpmlToOpmlConverter.INSTANCE, SyndFeedToFeedConverter.INSTANCE);
    }

    public RssServiceImpl(XmlAwareHttpService httpService, RomeOpmlToOpmlConverter opmlConverter, SyndFeedToFeedConverter feedConverter) {
        this.httpService = httpService;
        this.opmlConverter = opmlConverter;
        this.feedConverter = feedConverter;
    }

    @Override
    @NotNull
    public Optional<Opml> importOpml(@NotNull URL url) throws IOException, ExecutionException {
        final String protocol = url.getProtocol().toLowerCase();
        if ("http".equals(protocol) || "https".equals(protocol)) {
            return importOpmlWithRest(url);
        }

        if ("file".equals(protocol)) {
            final Path path = new File(url.getFile()).toPath();
            return importOpml(path);
        }

        final Charset charset = StandardCharsets.UTF_8;
        try (InputStream is = url.openConnection().getInputStream()) {
            final String body = new String(is.readAllBytes(), charset);
            return parseOpml(body, url);
        }
    }

    @Override
    @NotNull
    public Optional<Opml> importOpml(@NotNull Path path) throws IOException, ExecutionException {
        if (!Files.isReadable(path) || !Files.isRegularFile(path)) {
            return Optional.empty();
        }

        final Charset charset = StandardCharsets.UTF_8;
        final String body = Files.readString(path, charset);
        return parseOpml(body, ConversionUtilities.toUrl(path));
    }

    @NotNull
    public Optional<Opml> importOpmlWithRest(URL url) throws IOException, ExecutionException {
        final XmlAwareHttpResponse response = httpService.get(url);

        return parseOpml(response.body(), url, response);
    }

    @Override
    @NotNull
    public Optional<Feed> importFeed(@NotNull URL url) throws IOException, ExecutionException {
        final String protocol = url.getProtocol().toLowerCase();
        if ("http".equals(protocol) || "https".equals(protocol)) {
            return Optional.of(importFeedWithRest(url));
        }

        if ("file".equals(protocol)) {
            final Path path = new File(url.getFile()).toPath();
            return Optional.of(importFeed(path));
        }

        // default handler
        final Charset charset = StandardCharsets.UTF_8;
        try (InputStream is = url.openStream()) {
            return Optional.of(parseFeed(new String(is.readAllBytes(), charset), url));
        }
    }

    /**
     * Import Feed using REST call
     * <p>
     * This method will always return <b>something</b> so we have a record
     * of failing websites.
     * </p>
     *
     * @param url
     * @return
     */
    @NotNull
    public Feed importFeedWithRest(@NotNull URL url) {
        try {
            final XmlAwareHttpResponse response = httpService.get(url);

            if (200 == response.statusCode()) {
                final Feed feed = parseFeed(response.body(), url, response);
                addResponse(feed, response);
                return feed;
            } else {
                // this is almost always 403 or 404
                return handleBadResponse(response);
            }
        } catch (Exception e) {
            final Feed feed = new Feed(url);
            if (e instanceof ConnectException) {
                feed.setError(String.format("importFeed('%s'): site is unresponsive", url));
            } else if (e instanceof ExecutionException) {
                feed.setError(String.format("importFeed('%s'): %s(%s))", url, e.getCause().getClass().getName(), e.getCause().getMessage()));
            } else {
                feed.setError(String.format("importFeed('%s'): %s(%s))", url, e.getClass().getName(), e.getMessage()));
            }
            LOG.warn(feed.getError());
            feed.setLive(false);
            feed.setValid(false);
            return feed;
        }
    }

    @Override
    @NotNull
    public Feed importFeed(@NotNull Path path) {
        if (!Files.isReadable(path) || !Files.isRegularFile(path)) {
            final Feed feed = new Feed(path);
            feed.setError("File does not exist or is not readable");
            return feed;
        }

        final Charset charset = StandardCharsets.UTF_8;
        try {
            final String body = Files.readString(path, charset);
            try {
                return parseFeed(body, ConversionUtilities.toUrl(path));
            } catch (Exception e) {
                final Feed feed = new Feed(path);
                feed.setBody(new FeedBody(feed.getId(), ConversionUtilities.toUrl(path), body));

                if (e instanceof ExecutionException) {
                    feed.setError(String.format("importFeed('%s'): %s(%s)", path, e.getCause().getClass().getName(), e.getCause().getMessage()));
                } else {
                    feed.setError(String.format("importFeed('%s'): %s(%s)", path, e.getClass().getName(), e.getMessage()));
                }

                LOG.warn(feed.getError());
                return feed;
            }
        } catch (IOException e) {
            final Feed feed = new Feed(path);
            feed.setError(String.format("importFeed('%s'): %s(%s)", path, e.getClass().getName(), e.getMessage()));

            LOG.warn(feed.getError());
            return feed;
        }
    }

    /**
     * Create deduplicated Feeds from Opml object
     * @param opml
     * @return
     */
    @NotNull
    public List<Feed> importFeeds(@NotNull Opml opml) {
        final Map<String, Feed> feeds = new HashMap<>();
        for (Outline outline : opml.getOutlines()) {
            try {
                final Optional<Feed> feed = importFeed(outline.getXmlUrl());
                if (feed.isPresent()) {
                    feeds.put(feed.get().getUrl().toString(), feed.get());
                }
            } catch (IOException | ExecutionException e) {
                // ignore - exception was logged in 'importFeed()' method
            }
        }

        final List<String> keys = new ArrayList<>(feeds.keySet());
        Collections.sort(keys);
        return keys.stream().map(feeds::get).collect(Collectors.toList());
    }

    /**
     * Parse 'Opml' document
     * @param body
     * @param url
     * @return
     */
    Optional<Opml> parseOpml(@NotNull String body, @NotNull URL url) throws IOException, ExecutionException {
        return parseOpml(body, url, null);
    }

    /**
     * Parse 'Opml' document
     * @param body
     * @param url
     * @param response
     * @return
     */
    Optional<Opml> parseOpml(@NotNull String body, @NotNull URL url, @Nullable HttpResponse<String> response) throws IOException, ExecutionException {
        try (Reader reader = new StringReader(body)) {
            try {
                Opml opml = opmlConverter.from(reader);
                if (opml != null) {
                    opml.setUrl(url);
                    addResponse(opml, response);
                    return Optional.of(opml);
                }

                opml = new Opml(url);
                // this might be a 'file' URL...
                opml.getBody().setUrl(url);
                opml.getBody().setBody(body);
                opml.getBody().setContentLength(body.length());
                addResponse(opml, response);
                // opml.setError("File could not be converted");
                // opml.setUrl(url);
                // feed.getHttpResponse().setLastModified(...);
                return Optional.of(opml);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                }
                throw e;
                // if (e.getCause() instanceof FeedException) {
                //    return handleFeedException(new Feed(url), (FeedException) e.getCause());
                // } else if (e.getCause() instanceof JDOMParseException) {
                //    return handleJDOMParseException(new Feed(url), (JDOMParseException) e.getCause());
                //}
            }
        }
    }

    /**
     * Parse 'Feed' document
     * @param body
     * @param url
     * @return
     */
    Feed parseFeed(@NotNull String body, @NotNull URL url) throws ExecutionException {
        return parseFeed(body, url, null);
    }

    /**
     * Parse 'Feed' document
     * @param body
     * @param url
     * @param response
     * @return
     */
    Feed parseFeed(@NotNull String body, @NotNull URL url, @Nullable HttpResponse<String> response) throws ExecutionException {
        try (Reader reader = new StringReader(body)) {
            try {
                Feed feed = feedConverter.from(reader);
                if (feed != null) {
                    feed.setUrl(url);
                    addResponse(feed, response);
                    return feed;
                }

                feed = new Feed(url);
                addResponse(feed, response);
                feed.setError("File could not be converted");
                // feed.getHttpResponse().setLastModified(...);
                return feed;
            } catch (ExecutionException e) {
                if (e.getCause() instanceof FeedException) {
                    return handleFeedException(new Feed(url), response, (FeedException) e.getCause());
                } else if (e.getCause() instanceof JDOMParseException) {
                    return handleJDOMParseException(new Feed(url), response, (JDOMParseException) e.getCause());
                }
                throw e;
            }
        } catch (Exception e) {
            // final Feed feed = new Feed(path);
            final Feed feed = new Feed(url);
            feed.setBody(new FeedBody(feed.getId(), url, body));

            if (e instanceof ConnectException) {
                feed.setError(String.format("importFeed('%s'): site is unresponsive", url));
            } else if (e instanceof ExecutionException) {
                feed.setError(String.format("importFeed('%s'): %s(%s)", url, e.getCause().getClass().getName(), e.getCause().getMessage()));
            } else {
                feed.setError(String.format("importFeed('%s'): %s(%s)", url, e.getClass().getName(), e.getMessage()));
            }

            LOG.warn(feed.getError());
            return feed;
        }
    }

    /**
     * Handle feed exception
     *
     * @param feed
     * @param response
     * @param e
     * @return
     */
    Feed handleFeedException(@NotNull Feed feed, @Nullable HttpResponse<String> response, @NotNull FeedException e) {
        if (response != null) {
            addResponse(feed, response);
        }
        feed.setError(String.format("importFeed('%s'): %s", feed.getUrl(), e.getMessage()));
        LOG.warn(feed.getError());

        feed.setLive(true);
        feed.setValid(false);
        return feed;
    }

    /**
     * Handle JDOM Parser exception
     *
     * @param feed
     * @param e
     * @return
     */
    Feed handleJDOMParseException(@NotNull Feed feed, @Nullable HttpResponse<String> response, @NotNull JDOMParseException e) {
        if (response != null) {
            addResponse(feed, response);
        }
        feed.setError(String.format("importFeed('%s'): %s", feed.getUrl(), e.getMessage()));
        LOG.warn(feed.getError());

        feed.setLive(true);
        feed.setValid(false);
        return feed;
    }

    /**
     * Handle bad server response, typically 403 or 404.
     *
     * @param response
     * @return
     */
    Feed handleBadResponse(@NotNull XmlAwareHttpResponse response) {
        final URL url = ConversionUtilities.toUrl(response.uri());
        final Feed feed = new Feed(url);
        addResponse(feed, response);
        feed.setLive(true);
        feed.setValid(false);
        return feed;
    }

    /**
     * Normalize charset and content type
     * @param response
     */
    void normalizeContentType(@NotNull FeedHttpResponse response) {
        if (StringUtils.isBlank(response.getContentType())) {
            return;
        }

        final Map<String, String> substitutions = new LinkedHashMap<>();
        substitutions.put("utf-", "UTF-");
        substitutions.put("iso-", "ISO-");

        final String[] components = response.getContentType().split(";");
        for (int i = 0; i < components.length; i++) {
            final String key = components[i].trim();
            if (key.toLowerCase().startsWith("charset=")) {
                // extract charset string
                String charset = key.substring(8);
                for (Map.Entry<String, String> substitution : substitutions.entrySet()) {
                    charset = charset.replace(substitution.getKey(), substitution.getValue());
                }

                // now ensure it's a recognized charset
                Charset cs;
                if (charset.toUpperCase().contains("UTF-8")) {
                    response.setCharset(StandardCharsets.UTF_8.name());
                } else if (charset.toUpperCase().contains("ISO-8859-1")) {
                    response.setCharset(StandardCharsets.ISO_8859_1.name());
                } else {
                    try {
                        response.setCharset(Charset.forName(charset).name());
                    } catch (Exception e) {
                        LOG.info("unsupported charset: {}", charset);
                        // response.setCharset(StandardCharsets.UTF_8.name());
                    }
                }

                response.setCharset(charset);
                components[i] = null;
            }
        }

        if ((components.length > 0) && (components[0] != null)) {
            response.setContentType(components[0]);
        }
    }

    /**
     * Populate OpmlBody object
     * @param opml
     * @param response
     */
    void addResponse(@NotNull Opml opml, @Nullable HttpResponse<String> response) {
        if (response == null) {
            return;
        }

        opml.getBody().setUrl(ConversionUtilities.toUrl(response.uri()));
        opml.getBody().setBody(response.body());
        opml.getBody().setContentLength(response.body().length());

        // we handle contentLength in parseOpml
        final Optional<String> contentType = response.headers().firstValue("content-type");
        if (contentType.isPresent()) {
            opml.getBody().setContentType(contentType.get());
        }
        final Optional<String> charset = response.headers().firstValue("charset");
        if (charset.isPresent()) {
            opml.getBody().setCharset(charset.get());
        }
        final Optional<String> etag = response.headers().firstValue("etag");
        if (etag.isPresent()) {
            opml.getBody().setEtag(etag.get());
        }
    }

    /**
     * Populate FeedHttpResponse and FeedBody objects.
     * @param feed
     * @param response
     */
    void addResponse(@NotNull Feed feed, @NotNull HttpResponse<String> response) {

        feed.setLive(true);
        feed.setRequiresAuthentication(response.statusCode() == 403);
        feed.setUrl(ConversionUtilities.toUrl(response.uri()));

        feed.getBody().setUrl(ConversionUtilities.toUrl(response.uri()));
        feed.getBody().setBody(response.body());
        feed.getBody().setContentLength(response.body().length());

        // for convenience
        final FeedHttpResponse httpResponse = feed.getHttpResponse();
        httpResponse.setUrl(feed.getUrl());
        httpResponse.setStatusCode(response.statusCode());

        // personal preferences
        final HttpHeaders headers = response.headers();
        final List<String> keys = new ArrayList<>(headers.map().keySet());
        Collections.sort(keys);

        for (String key : keys) {
            @NotNull final List<String> values = headers.allValues(key);
            @NotNull final String lKey = key.toLowerCase();
            // does it make sense to store cookies here?
            if ("set-cookie".equals(lKey)) {
                // values.stream().map(java.net.HttpCookie::parse)
                //         .forEach(x -> x.forEach(cookie -> cookieStore.add(response.uri(), cookie)));
            } else if ("set-cookie2".equals(lKey)) {
                // does it make sense to
                // values.stream().map(java.net.HttpCookie::parse)
                //         .forEach(x -> x.forEach(cookie -> cookieStore.add(response.uri(), cookie)));
            } else {
                for (int position = 0; position < values.size(); position++) {
                    httpResponse.getHeaders().add(new FeedHeader(feed.getId(), lKey, position, values.get(position)));
                }
            }

            switch (lKey) {
                case "cache-control":
                    httpResponse.setCacheControl(values.get(0));
                    break;
                case "charset":
                    httpResponse.setCharset(values.get(0));
                    feed.getBody().setCharset(values.get(0));
                    break;
                case "content-type":
                    httpResponse.setContentType(values.get(0));
                    feed.getBody().setContentType(values.get(0));
                    break;
                case "etag":
                    httpResponse.setEtag(values.get(0));
                    feed.getBody().setEtag(values.get(0));
                    break;
                case "pragma":
                    httpResponse.setPragma(values.get(0));
                    break;

                case "expires":
                    httpResponse.setExpires(ConversionUtilities.toInstant(values.get(0)));
                    break;
                case "last-modified":
                    httpResponse.setLastModified(ConversionUtilities.toInstant(values.get(0)));
                    break;
            }
        }

        normalizeContentType(httpResponse);

        /*
        // finally a bit of an override due to things I've seen in the wild...
        if ((isBlank(getContentType()) || (getContentType().contains("html"))
                && getBody().toLowerCase().startsWith("<?xml "))) {
            setContentType("application/xml");
        }
         */
    }
}