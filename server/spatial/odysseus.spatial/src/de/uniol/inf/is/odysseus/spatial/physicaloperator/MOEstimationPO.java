package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMODataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.estimation.Estimator;
import de.uniol.inf.is.odysseus.spatial.estimation.ExtendedRadiusEstimatior;
import de.uniol.inf.is.odysseus.spatial.estimation.TimeCircleEstimator;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOEstimationAO;

public class MOEstimationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	private int pointInTimePosition;
	private GeoHashMODataStructure index;

	private Set<String> allIds;

	private int idAttributeIndex;
	private int centerMovingObjectAttributeIndex;
	private int geometryAttributeIndex;

	private double radius;
	private static final double radiusExtensionFactor = 3;

	private Estimator predictionEstimator;

	public MOEstimationPO(MOEstimationAO ao) {
		this.geometryAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.pointInTimePosition = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getPointInTimeAttribute());
		this.centerMovingObjectAttributeIndex = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getCenterMovingObjectAttribute());

		// TODO Name and "length" is not correct here. Remove length and use a time
		// window.
		this.index = new GeoHashMODataStructure("EstimationPO" + this.hashCode(), this.geometryAttributeIndex, 1000);
		this.allIds = new HashSet<>();

		this.radius = ao.getRadius();

		Option estimatorOption = null;
		long numberOfIterations = 0;
		for (Option option : ao.getOptions()) {
			if (option.getName().equals("estimator")) {
				estimatorOption = option;
			} else if (option.getName().equals("iterations")) {
				numberOfIterations = (long) ao.getOptions().get(1).getValue();
			}
		}

		if (estimatorOption.getValue().equals("timeCircle")) {
			this.predictionEstimator = new TimeCircleEstimator(index, radiusExtensionFactor, (int) numberOfIterations, 15);
		} else {
			this.predictionEstimator = new ExtendedRadiusEstimatior(index, radiusExtensionFactor);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if (port == 0) {
			processTrajectoryTuple(object);
		} else if (port == 1) {
			processTimeTuple(object);
		}
	}

	private void processTrajectoryTuple(T object) {
		Geometry geometry = ((GeometryWrapper) object.getAttribute(this.geometryAttributeIndex)).getGeometry();
		String id = "";
		if (object.getAttribute(this.idAttributeIndex) instanceof Long) {
			id = String.valueOf((Long) object.getAttribute(this.idAttributeIndex));
		} else if (object.getAttribute(this.idAttributeIndex) instanceof Integer) {
			id = String.valueOf((Integer) object.getAttribute(this.idAttributeIndex));
		} else if (object.getAttribute(this.idAttributeIndex) instanceof String) {
			id = (String) object.getAttribute(this.idAttributeIndex);
		}

		this.allIds.add(id);

		LocationMeasurement locationMeasurement = new LocationMeasurement(geometry.getCoordinate().x,
				geometry.getCoordinate().y, 0, 0, object.getMetadata().getStart(), id);
		this.index.add(locationMeasurement, object);
	}

	@SuppressWarnings("unchecked")
	private void processTimeTuple(T object) {

		// Get the point in time to which the moving objects need to be
		// predicted
		long pointInTime = 0;
		if (object.getAttribute(this.pointInTimePosition) instanceof Long) {
			pointInTime = object.getAttribute(this.pointInTimePosition);
		}

		long centerMovingObjectId = object.getAttribute(this.centerMovingObjectAttributeIndex);

		// As a first attempt get a list with all known moving objects
		List<String> allIdsAsList = new ArrayList<>();

		// Within this extended circle, we will predict all objects
		allIdsAsList.addAll(this.predictionEstimator.estimateObjectsToPredict("" + centerMovingObjectId, this.radius,
				new PointInTime(pointInTime)));

		// And put out a tuple with the name of the dataStructure
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(3, false);
		tuple.setAttribute(0, pointInTime);
		tuple.setAttribute(1, centerMovingObjectId);
		tuple.setAttribute(2, allIdsAsList);
		tuple.setMetadata(object.getMetadata().clone());
		transfer((T) tuple);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Clean up index
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
