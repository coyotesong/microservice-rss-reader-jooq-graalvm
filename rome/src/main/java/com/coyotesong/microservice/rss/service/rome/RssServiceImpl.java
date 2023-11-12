package com.coyotesong.microservice.rss.service.rome;

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.service.RssService;
import com.coyotesong.microservice.rss.service.rome.converters.RomeOpmlToOpmlConverter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class RssServiceImpl implements RssService {
    private static final Logger LOG = LoggerFactory.getLogger(RssServiceImpl.class);

    private final RomeOpmlToOpmlConverter opmlConverter;

    public RssServiceImpl(RomeOpmlToOpmlConverter opmlConverter) {
        this.opmlConverter = opmlConverter;
    }

    @Override
    public Optional<Opml> importOpml(@NotNull Path path) throws IOException, ExecutionException {
        if (!Files.isReadable(path) || !Files.isRegularFile(path)) {
            return Optional.empty();
        }

        final Charset charset = StandardCharsets.UTF_8;

        try (InputStream is = Files.newInputStream(path);
             Reader reader = new InputStreamReader(is, charset)) {
            final Opml opml = opmlConverter.from(reader);
            return (opml == null) ? Optional.empty() : Optional.of(opml);
        } catch (ExecutionException e) {
            final Throwable t = e.getCause();
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            throw e;
        }
    }

    public Opml get() throws ExecutionException {
        /*
        try (Reader reader = new StringReader(getBody())) {
            final WireFeedInput input = new WireFeedInput();
            input.setXmlHealerOn(true);

            final com.rometools.opml.feed.opml.Opml opml = (com.rometools.opml.feed.opml.Opml) input.build(reader);
            return romeOpmlToOpmlConverter.from(opml);
        } catch (FeedException | IOException e) {
            throw new ExecutionException("Unable to parse OPML document", e);
        }
         */
        return null;
    }

    /*

    @Override
    public List<Feed> importOpml(@NotNull Reader r) throws IOException {
        return importOpmlImpl(r, false);
    }

    @Override
    public String exportOpml(@NotNull List<Feed> feeds) {
        final WireFeedOutput output = new WireFeedOutput();
        final List<Outline> outlines = feeds.stream().map(outlineConverter::to).collect(Collectors.toList());
        try {
            Opml opml = new Opml();
            opml.setOutlines(outlines);
            opml.setFeedType("opml_2.0");
            opml.setCreated(new Date());
            // opml.setModified(Date);
            // opml.setOwnerEmail();
            // opml.setOwnerId();
            // opml.setOwnerName();
            // opml.setDocs(String);
            // opml.setTitle(String);
            // opml.setEncoding();
            // opml.setForeignMarkup();
            // opml.setStyleSheet();
            final boolean prettyPrint = true;
            return output.outputString(opml, prettyPrint);
        } catch (FeedException e) {
            LOG.error("feed exception: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Load Feed using REST call
     * <p>
     * Lood Feed using REST call. If there is an error this method will fall back to
     * the OPML outline values.
     *
     * @param outline
     * @return feed object
     * @throws Exception
     *
    public Feed readFeed(@NotNull Outline outline) throws Exception {

        String textUrl;
        if (StringUtils.isNotBlank(outline.getUrl())) {
            textUrl = outline.getUrl();
        } else if (StringUtils.isNotBlank(outline.getXmlUrl())) {
            textUrl = outline.getXmlUrl();
        } else {
            LOG.error("no url provided!");
            return null;
        }

        URL url;
        try {
            url = new URL(textUrl);
        } catch (MalformedURLException e) {
            LOG.error("MalformedURL for outline: {}", textUrl);
            throw e;
        }

        final HttpResponse<String> response = httpService.get(url);
        if (response == null) {
            return outlineConverter.from(outline);
        }

        switch (response.statusCode()) {
            case 200:
            case 403:
                try {
                    return parseOutline(outline, response, url);
                } catch (IllegalArgumentException e) {
                    LOG.error("readSyncFeed({}): {}({})", url, e.getClass().getName(), e.getMessage());
                    final Feed feed = outlineConverter.from(outline).addResponse(response);
                    feed.setError(e.getClass().getName() + ": " + e.getMessage());
                    feed.setStatusCode(-1);
                    return feed;
                }

            default:
                return outlineConverter.from(outline).addResponse(response);
        }
    }
    */

    /*
    Feed parseOutline(Outline outline, HttpResponse<String> response, URL url) {

        // TODO - use charset from response..
        final String body = response.body();
        try (Reader reader = new StringReader(body)) {
            final SyndFeed f = new SyndFeedInput().build(reader);
            final Feed feed = feedConverter.from(f).addResponse(response);
            feed.setOriginalUrl(url);
            return feed;
        } catch (ParsingFeedException e) {
            // Don't add log statement when content involves HTML.
            if (!body.substring(0, Math.min(5, body.length())).toUpperCase().startsWith("<HTML")) {
                LOG.info(String.format("readSyncFeed(%s): %s:\n%-60.60s\n%-60.60s\n",
                        url, e.getClass().getName(), stripNewlines(e.getMessage()), body));
            }
            final Feed feed = outlineConverter.from(outline).addResponse(response);
            feed.setError(e.getClass().getName() + ": " + e.getMessage());
            return feed;
        } catch (FeedException | IOException | IllegalArgumentException e) {
            LOG.error(String.format("readSyncFeed(%s): %s:\n%-60.60s\n%-60.60s\n",
                    url, e.getClass().getName(), stripNewlines(e.getMessage()), body));
            final Feed feed = outlineConverter.from(outline).addResponse(response);
            feed.setError(e.getClass().getName() + ": " + e.getMessage());
            return feed;
        }
    }
     */

    /**
     * Strip newlines from string.
     *
     * @param s input string, may be null
     * @return stripped input, may be null
     */
    String stripNewlines(@Nullable String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        return s.replace("\n", "\\n");
    }

}