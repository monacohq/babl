package com.aitusoftware.babl.websocket;

import com.aitusoftware.babl.config.BablConfig;
import com.aitusoftware.babl.config.DeploymentMode;
import org.agrona.concurrent.ShutdownSignalBarrier;

public class ServerOnlyLauncher {

    public static void main(final String[] args) {
        final BablConfig config = new BablConfig();
        config.sessionContainerConfig().deploymentMode(DeploymentMode.SERVER_ONLY);
        config.proxyConfig().mediaDriverDir("/tmp/aseq/Integration/aeron");
        config.proxyConfig().launchMediaDriver(false);
        try (final SessionContainers containers = BablServer.launch(config)) {
            containers.start();
            new ShutdownSignalBarrier().await();
        }
    }
}
