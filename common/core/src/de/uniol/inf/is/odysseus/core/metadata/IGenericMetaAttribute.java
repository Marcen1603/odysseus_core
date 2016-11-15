package de.uniol.inf.is.odysseus.core.metadata;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public interface IGenericMetaAttribute extends IMetaAttribute {

	void setContent(Tuple<?> tuple);
	Tuple<?> getContent();
	
	
}
