package de.uniol.inf.is.odysseus.peer.loadbalancing.active.status;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * Store for Stati used in LoadBalancing. Each QueryPart has a id, which can be used to identify a loadBalancingProcess.
 * Implemented as Singleton.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingStatusCache {
	
	/**
	 * HashMap used to store the QueryParts with a particular id.
	 */
	private ConcurrentHashMap<Integer,LoadBalancingMasterStatus> masterStati;
	
	private ConcurrentHashMap<PeerID,ConcurrentHashMap<Integer,LoadBalancingSlaveStatus>> slaveStati;

	/**
	 * Instance variable
	 */
	private static LoadBalancingStatusCache instance;
	
	/** 
	 * Current Process counter.
	 */
	private int counter;

	/**
	 * Returns Instance
	 * @return instance of Cache
	 */
	public static LoadBalancingStatusCache getInstance() {
		if(instance==null) {
			instance = new LoadBalancingStatusCache();
			instance.masterStati = new ConcurrentHashMap<Integer,LoadBalancingMasterStatus>();
			
			instance.counter = 0;
		}
		return instance;
	}
	
	/**
	 * private Constructor -> use getInstance().
	 */
	private LoadBalancingStatusCache() {
	}

	/**
	 * Adds new Status for QueryPart and generates new Id.
	 * @param part QueryPart to add.
	 * @return LoadBalancingProcessId.
	 */
	public int createNewLocalProcess(ILogicalQueryPart part) {
		
		LoadBalancingMasterStatus status = new LoadBalancingMasterStatus();
		status.setOriginalPart(part);
		
		masterStati.put(counter, status);
		int loadBalancingProcessId = counter;
		counter++;
		return loadBalancingProcessId;
	}
	
	public  LoadBalancingMasterStatus getStatusForLocalProcess(int loadBalancingProcessId) {
		return masterStati.get(loadBalancingProcessId);
	}
	
	public void deleteLocalStatus(int loadBalancingProcessId) {
		if(masterStati.containsKey(loadBalancingProcessId))
			masterStati.remove(loadBalancingProcessId);
	}
	
	public LoadBalancingSlaveStatus getSlaveStatus(PeerID peer, int lbProcessId) {
		if(slaveStati.contains(peer)) {
			ConcurrentHashMap<Integer,LoadBalancingSlaveStatus> statiForPeer = slaveStati.get(lbProcessId);
			if(statiForPeer.contains(lbProcessId)) {
				return statiForPeer.get(lbProcessId);
			}
		}
		return null;
	}
	
	public boolean storeSlaveStatus(PeerID peer, int lbProcessId, LoadBalancingSlaveStatus status) {
		if(!slaveStati.contains(peer)) {
			slaveStati.put(peer, new ConcurrentHashMap<Integer,LoadBalancingSlaveStatus>());
		}
		ConcurrentHashMap<Integer,LoadBalancingSlaveStatus> statiForPeer = slaveStati.get(peer);
		if(!statiForPeer.contains(lbProcessId)) {
			statiForPeer.put(lbProcessId, status);
			return true;
		}
		return false;
	}
	
	public void deleteSlaveStatus(PeerID peer, int lbProcessID) {
		if(slaveStati.contains(peer)) {
			ConcurrentHashMap<Integer,LoadBalancingSlaveStatus> statiForPeer = slaveStati.get(peer);
			if(statiForPeer.contains(lbProcessID)) {
				statiForPeer.remove(lbProcessID);
			}
			if(statiForPeer.size()==0) {
				slaveStati.remove(peer);
			}
		}
		
		
	}
	
	
	

}