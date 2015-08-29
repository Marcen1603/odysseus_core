package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.transfer;

import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OsgiServiceProvider;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQueryTransmissionHandlerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQueryTransmissionListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.ILoadBalancingLock;

public class QueryTransmissionHandler implements IQueryTransmissionListener {
	

	private static final Logger LOG = LoggerFactory
			.getLogger(QueryTransmissionHandler.class);
	
	private List<Integer> failedTransmissionQueryIDs = Lists.newArrayList();

	private List<QueryTransmission> transmissionHandlerList = Lists.newArrayList();
	
	private List<IQueryTransmissionHandlerListener> listeners = Lists.newArrayList();
	
	private IPeerDictionary peerDictionary;
	private ILoadBalancingLock lock;
	private IPeerCommunicator peerCommunicator;
	
	private ICommunicatorChooser communicatorChooser;
	private ISession activeSession;
	
	public QueryTransmissionHandler(ICommunicatorChooser chooser) {
		this.communicatorChooser = chooser;
		this.peerDictionary = OsgiServiceProvider.getPeerDictionary();
		this.lock = OsgiServiceProvider.getLock();
		this.peerCommunicator = OsgiServiceProvider.getPeerCommunicator();
	}
	
	
	public synchronized void addListener(IQueryTransmissionHandlerListener listener) {
		if(listeners.contains(listener))
			return;
		listeners.add(listener);
	}
	
	public synchronized void removeListener(IQueryTransmissionHandlerListener listener) {
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
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				for(Integer queryID : failedTransmissionQueryIDs) {
					LOG.warn("Query ID {} failed to transmit.",queryID);
				}

				lock.releaseLocalLock();
				notifyListeners();
			}
	}

	@Override
	public void transmissionSuccessful(QueryTransmission transmission) {
		transmission.removeListener(this);
		transmissionHandlerList.remove(transmission);

		LOG.error("Transmission of Query {} to Peer {} successful.",transmission.getQueryId(), peerDictionary.getRemotePeerName(transmission.getSlavePeerID()));
		if(transmissionHandlerList.size()>0) {

			QueryTransmission nextTransmission = transmissionHandlerList.get(0);
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				for(Integer queryID : failedTransmissionQueryIDs) {
					LOG.warn("Query ID {} failed to transmit.",queryID);
				}
				lock.releaseLocalLock();
				notifyListeners();
			}
	}


	@Override
	public void localLockFailed(QueryTransmission transmission) {
		try {
			LOG.warn("Acquiring Local Lock failed. Waiting for {} milliseconds.",OdyLoadConstants.WAITING_TIME_FOR_LOCAL_LOCK);
			Thread.sleep(OdyLoadConstants.WAITING_TIME_FOR_LOCAL_LOCK);
			transmission.initiateTransmission(this,communicatorChooser.chooseCommunicator(transmission.getQueryId(), getActiveSession()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}


	public void addTransmission(int queryId, PeerID slavePeerId) {
		this.transmissionHandlerList.add(new QueryTransmission(queryId, slavePeerId, peerCommunicator, lock));
	}
	

	public void startTransmissions() {
		
		if(transmissionHandlerList!=null && transmissionHandlerList.size()>0) {
			QueryTransmission nextTransmission = transmissionHandlerList.get(0);
			nextTransmission.initiateTransmission(this, communicatorChooser.chooseCommunicator(nextTransmission.getQueryId(), getActiveSession()));
		}
		else {
			notifyListeners();
		}
	}
	
	public List<Integer> getFailedTransmissions() {
		return failedTransmissionQueryIDs;
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

	
	

}
