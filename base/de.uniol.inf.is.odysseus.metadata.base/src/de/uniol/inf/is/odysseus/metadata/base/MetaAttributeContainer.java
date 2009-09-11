package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

/**
 * @author Jonas Jacobi
 */
public class MetaAttributeContainer<T extends IMetaAttribute> implements IMetaAttributeContainer<T> {
	private static final long serialVersionUID = -4027708515386331790L;
	
	private T metadata;

	public MetaAttributeContainer() {
		this.metadata = null;
	}

	public MetaAttributeContainer(T data) {
		this.metadata = data;
	}

	@SuppressWarnings("unchecked")
	public MetaAttributeContainer(MetaAttributeContainer<T> copy) {
		if (copy.metadata != null) {
			this.metadata = (T) copy.metadata.clone();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.dynaquest.queryexecution.po.base.IMetaAttribute#getMetadata()
	 */
	public final T getMetadata() {
		return metadata;
	}

	public MetaAttributeContainer<T> clone() {
		return new MetaAttributeContainer<T>(this);
	}

	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}
}
