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

/**
 * Model of RSS feeds and entries based on ROME library
 * <p>
 * These classes have JPA annotations but they're not guaranteed to be
 * sufficient for a JPA-based persistence layer (yet) since the imitial
 * implementation is using jOOQ - they're primarily intended for use by
 * the Hamcrest matcher code generator.
 * </p>
 * <p>
 * The test classes include auto-generated Hamcrest Matchers for all
 * of these classes. These Matchers are extremely useful since they'll
 * create one message for all failed matches in the object - this can
 * make the root cause much more apparent when there are related failures.
 * </p>
 * <p>
 * Unfortunately those matchers do not (yet) support Collections or Maps.
 * </p>
 */
package com.coyotesong.microservice.rss.model;