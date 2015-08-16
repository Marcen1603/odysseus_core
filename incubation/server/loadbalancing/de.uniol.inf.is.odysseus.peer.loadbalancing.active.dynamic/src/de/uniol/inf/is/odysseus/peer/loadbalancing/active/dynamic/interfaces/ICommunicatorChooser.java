package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;

public interface ICommunicatorChooser {
	public HashMap<Integer,ILoadBalancingCommunicator>  chooseCommunicators(List<Integer> queryIds, ISession session);
}
