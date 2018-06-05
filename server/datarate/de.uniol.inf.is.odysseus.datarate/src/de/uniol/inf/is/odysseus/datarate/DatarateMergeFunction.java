package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;


public class DatarateMergeFunction implements IInlineMetadataMergeFunction<IDatarate>, Cloneable {

	public DatarateMergeFunction() {

	}

	public DatarateMergeFunction( DatarateMergeFunction func ) {

	}

	@Override
	public void mergeInto(IDatarate result, IDatarate inLeft, IDatarate inRight) {
		result.setDatarates(inLeft.getDatarates());
		result.setDatarates(inRight.getDatarates());
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IDatarate.class;
	}


	@Override
	public DatarateMergeFunction clone() {
		return new DatarateMergeFunction(this);
	}
}
