package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;

public class Session implements Serializable {

	private static final long serialVersionUID = 6601485682039247781L;
	final int key;
	private long timestamp;

	Session(int key) {
		this.key = key;
		setTimestamp();
	}

	protected void setTimestamp() {
		this.timestamp = System.currentTimeMillis();
	}

	protected long getTimestamp() {
		return this.timestamp;
	}

}
