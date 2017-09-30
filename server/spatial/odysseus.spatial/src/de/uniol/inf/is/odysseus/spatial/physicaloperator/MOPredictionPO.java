package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

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
	private int pointInTimePosition;
	private int movingObjectListPosition;
	private int centerMovingObjectIdAttribute;

	// On the data-port
	private int geometryPosition;
	private int idPosition;
	private int courseOverGroundPosition;
	private int speedOverGroundPosition;

	private IMovingObjectLocationPredictor movingObjectInterpolator;
	private GeometryFactory geoFactory;

	public MOPredictionPO(MOPredictionAO ao) {
		this.pointInTimePosition = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getPointInTimeAttribute());
		this.movingObjectListPosition = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getMovingObjectListAttribute());
		this.centerMovingObjectIdAttribute = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getCentermovingObjectIdAttribute());

		this.geometryPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.courseOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getCourseOverGroundAttribute());
		this.speedOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getSpeedOverGroundAttribute());

		// Set the basetimeunit so that the passed time is correct
		this.movingObjectInterpolator = new MovingObjectLinearLocationPredictor(ao.getBaseTimeUnit());

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
		GeometryWrapper geometry = object.getAttribute(this.geometryPosition);

		double latitude = geometry.getGeometry().getCoordinate().x;
		double longitude = geometry.getGeometry().getCoordinate().y;
		double courseOverGround = object.getAttribute(this.courseOverGroundPosition);
		double speedOverGround = object.getAttribute(this.speedOverGroundPosition);
		String id = object.getAttribute(this.idPosition).toString();

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
	@SuppressWarnings("unchecked")
	private void processTimeTuple(T object) {
		List<String> movingObjectIds = object.getAttribute(this.movingObjectListPosition);
		long timeStamp = object.getAttribute(this.pointInTimePosition);
		PointInTime pointInTime = new PointInTime(timeStamp);

		for (String movingObjectId : movingObjectIds) {
			LocationMeasurement prediction = this.movingObjectInterpolator.predictLocation(movingObjectId, pointInTime);
			Tuple<IMetaAttribute> tuple = createTuple(prediction, object);
			this.transfer((T) tuple);
		}
	}

	private Tuple<IMetaAttribute> createTuple(LocationMeasurement interpolatedLocationMeasurement, T triggerTuple) {
		Geometry geometry = this.geoFactory.createPoint(new Coordinate(interpolatedLocationMeasurement.getLatitude(),
				interpolatedLocationMeasurement.getLongitude()));
		GeometryWrapper geoWrapper = new GeometryWrapper(geometry);

		Tuple<IMetaAttribute> tupleWithInterpolatedLocation = new Tuple<IMetaAttribute>(5, false);
		// The prediction is valid only one point in time
		tupleWithInterpolatedLocation.setMetadata(triggerTuple.getMetadata().clone());
//		((T) tupleWithInterpolatedLocation).getMetadata().setStart(interpolatedLocationMeasurement.getMeasurementTime());
//		((T) tupleWithInterpolatedLocation).getMetadata().setEnd(interpolatedLocationMeasurement.getMeasurementTime().plus(1));
		tupleWithInterpolatedLocation.setAttribute(0, interpolatedLocationMeasurement.getMovingObjectId());
		tupleWithInterpolatedLocation.setAttribute(1, geoWrapper);
		tupleWithInterpolatedLocation.setAttribute(2, interpolatedLocationMeasurement.getSpeedInMetersPerSecond());
		tupleWithInterpolatedLocation.setAttribute(3, interpolatedLocationMeasurement.getHorizontalDirection());
		// Keep the center
		tupleWithInterpolatedLocation.setAttribute(4, triggerTuple.getAttribute(this.centerMovingObjectIdAttribute));
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
