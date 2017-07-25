package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.ISpatioTemporalDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.SpatioTemporalDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialRangeAO;

/**
 * This operator searches for all objects in the given range around the object
 * that flows into this operator. It uses the queryCircle method of the data
 * structure.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class SpatialRangePO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private ISpatioTemporalDataStructure dataStructure;
	private int geometryPosition;
	private double range;

	public SpatialRangePO(SpatialRangeAO ao) {
		this.geometryPosition = ao.getInputSchema(0).findAttributeIndex(ao.getGeometryAttribute());

		this.range = ao.getRange();

		this.dataStructure = SpatioTemporalDataStructureProvider.getInstance()
				.getDataStructure(ao.getDataStructureName());
		if (this.dataStructure == null) {
			throw new RuntimeException("The spatial data structure with the name " + ao.getDataStructureName()
					+ " does not exist. Use a SpatialStore operator to create and fill one.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {

		Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) object;

		this.dataStructure.cleanUp(tuple.getMetadata().getStart());
		List<Tuple<ITimeInterval>> neighbors = queryObject(tuple);

		/*
		 * TODO What to do with this? Give the option or better only filter
		 * later? Right now I think this should not be part of this operator.
		 * 
		 * Other argument: Put it into the data structures. Why? Well, if all
		 * elements within the range are from the same vessel (which is
		 * especially the case with kNN) but I want results from all other
		 * vessels but me, I end up having nothing.
		 */

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

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
}
