package de.uniol.inf.is.odysseus.core.metadata;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements
		IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	private boolean inOrder = true;
	private Map<String, Object> metadataMap = new HashMap<String, Object>();
	private T metadata = null;

	public AbstractStreamObject() {
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractStreamObject(AbstractStreamObject<T> other) {
		this.inOrder = other.inOrder;
		this.metadataMap = new HashMap<>(other.metadataMap);
		if (other.metadata != null) {
			this.metadata = (T) other.metadata.clone();
		}
	}

	@Override
	public boolean isInOrder() {
		return inOrder;
	}

	@Override
	public void setInOrder(boolean inOrder) {
		this.inOrder = inOrder;
	}

	@Override
	public Object getMetadata(String name) {
		return metadataMap.get(name);
	}

	@Override
	public void setMetadata(String name, Object content) {
		metadataMap.put(name, content);
	}

	@Override
	public final T getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}

	abstract public AbstractStreamObject<T> clone();

}
