package de.uniol.inf.is.odysseus.physicaloperator.base.event;

import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class POPortEvent extends POEvent {

	private static final long serialVersionUID = 2873569132048007984L;
	private int port = -1;

	public int getPort() {
		return port;
	}

	public POPortEvent(ISource<?> source, POEventType type, int port) {
		super(source, type);
		this.port = port;
	}

	public POPortEvent(ISink<?> source, POEventType type, int port) {
		super(source, type);
		this.port = port;
	}
	
	public POPortEvent(IPipe<?,?> source, POEventType type, int port) {
		super(source, type);
		this.port = port;
	}

}
