package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.estimation.AllEstimator;
import de.uniol.inf.is.odysseus.spatial.estimation.ApproximateTimeCircleEstimator;
import de.uniol.inf.is.odysseus.spatial.estimation.Estimator;
import de.uniol.inf.is.odysseus.spatial.estimation.ExtendedRadiusEstimatior;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashTimeIntervalIndex;
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

	/*
	 * Use the time circle algorithm but only approximate circles with underlying
	 * spatial index to avoid distance calculations
	 */
	private static final String APPROXIMATE_TIME_CIRCLE_ESTIMATION = "approximatetimecircle";
	// Simply extends the radius by a given factor
	private static final String EXTENDED_RADIUS_ESTIMATION = "extendedradius";

	private SpatialIndex<ITimeInterval> spatialIndex;

	// True, if no estimation is used but all IDs are simply collected
	boolean collectAllIDs;
	// Will be filled if all IDs are collected
	private Set<String> allIDs;

	// Define the attribute indexes to find the attributes in the incoming tuples
	private int pointInTimeFutureAttributeIndex;
	private int pointInTimeNowAttributeIndex;
	private int idAttributeIndex;
	private int centerMovingObjectAttributeIndex;
	private int geometryAttributeIndex;

	private double radius;

	private Estimator predictionEstimator;

	// TODO Make customizable
	private long trajectoryStepSizeMs = 60000;

	public MOEstimationPO(MOEstimationAO ao) {
		this.geometryAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.pointInTimeFutureAttributeIndex = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getPointInTimeAttribute());
		if (ao.getPointInTimeNowAttribute() != null && !ao.getPointInTimeNowAttribute().isEmpty()) {
			this.pointInTimeNowAttributeIndex = ao.getInputSchema(DATA_PORT)
					.findAttributeIndex(ao.getPointInTimeNowAttribute());
		}
		this.centerMovingObjectAttributeIndex = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getCenterMovingObjectAttribute());

		this.spatialIndex = new GeoHashTimeIntervalIndex<ITimeInterval>(true, idAttributeIndex);
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
		case APPROXIMATE_TIME_CIRCLE_ESTIMATION:
			this.predictionEstimator = new ApproximateTimeCircleEstimator(this.spatialIndex, radiusExtensionFactor,
					(int) numberOfIterations, maxSpeed);
			break;
		case EXTENDED_RADIUS_ESTIMATION:
			this.predictionEstimator = new ExtendedRadiusEstimatior(this.spatialIndex, radiusExtensionFactor);
			break;
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

		/*
		 * Get the point in time to which the moving objects need to be predicted
		 */
		PointInTime futurePointInTime = PointInTime.ZERO;
		if (object.getAttribute(this.pointInTimeFutureAttributeIndex) instanceof Long) {
			long futurePointInTimeValue = object.getAttribute(this.pointInTimeFutureAttributeIndex);
			futurePointInTime = new PointInTime(futurePointInTimeValue);
		} else if (object.getAttribute(this.pointInTimeFutureAttributeIndex) instanceof PointInTime) {
			futurePointInTime = object.getAttribute(this.pointInTimeFutureAttributeIndex);
		}

		/*
		 * In case that we want to predict a trajectory, we also need to have the start
		 * point in time.
		 */
		PointInTime startPointInTime = null;
		long startTime = -1;
		if (object.getAttribute(this.pointInTimeNowAttributeIndex) instanceof Long) {
			startTime = object.getAttribute(this.pointInTimeNowAttributeIndex);
			startPointInTime = new PointInTime(startTime);
		}

		// The moving object ID we want to have the neighbors from
		String centerMovingObjectID = "" + object.getAttribute(this.centerMovingObjectAttributeIndex);

		if (startTime > -1) {
			sendTrajectoryPredictionTuples(startPointInTime, futurePointInTime, "" + centerMovingObjectID,
					object.getMetadata().clone());
		} else {
			// We only predict one point in time
			Tuple<IMetaAttribute> idsToPredictTuple = predictPoint(futurePointInTime, "" + centerMovingObjectID,
					object.getMetadata().clone());
			transfer((T) idsToPredictTuple);
		}
	}

	@SuppressWarnings("unchecked")
	private void sendTrajectoryPredictionTuples(PointInTime startTime, PointInTime futurePointInTime,
			String centerMovingObjectID, IMetaAttribute metadata) {
		/* Predict for all sampled points on the trajectory. */
		PointInTime timeSteps = new PointInTime(startTime);
		while (timeSteps.plus(this.trajectoryStepSizeMs).beforeOrEquals(futurePointInTime)) {
			Tuple<IMetaAttribute> idsToPredictTuple = predictPoint(timeSteps, "" + centerMovingObjectID, metadata);
			timeSteps = timeSteps.plus(this.trajectoryStepSizeMs);
			transfer((T) idsToPredictTuple);
		}
		/*
		 * The time distance may not exactly end on the endpoint given. We simply set it
		 * to the last point.
		 */
		if (timeSteps.before(futurePointInTime)) {
			Tuple<IMetaAttribute> idsToPredictTuple = predictPoint(futurePointInTime, "" + centerMovingObjectID,
					metadata);
			transfer((T) idsToPredictTuple);
		}
	}

	private Tuple<IMetaAttribute> predictPoint(PointInTime timestamp, String centerMovingObjectID,
			IMetaAttribute metadata) {
		// Collect all objects that need to be predicted
		Set<String> idsToPredict = new HashSet<>();

		if (this.collectAllIDs) {
			idsToPredict.addAll(this.allIDs);
		} else {
			// Calculate objects that need to be predicted
			TrajectoryElement latestLocationOfObject = this.spatialIndex
					.getLatestLocationOfObject(centerMovingObjectID);
			idsToPredict.addAll(this.predictionEstimator.estimateObjectsToPredict(latestLocationOfObject.getLatitude(),
					latestLocationOfObject.getLongitude(), this.radius, timestamp));
		}

		// Create a tuple with all the IDs that need to be predicted
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(3, false);
		tuple.setAttribute(0, timestamp);
		tuple.setAttribute(1, centerMovingObjectID);
		tuple.setAttribute(2, idsToPredict);
		tuple.setMetadata(metadata); // object.getMetadata().clone()
		return tuple;
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
