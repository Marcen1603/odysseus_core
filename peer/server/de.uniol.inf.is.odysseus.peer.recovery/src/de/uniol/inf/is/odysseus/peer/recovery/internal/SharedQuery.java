package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.List;

import net.jxta.id.ID;

/**
 * This class is a helper to have a store which contains the shared query id as
 * well as the associated PQL
 * 
 * @author Tobias Brandt
 *
 */
public class SharedQuery {

	private ID sharedQueryID;
	private List<String> pqlParts;

	public SharedQuery(ID sharedQueryID, List<String> pqlParts) {
		super();
		this.sharedQueryID = sharedQueryID;
		this.pqlParts = pqlParts;
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	public void setSharedQueryID(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}

	public List<String> getPqlParts() {
		return pqlParts;
	}

	public void setPqlParts(List<String> pqlParts) {
		this.pqlParts = pqlParts;
	}

}
