package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.IMovingObjectInterpolator;
import de.uniol.inf.is.odysseus.spatial.interpolation.interpolator.MovingObjectLinearInterpolator;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MovingObjectInterpolationAO;

public class MovingObjectInterpolationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private IMovingObjectInterpolator movingObjectInterpolator;

	private int latitudeIndex;
	private int longitudeIndex;
	private int courseOverGroundIndex;
	private int speedOverGroundIndex;
	private int idIndex;

	public MovingObjectInterpolationPO(MovingObjectInterpolationAO ao) {
		this.movingObjectInterpolator = new MovingObjectLinearInterpolator(TimeUnit.MILLISECONDS);

		this.latitudeIndex = 5;
		this.longitudeIndex = 9;
		this.courseOverGroundIndex = 6;
		this.speedOverGroundIndex = 7;
		this.idIndex = 8;
	}

	@Override
	protected void process_next(T object, int port) {
		double latitude = object.getAttribute(this.latitudeIndex);
		double longitude = object.getAttribute(this.longitudeIndex);
		double courseOverGround = object.getAttribute(this.courseOverGroundIndex);
		double speedOverGround = object.getAttribute(this.speedOverGroundIndex);
		String id = object.getAttribute(this.idIndex);

		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, courseOverGround,
				speedOverGround, object.getMetadata().getStart(), id);

		this.movingObjectInterpolator.addLocation(locationMeasurement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Here we have to calculate and output the new location of every
		// moving object
		Map<String, LocationMeasurement> allLocations = this.movingObjectInterpolator.calcAllLocations(punctuation.getTime());
		for (LocationMeasurement interpolatedLocationMeasurement : allLocations.values()) {
			this.transfer((T) createTuple(interpolatedLocationMeasurement));			
		}
	}
	
	private Tuple<ITimeInterval> createTuple(LocationMeasurement interpolatedLocationMeasurement) {
		Tuple<ITimeInterval> tupleWithInterpolatedLocation = new Tuple<ITimeInterval>(5, false);
		tupleWithInterpolatedLocation.setMetadata(new TimeInterval(interpolatedLocationMeasurement.getMeasurementTime()));
		tupleWithInterpolatedLocation.setAttribute(0, interpolatedLocationMeasurement.getMovingObjectId());
		tupleWithInterpolatedLocation.setAttribute(1, interpolatedLocationMeasurement.getLatitude());
		tupleWithInterpolatedLocation.setAttribute(2, interpolatedLocationMeasurement.getLongitude());
		tupleWithInterpolatedLocation.setAttribute(3, interpolatedLocationMeasurement.getSpeedInMetersPerSecond());
		tupleWithInterpolatedLocation.setAttribute(4, interpolatedLocationMeasurement.getHorizontalDirection());
		return tupleWithInterpolatedLocation;
	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}
}
