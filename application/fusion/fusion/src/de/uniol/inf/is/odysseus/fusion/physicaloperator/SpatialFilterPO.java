package de.uniol.inf.is.odysseus.fusion.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SpatialFilterPO  extends AbstractPipe<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	public SpatialFilterPO(SDFSchema outputSchema) { 
		super();
		this.setOutputSchema(outputSchema);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(RelationalTuple<? extends IMetaAttribute> object, int port) {
		
		// compute the distance between Predicted State and Measurement 
		
		
		
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
	public AbstractPipe<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> clone() {
		return this.clone();
	}

}
