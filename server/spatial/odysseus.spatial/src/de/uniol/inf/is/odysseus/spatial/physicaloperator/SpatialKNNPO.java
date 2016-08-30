package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;

public class SpatialKNNPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private int geometryPosition;
	private int k;
	
	public SpatialKNNPO(IMovingObjectDataStructure dataStructure, int geometryPosition, int k) {
		this.dataStructure = dataStructure;
		this.geometryPosition = geometryPosition;
		this.k = k;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		if (object instanceof Tuple) {
			// TODO Neighbors are the whole tuples, not only a list of points. How to do this best?
			List<Tuple<?>> neighbors = this.dataStructure.getKNN((Tuple<?>) object, geometryPosition, this.k);
			Tuple<?> newTuple = ((Tuple<?>) object).append(neighbors);
			transfer((T) newTuple);
		}
		
	}

}
