package de.uniol.inf.is.odysseus.temporaltypes.merge;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;

/**
 * Used to merge two metadata fields with valid times. Merges the lists by doing
 * an intersection. Just like the normal TimeIntervals.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimesIntersectionMetadataMergeFunction implements IInlineMetadataMergeFunction<IValidTimes> {

	@Override
	public void mergeInto(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		result = intersectionMerge(result, inLeft, inRight);
	}

	private IValidTimes intersectionMerge(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
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

	@Override
	public IInlineMetadataMergeFunction<? super IValidTimes> clone() {
		return new ValidTimesIntersectionMetadataMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IValidTimes.class;
	}

}