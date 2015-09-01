package de.uniol.inf.is.odysseus.core.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.Order;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	private Map<String, Object> metadataMap;
	private T metadata = null;
	private Map<String, Serializable> additionalContent;

	public AbstractStreamObject() {
	}

	@SuppressWarnings("unchecked")
	protected AbstractStreamObject(AbstractStreamObject<T> other) {
		if (other.metadataMap != null) {
			this.metadataMap = new HashMap<>(other.metadataMap);
		}
		if (other.metadata != null) {
			this.metadata = (T) other.metadata.clone();
		}
		if (other.additionalContent != null) {
			this.additionalContent = new HashMap<>(other.additionalContent);
		}
	}

	@Override
	final public Object getMetadata(String name) {
		return metadataMap.get(name);
	}

	@Override
	final public void setMetadata(String name, Object content) {
		if (metadataMap == null) {
			metadataMap = new HashMap<>();
		}
		metadataMap.put(name, content);
	}

	@Override
	final public void setMetadataMap(Map<String, Object> metaMap) {
		metadataMap = new HashMap<>();
		if (metaMap != null) {
			this.metadataMap.putAll(metaMap);
		}
	}

	@Override
	final public Map<String, Object> getMetadataMap() {
		if (metadataMap != null){
			return Collections.unmodifiableMap(metadataMap);
		}else{
			return null;
		}
	}

	@Override
	final public T getMetadata() {
		return metadata;
	}

	@Override
	final public void setMetadata(T metadata) {
		this.metadata = metadata;
	}

	// -----------------------------------------------------------------
	// additional content
	// -----------------------------------------------------------------
	@Override
	final public Serializable getAdditionalContent(String name) {
		if (this.additionalContent != null) {
			return this.additionalContent.get(name);
		} else {
			return null;
		}
	}

	@Override
	final public void setAdditionalContent(String name, Serializable content) {
		if (this.additionalContent == null) {
			this.additionalContent = new HashMap<>();
		}
		this.additionalContent.put(name, content);
	}

	@Override
	final public Map<String, Serializable> getAdditionalContent() {
		return additionalContent;
	}

	@Override
	final public void setAdditionalContent(Map<String, Serializable> additionalContent) {
		this.additionalContent = new HashMap<>();
		if (additionalContent != null) {
			this.additionalContent.putAll(additionalContent);
		}
	}

	// -------------------------------------
	// Merge
	// -------------------------------------

	@Override
	final public IStreamObject<T> merge(IStreamObject<T> left, IStreamObject<T> right, IMetadataMergeFunction<T> metamerge,
			Order order) {
		// Preserve meta data
		T metadateleft = left.getMetadata();
		T metadateright = right.getMetadata();

		IStreamObject<T> ret = process_merge(left, right, order);
		ret.setMetadata(metamerge.mergeMetadata(metadateleft, metadateright));

		// Use from right and overwrite with left
		if (order == Order.LeftRight) {
			mergeInternal(left, right, ret);
		} else if (order == Order.RightLeft) { // Use from Left and overwrite
												// with right
			mergeInternal(right, left, ret);
		}

		return ret;
	}

	private void mergeInternal(IStreamObject<T> left, IStreamObject<T> right, IStreamObject<T> ret) {
		// TODO: Merge function in cases where key is same!!

		if (right.getAdditionalContent() != null) {
			ret.setAdditionalContent(right.getAdditionalContent());
		}
		if (left.getAdditionalContent() != null) {
			if (right.getAdditionalContent() == null) {
				ret.setAdditionalContent(left.getAdditionalContent());
			} else {
				for (Entry<String, Serializable> a : left.getAdditionalContent().entrySet()) {
					ret.setAdditionalContent(a.getKey(), a.getValue());
				}
			}
		}
		if (right.getMetadataMap() != null) {
			ret.setMetadataMap(right.getMetadataMap());
		}
		if (left.getMetadataMap() != null) {
			if (right.getMetadataMap() == null) {
				ret.setMetadataMap(left.getMetadataMap());
			} else {
				for (Entry<String, Object> a : left.getMetadataMap().entrySet()) {
					ret.setMetadata(a.getKey(), a.getValue());
				}
			}
		}
	}

	protected IStreamObject<T> process_merge(IStreamObject<T> left, IStreamObject<T> right, Order order) {
		throw new IllegalArgumentException("Results cannot be merged!");
	}

	@Override
	abstract public AbstractStreamObject<T> clone();

	@Override
	abstract public AbstractStreamObject<T> newInstance();

	@Override
	public boolean isPunctuation() {
		return false;
	}

	@Override
	public int restrictedHashCode(int[] restriction) {
		// Default implementation will not restrict input
		return hashCode();
	}

	@Override
	public boolean equalsTolerance(Object o, double tolerance) {
		// Default with no restriction
		return equals(o);
	}

}
