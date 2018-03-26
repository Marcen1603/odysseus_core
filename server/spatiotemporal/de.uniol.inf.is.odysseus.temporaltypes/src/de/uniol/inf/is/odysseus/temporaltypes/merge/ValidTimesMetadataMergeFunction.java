package de.uniol.inf.is.odysseus.temporaltypes.merge;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;

public class ValidTimesMetadataMergeFunction implements IInlineMetadataMergeFunction<IValidTimes> {

	@Override
	public void mergeInto(IValidTimes result, IValidTimes inLeft, IValidTimes inRight) {
		for (IValidTime leftValidTime : inLeft.getValidTimes()) {
			for (IValidTime rightValidTime : inRight.getValidTimes()) {
				IValidTime mergedData = mergeValidTime(leftValidTime, rightValidTime);
				result = addValidTimeToValidTimes(result, mergedData);
			}
		}
	}
	
	private IValidTime mergeValidTime(IValidTime left, IValidTime right) {
		IValidTime mergedData = (IValidTime) left.createInstance();
		mergedData.setValidStart(PointInTime.max(left.getValidStart(), right.getValidStart()));
		mergedData.setValidEnd(PointInTime.min(left.getValidEnd(), right.getValidEnd()));
		return mergedData;
	}
	
	private IValidTimes addValidTimeToValidTimes(IValidTimes addTo, IValidTime validTime) {
		if(validTime.getValidEnd().after(validTime.getValidStart())) {
			addTo.addValidTime(validTime);
		}
		return addTo;
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