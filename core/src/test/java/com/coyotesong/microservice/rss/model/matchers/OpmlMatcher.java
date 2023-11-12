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

import com.coyotesong.microservice.rss.model.Opml;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * Hamcrest Matcher for Opml objects
 * <p>
 * This file was autogenerated.
 * </p>
 */
public class OpmlMatcher extends TypeSafeMatcher<Opml> {
    private final Map<String, Matcher<?>> matchers = new LinkedHashMap<>();
    private final List<String> expectedContent = new ArrayList<>();
    private final Opml expected;

    /**
     * Define the matchers that will perform the actual tests
     */
    public OpmlMatcher(Opml expected) {
        this.expected = expected;

        if (expected.getId() != null) {
            matchers.put("id", IsEqual.equalTo(expected.getId()));
        } else {
            matchers.put("id", IsNull.nullValue());
        }
        if (expected.getCreatedDate() != null) {
            matchers.put("createdDate", IsEqual.equalTo(ISO_INSTANT.format(expected.getCreatedDate())));
        } else {
            matchers.put("createdDate", IsNull.nullValue());
        }
        if (expected.getDocs() != null) {
            matchers.put("docs", IsEqual.equalTo(expected.getDocs()));
        } else {
            matchers.put("docs", IsNull.nullValue());
        }
        if (expected.getEncoding() != null) {
            matchers.put("encoding", IsEqual.equalTo(expected.getEncoding()));
        } else {
            matchers.put("encoding", IsNull.nullValue());
        }
        if (expected.getFeedType() != null) {
            matchers.put("feedType", IsEqual.equalTo(expected.getFeedType()));
        } else {
            matchers.put("feedType", IsNull.nullValue());
        }
        if (expected.getModifiedDate() != null) {
            matchers.put("modifiedDate", IsEqual.equalTo(ISO_INSTANT.format(expected.getModifiedDate())));
        } else {
            matchers.put("modifiedDate", IsNull.nullValue());
        }
        if (expected.getOwnerEmail() != null) {
            matchers.put("ownerEmail", IsEqual.equalTo(expected.getOwnerEmail()));
        } else {
            matchers.put("ownerEmail", IsNull.nullValue());
        }
        if (expected.getOwnerName() != null) {
            matchers.put("ownerName", IsEqual.equalTo(expected.getOwnerName()));
        } else {
            matchers.put("ownerName", IsNull.nullValue());
        }
        if (expected.getOwnerId() != null) {
            matchers.put("ownerId", IsEqual.equalTo(expected.getOwnerId()));
        } else {
            matchers.put("ownerId", IsNull.nullValue());
        }
        if (expected.getStylesheet() != null) {
            matchers.put("stylesheet", IsEqual.equalTo(expected.getStylesheet()));
        } else {
            matchers.put("stylesheet", IsNull.nullValue());
        }
        if (expected.getTitle() != null) {
            matchers.put("title", IsEqual.equalTo(expected.getTitle()));
        } else {
            matchers.put("title", IsNull.nullValue());
        }

        expectedContent.add("id: " + expected.getId());
        if (expected.getCreatedDate() != null) {
            expectedContent.add("createdDate: " + ISO_INSTANT.format(expected.getCreatedDate()));
        } else {
            expectedContent.add("createdDate: null");
        }
        expectedContent.add("docs: " + expected.getDocs());
        expectedContent.add("encoding: " + expected.getEncoding());
        expectedContent.add("feedType: " + expected.getFeedType());
        if (expected.getModifiedDate() != null) {
            expectedContent.add("modifiedDate: " + ISO_INSTANT.format(expected.getModifiedDate()));
        } else {
            expectedContent.add("modifiedDate: null");
        }
        expectedContent.add("ownerEmail: " + expected.getOwnerEmail());
        expectedContent.add("ownerName: " + expected.getOwnerName());
        expectedContent.add("ownerId: " + expected.getOwnerId());
        expectedContent.add("stylesheet: " + expected.getStylesheet());
        expectedContent.add("title: " + expected.getTitle());
    }

    /**
     * Describe the expected value
     */
    @Override
    public void describeTo(Description description) {
        description.appendValueList("Opml: [ ", ", ", " ]", expectedContent);
    }

