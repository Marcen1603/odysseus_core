package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.jxta.id.ID;
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
 * The substate represents a single queryPart and also holds a list of
 * inadequate peers. It also contains the queryPart.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class RecoverySubProcessState {

	private UUID identifier;
	private int localQueryId;
	private List<PeerID> inadequatePeers;
	private ILogicalQueryPart queryPart;
	private QueryState queryState;
	private ID sharedQuery;
	private boolean master;
	private PeerID masterId;
	
	// Connection Information
	private String clientIp;
	private String oldHostIP;
	private int oldHostPort;

	public RecoverySubProcessState(int localQueryId,
			ILogicalQueryPart queryPart, QueryState queryState, ID sharedQuery,
			boolean master, PeerID masterId, String clientIp, String oldHostIP, int oldHostPort) {
		this.localQueryId = localQueryId;
		this.queryPart = queryPart;
		this.queryState = queryState;
		this.sharedQuery = sharedQuery;
		this.master = master;
		this.masterId = masterId;
		this.identifier = UUID.randomUUID();
		this.inadequatePeers = new ArrayList<PeerID>();
		this.clientIp = clientIp;
		this.oldHostIP = oldHostIP;
		this.oldHostPort = oldHostPort;
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public int getLocalQueryId() {
		return localQueryId;
	}

	public void setLocalQueryId(int localQueryId) {
		this.localQueryId = localQueryId;
	}

	public ID getSharedQueryId() {
		return sharedQuery;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQuery = sharedQueryId;
	}

	public boolean isMaster() {
		return this.master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public PeerID getMasterId() {
		return this.masterId;
	}

	public void setMasterId(PeerID id) {
		this.masterId = id;
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

	public QueryState getQueryState() {
		return this.queryState;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getOldHostIP() {
		return oldHostIP;
	}

	public void setOldHostIP(String oldHostIP) {
		this.oldHostIP = oldHostIP;
	}

	public int getOldHostPort() {
		return oldHostPort;
	}

	public void setOldHostPort(int oldHostPort) {
		this.oldHostPort = oldHostPort;
	}
	
	

}
