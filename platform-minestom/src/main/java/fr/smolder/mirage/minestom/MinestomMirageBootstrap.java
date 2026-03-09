package fr.smolder.mirage.minestom;

import fr.smolder.mirage.core.MirageRuntime;
import fr.smolder.mirage.core.port.PermissionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Objects;

public final class MinestomMirageBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(MinestomMirageBootstrap.class);

	private MirageRuntime runtime;
	private MinestomPlatformAdapter platformAdapter;

	public void install(Path dataDirectory) {
		install(dataDirectory, PermissionProvider.allowAll());
	}

	public void install(Path dataDirectory, PermissionProvider permissionProvider) {
		Objects.requireNonNull(permissionProvider, "permissionProvider");
		this.platformAdapter = new MinestomPlatformAdapter(dataDirectory);
		this.runtime = new MirageRuntime(platformAdapter);

		MinestomMotdController motdController = new MinestomMotdController();
		motdController.install((key, protocolVersion) -> runtime.motd(key, protocolVersion));
		new MinestomCommandRegistrar(permissionProvider).registerReloadCommand(
				() -> {
					logger.debug("Received Mirage reload request.");
					platformAdapter.scheduler().executeAsync(runtime::reload);
				}
		);

		System.out.println("[Mirage] Scheduling initial reload.");
		logger.debug("Scheduling initial Mirage reload.");
		platformAdapter.scheduler().executeAsync(runtime::reload);
		logger.debug("Mirage runtime installed for Minestom at {}", dataDirectory);
	}

	public void close() {
		if (runtime != null) {
			runtime.close();
		}
		if (platformAdapter != null) {
			platformAdapter.close();
		}
	}

	MirageRuntime runtime() {
		return runtime;
	}
}
