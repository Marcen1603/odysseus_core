package de.uniol.inf.is.odysseus.scars.base.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVCDataInputStreamAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	@Override
	public SDFAttributeList getOutputSchema() {
		
		return null;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void transferNext() {
		// TODO Auto-generated method stub
		
	}

}
