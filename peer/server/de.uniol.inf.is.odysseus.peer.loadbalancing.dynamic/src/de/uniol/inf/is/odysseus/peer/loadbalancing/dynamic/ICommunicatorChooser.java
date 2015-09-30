package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;

public interface ICommunicatorChooser extends INamedInterface {
	public String getName();
	public HashMap<Integer,ILoadBalancingCommunicator>  chooseCommunicators(List<Integer> queryIds, ISession session);
	public ILoadBalancingCommunicator chooseCommunicator(int queryID, ISession session);
}
