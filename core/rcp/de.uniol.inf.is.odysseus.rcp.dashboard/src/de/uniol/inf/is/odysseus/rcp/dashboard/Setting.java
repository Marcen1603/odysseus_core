package de.uniol.inf.is.odysseus.rcp.dashboard;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public final class Setting<T> {

	private final SettingDescriptor<T> descriptor;
	private final String type;
	private T value;
	
	public Setting( SettingDescriptor<T> descriptor ) {
		this.descriptor = Preconditions.checkNotNull(descriptor, "SettingDescriptor for setting must not be null!");
		this.value = descriptor.getDefaultValue();
		this.type = descriptor.getType();
	}
	
	public SettingDescriptor<T> getSettingDescriptor() {
		return descriptor;
	}
	
	public T get() {
		return value;
	}
	
	public void set( T newValue ) {
		value = newValue;
	}
	
	@SuppressWarnings("unchecked")
	public void setAsString( String stringValue ) {
		value = (T) convertValue( stringValue, type );
	}
	
	public void reset() {
		value = descriptor.getDefaultValue();
	}
	
	private static Object convertValue(String value, String type) {
		if (value == null) {
			return null;
		}

		if ("Integer".equalsIgnoreCase(type)) {
			return Integer.valueOf(value);
		}

		if ("Long".equalsIgnoreCase(type)) {
			return Long.valueOf(value);
		}

		if ("Double".equalsIgnoreCase(type)) {
			return Double.valueOf(value);
		}

		if ("Boolean".equalsIgnoreCase(type)) {
			return Boolean.valueOf(value);
		}

		if ("Float".equalsIgnoreCase(type)) {
			return Float.valueOf(value);
		}

		return value;
	}
}
