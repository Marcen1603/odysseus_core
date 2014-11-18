package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

/**
 * Store for Stati used in LoadBalancing. Each QueryPart has a id, which can be
 * used to identify a loadBalancingProcess. Implemented as Singleton.
 * 
 * @author Carsten Cordes
 *
 */
public class LoadBalancingStatusCache {

	/**
	 * HashMap used to store the Master-Peer-Stati with a particular id.
	 */
	private ConcurrentHashMap<Integer, ILoadBalancingMasterStatus> masterStati;

	/**
	 * HashMap used to store Slave-Peer-Stati referenced by MasterPeerId and
	 * Process Id of current loadbalancing process.
	 */
	private ConcurrentHashMap<PeerID, ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus>> slaveStati;

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
	 * 
	 * @return instance of Cache
	 */
	public static LoadBalancingStatusCache getInstance() {
		if (instance == null) {
			instance = new LoadBalancingStatusCache();
			instance.masterStati = new ConcurrentHashMap<Integer, ILoadBalancingMasterStatus>();
			instance.slaveStati = new ConcurrentHashMap<PeerID, ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus>>();
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
	 * Adds new Status and generates new Id.
	 * 
	 * @param status
	 *            Status to add.
	 * @return LoadBalancingProcessId.
	 */
	public int storeLocalProcess(ILoadBalancingMasterStatus status) {
		status.setProcessId(counter);
		masterStati.put(counter, status);
		int loadBalancingProcessId = counter;
		counter++;
		return loadBalancingProcessId;
	}

	/**
	 * Returns status on a MasterPeer.
	 * 
	 * @param loadBalancingProcessId
	 *            LoadBalancing Process Id of current process.
	 * @return null if no status present.
	 */
	public ILoadBalancingMasterStatus getStatusForLocalProcess(
			int loadBalancingProcessId) {
		return masterStati.get(loadBalancingProcessId);
	}

	/**
	 * Deletes status on a MasterPeer.
	 * 
	 * @param loadBalancingProcessId
	 *            LoadBalancing Process Id of current process.
	 * @return
	 */
	public void deleteLocalStatus(int loadBalancingProcessId) {
		if (masterStati.containsKey(loadBalancingProcessId))
			masterStati.remove(loadBalancingProcessId);
	}

	/**
	 * Returns status on slave peer
	 * 
	 * @param peer
	 *            Master-Peer-Id
	 * @param lbProcessId
	 *            Id of loadbalancing Process
	 * @return null no status present.
	 */
	public ILoadBalancingSlaveStatus getSlaveStatus(PeerID peer, int lbProcessId) {
		if (slaveStati.containsKey(peer)) {
			ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus> statiForPeer = slaveStati
					.get(peer);
			if (statiForPeer.containsKey(lbProcessId)) {
				return statiForPeer.get(lbProcessId);
			}
		}
		return null;
	}

	/**
	 * Stores a new Slave Status in Cache
	 * 
	 * @param peer
	 *            Master-Peer-Id
	 * @param lbProcessId
	 *            Loadbalancing Process Id
	 * @param status
	 *            status to save
	 * @return true if saving worked, false if error occured.
	 */
	public boolean storeSlaveStatus(PeerID peer, int lbProcessId,
			ILoadBalancingSlaveStatus status) {
		if (!slaveStati.contains(peer)) {
			slaveStati
					.put(peer,
							new ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus>());
		}
		ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus> statiForPeer = slaveStati
				.get(peer);
		if (!statiForPeer.contains(lbProcessId)) {
			statiForPeer.put(lbProcessId, status);
			return true;
		}
		return false;
	}

	/***
	 * Deletes Status on Slave peer
	 * 
	 * @param peer
	 *            PeerID of Master Peer
	 * @param lbProcessID
	 *            LoadBalancingProcess ID
	 */
	public void deleteSlaveStatus(PeerID peer, int lbProcessID) {
		if (slaveStati.contains(peer)) {
			ConcurrentHashMap<Integer, ILoadBalancingSlaveStatus> statiForPeer = slaveStati
					.get(peer);
			if (statiForPeer.contains(lbProcessID)) {
				statiForPeer.remove(lbProcessID);
			}
			if (statiForPeer.size() == 0) {
				slaveStati.remove(peer);
			}
		}

	}

}