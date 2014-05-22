package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;

public class SystemLoadMergeFunction implements IInlineMetadataMergeFunction<ISystemLoad>, Cloneable{

	public SystemLoadMergeFunction() {
		
	}
	
	public SystemLoadMergeFunction(SystemLoadMergeFunction copy) {
		
	}
	
	@Override
	public void mergeInto(ISystemLoad result, ISystemLoad inLeft, ISystemLoad inRight) {
		((SystemLoad)result).insert((SystemLoad)inLeft);
		((SystemLoad)result).insert((SystemLoad)inRight);
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
