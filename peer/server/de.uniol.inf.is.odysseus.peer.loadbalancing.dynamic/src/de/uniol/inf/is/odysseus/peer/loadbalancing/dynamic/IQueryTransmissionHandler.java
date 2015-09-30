package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.IQueryTransmissionHandlerListener;

public interface IQueryTransmissionHandler extends INamedInterface {
	public String getName();
	
	public void clear();
	public void setCommunicatorChooser(ICommunicatorChooser chooser);
	public void addListener(IQueryTransmissionHandlerListener listener);
	public void removeListener(IQueryTransmissionHandlerListener listener);
	public void addTransmission(int queryID, PeerID destinationPeer);
	public List<Integer> getFailedTransmissions();
	public void startTransmissions();
}
