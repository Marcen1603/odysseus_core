package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex;

public class ApproximateTimeCircleTrajectoryEstimator implements TrajectoryEstimator {

	protected ApproximateTimeCircleEstimator approximateTimeCircleEstimator;
	protected long trajectoryTimeStepMs;

	public ApproximateTimeCircleTrajectoryEstimator(SpatialIndex index, double radiusExtensionFactor,
			int numberOfExtensions, double maxSpeedMeterPerSecond, long trajectoryTimeStepMs) {
		this.approximateTimeCircleEstimator = new ApproximateTimeCircleEstimator(index, radiusExtensionFactor,
				numberOfExtensions, maxSpeedMeterPerSecond);
		this.trajectoryTimeStepMs = trajectoryTimeStepMs;
	}

	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime startTime, PointInTime endTime) {

		Set<String> allObjectsToPredict = new HashSet<>();

		/* Predict for all sampled points on the trajectory. */
		PointInTime timeSteps = new PointInTime(startTime);
		while (timeSteps.plus(this.trajectoryTimeStepMs).beforeOrEquals(endTime)) {
			Set<String> estimatedObjects = this.approximateTimeCircleEstimator.estimateObjectsToPredict(centerLatitude,
					centerLongitude, radius, timeSteps);
			allObjectsToPredict.addAll(estimatedObjects);
			timeSteps = timeSteps.plus(this.trajectoryTimeStepMs);
		}

		/*
		 * The time distance may not exactly end on the endpoint given. We simply set it
		 * to the last point.
		 */
		if (timeSteps.before(endTime)) {
			Set<String> estimatedObjects = this.approximateTimeCircleEstimator.estimateObjectsToPredict(centerLatitude,
					centerLongitude, radius, endTime);
			allObjectsToPredict.addAll(estimatedObjects);
		}

		return allObjectsToPredict;
	}

}
