package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;


public interface IDataTransformation<In, Out extends IMetaAttributeContainer> {

	public Out transform(In inElem);
}
