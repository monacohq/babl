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
package com.aitusoftware.babl.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aitusoftware.babl.config.DeploymentMode.DIRECT;

/**
 * Aggregator for per-component configuration.
 */
public final class BablConfig {
    private final PerformanceConfig performanceConfig = new PerformanceConfig();
    private final ApplicationConfig applicationConfig = new ApplicationConfig(performanceConfig);
    private final SessionContainerConfig sessionContainerConfig = new SessionContainerConfig(performanceConfig);
    private final SessionConfig sessionConfig = new SessionConfig();
    private final SocketConfig socketConfig = new SocketConfig();
    private final ProxyConfig proxyConfig = new ProxyConfig(performanceConfig);

    /**
     * Returns the configuration for the {@code Application}.
     *
     * @return application configuration
     */
    public ApplicationConfig applicationConfig() {
        return applicationConfig;
    }

    /**
     * Returns the configuration for the server.
     *
     * @return server configuration
     */
    public SessionContainerConfig sessionContainerConfig() {
        return sessionContainerConfig;
    }

    /**
     * Returns the configuration for web socket sessions.
     *
     * @return session configuration
     */
    public SessionConfig sessionConfig() {
        return sessionConfig;
    }

    /**
     * Returns the configuration for network sockets.
     *
     * @return socket configuration
     */
    public SocketConfig socketConfig() {
        return socketConfig;
    }

    /**
     * Returns the configuration for network proxies.
     *
     * @return proxy configuration
     */
    public ProxyConfig proxyConfig() {
        return proxyConfig;
    }

    /**
     * Returns the configuration for performance.
     *
     * @return performance configuration
     */
    public PerformanceConfig performanceConfig() {
        return performanceConfig;
    }

    public void conclude() {
        applicationConfig.conclude();
        sessionConfig.conclude();
        sessionContainerConfig.conclude();
        if (sessionContainerConfig.deploymentMode() != DIRECT) {
            if (sessionContainerConfig.sessionContainerInstanceCount() < 1) {
                throw new IllegalStateException("Server instance count must be greater than zero");
            }
            if (proxyConfig.launchMediaDriver()) {
                final Path serverDir = Paths.get(sessionContainerConfig.serverDirectory(0));
                final Path mediaDriverDir = Paths.get(proxyConfig.mediaDriverDir());

                if (mediaDriverDir.startsWith(serverDir)) {
                    throw new IllegalStateException(
                            String.format("MediaDriver directory (%s) cannot be created within Server directory (%s)",
                                    mediaDriverDir, serverDir));
                }
            }
            proxyConfig.conclude();
        }
    }
}