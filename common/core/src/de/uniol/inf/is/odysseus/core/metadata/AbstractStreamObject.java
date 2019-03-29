package de.uniol.inf.is.odysseus.core.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.Order;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	private T metadata = null;
	private boolean timeOrderMarker = true;
	// will only be initialized when needed
	transient private Map<String, Object> transientMarker;

	public AbstractStreamObject() {
	}

	@SuppressWarnings("unchecked")
	protected AbstractStreamObject(AbstractStreamObject<T> other) {
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
	final public IStreamObject<T> merge(IStreamObject<T> left, IStreamObject<T> right,
			IMetadataMergeFunction<T> metamerge, Order order) {
		// Preserve meta data
		T metadateleft = left.getMetadata();
		T metadateright = right != null ? right.getMetadata() : null;

		IStreamObject<T> ret = process_merge(left, right, order);
		ret.setMetadata(metamerge.mergeMetadata(metadateleft, metadateright));
		if (right != null) {
			this.timeOrderMarker = left.isTimeProgressMarker() && right.isTimeProgressMarker();
		} else {
			this.timeOrderMarker = left.isTimeProgressMarker();
		}

		return ret;
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
	public AbstractStreamObject<T> copyAndReturnEmptyInstance() {
		if (isSchemaLess()) {
			return newInstance();
		}else {
			throw new UnsupportedOperationException("This stream object does not support this copy method");
		}
	}

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
	public int hashCode(boolean calcWithMeta) {
		if (!calcWithMeta) {
			return this.hashCode();
		}
		return Objects.hash(this.getMetadata(), this);
	}

	@Override
	public boolean equalsTolerance(Object o, double tolerance) {
		// Default with no restriction
		return equals(o);
	}

	@Override
	public boolean equals(IStreamObject<IMetaAttribute> o, boolean compareMeta) {
		boolean ret = equals(o);
		if (compareMeta) {
			ret = ret & o.getMetadata().equals(this.getMetadata());
		}
		return ret;
	}

	// --------------------------------------------------------------------------------------------
	// Methods for Marker
	// ---------------------------------------------------------------------------------------------
	public boolean hasTransientMarker(String key) {
		return transientMarker != null && transientMarker.containsKey(key);
	};

	@Override
	public void setTransientMarker(String key, Object value) {
		if (transientMarker == null) {
			transientMarker = new HashMap<>();
		}
		this.transientMarker.put(key, value);
	}

	@Override
	public Object getTransientMarker(String key) {
		return transientMarker != null ? transientMarker.get(key) : null;
	}
}
