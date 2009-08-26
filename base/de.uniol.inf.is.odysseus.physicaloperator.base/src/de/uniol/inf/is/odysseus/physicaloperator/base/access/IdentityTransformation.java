package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public class IdentityTransformation<InOut extends IMetaAttribute> implements IDataTransformation<InOut, InOut> {

	public InOut transform(InOut inElem){
		return inElem;
	}
}
