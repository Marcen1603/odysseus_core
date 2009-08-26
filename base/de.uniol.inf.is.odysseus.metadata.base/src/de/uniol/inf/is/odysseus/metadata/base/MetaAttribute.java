package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * @author Jonas Jacobi
 */
public class MetaAttribute<T extends IClone> implements IMetaAttribute<T> {
	private static final long serialVersionUID = -4027708515386331790L;
	
	private T metadata;

	public MetaAttribute() {
		this.metadata = null;
	}

	public MetaAttribute(T data) {
		this.metadata = data;
	}

	@SuppressWarnings("unchecked")
	public MetaAttribute(MetaAttribute<T> copy) {
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

	public MetaAttribute<T> clone() {
		return new MetaAttribute<T>(this);
	}

	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}
}
