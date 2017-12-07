package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashHelper;

@Deprecated
public class GeoHashMODataStructure extends GeoHashMONoCleanupIndexStructure {

	public static final String TYPE = "mo_geohash";

	// How much distance we want to store per moving object
	private double distancePerMovingObject;

	// To track the distance we already have per moving object
	private Map<String, Double> movingObjectDistances;

	public GeoHashMODataStructure(String name, int geometryPosition, double distancePerMovingObject) {
		super(name, geometryPosition);
		this.distancePerMovingObject = distancePerMovingObject;
		this.movingObjectDistances = new HashMap<>();
	}

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {
		// Get info for new TrajectoryElement
		GeoHash geoHash = GeoHashHelper.fromLatLong(locationMeasurement.getLatitude(),
				locationMeasurement.getLongitude(), GeoHashMONoCleanupIndexStructure.BIT_PRECISION);
		String id = locationMeasurement.getMovingObjectId();

		// Get the previously latest element from that trajectory
		TrajectoryElement latestElement = this.latestTrajectoryElementMap.get(id);

		// Create the new "latest" element and save it
		TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, id, geoHash,
				locationMeasurement.getMeasurementTime(), streamElement);
		latestTrajectoryElementMap.put(id, trajectoryElement);

		List<TrajectoryElement> geoHashList = this.pointMap.get(geoHash);
		if (geoHashList == null) {
			/*
			 * Probably we will only have one element in here as two objects on the same
			 * location are unlikely (but depends on the scenario)
			 */
			geoHashList = new ArrayList<>(1);
		}

		// Add the new element to the list
		geoHashList.add(trajectoryElement);
		this.pointMap.put(geoHash, geoHashList);

		// Calculate the new total distance of this trajectory
		double newDistance = (movingObjectDistances.get(id) == null ? 0.0 : movingObjectDistances.get(id))
				+ trajectoryElement.getDistanceToPreviousElement();
		movingObjectDistances.put(id, newDistance);

		// Search for the last but one element
		TrajectoryElement element = trajectoryElement;
		while (element != null && element.getPreviousElement() != null
				&& element.getPreviousElement().getPreviousElement() != null) {
			element = element.getPreviousElement();
		}

		if (newDistance - element.getDistanceToPreviousElement() > distancePerMovingObject) {
			// We can delete the last element

			// Remove from all elements
			TrajectoryElement previousElement = element.getPreviousElement();
			List<TrajectoryElement> elemList = pointMap.get(previousElement.getGeoHash());

			if (elemList != null && elemList.size() <= 1) {
				// Remove the whole list
				pointMap.remove(previousElement.getGeoHash());
			} else if (elemList != null) {
				// Only remove the one element from the list
				elemList.remove(previousElement);
			}

			// Remove from chained list
			element.setPreviousElement(null);
			movingObjectDistances.put(id, newDistance - element.getDistanceToPreviousElement());
		}
	}
}
