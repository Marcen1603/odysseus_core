package de.uniol.inf.is.odysseus.prediction.metadata;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * An identifier will be used in the prediction
 * based approach. In this approach all temporal adjacent
 * stream elements that represent the same real world
 * entity have the same identifier
 * 
 * @author Andre Bolles
 *
 */
public interface IIdentifier<T> extends IClone {

	public T getIdentifier();
	public void setIdentifier(T id);
	public boolean equals(IIdentifier<T> otherId);
}
