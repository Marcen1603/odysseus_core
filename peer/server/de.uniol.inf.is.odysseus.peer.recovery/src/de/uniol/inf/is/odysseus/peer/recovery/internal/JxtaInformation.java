package de.uniol.inf.is.odysseus.peer.recovery.internal;

import net.jxta.id.ID;

public class JxtaInformation {

	private ID sharedQueryID;
	private String key;
	private String value;

	public JxtaInformation(ID sharedQueryID, String key, String value) {
		super();
		this.sharedQueryID = sharedQueryID;
		this.key = key;
		this.value = value;
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	public void setSharedQueryID(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
