package de.uniol.inf.is.odysseus.rcp.dashboard;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public final class Setting<T> {

	private final SettingDescriptor<T> descriptor;
	private T value;
	
	public Setting( SettingDescriptor<T> descriptor ) {
		this.descriptor = Preconditions.checkNotNull(descriptor, "SettingDescriptor for setting must not be null!");
		this.value = descriptor.getDefaultValue();
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
	
	public void reset() {
		value = descriptor.getDefaultValue();
	}

}
