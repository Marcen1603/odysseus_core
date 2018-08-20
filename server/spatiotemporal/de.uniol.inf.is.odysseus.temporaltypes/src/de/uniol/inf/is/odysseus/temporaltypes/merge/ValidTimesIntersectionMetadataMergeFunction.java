package de.uniol.inf.is.odysseus.temporaltypes.merge;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;

/**
 * Used to merge two metadata fields with valid times. Merges the lists by doing
 * an intersection. Just like the normal TimeIntervals.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimesIntersectionMetadataMergeFunction implements IInlineMetadataMergeFunction<IValidTimes> {

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

	public ValidTimesIntersectionMetadataMergeFunction() {
		this.useSmallerTimeUnit = false;
	}

	public ValidTimesIntersectionMetadataMergeFunction(boolean useSmallerTimeUnit) {
		this.useSmallerTimeUnit = useSmallerTimeUnit;
	}

	@Override
	public void mergeInto(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		/*
		 * The previous prediction times need to be cleared, because its just a copy of
		 * the left previous one. But if we would keep it, we would simply copy the left
		 * one, because the left always already contains the intersection.
		 */
		result.clear();
		result = intersectionMerge(result, inLeft, inRight);
	}

	private IValidTimes intersectionMerge(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {

		TimeUnit targetTimeUnit = getSmallerBiggerTimeUnit(inLeft.getPredictionTimeUnit(),
				inRight.getPredictionTimeUnit(), useSmallerTimeUnit);
		result.setTimeUnit(targetTimeUnit);

		for (IValidTime leftValidTime : inLeft.getValidTimes()) {
			for (IValidTime rightValidTime : inRight.getValidTimes()) {

				IValidTime convertedLeftValidTime = convertToOtherTimeUnit(leftValidTime,
						inLeft.getPredictionTimeUnit(), targetTimeUnit);
				IValidTime convertedRightValidTime = convertToOtherTimeUnit(rightValidTime,
						inRight.getPredictionTimeUnit(), targetTimeUnit);

				IValidTime mergedData = ValidTime.intersect(convertedLeftValidTime, convertedRightValidTime);
				if (mergedData != null && !includesInterval(result, mergedData)) {
					result = addValidTimeToValidTimes(result, mergedData);
				}
			}
		}
		return result;
	}

	private IValidTime convertToOtherTimeUnit(IValidTime validTime, TimeUnit prevTimeUnit, TimeUnit targetTimeUnit) {
		long leftTargetStartTime = targetTimeUnit.convert(validTime.getValidStart().getMainPoint(), prevTimeUnit);
		PointInTime leftStart = new PointInTime(leftTargetStartTime);

		long leftTargetEndTime = targetTimeUnit.convert(validTime.getValidEnd().getMainPoint(), prevTimeUnit);
		PointInTime leftEnd = new PointInTime(leftTargetEndTime);

		return new ValidTime(leftStart, leftEnd);
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

	private boolean includesInterval(IValidTimes includes, IValidTime toInclude) {
		for (IValidTime time : includes.getValidTimes()) {
			if (leftIncludesRight(time, toInclude)) {
				return true;
			}
		}
		return false;
	}

	private boolean leftIncludesRight(IValidTime left, IValidTime right) {
		return left.getValidStart().beforeOrEquals(right.getValidStart())
				&& left.getValidEnd().afterOrEquals(right.getValidEnd());
	}

	private IValidTimes addValidTimeToValidTimes(IValidTimes addTo, IValidTime validTime) {
		if (validTime != null && validTime.getValidEnd().after(validTime.getValidStart())) {
			// Only add if the resulting interval is at least valid for one time instance
			addTo.addValidTime(validTime);
		}
		return addTo;
	}

	@Override
	public IInlineMetadataMergeFunction<? super IValidTimes> clone() {
		return new ValidTimesIntersectionMetadataMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IValidTimes.class;
	}

}