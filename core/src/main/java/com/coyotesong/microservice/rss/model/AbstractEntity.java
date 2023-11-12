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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.net.CookieManager;
import java.net.CookieStore;

/**
 * Abstract base class for entities that need a cookie store
 * <p>
 * At the moment we can use a shared CookieStore but if we ever add 'users' we'll
 * want to have a per-user CookieStore, at least for the feeds that require
 * authentication.
 * </p>
 */
public abstract class AbstractEntity implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    protected static final CookieStore cookieStore = (new CookieManager()).getCookieStore();

    /**
     * Convert string to a normalized format.
     *
     * @param name
     * @return
     */
    @NotNull
    protected final String toTitleCase(@NotNull String name) {
        final StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toTitleCase(sb.charAt(0)));
        for (int i = 1; i < sb.length(); i++) {
            // 'whitespace' can include tabs, etc.
            if ('-' == sb.charAt(i) || Character.isWhitespace(sb.charAt(i))) {
                sb.setCharAt(i++, ' ');
                if (i < sb.length()) {
                    sb.setCharAt(i, Character.toTitleCase(sb.charAt(i)));
                }
            }
        }

        return sb.toString().trim();
    }
}
