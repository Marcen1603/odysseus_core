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
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialKNNAO;

/**
 * The operator calls the kNN (k-nearest-neighbors) method from the given data
 * structure for the given input geometry and puts out the result in a list.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
@SuppressWarnings("deprecation")
public class SpatialKNNPO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private ISpatioTemporalDataStructure dataStructure;
	private int geometryPosition;
	private int k;

	public SpatialKNNPO(SpatialKNNAO ao) {
		this.geometryPosition = ao.getGeometryPosition();
		this.k = ao.getK();
		this.dataStructure = SpatioTemporalDataStructureProvider.getInstance().getDataStructure(ao.getDataStructureName());
		if (this.dataStructure == null) {
			throw new RuntimeException("The spatial data structure with the name " + ao.getDataStructureName()
					+ " does not exist. Use a SpatialStore operator to create and fill one.");
		}
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
		this.dataStructure.cleanUp(tuple.getMetadata().getStart());

		// Get the point from which we want to get the neighbors
		Object o = ((Tuple<?>) object).getAttribute(geometryPosition);
		GeometryWrapper geometryWrapper = null;
		if (o instanceof GeometryWrapper) {
			geometryWrapper = (GeometryWrapper) o;
			List<Tuple<ITimeInterval>> neighbors = this.dataStructure.queryKNN(geometryWrapper.getGeometry(), this.k,
					new TimeInterval(tuple.getMetadata().getStart(), tuple.getMetadata().getEnd()));
			Tuple<ITimeInterval> newTuple = ((Tuple<ITimeInterval>) object).append(neighbors);
			transfer((T) newTuple);
		}
	}
}
