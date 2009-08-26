package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.ObjectInputStream;

import de.uniol.inf.is.odysseus.physicaloperator.base.access.IAtomicDataHandler;

public abstract class AbstractAtomicDataHandler implements IAtomicDataHandler {

	private ObjectInputStream stream;
		
	public void setStream(ObjectInputStream stream) {
		this.stream = stream;
	}
	
	final public ObjectInputStream getStream() {
		return stream;
	}
}
