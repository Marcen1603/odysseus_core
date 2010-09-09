package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.BufferedInputStream;

public abstract class AbstractAtomicByteDataHandler implements IAtomicByteDataHandler {

	private BufferedInputStream stream;
		
	public void setStream(BufferedInputStream stream) {
		this.stream = stream;
	}
	
	final public BufferedInputStream getStream() {
		return stream;
	}
}
