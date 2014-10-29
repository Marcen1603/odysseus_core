package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class UncontrollablePhysicalSubscription<K> extends
		AbstractPhysicalSubscription<K> {

	private static final long serialVersionUID = 944595164816524572L;

	public UncontrollablePhysicalSubscription(K target, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
	}
}