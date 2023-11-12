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

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.service.rome.RssServiceImpl;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOpmlToOpmlConverter;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOutlineToOutlineConverter;
import com.coyotesong.microservice.rss.service.rome.converters.SyndFeedToFeedConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;


@RunWith(SpringRunner.class)
public class RssServiceTest {

    @TestConfiguration
    static class BaseTestContextConfiguration {

        @Bean
        public XmlAwareHttpService httpService() {
            return new XmlAwareHttpServiceImpl();
        }

        @Bean
        public RomeOutlineToOutlineConverter romeOutlineToOutlineConverter() {
            return new RomeOutlineToOutlineConverter();
        }

        @Bean
        @Autowired
        public RomeOpmlToOpmlConverter romeOpmlToOpmlConverter(RomeOutlineToOutlineConverter outlineConverter) {
            return new RomeOpmlToOpmlConverter(outlineConverter);
        }

        @Bean
        @Autowired
        public RssService rssService(RomeOpmlToOpmlConverter opmlConverter) {
            return new RssServiceImpl(httpService(), opmlConverter, SyndFeedToFeedConverter.INSTANCE);
        }
    }

    @Autowired
    private RssService rssService;

    @Test
    public void testOpmlImportFromPath() throws Exception {
        Path path = new File("/home/bgiles/Downloads/feedly-254a721a-d332-42d5-97de-8435b5896a41-2023-10-02.opml").toPath();
        Optional<Opml> o = rssService.importOpml(path);

        assertThat("OPML file could not be loaded", o.isPresent());

        try (StringWriter w = new StringWriter();
             PrintWriter pw = new PrintWriter(w)) {
            final Opml opml = o.get();

            assertThat("No outlines were loaded", opml.getOutlines(), not(empty()));

            /*
            pw.println(opml.toString());
            pw.println("---");
            // for (int i = 0; i < 30 && i < opml.getOutlines().size(); i++) {
            for (int i = 0; i < opml.getOutlines().size(); i++) {
                // pw.printf(" - %s\n", opml.getOutlines().get(i));
                pw.println(download(opml.getOutlines().get(i)));
            }
            pw.flush();
            System.out.println(w.toString());
             */
        }
    }
}