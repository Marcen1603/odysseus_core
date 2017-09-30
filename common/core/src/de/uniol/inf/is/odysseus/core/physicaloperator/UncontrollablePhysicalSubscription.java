package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class UncontrollablePhysicalSubscription<I extends ISource<IStreamObject<?>>,O extends ISink<IStreamObject<?>>> extends
		AbstractPhysicalSubscription<I,O> {

	private static final long serialVersionUID = 944595164816524572L;

	public UncontrollablePhysicalSubscription(I source, O sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(source, sink, sinkInPort, sourceOutPort, schema);
	}
}