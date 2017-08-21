package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.IMovingObjectTrajectoryPredictor;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.MovingObjectLinearTrajectoryPredictor;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOTrajectoryPredictionAO;

/**
 * Predicts a trajectory of a moving object. Trajectory element are predicted
 * with a certain granularity. A list of waypoints is calcualted for every
 * moving object.
 * 
 * @author Tobias Brandt
 *
 */
public class MOTrajectoryPredictionPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	// TODO Make configurable
	private int trajectoryTimeStepMs = 1000;

	// On the enrich-port
	private int pointInTimePosition;
	private int movingObjectListPosition;

	// On the data-port
	private int trajectoryPosition;
	private int idPosition;
	private int courseOverGroundPosition;
	private int speedOverGroundPosition;

	private IMovingObjectTrajectoryPredictor movingObjectTrajectoryPredictor;
	private GeometryFactory geoFactory;

	public MOTrajectoryPredictionPO(MOTrajectoryPredictionAO ao) {
		this.pointInTimePosition = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getPointInTimeAttribute());
		this.movingObjectListPosition = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getMovingObjectListAttribute());

		this.trajectoryPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getTrajectoryAttribute());
		this.idPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.courseOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getCourseOverGroundAttribute());
		this.speedOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getSpeedOverGroundAttribute());

		// Set the basetimeunit so that the passed time is correct
		this.movingObjectTrajectoryPredictor = new MovingObjectLinearTrajectoryPredictor(ao.getBaseTimeUnit());

		this.geoFactory = new GeometryFactory();
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
		GeometryWrapper geometry = object.getAttribute(this.trajectoryPosition);

		double latitude = geometry.getGeometry().getCoordinate().x;
		double longitude = geometry.getGeometry().getCoordinate().y;
		double courseOverGround = object.getAttribute(this.courseOverGroundPosition);
		double speedOverGround = object.getAttribute(this.speedOverGroundPosition);
		String id = object.getAttribute(this.idPosition).toString();

		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, courseOverGround,
				speedOverGround, object.getMetadata().getStart(), id);

		this.movingObjectTrajectoryPredictor.addLocation(locationMeasurement, object);
	}

	/**
	 * Time progress, e.g. by a timer
	 * 
	 * @param object
	 *            Tuple with time information
	 */
	@SuppressWarnings("unchecked")
	private void processTimeTuple(T object) {
		List<String> movingObjectIds = object.getAttribute(this.movingObjectListPosition);
		long timeStamp = object.getAttribute(this.pointInTimePosition);
		PointInTime pointInTime = new PointInTime(timeStamp);

		for (String movingObjectId : movingObjectIds) {
			TrajectoryElement trajectory = this.movingObjectTrajectoryPredictor.predictTrajectory(movingObjectId,
					pointInTime, this.trajectoryTimeStepMs);
			Tuple<ITimeInterval> tuple = createTuple(trajectory);
			this.transfer((T) tuple);
		}
	}

	private Tuple<ITimeInterval> createTuple(TrajectoryElement trajectoryElement) {

		List<Tuple<ITimeInterval>> tupleList = new ArrayList<>();
		PointInTime startTime = trajectoryElement.getMeasurementTime();
		PointInTime endTime = null;
		String movingObjectID = trajectoryElement.getMovingObjectID();

		// Create a tuple for every element of the trajectory
		do {
			Geometry geometry = this.geoFactory
					.createPoint(new Coordinate(trajectoryElement.getLatitude(), trajectoryElement.getLongitude()));
			GeometryWrapper geoWrapper = new GeometryWrapper(geometry);

			Tuple<ITimeInterval> tupleWithPredictedLocation = new Tuple<ITimeInterval>(2, false);
			// The prediction is valid only one point in time
			tupleWithPredictedLocation.setMetadata(new TimeInterval(trajectoryElement.getMeasurementTime(),
					trajectoryElement.getMeasurementTime().plus(1)));
			tupleWithPredictedLocation.setAttribute(0, trajectoryElement.getMovingObjectID());
			tupleWithPredictedLocation.setAttribute(1, geoWrapper);
			tupleList.add(tupleWithPredictedLocation);

			endTime = trajectoryElement.getMeasurementTime();

			// Go on with the next element of the trajectory
			trajectoryElement = trajectoryElement.getNextElement();
		} while (trajectoryElement != null);

		// Create a tuple with the list of elements as its content
		Tuple<ITimeInterval> tupleWithTrajectory = new Tuple<ITimeInterval>(2, false);
		tupleWithTrajectory.setMetadata(new TimeInterval(startTime, endTime));
		tupleWithTrajectory.setAttribute(0, movingObjectID);
		tupleWithTrajectory.setAttribute(1, tupleList);

		return tupleWithTrajectory;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
