package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectIndexOld;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;

/**
 * Use the time circle algorithm. Uses exact radius queries for the estimation,
 * hence, needs distance calculations on the index.
 * 
 * @author Tobias Brandt
 *
 */
public class TimeCircleEstimator implements Estimator {

	private static final int SECONDS_TO_MS = 1000;

	private MovingObjectIndexOld index;
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
	public TimeCircleEstimator(MovingObjectIndexOld index, double radiusExtensionFactor, int numberOfExtensions,
			double maxSpeedMeterPerSecond) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
		this.numberOfExtensions = numberOfExtensions;
		this.maxSpeedMeterPerSecond = maxSpeedMeterPerSecond;
	}

	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime targetTime) {
		Set<String> objectsToPredict = new HashSet<>();

		// First circle is simply an extension without the consideration of time
		double radiusLastExtendedCircle = radius * this.radiusExtensionFactor;
		Map<String, List<ResultElement>> extendedInnerCircleResults = this.index.queryCircleWOPrediction(centerLatitude,
				centerLongitude, radiusLastExtendedCircle, null);
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
			 * extended circle). In fact, we calculate the time from the border from the
			 * first extension to the real radius because this in the minimum distance the
			 * objects need to travel.
			 */
			long travelTimeMs = (long) ((distanceToQueryCircle / this.maxSpeedMeterPerSecond) * SECONDS_TO_MS);

			// Make this to a timestamp by subtracting this from the current timestamp
			PointInTime timeStampInPast = targetTime.minus(travelTimeMs);
			PointInTime timeStampInFuture = targetTime.plus(travelTimeMs);
			Map<String, List<ResultElement>> bigCircleTimeFiltered = this.index.queryCircleWOPrediction(centerLatitude,
					centerLongitude, outerCircle, new TimeInterval(timeStampInPast, timeStampInFuture));

			/*
			 * Add the new keys to the exiting set. We do not need to actually do the
			 * subtraction to have a donut query as we only add the elements which are not
			 * already in the result set.
			 */
			objectsToPredict.addAll(bigCircleTimeFiltered.keySet());

			// Extend the radius for the next iteration
			radiusLastExtendedCircle = outerCircle;
		}
		return objectsToPredict;
	}

}
