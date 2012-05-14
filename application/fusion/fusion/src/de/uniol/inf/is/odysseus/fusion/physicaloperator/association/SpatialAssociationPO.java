package de.uniol.inf.is.odysseus.fusion.physicaloperator.association;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class SpatialAssociationPO  extends AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	
	public SpatialAssociationPO(SDFSchema outputSchema) { 
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IMetaAttribute> tuple, int port) {

		transfer(tuple);
		process_done();
		throw new RuntimeException("Association is not Implemented.");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> clone() {
		return this.clone();
	}

}
