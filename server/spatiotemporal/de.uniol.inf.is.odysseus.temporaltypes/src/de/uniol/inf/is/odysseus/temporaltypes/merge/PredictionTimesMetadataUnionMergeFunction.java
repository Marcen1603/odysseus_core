package de.uniol.inf.is.odysseus.temporaltypes.merge;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.PredictionTime;

/**
 * Used to merge two metadata fields with prediction times, especially for
 * aggregation. Merges the lists by doing a union.
 * 
 * @author Tobias Brandt
 *
 */
public class PredictionTimesMetadataUnionMergeFunction implements IInlineMetadataMergeFunction<IPredictionTimes> {

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

	public PredictionTimesMetadataUnionMergeFunction() {
		this.useSmallerTimeUnit = false;
	}

	public PredictionTimesMetadataUnionMergeFunction(boolean useSmallerTimeUnit) {
		this.useSmallerTimeUnit = useSmallerTimeUnit;
	}

	@Override
	public void mergeInto(IPredictionTimes result, IPredictionTimes inLeft, IPredictionTimes inRight) {
		result = unionMerge(result, inLeft, inRight);
	}

	private IPredictionTimes unionMerge(IPredictionTimes result, IPredictionTimes inLeft, IPredictionTimes inRight) {

		TimeUnit smallerTimeUnit = getSmallerBiggerTimeUnit(inLeft.getPredictionTimeUnit(),
				inRight.getPredictionTimeUnit(), useSmallerTimeUnit);
		result.setTimeUnit(smallerTimeUnit);

		for (IPredictionTime leftPredictionTime : inLeft.getPredictionTimes()) {
			result = mergeInto(leftPredictionTime, inLeft.getPredictionTimeUnit(), result, smallerTimeUnit);
		}

		for (IPredictionTime rightPredictionTime : inRight.getPredictionTimes()) {
			result = mergeInto(rightPredictionTime, inRight.getPredictionTimeUnit(), result, smallerTimeUnit);
		}

		return result;
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

	private IPredictionTimes mergeInto(IPredictionTime merge, TimeUnit mergeTimeUnit, IPredictionTimes into, TimeUnit targetTimeUnit) {

		IPredictionTime toRemove = null;
		IPredictionTime toAdd = null;

		for (IPredictionTime time : into.getPredictionTimes()) {
			IPredictionTime union = union(merge, mergeTimeUnit, time, into.getPredictionTimeUnit(), targetTimeUnit);
			if (union != null) {
				toRemove = time;
				toAdd = union;
			}
		}

		if (toAdd != null && toRemove != null && into instanceof IPredictionTimes) {
			IPredictionTimes predictionTimes = (IPredictionTimes) into;
			predictionTimes.getPredictionTimes().remove(toRemove);
			predictionTimes.getPredictionTimes().add(toAdd);
		} else if (toAdd == null) {
			into.addPredictionTime(merge);
		}

		return into;
	}

	private IPredictionTime union(IPredictionTime left, TimeUnit leftTimeUnit, IPredictionTime right, TimeUnit rightTimeUnit,
			TimeUnit targetTimeUnit) {

		long leftTargetStartTime = targetTimeUnit.convert(left.getPredictionStart().getMainPoint(), leftTimeUnit);
		PointInTime leftStart = new PointInTime(leftTargetStartTime);

		long leftTargetEndTime = targetTimeUnit.convert(left.getPredictionEnd().getMainPoint(), leftTimeUnit);
		PointInTime leftEnd = new PointInTime(leftTargetEndTime);

		long rightTargetTime = targetTimeUnit.convert(right.getPredictionStart().getMainPoint(), rightTimeUnit);
		PointInTime rightStart = new PointInTime(rightTargetTime);

		long rightTargetEndTime = targetTimeUnit.convert(right.getPredictionEnd().getMainPoint(), rightTimeUnit);
		PointInTime rightEnd = new PointInTime(rightTargetEndTime);

		TimeInterval leftInterval = new TimeInterval(leftStart, leftEnd);
		TimeInterval rightInterval = new TimeInterval(rightStart, rightEnd);
		TimeInterval unified = PredictionTime.union(leftInterval, rightInterval);
		if (unified == null) {
			return null;
		}
		IPredictionTime union = (IPredictionTime) left.clone();
		union.setPredictionStartAndEnd(unified.getStart(), unified.getEnd());
		return union;
	}

	@Override
	public IInlineMetadataMergeFunction<? super IPredictionTimes> clone() {
		return new PredictionTimesMetadataUnionMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IPredictionTimes.class;
	}

}