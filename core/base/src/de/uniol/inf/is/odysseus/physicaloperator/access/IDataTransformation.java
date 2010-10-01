package de.uniol.inf.is.odysseus.physicaloperator.access;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;


public interface IDataTransformation<In, Out extends IMetaAttributeContainer<?>> {

	public Out transform(In inElem);
}
