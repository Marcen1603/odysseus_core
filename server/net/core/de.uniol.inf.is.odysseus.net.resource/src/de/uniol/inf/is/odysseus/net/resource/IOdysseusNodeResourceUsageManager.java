package de.uniol.inf.is.odysseus.net.resource;

import java.util.concurrent.Future;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeResourceUsageManager {
	public Future<Optional<IResourceUsage>> getRemoteResourceUsage( IOdysseusNode node, boolean forceNetwork );
	public IResourceUsage getLocalResourceUsage();
}
