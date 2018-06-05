package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex;

/**
 * Use the time circle algorithm but only approximate circles with underlying
 * spatial index to avoid distance calculations
 * 
 * @author Tobias Brandt
 *
 */
public class ApproximateTimeCircleEstimator implements Estimator {

	private static final int SECONDS_TO_MS = 1000;

	@SuppressWarnings("rawtypes")
	private SpatialIndex index;
	private double radiusExtensionFactor;
	private int numberOfExtensions;
	private double maxSpeedMeterPerSecond;

	/**
	 * 
	 * @param index
	 *            The index to query on
	 * @param radiusExtensionFactor
	 *            The radius is extended by a certain factor in each iteration.
	 * @param numberOfExtensions
	 *            The number of iterations. One extension is always done, this only
	 *            defines how many addition extensions after the first one are done.
	 *            Only these additional extensions consider the time.
	 * @param maxSpeedMeterPerSecond
	 *            The maximum speed of the moving objects. Needed to filter out
	 *            objects that can't reach the queried region.
	 */
	@SuppressWarnings("rawtypes")
	public ApproximateTimeCircleEstimator(SpatialIndex index, double radiusExtensionFactor, int numberOfExtensions,
			double maxSpeedMeterPerSecond) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
		this.numberOfExtensions = numberOfExtensions;
		this.maxSpeedMeterPerSecond = maxSpeedMeterPerSecond;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime targetTime) {

		Set<String> objectsToPredict = new HashSet<>();

		// First circle is simply an extension without the consideration of time
		double radiusLastExtendedCircle = radius * this.radiusExtensionFactor;
		Map<String, TrajectoryElement> extendedInnerCircleResults = this.index
				.approximateCircleOnLatestElements(centerLatitude, centerLongitude, radiusLastExtendedCircle, null);
		Set<String> extendedInnerCircle = extendedInnerCircleResults.keySet();
		objectsToPredict.addAll(extendedInnerCircle);

		/*
		 * Second and more circles take time into consideration without calculation of
		 * the distance between the objects and the center (because we want to avoid
		 * slow calculations).
		 */
		for (int extensionNum = 0; extensionNum < this.numberOfExtensions; extensionNum++) {
			/*
			 * Here we need to calculate the distance to the query radius from the outer
			 * radius of the PREVIOUS iteration. Because we use the shortest possible
			 * distance as an approximation for the distance to avoid distance calculation
			 * for each single object.
			 */
			double distanceToQueryCircle = radiusLastExtendedCircle - radius;

			// Define the outer limit of the bigger circle (the second extension)
			double outerCircle = radiusLastExtendedCircle * this.radiusExtensionFactor;

			/*
			 * How much older must they be at minimum? This is the time they need at minimum
			 * to get from the outer donut to the inner circle of the query (not the
			 * extended circle). In fact, we calculate the time from the outer border of the
			 * previous extension / the inner border of this donut to the real query radius
			 * because this in the minimum distance the objects need to travel.
			 */
			long travelTimeMs = (long) ((distanceToQueryCircle / this.maxSpeedMeterPerSecond) * SECONDS_TO_MS);

			// Make this to a timestamp by subtracting this from the current timestamp
			PointInTime timeStampInPast = targetTime.minus(travelTimeMs);
			PointInTime timeStampInFuture = targetTime.plus(travelTimeMs);

			TimeInterval intervalBefore = new TimeInterval(PointInTime.ZERO, timeStampInPast);
			TimeInterval intervalAfter = new TimeInterval(timeStampInFuture, PointInTime.INFINITY);

			/*
			 * TODO: We have to remove the elements from the rounds before. If we don't, we
			 * include more elements than we want to, because the elements in the inner
			 * circles get a new chance with more time.* If we would keep it this way, we
			 * could just skip the inner circles and only do the calculation with the last
			 * circle.
			 * 
			 * So, the solution is to remember which elements were considered in the rounds
			 * before and remove them to get an approximated donut.
			 * 
			 * 
			 * * No, they don't. They get a new chance with even heavier time constrains,
			 * hence, they won't be chosen the next round if they weren't chosen the round
			 * before. It's AT LEAST x minutes time difference.
			 * 
			 * Nevertheless, the filter predicate is wrong I guess. It needs to be the other
			 * way around: excluding all elements which are within this timespan, not
			 * including them.
			 */
			Map<String, TrajectoryElement> bigCircle = this.index.approximateCircleOnLatestElements(centerLatitude,
					centerLongitude, outerCircle, null);

			// Filter for the time intervals
			List<String> IDsToRemove = new ArrayList<>();
			for (String movingObjectID : bigCircle.keySet()) {
				PointInTime measurementTime = bigCircle.get(movingObjectID).getMeasurementTime();
				if (!intervalBefore.includes(measurementTime) && !intervalAfter.includes(measurementTime)) {
					// The element is not within the specified time intervals: remove it
					IDsToRemove.add(movingObjectID);
				}
			}

			// Remove from map
			for (String movingObjectIDToRemove : IDsToRemove) {
				bigCircle.remove(movingObjectIDToRemove);
			}

			/*
			 * Add the new keys to the exiting set. We do not need to actually do the
			 * subtraction to have a donut query as we only add the elements which are not
			 * already in the result set.
			 */
			objectsToPredict.addAll(bigCircle.keySet());

			// Extend the radius for the next iteration
			radiusLastExtendedCircle = outerCircle;
		}
		return objectsToPredict;
	}

}
