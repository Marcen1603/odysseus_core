package de.uniol.inf.is.odysseus.scars.base.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVEDataInputStreamAccessMVPO <M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	@Override
	public SDFAttributeList getOutputSchema() {
		return null;
	}

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public void transferNext() {
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
