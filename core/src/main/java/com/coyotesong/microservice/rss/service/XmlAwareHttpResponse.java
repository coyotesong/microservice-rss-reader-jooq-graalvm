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

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * HttpResponse that honors the charset and removes some XML cruft
 */
public class XmlAwareHttpResponse implements HttpResponse<String> {
    private final HttpResponse<byte[]> response;
    private final String body;

    public XmlAwareHttpResponse(HttpResponse<byte[]> response) {
        this.response = response;
        this.body = extractContentFromHttpResponse(response);
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public HttpRequest request() {
        return response.request();
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        // ---------------------------------- FIXME  -------------------------------
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return response.headers();
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return response.sslSession();
    }

    @Override
    public URI uri() {
        return response.uri();
    }

    @Override
    public HttpClient.Version version() {
        return response.version();
    }

    final String extractContentFromHttpResponse(@NotNull HttpResponse<byte[]> response) {
        if (response.body() == null) {
            return null;
        }

        Optional<String> optContentType = response.headers().firstValue("content-type");

        // TODO: extract charset from headers
        final Charset charset = StandardCharsets.UTF_8;

        // Create string using specified character set, remove all \r, then trim it.
        final String content = new String(response.body(), charset).replace("\r", "").trim();

        return stripCruft(content);
    }

    @NotNull
    final String stripCruft(@NotNull String content) {
        final List<String> directives = new ArrayList<>();

        int idx = 0;
        int lastIdx = idx;
        while (0 <= idx && idx < content.length()) {
            lastIdx = idx;
            if ((idx = content.indexOf("<", idx)) < 0) {
                break;
            }
            final String lead = content.substring(idx, Math.min(idx + 30, content.length()));
            if (lead.startsWith("<?")) {
                // XML directive - we want to preserve and restore them
                idx = content.indexOf("?>", idx);
                if (idx < 0) {
                    break;
                }
                idx += 2;
                directives.add(content.substring(lastIdx, idx + 2).trim());
            } else if (lead.startsWith("<!")) {
                // DOCTYPE - the parser won't handle this
                // comments - we can simplify some tasks if we strip these now
                idx = content.indexOf(">", idx);
                if (idx < 0) {
                    break;
                }
                idx++;
            } else {
                return String.join("", directives) + content.substring(idx).trim();
            }
        }

        // we should only hit this if there's either no tags or the current one is unclosed.
        return String.join("", directives) + content.substring(lastIdx).trim();
    }
}