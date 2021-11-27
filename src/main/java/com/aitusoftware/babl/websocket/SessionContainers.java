/*
 * Copyright 2019-2020 Aitu Software Limited.
 *
 * https://aitusoftware.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aitusoftware.babl.websocket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.agrona.CloseHelper;

/**
 * A container for the server and any dependencies.
 */
public final class SessionContainers implements AutoCloseable
{
    private final SessionContainer[] sessionContainers;
    private final List<AutoCloseable> dependencies;

    public SessionContainer[] getSessionContainers() {
        return sessionContainers;
    }

    public List<AutoCloseable> getDependencies() {
        return dependencies;
    }

    SessionContainers(final SessionContainer sessionContainer)
    {
        this(new SessionContainer[] {sessionContainer}, Collections.emptyList());
    }

    SessionContainers(
        final SessionContainer[] sessionContainers,
        final List<AutoCloseable> dependencies)
    {
        this.sessionContainers = sessionContainers;
        this.dependencies = dependencies;
    }

    /**
     * Start the server
     */
    public void start()
    {
        Arrays.stream(sessionContainers).forEach(SessionContainer::start);
    }

    /**
     * Stop the server
     */
    @Override
    public void close()
    {
        CloseHelper.closeAll(sessionContainers);
        CloseHelper.closeAll(dependencies);
    }
}