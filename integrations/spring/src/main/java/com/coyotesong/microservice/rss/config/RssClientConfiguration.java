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

package com.coyotesong.microservice.rss.config;

import com.coyotesong.microservice.rss.service.spi.RssServiceFactory;
import com.coyotesong.microservice.rss.service.XmlAwareHttpService;
import com.coyotesong.microservice.rss.service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Configures RSS Client implementation via ServiceLoader
 */
@Configuration
public class RssClientConfiguration {
    @Autowired
    private XmlAwareHttpService httpService;

    @Bean
    public RssServiceFactory rssServiceFactory() {
        Optional<RssServiceFactory> factory = ServiceLoader.load(RssServiceFactory.class).findFirst();
        if (factory.isEmpty()) {
            throw new IllegalStateException("No RssServiceFactory could be found!");
        }
        return factory.get();

    }

    @Bean
    public RssService rssService(@Autowired RssServiceFactory factory) {
        return factory.newInstance(httpService);
    }
}
