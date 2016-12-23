package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.FastQuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.SpatialDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;

public class SpatialRangePO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private int geometryPosition;
	private double range;

	public SpatialRangePO(int geometryPosition, double range) {
		this.geometryPosition = geometryPosition;
		this.range = range;

		int rand = (int) (Math.random() * 1000);
		this.dataStructure = SpatialDataStructureProvider.getInstance().getOrCreateDataStructure("spatialRange" + rand,
				FastQuadTreeSTDataStructure.TYPE, geometryPosition);
	}

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

		Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) object;

		// Port mapping:
		// 0: objects to store in the data structure
		// 1: objects to query for

		if (port == 0) {
			this.dataStructure.add(tuple);
		} else if (port == 1) {
			this.dataStructure.cleanUp(tuple.getMetadata().getStart());
			List<Tuple<ITimeInterval>> neighbors = queryObject(tuple);

			// IAttributeResolver attributeResolver = new
			// DirectAttributeResolver(this.getOutputSchema());
			// IPredicate test =
			// OperatorBuilderFactory.getPredicateBuilder("RELATIONALPREDICATE")
			// .createPredicate(attributeResolver, "MMSI != 316652310");

			// Tuple[] tuples = neighbors.stream().filter(e ->
			// test.evaluate(e)).toArray(size -> new Tuple[size]);
			// List<Tuple> filteredNeighbors = new
			// ArrayList<Tuple>(Arrays.asList(tuples));

			Tuple<?> newTuple = tuple.append(neighbors);
			transfer((T) newTuple);
		}
	}

	/**
	 * Queries the data structure and returns the list with tuples that are in
	 * the given range
	 * 
	 * @param tuple
	 *            The tuple you want to have the neighbors for
	 * @return The neighbors (full tuples) or null, if no geometry can be found
	 *         in the given tuple.
	 */
	private List<Tuple<ITimeInterval>> queryObject(Tuple<ITimeInterval> tuple) {
		Object o = tuple.getAttribute(geometryPosition);
		GeometryWrapper geometryWrapper = null;
		if (o instanceof GeometryWrapper) {
			geometryWrapper = (GeometryWrapper) o;
			return this.dataStructure.queryCircle(geometryWrapper.getGeometry(), this.range,
					new TimeInterval(tuple.getMetadata().getStart(), tuple.getMetadata().getEnd()));
		}
		return null;
	}
}
