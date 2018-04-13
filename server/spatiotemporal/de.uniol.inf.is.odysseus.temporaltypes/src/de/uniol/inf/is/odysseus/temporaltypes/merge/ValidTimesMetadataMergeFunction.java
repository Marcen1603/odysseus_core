package de.uniol.inf.is.odysseus.temporaltypes.merge;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;

/**
 * Used to merge two metadata fields with valid times. Merges the lists.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimesMetadataMergeFunction implements IInlineMetadataMergeFunction<IValidTimes> {

	@Override
	public void mergeInto(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		// result = substractingMerge(result, inLeft, inRight);
		result = unionMerge(result, inLeft, inRight);
	}

	private IValidTimes substractingMerge(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		for (IValidTime leftValidTime : inLeft.getValidTimes()) {
			for (IValidTime rightValidTime : inRight.getValidTimes()) {
				IValidTime mergedData = mergeValidTime(leftValidTime, rightValidTime);
				result = addValidTimeToValidTimes(result, mergedData);
			}
		}
		return result;
	}

	private IValidTime mergeValidTime(IValidTime left, IValidTime right) {
		IValidTime mergedData = (IValidTime) left.createInstance();
		PointInTime mergedStart = PointInTime.max(left.getValidStart(), right.getValidStart());
		PointInTime mergedEnd = PointInTime.min(left.getValidEnd(), right.getValidEnd());
		if (!mergedEnd.before(mergedStart)) {
			mergedData.setValidStart(mergedStart);
			mergedData.setValidEnd(mergedEnd);
		}
		return mergedData;
	}

	private IValidTimes addValidTimeToValidTimes(IValidTimes addTo, IValidTime validTime) {
		if (validTime.getValidEnd().after(validTime.getValidStart())) {
			// Only add if the resulting interval is at least valid for one time instance
			addTo.addValidTime(validTime);
		}
		return addTo;
	}

	private IValidTimes unionMerge(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		
		for (IValidTime leftValidTime : inLeft.getValidTimes()) {
			result = mergeInto(leftValidTime, result);
		}
		
		for (IValidTime rightValidTime : inRight.getValidTimes()) {
			result = mergeInto(rightValidTime, result);
		}
		
		return result;
	}
	
	private IValidTimes mergeInto(IValidTime merge, IValidTimes into) {
		
		IValidTime toRemove = null;
		IValidTime toAdd = null;
		
		for (IValidTime time : into.getValidTimes()) {
			IValidTime union = union(merge, time);
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

	private IValidTime union(IValidTime left, IValidTime right) {
		TimeInterval leftInterval = new TimeInterval(left.getValidStart(), left.getValidEnd());
		TimeInterval rightInterval = new TimeInterval(right.getValidStart(), right.getValidEnd());
		TimeInterval unified = TimeInterval.union(leftInterval, rightInterval);
		if (unified == null) {
			return null;
		}
		IValidTime union = (IValidTime) left.clone();
		union.setValidStartAndEnd(unified.getStart(), unified.getEnd());
		return union;
	}

	@Override
	public IInlineMetadataMergeFunction<? super IValidTimes> clone() {
		return new ValidTimesMetadataMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IValidTimes.class;
	}

}