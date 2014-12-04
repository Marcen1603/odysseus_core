package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

/**
 * The RecoveryProcessState handles data for a given recovery process. These
 * Data are needed for reallocation. Every RecoveryProcessState has Substates.
 * These SubStates represents the allocation of a single QueryPart. If the
 * allocation of a queryPart is successful, the corresponding subProcess will be
 * removed.
 * 
 * The substate represents a single queryPart and also holds a list of
 * inadequate peers. It also contains the queryPart.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class RecoverySubProcessState {

	private UUID identifier;
	private ID sharedQueryId;
	private List<PeerID> inadequatePeers;
	private ILogicalQueryPart queryPart;

	public RecoverySubProcessState(ID sharedQueryId, ILogicalQueryPart queryPart) {
		this.sharedQueryId = sharedQueryId;
		this.queryPart = queryPart;
		this.identifier = UUID.randomUUID();
		this.inadequatePeers = new ArrayList<PeerID>();
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public ILogicalQueryPart getQueryPart() {
		return queryPart;
	}

	public void setQueryPart(ILogicalQueryPart queryPart) {
		this.queryPart = queryPart;
	}

	public List<PeerID> getInadequatePeers() {
		return inadequatePeers;
	}

	public void setInadequatePeers(List<PeerID> inadequatePeers) {
		this.inadequatePeers = inadequatePeers;
	}

}
