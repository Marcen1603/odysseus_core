package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;

public class SpatialQueryPO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private List<Point> polygonPoints;

	public SpatialQueryPO(IMovingObjectDataStructure dataStructure, List<Point> polygonPoints) {
		this.dataStructure = dataStructure;
		this.polygonPoints = polygonPoints;

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// As for now, a new input is just a trigger that the data structure has
		// changed. Therefore, we will put out the new result now.
		List<Tuple<?>> result = dataStructure.queryBoundingBox(polygonPoints);
		Tuple<?> newTuple = ((Tuple<?>) object).append(result);
		transfer((T) newTuple);
	}

}
