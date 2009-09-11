package de.uniol.inf.is.odysseus.metadata.base;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

/**
 * @author Jonas Jacobi
 */
public interface IMetaAttributeContainer<T extends IMetaAttribute> extends IClone, Serializable {
	public T getMetadata();
	public void setMetadata(T metadata);
}