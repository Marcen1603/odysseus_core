package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.transfer;

import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQueryTransmissionHandlerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.IPeerLockContainerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.PeerLockContainer;


public class QueryTransmissionHandler implements IPeerLockContainerListener, ILoadBalancingListener {
	

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(QueryTransmissionHandler.class);
	
	private int queryId;
	private ILoadBalancingCommunicator communicator;
	private List<PeerID> involvedPeers;
	private PeerID slavePeerId;
	private PeerLockContainer locks;
	private ILoadBalancingLock lock;
	private IPeerCommunicator peerCommunicator;
	
	private List<IQueryTransmissionHandlerListener> listeners = Lists.newArrayList();
	
	private boolean lbResult = false;
	
	public int getQueryId() {
		return queryId;
	}
	
	public PeerID getSlavePeerID() {
		return slavePeerId;
	}

	public QueryTransmissionHandler(int queryIdToTransmit, PeerID destinationPeerId, ILoadBalancingCommunicator communicator,IPeerCommunicator peerCommunicator, ILoadBalancingLock lock) {
		LOG.debug("New Transmission Handler for Query Id {}",queryIdToTransmit);
		this.communicator = communicator;
		this.queryId = queryIdToTransmit;
		this.slavePeerId = destinationPeerId;
		this.involvedPeers = communicator.getInvolvedPeers(queryIdToTransmit);
		if(!involvedPeers.contains(slavePeerId)) {
			involvedPeers.add(slavePeerId);
		}
		this.lock = lock;
		this.peerCommunicator = peerCommunicator;
	}
	
	public void registerListener(IQueryTransmissionHandlerListener listener) {
		if(this.listeners.contains(listener))
			return;
		listeners.add(listener);
	}
	
	public void removeListener(IQueryTransmissionHandlerListener listener) {
		if(this.listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public void initiateTransmission(IQueryTransmissionHandlerListener callback) {
		LOG.debug("{} - Initiated Transmission.",queryId);
		if(lock.requestLocalLock()) {
			LOG.debug("{} - Local lock acquired. Requesting other locks.",queryId);
			registerListener(callback);
			locks = new PeerLockContainer(peerCommunicator,involvedPeers,this);
			locks.requestLocks();
			//TODO tell that it worked... or didn't
		} 
		else {
			LOG.debug("{} - No local lock acquired.",queryId);
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
		communicator.initiateLoadBalancing(slavePeerId, queryId);
	}

	@Override
	public void notifyReleasingFinished() {
		LOG.debug("{} - Releasing other peers done.",queryId);
		tellListeners(lbResult);
		
	}
	
	private synchronized void tellListeners(boolean success) {
		List<IQueryTransmissionHandlerListener> listenerCopy = new ArrayList<IQueryTransmissionHandlerListener>(listeners);
		
		for(IQueryTransmissionHandlerListener listener : listenerCopy) {
		
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
