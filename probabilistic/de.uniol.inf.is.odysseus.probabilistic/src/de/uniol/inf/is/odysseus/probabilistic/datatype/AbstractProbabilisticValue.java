package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <V>
 */
public class AbstractProbabilisticValue<V> implements Serializable, IClone {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8024287769210294227L;
	Map<V, Double> values = new HashMap<V, Double>();

	public AbstractProbabilisticValue(final V value, Double probability) {
		this.values.put(value, probability);
	}

	public AbstractProbabilisticValue(final Map<V, Double> values) {
		this.values.putAll(values);
	}

	public AbstractProbabilisticValue(AbstractProbabilisticValue<V> other) {
		for (final Entry<V, Double> value : other.values.entrySet()) {
			this.values.put(value.getKey(), value.getValue());
		}
	}

	public AbstractProbabilisticValue(final V[] values,
			final Double[] probabilities) {
		final int length = Math.min(values.length, probabilities.length);
		for (int i = 0; i < length; i++) {
			this.values.put(values[i], probabilities[i]);
		}
	}

	public Map<V, Double> getValues() {
		return this.values;
	}

	@Override
	public AbstractProbabilisticValue<V> clone() {
		return new AbstractProbabilisticValue<V>(this);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (final Entry<V, Double> value : this.values.entrySet()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			sb.append(value.getKey()).append(":").append(value.getValue());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() == obj.getClass()) {
			AbstractProbabilisticValue<?> other = (AbstractProbabilisticValue<?>) obj;
			for (Entry<?, Double> entry : other.values.entrySet()) {
				if ((!values.containsKey(entry.getKey()))
						|| (values.get(entry.getKey()) != entry.getValue())) {
					return false;
				}
			}
		} else {
			if (values.containsKey(obj)) {
				return true;
			}
		}
		return false;
	}

}
