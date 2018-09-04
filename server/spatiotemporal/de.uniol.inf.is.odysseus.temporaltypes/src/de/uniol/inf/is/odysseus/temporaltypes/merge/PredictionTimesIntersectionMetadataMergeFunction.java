package de.uniol.inf.is.odysseus.temporaltypes.merge;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.PredictionTime;

/**
 * Used to merge two metadata fields with prediction times. Merges the lists by doing
 * an intersection. Just like the normal TimeIntervals.
 * 
 * @author Tobias Brandt
 *
 */
public class PredictionTimesIntersectionMetadataMergeFunction implements IInlineMetadataMergeFunction<IPredictionTimes> {

	/*
	 * It is possible that we have to merge two prediction times that have different
	 * base time units. In that case, typically the less granular unit is used,
	 * because for the more granular times the values would be missing in the
	 * temporal attribute with the less granular prediction time.
	 * 
	 * If, for whatever reason, the more granular unit should be used, is can be set
	 * with this variable.
	 * 
	 * Example: Seconds are more granular (have a higher granularity) than minutes.
	 */
	private boolean useSmallerTimeUnit;

	public PredictionTimesIntersectionMetadataMergeFunction() {
		this.useSmallerTimeUnit = false;
	}

	public PredictionTimesIntersectionMetadataMergeFunction(boolean useSmallerTimeUnit) {
		this.useSmallerTimeUnit = useSmallerTimeUnit;
	}

	@Override
	public void mergeInto(IPredictionTimes result, IPredictionTimes inLeft, IPredictionTimes inRight) {
		/*
		 * The previous prediction times need to be cleared, because its just a copy of
		 * the left previous one. But if we would keep it, we would simply copy the left
		 * one, because the left always already contains the intersection.
		 */
		result.clear();
		result = intersectionMerge(result, inLeft, inRight);
	}

	private IPredictionTimes intersectionMerge(IPredictionTimes result, IPredictionTimes inLeft, IPredictionTimes inRight) {

		TimeUnit targetTimeUnit = getSmallerBiggerTimeUnit(inLeft.getPredictionTimeUnit(),
				inRight.getPredictionTimeUnit(), useSmallerTimeUnit);
		result.setTimeUnit(targetTimeUnit);

		for (IPredictionTime leftPredictionTime : inLeft.getPredictionTimes()) {
			for (IPredictionTime rightPredictionTime : inRight.getPredictionTimes()) {

				IPredictionTime convertedLeftPredictionTime = convertToOtherTimeUnit(leftPredictionTime,
						inLeft.getPredictionTimeUnit(), targetTimeUnit);
				IPredictionTime convertedRightPredictionTime = convertToOtherTimeUnit(rightPredictionTime,
						inRight.getPredictionTimeUnit(), targetTimeUnit);

				IPredictionTime mergedData = PredictionTime.intersect(convertedLeftPredictionTime, convertedRightPredictionTime);
				if (mergedData != null && !includesInterval(result, mergedData)) {
					result = addPredictionTimeToPredictionTimes(result, mergedData);
				}
			}
		}
		return result;
	}

	private IPredictionTime convertToOtherTimeUnit(IPredictionTime predictionTime, TimeUnit prevTimeUnit, TimeUnit targetTimeUnit) {
		long leftTargetStartTime = targetTimeUnit.convert(predictionTime.getPredictionStart().getMainPoint(), prevTimeUnit);
		PointInTime leftStart = new PointInTime(leftTargetStartTime);

		long leftTargetEndTime = targetTimeUnit.convert(predictionTime.getPredictionEnd().getMainPoint(), prevTimeUnit);
		PointInTime leftEnd = new PointInTime(leftTargetEndTime);

		return new PredictionTime(leftStart, leftEnd);
	}

	private TimeUnit getSmallerBiggerTimeUnit(TimeUnit left, TimeUnit right, boolean useSmallerTimeUnit) {
		if (left == null) {
			return right;
		} else if (right == null) {
			return left;
		}

		int timeUnitCompare = left.compareTo(right);
		if (timeUnitCompare == 0) {
			return left;
		} else if (useSmallerTimeUnit ? timeUnitCompare < 0 : timeUnitCompare > 0) {
			return left;
		} else {
			return right;
		}
	}

	private boolean includesInterval(IPredictionTimes includes, IPredictionTime toInclude) {
		for (IPredictionTime time : includes.getPredictionTimes()) {
			if (leftIncludesRight(time, toInclude)) {
				return true;
			}
		}
		return false;
	}

	private boolean leftIncludesRight(IPredictionTime left, IPredictionTime right) {
		return left.getPredictionStart().beforeOrEquals(right.getPredictionStart())
				&& left.getPredictionEnd().afterOrEquals(right.getPredictionEnd());
	}

	private IPredictionTimes addPredictionTimeToPredictionTimes(IPredictionTimes addTo, IPredictionTime predictionTime) {
		if (predictionTime != null && predictionTime.getPredictionEnd().after(predictionTime.getPredictionStart())) {
			// Only add if the resulting interval is at least valid for one time instance
			addTo.addPredictionTime(predictionTime);
		}
		return addTo;
	}

	@Override
	public IInlineMetadataMergeFunction<? super IPredictionTimes> clone() {
		return new PredictionTimesIntersectionMetadataMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IPredictionTimes.class;
	}

}