package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.ObjectInputStream;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;


public class ObjectInput2ObjectInputStreamTransformer implements
		IToObjectInputStreamTransformer<ObjectInputStream> {

	@Override
	public ObjectInputStream transform(ObjectInputStream input) {
		return input;
	}

}
