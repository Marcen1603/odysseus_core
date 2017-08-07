package de.uniol.inf.is.odysseus.spatial.datatype;

import com.vividsolutions.jts.geom.Coordinate;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datastructures.GeoHashHelper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class TrajectoryElement {

	private static final int BIT_PRECISION = 64;

	private String movingObjectID;
	private transient TrajectoryElement previousElement;
	private transient TrajectoryElement nextElement;
	private GeoHash geoHash;
	private double distanceToPreviousElement;
	private PointInTime measurementTime;
	private IStreamObject<? extends IMetaAttribute> streamElement;

	public TrajectoryElement(TrajectoryElement previousElement, String movingObjectID, double latitude,
			double longitude, PointInTime measurementTime, IStreamObject<? extends IMetaAttribute> streamElement) {
		this.previousElement = previousElement;
		if (this.previousElement != null) {
			this.previousElement.nextElement = this;
		}
		this.movingObjectID = movingObjectID;
		this.geoHash = GeoHashHelper.fromLatLong(latitude, longitude, BIT_PRECISION);
		this.setMeasurementTime(measurementTime);
		this.streamElement = streamElement;

		// Calculate distance to previous element
		if (previousElement != null) {
			distanceToPreviousElement = MetrticSpatialUtils.getInstance().calculateDistance(null,
					new Coordinate(geoHash.getPoint().getLatitude(), geoHash.getPoint().getLongitude()),
					new Coordinate(previousElement.getGeoHash().getPoint().getLatitude(),
							previousElement.getGeoHash().getPoint().getLongitude()));

			// This element is the next element of the previous one
			previousElement.setNextElement(this);
		}
	}

	public TrajectoryElement(TrajectoryElement previousElement, String movingObjectID, GeoHash geoHash,
			IStreamObject<? extends IMetaAttribute> streamElement) {
		this.previousElement = previousElement;
		if (this.previousElement != null) {
			this.previousElement.nextElement = this;
		}
		this.movingObjectID = movingObjectID;
		this.geoHash = geoHash;
		this.streamElement = streamElement;

		// Calculate distance to previous element
		if (previousElement != null) {
			distanceToPreviousElement = MetrticSpatialUtils.getInstance().calculateDistance(null,
					new Coordinate(geoHash.getPoint().getLatitude(), geoHash.getPoint().getLongitude()),
					new Coordinate(previousElement.getGeoHash().getPoint().getLatitude(),
							previousElement.getGeoHash().getPoint().getLongitude()));

			// This element is the next element of the previous one
			previousElement.setNextElement(this);
		}
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
		return geoHash;
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

	public double getDistanceToPreviousElement() {
		return distanceToPreviousElement;
	}

	public String getMovingObjectID() {
		return movingObjectID;
	}

	public void setMovingObjectID(String movingObjectID) {
		this.movingObjectID = movingObjectID;
	}
	
	public double getLatitude() {
		return this.geoHash.getPoint().getLatitude();
	}
	
	public double getLongitude() {
		return this.geoHash.getPoint().getLongitude();
	}

	public PointInTime getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(PointInTime measurementTime) {
		this.measurementTime = measurementTime;
	}

}
