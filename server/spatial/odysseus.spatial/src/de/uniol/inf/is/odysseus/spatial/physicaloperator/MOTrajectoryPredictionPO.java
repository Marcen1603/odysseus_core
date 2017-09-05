package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
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
	private int trajectoryTimeStepMs = 60000;

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

		// Collect the tuples and send them together
		List<Tuple<ITimeInterval>> tupleList = new ArrayList<>();
//		List<KeyValueObject<ITimeInterval>> kvList = new ArrayList<>();
		
		for (String movingObjectId : movingObjectIds) {
			TrajectoryElement trajectory = this.movingObjectTrajectoryPredictor.predictTrajectory(movingObjectId,
					pointInTime, this.trajectoryTimeStepMs);
			Tuple<ITimeInterval> tuple = createInnerTuple(trajectory, pointInTime);
			
//			KeyValueObject<ITimeInterval> innerELement = createInnerElement(trajectory, pointInTime);
			
			tupleList.add(tuple);
//			kvList.add(innerELement);
		}
		
		Tuple<IMetaAttribute> tupleWithTupleList = new Tuple<>(2, false);
		tupleWithTupleList.setMetadata(object.getMetadata().clone());
		((ITimeInterval) tupleWithTupleList.getMetadata()).setStart(pointInTime);
		((ITimeInterval) tupleWithTupleList.getMetadata()).setEnd(pointInTime.plus(1));
		tupleWithTupleList.setAttribute(0, tupleList);
		
//		KeyValueObject<ITimeInterval> elementWithElements = new KeyValueObject<>();
//		elementWithElements.setMetadata(object.getMetadata());
//		elementWithElements.getMetadata().setStart(pointInTime);
//		elementWithElements.getMetadata().setEnd(pointInTime.plus(1));
		
		this.transfer((T) tupleWithTupleList);
	}

	/**
	 * 
	 * @param trajectoryElement
	 * @param predictionTime
	 *            The point in time when the prediction was created
	 * @return
	 */
	private Tuple<ITimeInterval> createInnerTuple(TrajectoryElement trajectoryElement, PointInTime predictionTime) {

		List<Tuple<ITimeInterval>> tupleList = new ArrayList<>();
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

			// Go on with the next element of the trajectory
			trajectoryElement = trajectoryElement.getNextElement();
		} while (trajectoryElement != null);

		// Create a tuple with the list of elements as its content
		Tuple<ITimeInterval> tupleWithTrajectory = new Tuple<ITimeInterval>(2, false);
		tupleWithTrajectory.setMetadata(new TimeInterval(predictionTime, predictionTime.plus(1)));
		tupleWithTrajectory.setAttribute(0, movingObjectID);
		tupleWithTrajectory.setAttribute(1, tupleList);

		return tupleWithTrajectory;
	}
	
	private KeyValueObject<ITimeInterval> createInnerElement(TrajectoryElement trajectoryElement, PointInTime predictionTime) {
		List<KeyValueObject<ITimeInterval>> objectList = new ArrayList<>();
		String movingObjectID = trajectoryElement.getMovingObjectID();
		
		do {
			
			KeyValueObject<ITimeInterval> kvObject = new KeyValueObject<>();
			
			kvObject.setAttribute(movingObjectID, trajectoryElement.getMovingObjectID());
			kvObject.setAttribute("latitude", trajectoryElement.getLatitude());
			kvObject.setAttribute("longitude", trajectoryElement.getLongitude());
			
			kvObject.setMetadata(new TimeInterval(trajectoryElement.getMeasurementTime(),
					trajectoryElement.getMeasurementTime().plus(1)));
			
			objectList.add(kvObject);

			// Go on with the next element of the trajectory
			trajectoryElement = trajectoryElement.getNextElement();
		} while (trajectoryElement != null);

		// Create a tuple with the list of elements as its content
//		Tuple<ITimeInterval> tupleWithTrajectory = new Tuple<ITimeInterval>(2, false);
//		tupleWithTrajectory.setMetadata(new TimeInterval(predictionTime, predictionTime.plus(1)));
//		tupleWithTrajectory.setAttribute(0, movingObjectID);
//		tupleWithTrajectory.setAttribute(1, objectList);
		
		KeyValueObject<ITimeInterval> elementWithElements = new KeyValueObject<ITimeInterval>();
		elementWithElements.setMetadata(new TimeInterval(predictionTime, predictionTime.plus(1)));
		elementWithElements.setAttribute("movingObjectID", movingObjectID);
		elementWithElements.setAttribute("elements", objectList);

		return elementWithElements;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
