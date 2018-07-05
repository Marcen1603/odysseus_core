package de.uniol.inf.is.odysseus.temporaltypes.merge;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;

/**
 * Used to merge two metadata fields with valid times, especially for
 * aggregation. Merges the lists by doing a union.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimesMetadataUnionMergeFunction implements IInlineMetadataMergeFunction<IValidTimes> {

	@Override
	public void mergeInto(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		result = unionMerge(result, inLeft, inRight);
	}

	private IValidTimes unionMerge(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {

		TimeUnit smallerTimeUnit = getSmallerTimeUnit(inLeft.getPredictionTimeUnit(), inRight.getPredictionTimeUnit());
		result.setTimeUnit(smallerTimeUnit);

		for (IValidTime leftValidTime : inLeft.getValidTimes()) {
			result = mergeInto(leftValidTime, inLeft.getPredictionTimeUnit(), result, smallerTimeUnit);
		}

		for (IValidTime rightValidTime : inRight.getValidTimes()) {
			result = mergeInto(rightValidTime, inRight.getPredictionTimeUnit(), result, smallerTimeUnit);
		}

		return result;
	}

	private TimeUnit getSmallerTimeUnit(TimeUnit left, TimeUnit right) {
		if (left == null) {
			return right;
		} else if (right == null) {
			return left;
		}
		
		int timeUnitCompare = left.compareTo(right);
		if (timeUnitCompare == 0) {
			return left;
		} else if (timeUnitCompare < 0) {
			return left;
		} else {
			return right;
		}
	}

	private IValidTimes mergeInto(IValidTime merge, TimeUnit mergeTimeUnit, IValidTimes into, TimeUnit targetTimeUnit) {

		IValidTime toRemove = null;
		IValidTime toAdd = null;

		for (IValidTime time : into.getValidTimes()) {
			IValidTime union = union(merge, mergeTimeUnit, time, into.getPredictionTimeUnit(), targetTimeUnit);
			if (union != null) {
				toRemove = time;
				toAdd = union;
			}
		}

		if (toAdd != null && toRemove != null && into instanceof IValidTimes) {
			IValidTimes validTimes = (IValidTimes) into;
			validTimes.getValidTimes().remove(toRemove);
			validTimes.getValidTimes().add(toAdd);
		} else if (toAdd == null) {
			into.addValidTime(merge);
		}

		return into;
	}

	private IValidTime union(IValidTime left, TimeUnit leftTimeUnit, IValidTime right, TimeUnit rightTimeUnit,
			TimeUnit targetTimeUnit) {

		long leftTargetStartTime = targetTimeUnit.convert(left.getValidStart().getMainPoint(), leftTimeUnit);
		PointInTime leftStart = new PointInTime(leftTargetStartTime);
		
		long leftTargetEndTime = targetTimeUnit.convert(left.getValidEnd().getMainPoint(), leftTimeUnit);
		PointInTime leftEnd = new PointInTime(leftTargetEndTime);

		long rightTargetTime = targetTimeUnit.convert(right.getValidStart().getMainPoint(), rightTimeUnit);
		PointInTime rightStart = new PointInTime(rightTargetTime);
		
		long rightTargetEndTime = targetTimeUnit.convert(right.getValidEnd().getMainPoint(), rightTimeUnit);
		PointInTime rightEnd = new PointInTime(rightTargetEndTime);

		TimeInterval leftInterval = new TimeInterval(leftStart, leftEnd);
		TimeInterval rightInterval = new TimeInterval(rightStart, rightEnd);
		TimeInterval unified = ValidTime.union(leftInterval, rightInterval);
		if (unified == null) {
			return null;
		}
		IValidTime union = (IValidTime) left.clone();
		union.setValidStartAndEnd(unified.getStart(), unified.getEnd());
		return union;
	}

	@Override
	public IInlineMetadataMergeFunction<? super IValidTimes> clone() {
		return new ValidTimesMetadataUnionMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IValidTimes.class;
	}

}