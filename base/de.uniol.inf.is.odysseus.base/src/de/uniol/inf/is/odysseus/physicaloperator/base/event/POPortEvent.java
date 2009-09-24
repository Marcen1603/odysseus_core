package de.uniol.inf.is.odysseus.physicaloperator.base.event;

import de.uniol.inf.is.odysseus.physicaloperator.base.IPOEventSender;

public class POPortEvent extends POEvent {

	private static final long serialVersionUID = 2873569132048007984L;
	private int port = -1;

	public int getPort() {
		return port;
	}

	public POPortEvent(IPOEventSender source, POEventType type, int port) {
		super(source, type);
		this.port = port;
	}

}
