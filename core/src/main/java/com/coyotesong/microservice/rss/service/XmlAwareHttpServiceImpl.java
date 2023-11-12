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

package com.coyotesong.microservice.rss.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Implementation of XmlAwareHttpService
 * <p>
 * This class uses java.net.http instead of the usual suspects since it's
 * good enough (for now) and avoids introducing a ton of dependencies.
 * </p>
 * <p>
 * The HttpClient class also supports asynchronous operations using
 * 'sendAsync()' instead of 'send()'.
 * </p>
 */
public class XmlAwareHttpServiceImpl implements XmlAwareHttpService {
    private static final Logger LOG = LoggerFactory.getLogger(XmlAwareHttpServiceImpl.class);

    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClient.Builder client;

    // cached values in case we need to follow a redirect URL.
    private Authenticator authenticator;
    private Duration connectTimeout;
    private InetSocketAddress proxy;

    /**
     * Default constructor
     */
    public XmlAwareHttpServiceImpl() {

        // the java.net.http.HttpClient is sufficient for our needs. This allows
        // us to avoid a large number of imports.
        //
        // Note - we'll need to revisit when we add authentication.
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Set authenticator
     *
     * @param authenticator
     * @return
     */
    public XmlAwareHttpService withAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        this.client.authenticator(authenticator);
        return this;
    }

    /**
     * Set connection timeout
     *
     * @param connectTimeout
     * @return
     */
    public XmlAwareHttpService withConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        this.client.connectTimeout(connectTimeout);
        return this;
    }

    /**
     * Set proxy
     *
     * @param proxy
     */
    public XmlAwareHttpService withProxy(InetSocketAddress proxy) {
        this.proxy = proxy;
        this.client.proxy(ProxySelector.of(proxy));
        return this;
    }

    /**
     * Make GET call
     * <p>
     * Note: this method will recursively call itself to follow redirects.
     * </p>
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public XmlAwareHttpResponse get(@NotNull HttpRequest request) throws IOException {
        try {
            // we can't use HttpResponse.BodyHandlers.ofString(charset) since we won't know the
            // appropriate until after the call successes.
            final HttpResponse<byte[]> response = client.build().send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response == null) {
                return null;
            }

            // do we need to follow a permanent redirection?
            final Optional<String> location = response.headers().firstValue("Location");
            if ((response.statusCode() == 301) && location.isPresent()) {
                return redirect(location.get());
            }

            return new XmlAwareHttpResponse(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Make GET call
     * <p>
     * Note: this method will recursively call itself to follow redirects.
     * </p>
     *
     * @param url
     * @return
     * @throws IOException
     */
    @Override
    public XmlAwareHttpResponse get(@NotNull URL url) throws IOException {

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.toExternalForm()))
                // .header("Accept", "application/rss-xml,text/xml;q=0.9")
                .timeout(DEFAULT_CONNECTION_TIMEOUT)
                .GET()
                .build();

        return get(request);
    }

    /**
     * Follow a 301 redirect
     *
     * @param location
     * @return
     * @throws IOException
     */
    XmlAwareHttpResponse redirect(@NotNull String location) throws IOException {
        final URI redirectUri = URI.create(location);
        final HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(redirectUri)
                // .header("Accept", "application/rss-xml,text/xml;q=0.9")
                .timeout(DEFAULT_CONNECTION_TIMEOUT)
                .GET();

        return get(request.build());
    }
}
