package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;

public class SpatialRangePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private int geometryPosition;
	private double range;

	public SpatialRangePO(IMovingObjectDataStructure dataStructure, int geometryPosition, double range) {
		this.dataStructure = dataStructure;
		this.geometryPosition = geometryPosition;
		this.range = range;
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
		if (object instanceof Tuple) {
			// TODO Neighbors are the whole tuples, not only a list of points.
			// How to do this best?

			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) object;

			// Get the point from which we want to get the neighbors
			Object o = ((Tuple<?>) object).getAttribute(geometryPosition);
			GeometryWrapper geometryWrapper = null;
			if (o instanceof GeometryWrapper) {
				geometryWrapper = (GeometryWrapper) o;
				List<Tuple<?>> neighbors = this.dataStructure.getNeighborhood(geometryWrapper.getGeometry(), this.range,
						new TimeInterval(tuple.getMetadata().getStart(), tuple.getMetadata().getEnd()));
				Tuple<?> newTuple = ((Tuple<?>) object).append(neighbors);
				transfer((T) newTuple);
			}
		}
	}
}
