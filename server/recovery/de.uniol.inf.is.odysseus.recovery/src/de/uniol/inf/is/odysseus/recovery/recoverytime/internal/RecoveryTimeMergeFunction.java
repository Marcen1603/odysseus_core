package de.uniol.inf.is.odysseus.recovery.recoverytime.internal;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;

/**
 * Function to merge two recovery time meta attribute values. <br />
 * The longer recovery time is taken, recovery times get not mixed.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryTimeMergeFunction implements IInlineMetadataMergeFunction<IRecoveryTime> {

	@Override
	public void mergeInto(IRecoveryTime result, IRecoveryTime inLeft, IRecoveryTime inRight) {
		// Take the longer recovery time, but don't mix.
		if (inLeft.getRecoverySystemTime() >= inRight.getRecoverySystemTime()) {
			mergeInto(result, inLeft);
		} else {
			mergeInto(result, inRight);
		}
	}

	private static void mergeInto(IRecoveryTime result, IRecoveryTime in) {
		result.setSystemTimeStart(in.getSystemTimeStart());
		result.setSystemTimeEnd(in.getSystemTimeEnd());
		result.setApplicationTimeStart(in.getApplicationTimeStart());
		result.setApplicationTimeEnd(in.getApplicationTimeEnd());
	}

	@Override
	public IInlineMetadataMergeFunction<? super IRecoveryTime> clone() {
		return new RecoveryTimeMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IRecoveryTime.class;
	}

}
