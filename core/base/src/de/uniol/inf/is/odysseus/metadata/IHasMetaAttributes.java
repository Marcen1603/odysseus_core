package de.uniol.inf.is.odysseus.metadata;

import java.util.List;

public interface IHasMetaAttributes {
	public List<Class<? extends IMetaAttribute>> getMetaAttributes();
}
