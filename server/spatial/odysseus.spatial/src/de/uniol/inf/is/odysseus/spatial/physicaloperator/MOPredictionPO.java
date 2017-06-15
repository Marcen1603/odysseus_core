package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.IMovingObjectInterpolator;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.MovingObjectLinearInterpolator;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOPredictionAO;

public class MOPredictionPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	// On the enrich-port
	private int pointInTimePosition;
	private int movingObjectListPosition;

	// On the data-port
	private int geometryPosition;
	private int idPosition;
	private int courseOverGroundPosition;
	private int speedOverGroundPosition;

	private IMovingObjectInterpolator movingObjectInterpolator;
	private GeometryFactory geoFactory;

	public MOPredictionPO(MOPredictionAO ao) {
		this.pointInTimePosition = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getPointInTimeAttribute());
		this.movingObjectListPosition = ao.getInputSchema(ENRICH_PORT)
				.findAttributeIndex(ao.getMovingObjectListAttribute());

		this.geometryPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idPosition = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.courseOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getCourseOverGroundAttribute());
		this.speedOverGroundPosition = ao.getInputSchema(DATA_PORT)
				.findAttributeIndex(ao.getSpeedOverGroundAttribute());
		
		// TODO Read basetimeunit from stream
		this.movingObjectInterpolator = new MovingObjectLinearInterpolator(TimeUnit.MILLISECONDS);
		
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

	private void processTrajectoryTuple(T object) {
		GeometryWrapper geometry = object.getAttribute(this.geometryPosition);

		double latitude = geometry.getGeometry().getCoordinate().y;
		double longitude = geometry.getGeometry().getCoordinate().x;
		double courseOverGround = object.getAttribute(this.courseOverGroundPosition);
		double speedOverGround = object.getAttribute(this.speedOverGroundPosition);
		String id = object.getAttribute(this.idPosition).toString();

		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, courseOverGround,
				speedOverGround, object.getMetadata().getStart(), id);

		this.movingObjectInterpolator.addLocation(locationMeasurement);
	}

	private void processTimeTuple(T object) {
		List<String> movingObjectIds = object.getAttribute(this.movingObjectListPosition);
		long timeStamp = object.getAttribute(this.pointInTimePosition);
		PointInTime pointInTime = new PointInTime(timeStamp);

		for (String movingObjectId : movingObjectIds) {
			LocationMeasurement prediction = this.movingObjectInterpolator.calcLocation(movingObjectId, pointInTime);
			Tuple<ITimeInterval> tuple = createTuple(prediction);
			this.transfer((T) tuple);
		}
	}

	private Tuple<ITimeInterval> createTuple(LocationMeasurement interpolatedLocationMeasurement) {
		
		Geometry geometry = this.geoFactory.createPoint(new Coordinate(interpolatedLocationMeasurement.getLatitude(), interpolatedLocationMeasurement.getLongitude()));
		GeometryWrapper geoWrapper = new GeometryWrapper(geometry);
		
		Tuple<ITimeInterval> tupleWithInterpolatedLocation = new Tuple<ITimeInterval>(5, false);
		tupleWithInterpolatedLocation
				.setMetadata(new TimeInterval(interpolatedLocationMeasurement.getMeasurementTime()));
		tupleWithInterpolatedLocation.setAttribute(0, interpolatedLocationMeasurement.getMovingObjectId());
		tupleWithInterpolatedLocation.setAttribute(1, geoWrapper);
		tupleWithInterpolatedLocation.setAttribute(2, interpolatedLocationMeasurement.getSpeedInMetersPerSecond());
		tupleWithInterpolatedLocation.setAttribute(3, interpolatedLocationMeasurement.getHorizontalDirection());
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
