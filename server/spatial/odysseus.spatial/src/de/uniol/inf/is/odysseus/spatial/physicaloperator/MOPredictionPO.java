package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.Collection;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.IMovingObjectLocationPredictor;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.MovingObjectLinearLocationPredictor;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOPredictionAO;

/**
 * This operator can use predictors to predict the locations of moving objects
 * at certain points in time.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class MOPredictionPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	// On the enrich-port
	private int pointInTimeFuturePosition;
	private int pointInTimeNowPosition;
	private int movingObjectListPosition = -1;
	private int centerMovingObjectIdAttribute;

	// On the data-port
	private int geometryPosition;
	private int idPosition;
	private int courseOverGroundPosition;
	private int speedOverGroundPosition;

	// One minute
	private static final long DEFAULT_TIME_STEP = 60000;
	private long trajectoryStepSizeMs;

	private IMovingObjectLocationPredictor movingObjectInterpolator;
	private GeometryFactory geoFactory;

	public MOPredictionPO(MOPredictionAO ao) {
		this.pointInTimeFuturePosition = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getPointInTimeAttribute());
		if (ao.getPointInTimeNowAttribute() != null && !ao.getPointInTimeNowAttribute().isEmpty()) {
			this.pointInTimeNowPosition = ao.getInputSchema(ENRICH_PORT)
					.findAttributeIndex(ao.getPointInTimeNowAttribute());
		}
		if (ao.getMovingObjectListAttribute() != null && !ao.getMovingObjectListAttribute().isEmpty()) {
			this.movingObjectListPosition = ao.getInputSchema(ENRICH_PORT)
					.findAttributeIndex(ao.getMovingObjectListAttribute());
		}
		this.centerMovingObjectIdAttribute = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getCentermovingObjectIdAttribute());

		this.geometryPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.courseOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getCourseOverGroundAttribute());
		this.speedOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getSpeedOverGroundAttribute());

		// Set the basetimeunit so that the passed time is correct
		this.movingObjectInterpolator = new MovingObjectLinearLocationPredictor(ao.getBaseTimeUnit());

		this.geoFactory = new GeometryFactory();

		this.trajectoryStepSizeMs = ao.getTimeStepSizeMs();
		if (this.trajectoryStepSizeMs < 1) {
			this.trajectoryStepSizeMs = DEFAULT_TIME_STEP;
		}
	}

	@Override
	protected void process_next(T object, int port) {
		if (port == DATA_PORT) {
			processTrajectoryTuple(object);
		} else if (port == ENRICH_PORT) {
			processTimeTuple(object);
		}
	}

	/**
	 * A new location of a moving object
	 * 
	 * @param object
	 *            The tuple with the location information
	 */
	private void processTrajectoryTuple(T object) {
		GeometryWrapper geometry = object.getAttribute(this.geometryPosition);

		double latitude = geometry.getGeometry().getCoordinate().x;
		double longitude = geometry.getGeometry().getCoordinate().y;
		
		// TODO Calculate this from the movement, not reading this from the attributes
		double courseOverGround = object.getAttribute(this.courseOverGroundPosition);
		double speedOverGround = object.getAttribute(this.speedOverGroundPosition);
		String id = object.getAttribute(this.idPosition).toString();

		// TODO Why not a TrajectoryElement?
		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, courseOverGround,
				speedOverGround, object.getMetadata().getStart(), id);

		this.movingObjectInterpolator.addLocation(locationMeasurement, object);
	}

	/**
	 * Time progress, e.g. by a timer
	 * 
	 * @param object
	 *            Tuple with time information
	 */
	private void processTimeTuple(T object) {
		Collection<String> movingObjectIds = null;
		if (this.movingObjectListPosition != -1) {
			/*
			 * Only use if we have this given. If we only predict one object, this may not
			 * be given
			 */
			movingObjectIds = object.getAttribute(this.movingObjectListPosition);
		}
		String centerMovingObjectID = "" + object.getAttribute(this.centerMovingObjectIdAttribute);

		/*
		 * Get the point in time to which the moving objects need to be predicted
		 */
		PointInTime futurePointInTime = PointInTime.ZERO;
		if (object.getAttribute(this.pointInTimeFuturePosition) instanceof Long) {
			long futurePointInTimeValue = object.getAttribute(this.pointInTimeFuturePosition);
			futurePointInTime = new PointInTime(futurePointInTimeValue);
		} else if (object.getAttribute(this.pointInTimeFuturePosition) instanceof PointInTime) {
			futurePointInTime = object.getAttribute(this.pointInTimeFuturePosition);
		}

		/*
		 * In case that we want to predict a trajectory, we also need to have the start
		 * point in time.
		 */
		PointInTime startPointInTime = null;
		long startTime = -1;
		if (object.getAttribute(this.pointInTimeNowPosition) instanceof Long) {
			startTime = object.getAttribute(this.pointInTimeNowPosition);
			startPointInTime = new PointInTime(startTime);
		} else if (object.getAttribute(this.pointInTimeNowPosition) instanceof PointInTime) {
			startPointInTime = object.getAttribute(this.pointInTimeNowPosition);
		}

		if (startTime < 0) {
			// Only predict one point in time
			predictPoint(centerMovingObjectID, futurePointInTime, movingObjectIds, object);
		} else {
			// Predict a trajectory

			/* Predict all points on the trajectory. */
			PointInTime timeSteps = new PointInTime(startPointInTime);
			while (timeSteps.plus(this.trajectoryStepSizeMs).beforeOrEquals(futurePointInTime)) {
				predictPoint(centerMovingObjectID, timeSteps, movingObjectIds, object);
				timeSteps = timeSteps.plus(this.trajectoryStepSizeMs);
			}

			/*
			 * The time distance may not exactly end on the endpoint given. We simply set it
			 * to the last point.
			 */
			if (timeSteps.before(futurePointInTime)) {
				predictPoint(centerMovingObjectID, futurePointInTime, movingObjectIds, object);
			}

		}

	}

	@SuppressWarnings("unchecked")
	private void predictPoint(String movingObjectID, PointInTime predictedTime, Collection<String> idsToPredict,
			T originalStreamElement) {
		// Predict the location of the center
		LocationMeasurement centerPrediction = this.movingObjectInterpolator.predictLocation(movingObjectID,
				predictedTime);

		// We only predict the center
		if (idsToPredict == null) {
			Tuple<IMetaAttribute> tuple = createTuple(centerPrediction, centerPrediction, originalStreamElement);
			this.transfer((T) tuple);
			return;
		}

		// We predict some more objects
		for (String movingObjectId : idsToPredict) {
			LocationMeasurement prediction = this.movingObjectInterpolator.predictLocation(movingObjectId,
					predictedTime);
			Tuple<IMetaAttribute> tuple = createTuple(prediction, centerPrediction, originalStreamElement);
			this.transfer((T) tuple);
		}
	}

	private Tuple<IMetaAttribute> createTuple(LocationMeasurement interpolatedLocationMeasurement,
			LocationMeasurement predictedCenterLocation, T triggerTuple) {
		Geometry geometry = this.geoFactory.createPoint(new Coordinate(interpolatedLocationMeasurement.getLatitude(),
				interpolatedLocationMeasurement.getLongitude()));
		GeometryWrapper geoWrapper = new GeometryWrapper(geometry);

		Geometry centerGeometry = this.geoFactory.createPoint(
				new Coordinate(predictedCenterLocation.getLatitude(), predictedCenterLocation.getLongitude()));
		GeometryWrapper centerGeoWrapper = new GeometryWrapper(centerGeometry);

		Tuple<IMetaAttribute> tupleWithInterpolatedLocation = new Tuple<IMetaAttribute>(7, false);
		// The prediction is valid only one point in time
		tupleWithInterpolatedLocation.setMetadata(triggerTuple.getMetadata().clone());
		tupleWithInterpolatedLocation.setAttribute(0, interpolatedLocationMeasurement.getMovingObjectId());
		tupleWithInterpolatedLocation.setAttribute(1, geoWrapper);
		tupleWithInterpolatedLocation.setAttribute(2, interpolatedLocationMeasurement.getSpeedInMetersPerSecond());
		tupleWithInterpolatedLocation.setAttribute(3, interpolatedLocationMeasurement.getHorizontalDirection());
		// Keep the center
		tupleWithInterpolatedLocation.setAttribute(4, triggerTuple.getAttribute(this.centerMovingObjectIdAttribute));
		tupleWithInterpolatedLocation.setAttribute(5, centerGeoWrapper);
		tupleWithInterpolatedLocation.setAttribute(6, interpolatedLocationMeasurement.getMeasurementTime());
		return tupleWithInterpolatedLocation;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
