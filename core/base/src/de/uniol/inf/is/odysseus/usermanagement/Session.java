package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.Date;

final public class Session implements Serializable {

	private static final long serialVersionUID = 6601485682039247781L;
	final int key;
	private long timestamp;

	Session(int key) {
		synchronized (this) {
			this.key = key;
			updateTimestamp();
		}
	}

	synchronized void updateTimestamp() {
		this.timestamp = System.currentTimeMillis();
	}

	synchronized long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String toString() {
		return key+" "+new Date(timestamp);
	}
	
}
