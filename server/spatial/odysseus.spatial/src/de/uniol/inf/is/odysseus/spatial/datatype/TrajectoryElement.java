package de.uniol.inf.is.odysseus.spatial.datatype;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

/**
 * One element in a trajectory. The trajectory is made of a double linked list
 * (to previous and next element).
 * 
 * @author Tobias Brandt
 *
 */
public class TrajectoryElement {

	static Logger logger = LoggerFactory.getLogger(TrajectoryElement.class);

	private static final int BIT_PRECISION = 64;

	private String movingObjectID;

	// Double linked list
	private transient TrajectoryElement previousElement;
	private transient TrajectoryElement nextElement;

	// Location
	private double latitude;
	private double longitude;
	private GeoHash geoHash;

	// Time
	private PointInTime measurementTime;
	private IStreamObject<? extends IMetaAttribute> streamElement;

	// For possible rounding errors
	private static double DISTANCE_TOLERANCE_METERS = 0.01;

	public TrajectoryElement(TrajectoryElement previousElement, String movingObjectID, double latitude,
			double longitude, PointInTime measurementTime, IStreamObject<? extends IMetaAttribute> streamElement) {
		this.previousElement = previousElement;
		if (this.previousElement != null) {
			this.previousElement.nextElement = this;
		}
		this.movingObjectID = movingObjectID;
		this.latitude = latitude;
		this.longitude = longitude;

		this.setMeasurementTime(measurementTime);
		this.streamElement = streamElement;

		if (previousElement != null) {
			// This element is the next element of the previous one
			previousElement.setNextElement(this);
		}
	}

	public TrajectoryElement(TrajectoryElement previousElement, String movingObjectID, GeoHash geoHash,
			PointInTime measurementTime, IStreamObject<? extends IMetaAttribute> streamElement) {
		this.previousElement = previousElement;
		if (this.previousElement != null) {
			this.previousElement.nextElement = this;
		}
		this.movingObjectID = movingObjectID;
		this.geoHash = geoHash;
		this.latitude = this.geoHash.getPoint().getLatitude();
		this.longitude = this.geoHash.getPoint().getLongitude();
		this.measurementTime = measurementTime;
		this.streamElement = streamElement;

		if (previousElement != null) {
			// This element is the next element of the previous one
			previousElement.setNextElement(this);
		}
	}

	/**
	 * Calculates the azimuth (direction) of the last trajectory element (from the
	 * previous to this location)
	 * 
	 * @return The azimuth.
	 */
	public double getAzimuth() {
		double azimuth = 0.0;

		if (this.previousElement != null) {
			Coordinate previousCoord = new Coordinate(this.previousElement.getLatitude(),
					this.previousElement.getLongitude());
			Coordinate currentCoord = new Coordinate(this.getLatitude(), this.getLongitude());
			azimuth = MetrticSpatialUtils.getInstance().calculateAzimuth(null, previousCoord, currentCoord);
		}
		return azimuth;
	}

	/**
	 * Calculates the speed in meters per second of the last trajectory element
	 * (from the previous to this location)
	 * 
	 * @param baseTimeUnit
	 *            time unit of the data stream to interpret the timestamps
	 * @return The speed in meters per second
	 */
	public double getSpeed(TimeUnit baseTimeUnit) {
		double speed = 0.0;

		if (this.previousElement != null) {
			long duration = (this.measurementTime.minus(this.previousElement.measurementTime)).getMainPoint();
			long seconds = baseTimeUnit.toSeconds(duration);
			if (seconds != 0) {
				speed = this.getDistanceToPreviousElement() / seconds;
			} else if (this.getDistanceToPreviousElement() <= DISTANCE_TOLERANCE_METERS && seconds == 0) {
				// Avoid infinitive speeds
				speed = 0;
			} else {
				speed = Double.MAX_VALUE;
				logger.warn("Possibly infinitive speed value.");
			}
		}
		return speed;
	}

	/**
	 * Calculates the distance to the previous trajectory element.
	 * 
	 * @return Distance in meters
	 */
	public double getDistanceToPreviousElement() {
		double distanceInMeters = 0;
		// Calculate distance to previous element in meters
		if (previousElement != null) {
			distanceInMeters = MetrticSpatialUtils.getInstance().calculateDistance(null,
					new Coordinate(this.getLatitude(), this.getLongitude()),
					new Coordinate(this.previousElement.getLatitude(), this.previousElement.getLongitude()));
		}
		return distanceInMeters;
	}

	public void decoupleFromNextElement() {
		if (this.nextElement != null)
			this.nextElement.setPreviousElement(null);
		this.nextElement = null;
	}

	public TrajectoryElement getPreviousElement() {
		return previousElement;
	}

	public void setPreviousElement(TrajectoryElement previousElement) {
		this.previousElement = previousElement;
	}

	public TrajectoryElement getNextElement() {
		return nextElement;
	}

	public void setNextElement(TrajectoryElement nextElement) {
		this.nextElement = nextElement;
	}

	public GeoHash getGeoHash() {
		this.geoHash = GeoHashHelper.fromLatLong(latitude, longitude, BIT_PRECISION);
		return this.geoHash;
	}

	public void setGeoHash(GeoHash geoHash) {
		this.geoHash = geoHash;
	}

	public IStreamObject<? extends IMetaAttribute> getStreamElement() {
		return streamElement;
	}

	public void setStreamElement(IStreamObject<? extends IMetaAttribute> streamElement) {
		this.streamElement = streamElement;
	}

	public String getMovingObjectID() {
		return movingObjectID;
	}

	public void setMovingObjectID(String movingObjectID) {
		this.movingObjectID = movingObjectID;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public PointInTime getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(PointInTime measurementTime) {
		this.measurementTime = measurementTime;
	}

}
