package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.transmissionhandler.odyload;

import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQueryTransmissionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.IQueryTransmissionHandlerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;

public class OdyLoadTransmissionHandlerImpl implements IQueryTransmissionHandler, IQueryTransmissionListener {
	
	private static final String HANDLER_NAME = "OdyLoad";

	private static final Logger LOG = LoggerFactory
			.getLogger(OdyLoadTransmissionHandlerImpl.class);
	
	private List<Integer> failedTransmissionQueryIDs;
	private List<QueryTransmission> transmissionHandlerList;
	private List<IQueryTransmissionHandlerListener> listeners;
	
	private IPeerDictionary peerDictionary;
	private ILoadBalancingLock lock;
	private IPeerCommunicator peerCommunicator;
	
	
	private ICommunicatorChooser communicatorChooser;
	private ISession activeSession;
	
	
	public void bindLoadBalancingLock(ILoadBalancingLock serv) {
		lock = serv;
	}
	
	public void unbindLoadBalancingLock(ILoadBalancingLock serv) {
		if(lock==serv) {
			lock=null;
		}
	}
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(peerCommunicator==serv) {
			peerCommunicator = null;
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary = null;
		}
	}
	
	public void clear() {
		failedTransmissionQueryIDs = Lists.newArrayList();
		transmissionHandlerList = Lists.newArrayList();
		listeners = Lists.newArrayList();
	}
	
	
	public synchronized void addListener(IQueryTransmissionHandlerListener listener) {
		if(listeners==null) {
			LOG.warn("Called removeListener with uninitialized List!");
			listeners = Lists.newArrayList();
		}
		if(listeners.contains(listener))
			return;
		listeners.add(listener);
	}
	
	public synchronized void removeListener(IQueryTransmissionHandlerListener listener) {
		if(listeners==null) {
			LOG.warn("Called removeListener with uninitialized List!");
			return;
		}
		if(!listeners.contains(listener))
			return;
		listeners.remove(listener);
	}
	
	public synchronized void notifyListeners() {
		List<IQueryTransmissionHandlerListener> listenersCopy = new ArrayList<IQueryTransmissionHandlerListener>(listeners);
		for(IQueryTransmissionHandlerListener listener : listenersCopy) {
			listener.transmissionsFinished();
		}
	}

	@Override
	public void tranmissionFailed(QueryTransmission transmission) {
		transmission.removeListener(this);
		transmissionHandlerList.remove(transmission);
		failedTransmissionQueryIDs.add(transmission.getQueryId());
		LOG.error("Transmission of Query {} to Peer {} failed.",transmission.getQueryId(), peerDictionary.getRemotePeerName(transmission.getSlavePeerID()));
		
		if(transmissionHandlerList.size()>0) {

			QueryTransmission nextTransmission = transmissionHandlerList.get(0);
			LOG.info("Starting Transmission of QueryID {} to Peer {}",nextTransmission.getQueryId(),peerDictionary.getRemotePeerName(nextTransmission.getSlavePeerID()));
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				for(Integer queryID : failedTransmissionQueryIDs) {
					LOG.warn("Query ID {} failed to transmit.",queryID);
				}
				notifyListeners();
			}
	}

	@Override
	public void transmissionSuccessful(QueryTransmission transmission) {
		
		Preconditions.checkNotNull(communicatorChooser,"Communicator Chooser must not be null!");
		
		transmission.removeListener(this);
		transmissionHandlerList.remove(transmission);

		LOG.info("Transmission of Query {} to Peer {} successful.",transmission.getQueryId(), peerDictionary.getRemotePeerName(transmission.getSlavePeerID()));
		if(transmissionHandlerList.size()>0) {

			QueryTransmission nextTransmission = transmissionHandlerList.get(0);
			LOG.info("Starting Transmission of QueryID {} to Peer {}",nextTransmission.getQueryId(),peerDictionary.getRemotePeerName(nextTransmission.getSlavePeerID()));
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				for(Integer queryID : failedTransmissionQueryIDs) {
					LOG.warn("Query ID {} failed to transmit.",queryID);
				}
				notifyListeners();
			}
	}


	@Override
	public void localLockFailed(QueryTransmission transmission) {
		try {
			transmission.removeListener(this);
			LOG.warn("Acquiring Local Lock failed. Waiting for {} milliseconds.",OdyLoadConstants.WAITING_TIME_FOR_LOCAL_LOCK);
			Thread.sleep(OdyLoadConstants.WAITING_TIME_FOR_LOCAL_LOCK);
			transmission.initiateTransmission(this,communicatorChooser.chooseCommunicator(transmission.getQueryId(), getActiveSession()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}


	public void addTransmission(int queryId, PeerID slavePeerId) {
		this.transmissionHandlerList.add(new QueryTransmission(queryId, slavePeerId, peerCommunicator,peerDictionary, lock));
	}
	

	public void startTransmissions() {
		
		if(transmissionHandlerList!=null && transmissionHandlerList.size()>0) {
			QueryTransmission nextTransmission = transmissionHandlerList.get(0);
			LOG.info("Starting Transmission of QueryID {} to Peer {}",nextTransmission.getQueryId(),peerDictionary.getRemotePeerName(nextTransmission.getSlavePeerID()));
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
			LOG.warn("No transmission to be processed.");
			notifyListeners();
		}
	}
	
	public List<Integer> getFailedTransmissions() {
		if(failedTransmissionQueryIDs==null) {
			LOG.warn("Failed Transmission QueryIDs is null.");
			return new ArrayList<Integer>();
		}
		return new ArrayList<Integer>(failedTransmissionQueryIDs);
	}
	

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}


	@Override
	public void setCommunicatorChooser(ICommunicatorChooser chooser) {
		communicatorChooser = chooser;
	}

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	
	

}
