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

package com.coyotesong.microservice.rss.model.util.flyway;

import java.io.*;

/**
 * Generate 'initial data' script
 */
public class GenerateInitialDataScript {
    private final File root;

    public GenerateInitialDataScript(File root) {
        this.root = root;
    }

    public void generate() throws IOException {
        final File dataFile = new File(root, "V3__postgres-rss-schema-initial-data.sql");
        try (Writer w = new FileWriter(dataFile);
             PrintWriter pw = new PrintWriter(w)) {
            pw.print("--\n-- Initial data\n--\n");
        }
    }
}
