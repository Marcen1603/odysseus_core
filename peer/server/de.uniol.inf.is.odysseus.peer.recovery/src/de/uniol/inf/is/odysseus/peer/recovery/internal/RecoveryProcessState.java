package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * The RecoveryProcessState handles data for a given recovery process. These
 * Data are needed for reallocation. Every RecoveryProcessState has Substates.
 * These SubStates represents the allocation of a single QueryPart. If the
 * allocation of a queryPart is successful, the corresponding subProcess will be
 * removed. 
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class RecoveryProcessState {
	// identifier of the process
	private UUID identifier;
	
	// the failed peer, which need to be recovered
	private PeerID failedPeerID;

	// maps the subprocessIdentifier to the subprocess
	private Map<UUID, RecoverySubProcessState> subProcesses;

	// maps local query id to allocationMap, needed for reallocation
	private Map<Integer, Map<ILogicalQueryPart, PeerID>> allocationMaps;

	public RecoveryProcessState(PeerID failedPeerID) {
		this.failedPeerID = failedPeerID;
		identifier = UUID.randomUUID();
		subProcesses = new HashMap<UUID, RecoverySubProcessState>();
		allocationMaps = new HashMap<Integer, Map<ILogicalQueryPart, PeerID>>();
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public PeerID getFailedPeerId() {
		return failedPeerID;
	}

	public UUID createNewSubprocess(int localQueryId,
			ILogicalQueryPart queryPart, QueryState queryState) {
		RecoverySubProcessState subprocess = new RecoverySubProcessState(
				localQueryId, queryPart, queryState);
		subProcesses.put(subprocess.getIdentifier(), subprocess);
		return subprocess.getIdentifier();
	}

	public RecoverySubProcessState getRecoverySubprocess(
			UUID subprocessIdentifier) {
		return subProcesses.get(subprocessIdentifier);
	}

	public List<PeerID> getInadequatePeers(UUID subprocessIdentifier) {
		if (!subProcesses.containsKey(subprocessIdentifier)) {
			return new ArrayList<PeerID>();
		}
		return subProcesses.get(subprocessIdentifier).getInadequatePeers();
	}

	public void addInadequatePeer(PeerID peerId, UUID subprocessIdentifier) {
		if (subProcesses.containsKey(subprocessIdentifier)) {
			subProcesses.get(subprocessIdentifier).getInadequatePeers()
					.add(peerId);
		}
	}

	public void subprocessIsDone(UUID subprocessIdentifier) {
		subProcesses.remove(subprocessIdentifier);
	}

	public Collection<RecoverySubProcessState> getCurrentSubprocesses() {
		return subProcesses.values();
	}

	public boolean allSubprocessesDone() {
		return subProcesses.isEmpty();
	}

	public Map<ILogicalQueryPart, PeerID> getAllocationMap(int localQueryId) {
		if (allocationMaps.isEmpty()) {
			return null;
		}
		if (allocationMaps.containsKey(localQueryId)) {
			return allocationMaps.get(localQueryId);
		}
		return null;
	}

	public void setAllocationMap(int localQueryId,
			Map<ILogicalQueryPart, PeerID> allocationMap) {
		this.allocationMaps.put(localQueryId, allocationMap);
	}
}
