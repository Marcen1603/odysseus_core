package de.uniol.inf.is.odysseus.spatial.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public class GeoHashIndex implements SpatialIndex {

	// The precision of the GeoHashes
	public static final int BIT_PRECISION = 64;

	// The latest locations of the moving objects
	protected NavigableMap<GeoHash, List<TrajectoryElement>> locationMap;

	/*
	 * The latest known location of each known moving object (ID of moving object ->
	 * latest known element of the trajectory)
	 */
	protected Map<String, TrajectoryElement> latestTrajectoryElementMap;

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {

		// TODO How to clean up with window?

		// Get info for new TrajectoryElement
		GeoHash geoHash = GeoHashHelper.fromLatLong(locationMeasurement.getLatitude(),
				locationMeasurement.getLongitude(), BIT_PRECISION);
		String id = locationMeasurement.getMovingObjectId();

		// Get the previously latest element from that trajectory
		TrajectoryElement latestElement = this.latestTrajectoryElementMap.get(id);

		// Create the new "latest" element and save it
		TrajectoryElement trajectoryElement = new TrajectoryElement(latestElement, id, geoHash,
				locationMeasurement.getMeasurementTime(), streamElement);
		this.latestTrajectoryElementMap.put(id, trajectoryElement);

		// Remove previously last element from index
		List<TrajectoryElement> movingObjectList = this.locationMap.get(latestElement.getGeoHash());
		if (movingObjectList.size() <= 1) {
			// In case there's only one element at that point, we can remove the whole entry
			this.locationMap.remove(latestElement.getGeoHash());
		} else {
			/*
			 * There are more elements at this point (uhh, a crash). Only remove the element
			 * that belongs to this moving object id.
			 */
			for (TrajectoryElement element : movingObjectList) {
				if (element.getMovingObjectID().equals(id)) {
					movingObjectList.remove(element);
				}
			}
		}

		// Add new latest location to index
		List<TrajectoryElement> geoHashList = this.locationMap.get(geoHash);
		if (geoHashList == null) {
			/*
			 * Probably we will only have one element in here as two objects on the same
			 * location are unlikely (but depends on the scenario)
			 */
			geoHashList = new ArrayList<>(1);
		}

		// Add the new element to the list
		geoHashList.add(trajectoryElement);

		/*
		 * To have the double linked list correctly add the new element as the next
		 * element in the previous one
		 */
		latestElement.setNextElement(trajectoryElement);
		this.locationMap.put(geoHash, geoHashList);
	}

	@Override
	public Map<String, ResultElement> queryCircleOnLatestElements(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TrajectoryElement> approximateCircleOnLatestElements(double centerLatitude, double longitude,
			double radius, TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrajectoryElement getLatestLocationOfObject(String movingObjectID) {
		// TODO Auto-generated method stub
		return null;
	}

}