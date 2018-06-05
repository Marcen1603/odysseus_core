package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;

public class SystemLoadMergeFunction implements IInlineMetadataMergeFunction<ISystemLoad>, Cloneable{

	public SystemLoadMergeFunction() {
		
	}
	
	public SystemLoadMergeFunction(SystemLoadMergeFunction copy) {
		
	}
	
	@Override
	public void mergeInto(ISystemLoad result, ISystemLoad inLeft, ISystemLoad inRight) {
		// TODO: insert now in interface of ISystemLoad
		result.insert(inLeft);
		result.insert(inRight);
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return ISystemLoad.class;
	}

	@Override
	public SystemLoadMergeFunction clone() {
		return new SystemLoadMergeFunction(this);
	}
}
