package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class MaxLoadBalancer implements ILoadBalancer {

	@Override
	public Map<ILogicalQueryPart, PeerID> balance(Map<PeerID, IResourceUsage> currentResourceUsageMap, Map<ILogicalQueryPart, OperatorCost<?>> partCosts) {
		// TODO Auto-generated method stub
		return null;
	}

}
