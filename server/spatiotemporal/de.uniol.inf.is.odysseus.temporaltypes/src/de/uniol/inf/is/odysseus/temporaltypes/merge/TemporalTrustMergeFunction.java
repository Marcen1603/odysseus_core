package de.uniol.inf.is.odysseus.temporaltypes.merge;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ITemporalTrust;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;
import de.uniol.inf.is.odysseus.trust.TrustMergeFunction;

public class TemporalTrustMergeFunction implements IInlineMetadataMergeFunction<ITemporalTrust> {

	private IInlineMetadataMergeFunction<ITrust> trustMerger; 
		
	public TemporalTrustMergeFunction() {
		trustMerger = new TrustMergeFunction();
	}
	
	
	@Override
	public void mergeInto(ITemporalTrust result, ITemporalTrust inLeft, ITemporalTrust inRight) {
				
		result = (ITemporalTrust) inLeft.clone();
		
		for (PointInTime time : inRight.getTemporalPoints()) {
			ITrust rightTrust = inRight.getTrustValue(time);
			ITrust leftTrust = inLeft.getTrustValue(time);
			if (leftTrust == null) {
				result.setTrust(time, rightTrust);
			} else {
				ITrust newTrust = new Trust();
				trustMerger.mergeInto(newTrust, leftTrust, rightTrust);
				result.setTrust(time, newTrust);
			}
		}
	}

	@Override
	public IInlineMetadataMergeFunction<? super ITemporalTrust> clone() {
		return new TemporalTrustMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return ITemporalTrust.class;
	}


}
