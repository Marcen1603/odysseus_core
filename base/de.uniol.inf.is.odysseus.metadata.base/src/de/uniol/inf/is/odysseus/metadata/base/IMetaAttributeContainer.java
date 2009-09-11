package de.uniol.inf.is.odysseus.metadata.base;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * @author Jonas Jacobi
 */
public interface IMetaAttributeContainer<T extends IClone> extends IClone, Serializable {
	public T getMetadata();
	public void setMetadata(T metadata);
}