    /**
     * Describe difference between actual and expected values
     */
    @Override
    public void describeMismatchSafely(Opml actual, Description description) {
        final List<String> actualContent = new ArrayList<>();

        if (!matchers.get("id").matches(actual.getId())) {
            actualContent.add("id: " + actual.getId());
        }
        if (actual.getCreatedDate() != null) {
            if (!matchers.get("createdDate").matches(ISO_INSTANT.format(actual.getCreatedDate()))) {
                actualContent.add("createdDate: " + ISO_INSTANT.format(actual.getCreatedDate()));
            }
        } else if (expected.getCreatedDate() != null) {
            actualContent.add("createdDate: null");
        }
        if (!matchers.get("docs").matches(actual.getDocs())) {
            actualContent.add("docs: " + actual.getDocs());
        }
        if (!matchers.get("encoding").matches(actual.getEncoding())) {
            actualContent.add("encoding: " + actual.getEncoding());
        }
        if (!matchers.get("feedType").matches(actual.getFeedType())) {
            actualContent.add("feedType: " + actual.getFeedType());
        }
        if (actual.getModifiedDate() != null) {
            if (!matchers.get("modifiedDate").matches(ISO_INSTANT.format(actual.getModifiedDate()))) {
                actualContent.add("modifiedDate: " + ISO_INSTANT.format(actual.getModifiedDate()));
            }
        } else if (expected.getModifiedDate() != null) {
            actualContent.add("modifiedDate: null");
        }
        if (!matchers.get("ownerEmail").matches(actual.getOwnerEmail())) {
            actualContent.add("ownerEmail: " + actual.getOwnerEmail());
        }
        if (!matchers.get("ownerName").matches(actual.getOwnerName())) {
            actualContent.add("ownerName: " + actual.getOwnerName());
        }
        if (!matchers.get("ownerId").matches(actual.getOwnerId())) {
            actualContent.add("ownerId: " + actual.getOwnerId());
        }
        if (!matchers.get("stylesheet").matches(actual.getStylesheet())) {
            actualContent.add("stylesheet: " + actual.getStylesheet());
        }
        if (!matchers.get("title").matches(actual.getTitle())) {
            actualContent.add("title: " + actual.getTitle());
        }

        if (!actualContent.isEmpty()) {
            description.appendValueList("Opml [ ", ", ", " ]", actualContent);
        }
    }

    /**
     * Determine the differences between the expected and actual values.
     */
    @Override
    protected boolean matchesSafely(Opml actual) {
        if (!matchers.get("id").matches(actual.getId())) {
            return false;
        }
        if (actual.getCreatedDate() != null) {
            if (!matchers.get("createdDate").matches(ISO_INSTANT.format(actual.getCreatedDate()))) {
                return false;
            }
        } else if (expected.getCreatedDate() != null) {
            return false;
        }
        if (!matchers.get("docs").matches(actual.getDocs())) {
            return false;
        }
        if (!matchers.get("encoding").matches(actual.getEncoding())) {
            return false;
        }
        if (!matchers.get("feedType").matches(actual.getFeedType())) {
            return false;
        }
        if (actual.getModifiedDate() != null) {
            if (!matchers.get("modifiedDate").matches(ISO_INSTANT.format(actual.getModifiedDate()))) {
                return false;
            }
        } else if (expected.getModifiedDate() != null) {
            return false;
        }
        if (!matchers.get("ownerEmail").matches(actual.getOwnerEmail())) {
            return false;
        }
        if (!matchers.get("ownerName").matches(actual.getOwnerName())) {
            return false;
        }
        if (!matchers.get("ownerId").matches(actual.getOwnerId())) {
            return false;
        }
        if (!matchers.get("stylesheet").matches(actual.getStylesheet())) {
            return false;
        }
        if (!matchers.get("title").matches(actual.getTitle())) {
            return false;
        }

        return true;
    }

    /**
     * Static factory
     *
     * @param expected
     */
    public static OpmlMatcher equalTo(Opml expected) {
        return new OpmlMatcher(expected);
    }

    /**
     * Static factory
     *
     * @param expected
     */
    public static OpmlMatcher deepEqualTo(Opml expected) {
        throw new AssertionError("unimplemented method");
    }
}

