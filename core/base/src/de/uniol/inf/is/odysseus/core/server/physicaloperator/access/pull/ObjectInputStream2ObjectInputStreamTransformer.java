package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.ObjectInputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;


public class ObjectInputStream2ObjectInputStreamTransformer implements
		IToObjectInputStreamTransformer<ObjectInputStream> {

	public ObjectInputStream2ObjectInputStreamTransformer() {
	}
	
	@Override
	public ITransformer<ObjectInputStream, ObjectInputStream> getInstance(
			Map<String, String> options, SDFSchema schema) {
		return new ObjectInputStream2ObjectInputStreamTransformer();
	}
	
	@Override
	public ObjectInputStream transform(ObjectInputStream input) {
		return input;
	}

	@Override
	public String getName() {
		return "ObjectInputStream2ObjectInputStream";
	}
}
