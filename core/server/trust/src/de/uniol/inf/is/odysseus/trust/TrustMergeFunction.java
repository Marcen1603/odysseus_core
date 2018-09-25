package de.uniol.inf.is.odysseus.trust;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class TrustMergeFunction implements IInlineMetadataMergeFunction<ITrust> {

	@Override
	public void mergeInto(ITrust result, ITrust inLeft, ITrust inRight) {
		result.setTrust(Math.min(inLeft.getTrust(), inRight.getTrust()));
	}

	@Override
	public IInlineMetadataMergeFunction<? super ITrust> clone() {
		return this;
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return ITrust.class;
	}

}
