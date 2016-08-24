package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;

public class STDataStructurePO<T extends IStreamObject<?>> extends AbstractPipe<T, T>{
	
	private IMovingObjectDataStructure dataStructure;
	
	public STDataStructurePO(IMovingObjectDataStructure dataStructure) {
		this.dataStructure = dataStructure;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		this.dataStructure.add(object);
	}

}
