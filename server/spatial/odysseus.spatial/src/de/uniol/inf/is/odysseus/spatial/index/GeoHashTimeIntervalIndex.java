package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

/**
 * The spatial index that can work with time windows. Cleaning up the index
 * costs some performance - in some use cases a spatial index without cleanup
 * can perform better.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class GeoHashTimeIntervalIndex<T extends ITimeInterval> extends GeoHashIndex<T> {

	// Where in the tuple
	protected int idAttributeIndex;

	// To distinguish tuples and key-value-objects
	protected boolean isTuple;

	// Index by time
	protected DefaultTISweepArea<IStreamObject<? extends T>> sweepArea;

	public GeoHashTimeIntervalIndex(boolean isTuple, int idAttributeIndex) {
		super();
		this.sweepArea = new DefaultTISweepArea<>();
		this.isTuple = isTuple;
		this.idAttributeIndex = idAttributeIndex;
	}

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends T> streamElement) {
		super.add(locationMeasurement, streamElement);

		// Insert in SweepArea
		this.sweepArea.insert(streamElement);

		// Clean the index (window)
		this.cleanUp(streamElement.getMetadata().getEnd());
	}

	private void cleanUp(PointInTime endTime) {
		// Remove old elements from sweepArea
		List<IStreamObject<? extends T>> removed = this.sweepArea.extractElementsBeforeAsList(endTime);

		for (IStreamObject<? extends T> streamObject : removed) {
			if (this.isTuple) {
				Tuple<? extends T> tuple = (Tuple<? extends T>) streamObject;
				String id = "" + tuple.getAttribute(idAttributeIndex);

				// Remove this from the indexes

				/*
				 * 1. from the index with the double linked list. Go through the list and start
				 * from the latest element.
				 */
				TrajectoryElement trajectoryElement = latestTrajectoryElementMap.get(id);

				while (trajectoryElement != null) {
					if (trajectoryElement.getMeasurementTime().afterOrEquals(endTime)) {

						/*
						 * If this element is the latest element of the trajectory, we have to remove it
						 * from the spatial map with the latest elements. If not, this is not in this
						 * map.
						 */
						if (trajectoryElement.getNextElement() == null) {
							// Only the latest element does not have a next element
							List<TrajectoryElement> locations = locationMap.get(trajectoryElement.getGeoHash());
							if (locations.size() <= 1) {
								// We can remove the while list
								locationMap.remove(trajectoryElement.getGeoHash());
							} else {
								// We have to remove this element from the list
								locations.remove(trajectoryElement);
							}

							/*
							 * In this case, we also need to remove it from the map with the latest
							 * locations
							 */
							this.latestTrajectoryElementMap.remove(id);
						}

						/*
						 * remove this element from the list and from other index (garbage collector
						 * should clean up previous elements, too)
						 */
						trajectoryElement.decoupleFromNextElement();
					}
					trajectoryElement = trajectoryElement.getPreviousElement();
				}
			}
		}
	}

}
