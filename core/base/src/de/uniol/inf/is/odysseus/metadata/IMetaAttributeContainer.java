package de.uniol.inf.is.odysseus.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.IClone;

/**
 * @author Jonas Jacobi
 */
public interface IMetaAttributeContainer<T extends IMetaAttribute> extends IClone, Serializable {
	public T getMetadata();
	public void setMetadata(T metadata);
	@Override
	public IMetaAttributeContainer<T> clone();
}