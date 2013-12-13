package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class MinLoadBalancer implements ILoadBalancer {

	@Override
	public Map<ILogicalQueryPart, PeerID> balance(Map<PeerID, IResourceUsage> currentUsages, Collection<ILogicalQueryPart> queryParts) {
		return null;
	}

}
