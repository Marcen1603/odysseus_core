package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;

@SuppressWarnings("rawtypes")
public class DummyReceiverPO<T extends IStreamObject> extends AbstractSource<T> {

	public DummyReceiverPO() {
		
	}
	
	public DummyReceiverPO(DummyReceiverPO<T> other) {
		
	}
	
	public DummyReceiverPO(DummyAO dummyAO) {
		SDFSchema schema = dummyAO.getOutputSchema().clone();
		setOutputSchema(schema);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractSource<T> clone() {
		return new DummyReceiverPO<T>(this);
	}

}
