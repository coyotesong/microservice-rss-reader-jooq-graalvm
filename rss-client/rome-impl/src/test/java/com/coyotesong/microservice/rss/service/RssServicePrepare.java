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

import com.coyotesong.microservice.rss.service.rome.RssServiceImpl;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Convenience class that cache's live data for subsequent tests.
 */
@RunWith(SpringRunner.class)
public class RssServicePrepare {

    @TestConfiguration
    static class BaseTestContextConfiguration {

        @Bean
        public XmlAwareHttpService httpService() {
            return new XmlAwareHttpServiceImpl();
        }

        @Bean
        public RssService rssService() {
            return new RssServiceImpl(httpService());
        }

        @Bean
        public HttpClient httpClient() {
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    // .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
                    // .authenticator(Authenticator.getDefault())
                    .build();
        }
    }

    @Autowired
    private RssService rssService;

    @Autowired
    private HttpClient client;

    String path = "classpath:feedly-254a721a-d332-42d5-97de-8435b5896a41-2023-10-02.opml";

    @Test
    public void cacheRssData() throws Exception {
        final URL url = URI.create(path).toURL();
        final WireFeedInput input = new WireFeedInput();
        input.setXmlHealerOn(true);

        try (InputStream is = url.openStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            final Opml opml = (Opml) input.build(reader);
            // opml.getOutlines().forEach(this::download);
        }

        // FIXME - update cached location in Opml file!
    }

    void download(Outline outline) throws Exception {
        // this is legitimate, e.g., for folder headings.
        if (StringUtils.isBlank(outline.getXmlUrl())) {
            return;
        }

        final URL url = URI.create(outline.getXmlUrl()).toURL();

        final HttpRequest request = HttpRequest.newBuilder()
            .uri(url.toURI())
            // .header("Accept", "application/rss-xml,text/xml;q=0.9")
            .timeout(Duration.ofSeconds(20))
            .GET()
            .build();


        final HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        // save contents, response headers.
    }

    String getTempPath(URL url) {
        List<String> comp = Arrays.asList(url.getHost().split("\\."));
        Collections.reverse(comp);

        String tpath = Strings.join(comp, '/') + url.getPath();
        int ridx = tpath.lastIndexOf("/");
        if (0 < ridx && ridx + 1 < tpath.length()) {
            String suffix = tpath.substring(ridx + 1).toLowerCase();
            System.out.printf(" -- %s\n", tpath);
            System.out.printf("    %s\n", suffix);
            // also "feeds/posts/default"...
            // also "data/rss? ")
            // also "www/feed ?
            if ("feed".equals(suffix) || "feed.xml".equals(suffix) || "feed.atom".equals(suffix) || "atom".equals(suffix) || "atom.xml".equals(suffix) || "rss".equals(suffix) || "rss.xml".equals(suffix) || "index.xml".equals(suffix)) {
                String s = tpath.substring(0, ridx).replace("\\.", "/");
                System.out.printf("    %s\n", s);
                return s;
            }
        }
        return tpath.replace("\\.", "/");
    }
}
