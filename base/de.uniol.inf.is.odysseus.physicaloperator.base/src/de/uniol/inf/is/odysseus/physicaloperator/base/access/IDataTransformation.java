package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;


public interface IDataTransformation<In, Out extends IMetaAttribute> {

	public Out transform(In inElem);
}
