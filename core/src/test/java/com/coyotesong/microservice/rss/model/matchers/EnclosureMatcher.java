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

package com.coyotesong.microservice.rss.model.matchers;

import com.coyotesong.microservice.rss.model.Enclosure;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hamcrest Matcher for Enclosure objects
 * <p>
 * This file was autogenerated.
 * </p>
 */
public class EnclosureMatcher extends TypeSafeMatcher<Enclosure> {
    private final Map<String, Matcher<?>> matchers = new LinkedHashMap<>();
    private final List<String> expectedContent = new ArrayList<>();
    private final Enclosure expected;

    /**
     * Define the matchers that will perform the actual tests
     */
    public EnclosureMatcher(Enclosure expected) {
        this.expected = expected;

        if (expected.getId() != null) {
            matchers.put("id", IsEqual.equalTo(expected.getId()));
        } else {
            matchers.put("id", IsNull.nullValue());
        }
        if (expected.getType() != null) {
            matchers.put("type", IsEqual.equalTo(expected.getType()));
        } else {
            matchers.put("type", IsNull.nullValue());
        }
        if (expected.getUrl() != null) {
            matchers.put("url", IsEqual.equalTo(expected.getUrl()));
        } else {
            matchers.put("url", IsNull.nullValue());
        }
        if (expected.getLength() != null) {
            matchers.put("length", IsEqual.equalTo(expected.getLength()));
        } else {
            matchers.put("length", IsNull.nullValue());
        }

        expectedContent.add("id: " + expected.getId());
        expectedContent.add("type: " + expected.getType());
        expectedContent.add("url: " + expected.getUrl());
        expectedContent.add("length: " + expected.getLength());
    }

    /**
     * Describe the expected value
     */
    @Override
    public void describeTo(Description description) {
        description.appendValueList("Enclosure: [ ", ", ", " ]", expectedContent);
    }

    /**
     * Describe difference between actual and expected values
     */
    @Override
    public void describeMismatchSafely(Enclosure actual, Description description) {
        final List<String> actualContent = new ArrayList<>();

        if (!matchers.get("id").matches(actual.getId())) {
            actualContent.add("id: " + actual.getId());
        }
        if (!matchers.get("type").matches(actual.getType())) {
            actualContent.add("type: " + actual.getType());
        }
        if (!matchers.get("url").matches(actual.getUrl())) {
            actualContent.add("url: " + actual.getUrl());
        }
        if (!matchers.get("length").matches(actual.getLength())) {
            actualContent.add("length: " + actual.getLength());
        }

        if (!actualContent.isEmpty()) {
            description.appendValueList("Enclosure [ ", ", ", " ]", actualContent);
        }
    }

    /**
     * Determine the differences between the expected and actual values.
     */
    @Override
    protected boolean matchesSafely(Enclosure actual) {
        if (!matchers.get("id").matches(actual.getId())) {
            return false;
        }
        if (!matchers.get("type").matches(actual.getType())) {
            return false;
        }
        if (!matchers.get("url").matches(actual.getUrl())) {
            return false;
        }
        if (!matchers.get("length").matches(actual.getLength())) {
            return false;
        }

        return true;
    }

    /**
     * Static factory
     *
     * @param expected
     */
    public static EnclosureMatcher equalTo(Enclosure expected) {
        return new EnclosureMatcher(expected);
    }

    /**
     * Static factory
     *
     * @param expected
     */
    public static EnclosureMatcher deepEqualTo(Enclosure expected) {
        throw new AssertionError("unimplemented method");
    }
}

