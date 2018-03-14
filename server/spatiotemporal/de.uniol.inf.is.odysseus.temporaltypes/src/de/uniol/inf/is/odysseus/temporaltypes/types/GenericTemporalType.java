package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class GenericTemporalType<T> implements IClone, Cloneable, Serializable, TemporalType<T> {

	private static final long serialVersionUID = -903000410292576845L;
	private Map<PointInTime, T> values;

	public GenericTemporalType() {
		this.values = new HashMap<>();
	}
	
	public GenericTemporalType(GenericTemporalType<T> other) {
		this.values = other.getValues();
	}
	
	public void setValue(PointInTime time, T value) {
		this.values.put(time, value);
	}

	@Override
	public T getValue(PointInTime time) {
		return this.values.get(time);
	}

	@Override
	public T[] getValues(TimeInterval interval) {
		List<T> intervalValues = new ArrayList<>();

		for (PointInTime i = interval.getStart(); i.before(interval.getEnd()); i = i.plus(1)) {
			intervalValues.add(this.getValue(i));
		}

		Object[] returnObject = new Object[intervalValues.size()];
		for (int i = 0; i < intervalValues.size(); i++) {
			returnObject[i] = intervalValues.get(i);
		}
		return (T[]) returnObject;
	}
	
	public Map<PointInTime, T> getValues() {
		return this.values;
	}

	@Override
	public IClone clone() {
		return new GenericTemporalType<>(this);
	}
	
	@Override
	public String toString() {
		return "GenericTemporalType: " + this.values;
	}

}
