package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public class RecoveryProcessState {

	private UUID identifier;
	private PeerID failedPeerID;

	// maps the subprocessIdentifier to the subprocess
	private Map<UUID, RecoverySubProcessState> subProcesses;

	// maps sharedQueryId to allocationMap, needed for reallocation
	private Map<ID, Map<ILogicalQueryPart, PeerID>> allocationMaps;

	public RecoveryProcessState(PeerID failedPeerID) {
		this.failedPeerID = failedPeerID;
		identifier = UUID.randomUUID();
		subProcesses = new HashMap<UUID, RecoverySubProcessState>();
		allocationMaps = new HashMap<ID, Map<ILogicalQueryPart, PeerID>>();
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public PeerID getFailedPeerId() {
		return failedPeerID;
	}

	public UUID createNewSubprocess(ID sharedQueryId,
			ILogicalQueryPart queryPart) {
		RecoverySubProcessState subprocess = new RecoverySubProcessState(
				sharedQueryId, queryPart);
		subProcesses.put(subprocess.getIdentifier(), subprocess);
		return subprocess.getIdentifier();
	}

	public RecoverySubProcessState getRecoverySubprocess(
			UUID subprocessIdentifier) {
		return subProcesses.get(subprocessIdentifier);
	}

	public List<PeerID> getInadequatePeers(UUID subprocessIdentifier) {
		if (!subProcesses.containsKey(subprocessIdentifier)){
			return new ArrayList<PeerID>();
		}
		return subProcesses.get(subprocessIdentifier).getInadequatePeers();
	}

	public void addInadequatePeer(PeerID peerId, UUID subprocessIdentifier) {
		if (subProcesses.containsKey(subprocessIdentifier)){			
			subProcesses.get(subprocessIdentifier).getInadequatePeers().add(peerId);
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

	public Map<ILogicalQueryPart, PeerID> getAllocationMap(ID sharedQueryId) {
		if (allocationMaps.isEmpty()) {
			return null;
		}
		if (allocationMaps.containsKey(sharedQueryId)) {
			return allocationMaps.get(sharedQueryId);
		}
		return null;
	}

	public void setAllocationMap(ID sharedQueryId,
			Map<ILogicalQueryPart, PeerID> allocationMap) {
		this.allocationMaps.put(sharedQueryId, allocationMap);
	}
}
