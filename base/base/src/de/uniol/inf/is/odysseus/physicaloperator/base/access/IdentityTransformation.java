package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class IdentityTransformation<InOut extends IMetaAttributeContainer<?>> implements IDataTransformation<InOut, InOut> {

	public InOut transform(InOut inElem){
		return inElem;
	}
}
