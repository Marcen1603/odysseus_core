package de.uniol.inf.is.odysseus.persistentqueries;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IProvidesMetadata;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

@SuppressWarnings({ "unchecked" })
public class StreamObjectWrapper<T extends IStreamObject<M>, M extends IMetaAttribute> implements IProvidesMetadata<M> {

	T wrapped;

	public StreamObjectWrapper(T elemToWrapp) {
		this.wrapped = elemToWrapp;
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode(true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StreamObjectWrapper) {
			return wrapped.equals(((StreamObjectWrapper) obj).wrapped, true);
		} else {
			return false;
		}
	}

	public T getWrappedObject() {
		return wrapped;
	}

	@Override
	public M getMetadata() {
		return wrapped.getMetadata();
	}

	@Override
	public void setMetadata(M metadata) {
		wrapped.setMetadata(metadata);
	}

}
