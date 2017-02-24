package de.uniol.inf.is.odysseus.spatial.datatype;

import com.vividsolutions.jts.geom.Coordinate;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class TrajectoryElement {

	private String movingObjectID;
	private TrajectoryElement previousElement;
	private TrajectoryElement nextElement;
	private GeoHash geoHash;
	private double distanceToPreviousElement;
	private IStreamObject<? extends IMetaAttribute> streamElement;

	public TrajectoryElement(TrajectoryElement previousElement, GeoHash geoHash,
			IStreamObject<? extends IMetaAttribute> streamElement) {
		this.previousElement = previousElement;
		this.previousElement.nextElement = this;
		this.geoHash = geoHash;
		this.streamElement = streamElement;

		// Calculate distance to previous element
		if (previousElement != null) {
			distanceToPreviousElement = MetrticSpatialUtils.getInstance().calculateDistance(null,
					new Coordinate(geoHash.getPoint().getLongitude(), geoHash.getPoint().getLatitude()),
					new Coordinate(previousElement.getGeoHash().getPoint().getLongitude(),
							previousElement.getGeoHash().getPoint().getLatitude()));
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

}
