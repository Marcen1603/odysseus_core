package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.ObjectInputStream;


public class ObjectInputStreamTransformer implements
		IObjectInputStreamTransformer<ObjectInputStream> {

	@Override
	public ObjectInputStream transform(ObjectInputStream input) {
		return input;
	}

}
