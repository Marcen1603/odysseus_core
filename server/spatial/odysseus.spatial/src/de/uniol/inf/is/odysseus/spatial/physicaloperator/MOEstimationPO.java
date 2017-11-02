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
import de.uniol.inf.is.odysseus.spatial.estimation.AllEstimator;
import de.uniol.inf.is.odysseus.spatial.estimation.ApproximateTimeCircleEstimator;
import de.uniol.inf.is.odysseus.spatial.estimation.Estimator;
import de.uniol.inf.is.odysseus.spatial.estimation.ExtendedRadiusEstimatior;
import de.uniol.inf.is.odysseus.spatial.estimation.TimeCircleEstimator;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashIndex;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOEstimationAO;

/**
 * Operator that estimates the moving objects that need to be predicted.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class MOEstimationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	// Use the time circle algorithm with exact circle calculations
	private static final String TIME_CIRCLE_ESTIMATION = "timecircle";
	/*
	 * Use the time circle algorithm but only approximate circles with underlying
	 * spatial index to avoid distance calculations
	 */
	private static final String APPROXIMATE_TIME_CIRCLE_ESTIMATION = "approximatetimecircle";
	// Simply extends the radius by a given factor
	private static final String EXTENDED_RADIUS_ESTIMATION = "extendedradius";

	private GeoHashMODataStructure index;
	private SpatialIndex spatialIndex;

	// True, if no estimation is used but all IDs are simply collected
	boolean collectAllIDs;
	// Will be filled if all IDs are collected
	private Set<String> allIDs;

	// Define the attribute indexes to find the attributes in the incoming tuples
	private int pointInTimeAttributeIndex;
	private int idAttributeIndex;
	private int centerMovingObjectAttributeIndex;
	private int geometryAttributeIndex;

	private double radius;

	private Estimator predictionEstimator;

	public MOEstimationPO(MOEstimationAO ao) {
		this.geometryAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.pointInTimeAttributeIndex = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getPointInTimeAttribute());
		this.centerMovingObjectAttributeIndex = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getCenterMovingObjectAttribute());

		// TODO Name and "length" is not correct here. Remove length and use a time
		// window. Remove old index structure by new spatial index
		this.index = new GeoHashMODataStructure("EstimationPO" + this.hashCode(), this.geometryAttributeIndex, 1000);
		this.spatialIndex = new GeoHashIndex();
		this.allIDs = new HashSet<>();
		this.radius = ao.getRadius();

		// Read the options to use the settings of the user
		long numberOfIterations = 0;
		String estimatorName = "";
		double radiusExtensionFactor = 1;
		double maxSpeed = 1;

		for (Option option : ao.getOptions()) {
			if (option.getName().equals("estimator")) {
				estimatorName = option.getValue();
			} else if (option.getName().equals("iterations")) {
				numberOfIterations = (long) option.getValue();
			} else if (option.getName().equals("extensionFactor")) {
				radiusExtensionFactor = option.getValue();
			} else if (option.getName().equals("maxSpeed")) {
				maxSpeed = option.getValue();
			}
		}

		// Define the estimator. If no one is defined, do not estimate but use all IDs.
		this.collectAllIDs = false;

		switch (estimatorName.toLowerCase()) {
		case TIME_CIRCLE_ESTIMATION:
			this.predictionEstimator = new TimeCircleEstimator(this.index, radiusExtensionFactor,
					(int) numberOfIterations, maxSpeed);
		case APPROXIMATE_TIME_CIRCLE_ESTIMATION:
			this.predictionEstimator = new ApproximateTimeCircleEstimator(this.spatialIndex, radiusExtensionFactor,
					(int) numberOfIterations, maxSpeed);
		case EXTENDED_RADIUS_ESTIMATION:
			this.predictionEstimator = new ExtendedRadiusEstimatior(this.index, radiusExtensionFactor);
		default:
			this.predictionEstimator = new AllEstimator(this.allIDs);
			this.collectAllIDs = true;
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

		if (this.collectAllIDs) {
			this.allIDs.add(id);
		} else {
			LocationMeasurement locationMeasurement = new LocationMeasurement(geometry.getCoordinate().x,
					geometry.getCoordinate().y, 0, 0, object.getMetadata().getStart(), id);
			this.spatialIndex.add(locationMeasurement, object);
		}
	}

	@SuppressWarnings("unchecked")
	private void processTimeTuple(T object) {

		// Get the point in time to which the moving objects need to be
		// predicted
		long pointInTime = 0;
		if (object.getAttribute(this.pointInTimeAttributeIndex) instanceof Long) {
			pointInTime = object.getAttribute(this.pointInTimeAttributeIndex);
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
