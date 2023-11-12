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
package com.coyotesong.microservice.rss.model;

import com.coyotesong.microservice.rss.internal.ConversionUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * ConversionUtilities unit tests
 */
public class ConversionUtilitiesTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConversionUtilitiesTest.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private final File home = new File(System.getProperty("user.home"));

    @Test
    public void homeDirectoryToUrlTest() {
        final URL actual = ConversionUtilities.toUrl(home.toPath());
        assertThat(actual.toString(), equalTo("file:" + home.getAbsolutePath() + FILE_SEPARATOR));
    }

    @Test
    public void homeDirectoryPlusToUrlTest() {
        final String filename = "foobar";
        final File test = new File(home, filename);
        final URL actual = ConversionUtilities.toUrl(test.toPath());
        assertThat(actual.toString(), equalTo("file:" + test.getAbsolutePath()));
    }

    @Test
    public void tildeToUrlTest() {
        final File test = new File("~" + System.getProperty("user.name"));
        final URL actual = ConversionUtilities.toUrl(test.toPath());
        assertThat(actual.toString(), equalTo("file:" + home.getAbsolutePath() + FILE_SEPARATOR));
    }

    @Test
    public void tildePlusToUrlTest() {
        final String filename = "foobar";
        final File test = new File("~" + System.getProperty("user.name"), filename);
        final URL actual = ConversionUtilities.toUrl(test.toPath());
        assertThat(actual.toString(), equalTo("file:" + new File(home, filename).getAbsolutePath()));
    }

    @Test
    public void httpUrlTest() {
        final String expected = "http://abc.def:8080/ghi?jkl";
        final URL actual = ConversionUtilities.toUrl(expected);
        assertThat(actual.toString(), equalTo(expected));
    }
}
