package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * Preserves a loadBalancing Status in the initiating Peer to control LoadBalancing.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingStatus {
	
	public enum LB_PHASES {
		initiating,copying,relinking,synchronizing,failure
	}
	
	private LB_PHASES phase = LB_PHASES.initiating;
	
	private int processId;
	private int logicalQuery;
	private ILogicalQueryPart originalPart;
	private ILogicalQueryPart modifiedPart;
	private HashMap<String,String> replacedPipes;
	
	Integer[] foreignQueryIDs;
	
	private ArrayList<String> pipesInstalled;
	public Integer[] getForeignQueryIDs() {
		return foreignQueryIDs;
	}
	public void setForeignQueryIDs(Integer[] foreignQueryIDs) {
		this.foreignQueryIDs = foreignQueryIDs;
	}
	public ArrayList<String> getPipesInstalled() {
		return pipesInstalled;
	}
	public void setPipesInstalled(ArrayList<String> pipesInstalled) {
		this.pipesInstalled = pipesInstalled;
	}
	public ArrayList<String> getPipesToInstall() {
		return pipesToInstall;
	}
	public void setPipesToInstall(ArrayList<String> pipesToInstall) {
		this.pipesToInstall = pipesToInstall;
	}
	public HashMap<String, PeerID> getPeersForPipe() {
		return peersForPipe;
	}
	public void setPeersForPipe(HashMap<String, PeerID> peersForPipe) {
		this.peersForPipe = peersForPipe;
	}
	private ArrayList<String> pipesToInstall;
	private HashMap <String,PeerID> peersForPipe;
	
	private PeerID volunteeringPeer;
	
	

	public LB_PHASES getPhase() {
		return phase;
	}
	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public int getLogicalQuery() {
		return logicalQuery;
	}
	public void setLogicalQuery(int logicalQuery) {
		this.logicalQuery = logicalQuery;
	}
	public ILogicalQueryPart getOriginalPart() {
		return originalPart;
	}
	public void setOriginalPart(ILogicalQueryPart originalPart) {
		this.originalPart = originalPart;
	}
	public ILogicalQueryPart getModifiedPart() {
		return modifiedPart;
	}
	public void setModifiedPart(ILogicalQueryPart modifiedPart) {
		this.modifiedPart = modifiedPart;
	}
	public PeerID getVolunteeringPeer() {
		return volunteeringPeer;
	}
	public void setVolunteeringPeer(PeerID volunteeringPeer) {
		this.volunteeringPeer = volunteeringPeer;
	}
	public HashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}
	public void setReplacedPipes(HashMap<String, String> replacedPipes) {
		this.replacedPipes = replacedPipes;
	}
	public synchronized void markPipeInstalled(String pipeID) {
		if(pipesToInstall.contains(pipeID)) {
			pipesToInstall.remove(pipeID);
			pipesInstalled.add(pipeID);
		}
		
	}
	
	public int pipesLeft() {
		return pipesToInstall.size();
	}
	
	
	
}
