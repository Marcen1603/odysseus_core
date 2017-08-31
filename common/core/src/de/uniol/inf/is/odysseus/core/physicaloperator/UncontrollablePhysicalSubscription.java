package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class UncontrollablePhysicalSubscription extends
		AbstractPhysicalSubscription {

	private static final long serialVersionUID = 944595164816524572L;

	public UncontrollablePhysicalSubscription(ISource<IStreamObject<?>> source, ISink<IStreamObject<?>> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(source, sink, sinkInPort, sourceOutPort, schema);
	}
}