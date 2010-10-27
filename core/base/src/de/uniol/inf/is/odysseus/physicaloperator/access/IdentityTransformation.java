package de.uniol.inf.is.odysseus.physicaloperator.access;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;

public class IdentityTransformation<InOut extends IMetaAttributeContainer<?>> implements IDataTransformation<InOut, InOut> {

	@Override
	public InOut transform(InOut inElem){
		return inElem;
	}
}
