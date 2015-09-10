package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.transmissionhandler.odyload;

import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.IPeerLockContainerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.PeerLockContainer;


public class QueryTransmission implements IPeerLockContainerListener, ILoadBalancingListener {
	

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(QueryTransmission.class);
	
	private int queryId;
	private List<PeerID> involvedPeers;
	private PeerID slavePeerId;
	private PeerLockContainer locks;
	private ILoadBalancingLock lock;
	private IPeerCommunicator peerCommunicator;
	
	private ILoadBalancingCommunicator communicator;
	
	private List<IQueryTransmissionListener> listeners = Lists.newArrayList();
	
	private boolean lbResult = false;
	
	public int getQueryId() {
		return queryId;
	}
	
	public PeerID getSlavePeerID() {
		return slavePeerId;
	}

	public QueryTransmission(int queryIdToTransmit, PeerID destinationPeerId, IPeerCommunicator peerCommunicator, ILoadBalancingLock lock) {
		LOG.debug("New Transmission Handler for Query Id {}",queryIdToTransmit);
		this.queryId = queryIdToTransmit;
		this.slavePeerId = destinationPeerId;
		this.lock = lock;
		this.peerCommunicator = peerCommunicator;
	}
	
	public void registerListener(IQueryTransmissionListener listener) {
		if(this.listeners.contains(listener))
			return;
		listeners.add(listener);
	}
	
	public void removeListener(IQueryTransmissionListener listener) {
		if(this.listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public void initiateTransmission(IQueryTransmissionListener callback,ILoadBalancingCommunicator communicator) {
		
		registerListener(callback);
		this.communicator = communicator;
		LOG.debug("{} - Initiated Transmission.",queryId);
		if(lock.requestLocalLock()) {
			
			try {
				
			
				LOG.debug("Got local Lock.");
				this.involvedPeers = communicator.getInvolvedPeers(queryId);
				LOG.debug("Got list of involved Peers");
				if(!involvedPeers.contains(slavePeerId)) {
					involvedPeers.add(slavePeerId);
				}
				
				LOG.debug("{} - Local lock acquired. Requesting other locks.",queryId);
				
				locks = new PeerLockContainer(peerCommunicator,involvedPeers,this);
				locks.requestLocks();
			}
			catch(Exception e) {
				LOG.error("Uncaught Exception in Locking Process: {}",e.getMessage());
				tellListeners(false);
			}
		} 
		else {
			LOG.warn("{} - No local lock acquired.",queryId);
			tellListenersLocalLockFailed();
		}
		
	}
	
	@Override
	public void notifyLockingFailed() {
		LOG.debug("{} - Locking other peers failed.",queryId);
		tellListeners(false);
	}

	@Override
	public void notifyLockingSuccessfull() {
		LOG.debug("{} - Locking other peers successful. Starting transmission.",queryId);
		communicator.registerLoadBalancingListener(this);
		LOG.info("Starting Communicator {}",communicator.getName());
		communicator.initiateLoadBalancing(slavePeerId, queryId);
	}

	@Override
	public void notifyReleasingFinished() {
		LOG.info("{} - Releasing other peers done.",queryId);
		tellListeners(lbResult);
		
	}
	
	private void tellListenersLocalLockFailed() {
		List<IQueryTransmissionListener> listenerCopy = new ArrayList<IQueryTransmissionListener>(listeners);
		
		for(IQueryTransmissionListener listener : listenerCopy) {
			listener.localLockFailed(this);	
		}
	}
	
	private void tellListeners(boolean success) {
		List<IQueryTransmissionListener> listenerCopy = new ArrayList<IQueryTransmissionListener>(listeners);
		
		for(IQueryTransmissionListener listener : listenerCopy) {
		
			if(success) {
				listener.transmissionSuccessful(this);
			}
			else {
				listener.tranmissionFailed(this);
			}
			
		}
	}

	@Override
	public void notifyLoadBalancingFinished(boolean successful) {
		LOG.info("Communicator is finished.");
		communicator.removeLoadBalancingListener(this);
		this.lbResult = successful;
		if(successful) {
			LOG.debug("{} - Transferring Query successful.",queryId);
		}
		else {
			LOG.debug("{} - Transferring Query failed",queryId);
		}
		LOG.debug("{} - Releasing Locks.",queryId);
		locks.releaseLocks();
		
	}
	

}
