package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.ObjectInputStream;

import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;

public abstract class AbstractAtomicDataHandler implements IAtomicDataHandler {

	private ObjectInputStream stream;
		
	@Override
	public void setStream(ObjectInputStream stream) {
		this.stream = stream;
	}
	
	final public ObjectInputStream getStream() {
		return stream;
	}
}
