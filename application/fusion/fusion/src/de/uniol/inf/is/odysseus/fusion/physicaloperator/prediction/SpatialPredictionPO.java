package de.uniol.inf.is.odysseus.fusion.physicaloperator.prediction;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class SpatialPredictionPO  extends AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	public SpatialPredictionPO(SDFSchema outputSchema) { 
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IMetaAttribute> object, int port) {
		// TODO Auto-generated method stub
		// System.out.println("Inputport: " + port + " Tuple: " + object.toString());
		System.out.println("Start: " + ((ITimeInterval)object.getMetadata()).getStart() + " Ende: " + ((ITimeInterval)object.getMetadata()).getEnd() + " Interval: " + (((ITimeInterval)object.getMetadata()).getEnd().minus(((ITimeInterval)object.getMetadata()).getStart())));
		if(port == 0)
			transfer(object);
		
		System.out.println(object.getAttribute(3));
		
		process_done();
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
