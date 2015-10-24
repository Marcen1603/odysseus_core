package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import java.util.List;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.IQueryTransmissionHandlerListener;

/**
 * Provides Method to implement own QueryTransmissionHandler that coordinates multiple Query Transmissions across network and peer in dynamic Load Balancing Process
 * @author Carsten Cordes
 *
 */
public interface IQueryTransmissionHandler extends INamedInterface {
	/** Gets name of Transmission Handler
	 * @see de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface#getName()
	 */
	public String getName();
	
	/**
	 * Prepare Transmission handler for Transmissions (e.g. Clean Up old Transmission States)
	 */
	public void clear();
	
	/**
	 * Set @link{ICommunicatorChooser} that chooses Communicator for transmission
	 * @param @link{ICommunicatorChooser} to set. 
	 */
	public void setCommunicatorChooser(ICommunicatorChooser chooser);
	
	/**
	 * Add listener that is notified when transmissions are finished
	 * @param listener Listener to add
	 */
	public void addListener(IQueryTransmissionHandlerListener listener);
	
	/**
	 * Remove listener that is notified when transmissions are finished
	 * @param listener Listener to remove.
	 */
	public void removeListener(IQueryTransmissionHandlerListener listener);
	
	/**
	 * Adds transmission to be processed. 
	 * @param queryID Query ID to transmit to other peer.
	 * @param destinationPeer Other Peer that Query is transmitted to.
	 */
	public void addTransmission(int queryID, PeerID destinationPeer);
	
	/**
	 * Gets list of failed transmissions.
	 * @return List with QueryIDs of Queries that failed to transmit.
	 */
	public List<Integer> getFailedTransmissions();
	
	/**
	 * Starts transmissions that were added to transmission Handler.
	 */
	public void startTransmissions();
}
