package de.uniol.inf.is.odysseus.core.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements
		IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	private boolean inOrder = true;
	private Map<String, Object> metadataMap = new HashMap<String, Object>();
	private T metadata = null;
	private Map<String, Serializable> additionalContent;

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
	
	   // -----------------------------------------------------------------
    // additional content
    // -----------------------------------------------------------------
    @Override
    public Serializable getAdditionalContent(String name) {
        return this.additionalContent.get(name);
    }

    @Override
    public void setAdditionalContent(String name, Serializable content) {
        this.additionalContent.put(name, content);
    }

    @Override
    public Map<String, Serializable> getAdditionalContent() {
        return additionalContent;
    }

    @Override
    public void setAdditionalContent(Map<String, Serializable> additionalContent) {
        this.additionalContent = additionalContent;
    }

	abstract public AbstractStreamObject<T> clone();

}
