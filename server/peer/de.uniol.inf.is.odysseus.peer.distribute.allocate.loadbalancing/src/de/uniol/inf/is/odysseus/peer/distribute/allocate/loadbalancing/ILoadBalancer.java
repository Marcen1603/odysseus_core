package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public interface ILoadBalancer {

	public Map<ILogicalQueryPart, PeerID> balance( Map<PeerID, IResourceUsage> currentUsages, Collection<ILogicalQueryPart> queryParts );
}
