package de.uniol.inf.is.odysseus.core.metadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.Order;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	private Map<String, Object> keyValueMap;
	private T metadata = null;
	private boolean timeOrderMarker = true;

	public AbstractStreamObject() {
	}

	@SuppressWarnings("unchecked")
	protected AbstractStreamObject(AbstractStreamObject<T> other) {
		if (other.keyValueMap != null) {
			this.keyValueMap = new HashMap<>(other.keyValueMap);
		}
		if (other.metadata != null) {
			this.metadata = (T) other.metadata.clone();
		}
		this.timeOrderMarker = other.timeOrderMarker;
	}

	@Override
	public boolean isSchemaLess() {
		return true;
	}

	@Override
	final public Object getKeyValue(String name) {
		return keyValueMap.get(name);
	}

	@Override
	final public boolean hasKeyValue(String name) {
		return this.keyValueMap.containsKey(name);
	}

	@Override
	final public void setKeyValue(String name, Object content) {
		if (keyValueMap == null) {
			keyValueMap = new HashMap<>();
		}
		keyValueMap.put(name, content);
	}

	@Override
	final public void setKeyValueMap(Map<String, Object> metaMap) {
		keyValueMap = new HashMap<>();
		if (metaMap != null) {
			this.keyValueMap.putAll(metaMap);
		}
	}

	@Override
	final public Map<String, Object> getGetValueMap() {
		if (keyValueMap != null){
			return Collections.unmodifiableMap(keyValueMap);
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

		this.timeOrderMarker = left.isTimeProgressMarker() && right.isTimeProgressMarker();

		return ret;
	}

	private void mergeInternal(IStreamObject<T> left, IStreamObject<T> right, IStreamObject<T> ret) {
		// TODO: Merge function in cases where key is same!!

		if (right.getGetValueMap() != null) {
			ret.setKeyValueMap(right.getGetValueMap());
		}
		if (left.getGetValueMap() != null) {
			if (right.getGetValueMap() == null) {
				ret.setKeyValueMap(left.getGetValueMap());
			} else {
				for (Entry<String, Object> a : left.getGetValueMap().entrySet()) {
					ret.setKeyValue(a.getKey(), a.getValue());
				}
			}
		}
	}

	@Override
	public boolean isTimeProgressMarker() {
		return timeOrderMarker;
	}

	@Override
	public void setTimeProgressMarker(boolean timeOrderMarker) {
		this.timeOrderMarker = timeOrderMarker;
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

	@Override
	public boolean equals(IStreamObject<IMetaAttribute> o, boolean compareMeta) {
		boolean ret = equals(o);
		if (compareMeta){
			ret = ret & o.getMetadata().equals(this.getMetadata());
		}
		return ret;
	}

}
