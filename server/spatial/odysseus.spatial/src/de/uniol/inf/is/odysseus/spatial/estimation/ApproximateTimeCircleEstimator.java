package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;

public class ApproximateTimeCircleEstimator implements Estimator {

	private IMovingObjectDataStructure index;
	private double radiusExtensionFactor;
	private int numberOfExtensions;
	private double maxSpeedMeterPerSecond;

	public ApproximateTimeCircleEstimator(IMovingObjectDataStructure index, double radiusExtensionFactor, int numberOfExtensions,
			double maxSpeedMeterPerSecond) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
		this.numberOfExtensions = numberOfExtensions;
		this.maxSpeedMeterPerSecond = maxSpeedMeterPerSecond;
	}

	@Override
	public Set<String> estimateObjectsToPredict(String centerObjectId, double radius, PointInTime targetTime) {

		// TODO Use methods in the index which only approximated circle calculations to
		// avoid distance calculations

		// TODO Use the number of extensions: make this more generic by using a loop
		// instead of a fixed number of extensions

		Set<String> objectsToPredict = new HashSet<>();

		// First circle is simply an extension without the consideration of time
		double radiusFirstExtendedCircle = radius * this.radiusExtensionFactor;
		Map<String, List<ResultElement>> extendedInnerCircleResults = this.index.queryCircleWOPrediction(centerObjectId,
				radiusFirstExtendedCircle);
		Set<String> extendedInnerCircle = extendedInnerCircleResults.keySet();
		objectsToPredict.addAll(extendedInnerCircle);

		/*
		 * Second and more circles take time into consideration without calculation of
		 * the distance between the objects and the center (because we want to avoid
		 * slow calculations)
		 */
		double distanceRadiusToFirstCircle = (radius * this.radiusExtensionFactor) - radius;

		// Define the outer limit of the bigger circle (the second extension)
		double outerCircle = radiusFirstExtendedCircle * this.radiusExtensionFactor;

		/*
		 * How much older must they be at minimum? This is the time they need at minimum
		 * to get from the outer donut to the inner circle of the query (not the
		 * extended circle). In fact, we calculate the time from the border from the
		 * first extension to the real radius because this in the minimum distance the
		 * objects need to travel.
		 */
		long travelTimeMs = (long) ((distanceRadiusToFirstCircle / this.maxSpeedMeterPerSecond) * 1000);

		// Make this to a timestamp by subtracting this from the current timestamp
		PointInTime timeStampInPast = targetTime.minus(travelTimeMs);
		PointInTime timeStampInFuture = targetTime.plus(travelTimeMs);
		Map<String, List<ResultElement>> bigCircleTimeFiltered = this.index.queryCircleWOPrediction(centerObjectId,
				outerCircle, new TimeInterval(timeStampInPast, timeStampInFuture));

		/*
		 * Add the new keys to the exiting set. We do not need to actually do the
		 * subtraction to have a donut query as we only add the elements which are not
		 * already in the result set.
		 */
		objectsToPredict.addAll(bigCircleTimeFiltered.keySet());
		return objectsToPredict;
	}

}
