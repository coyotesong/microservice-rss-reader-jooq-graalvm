package com.coyotesong.microservice.rss.service;

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.service.rome.RssServiceImpl;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOpmlToOpmlConverter;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOutlineToOutlineConverter;
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

@RunWith(SpringRunner.class)
public class RssServiceTest {

    @TestConfiguration
    static class BaseTestContextConfiguration {

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
        public RssService opmlService(RomeOpmlToOpmlConverter opmlConverter) {
            return new RssServiceImpl(opmlConverter);
        }
    }

    @Autowired
    private RssService rssService;

    @Test
    public void testOpmlImportFromPath() throws Exception {
        Path path = new File("/home/bgiles/Downloads/feedly-254a721a-d332-42d5-97de-8435b5896a41-2023-10-02.opml").toPath();
        Optional<Opml> o = rssService.importOpml(path);

        try (StringWriter w = new StringWriter();
             PrintWriter pw = new PrintWriter(w)) {
            Opml opml = o.get();
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