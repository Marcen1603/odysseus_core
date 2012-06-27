package de.uniol.inf.is.odysseus.rcp.dashboard.desc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class SettingDescriptor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SettingDescriptor.class);
	
	private static final String DEFAULT_DESCRIPTION = "Description of Setting";
	
	private final String name;
	private final String description;
	private final T defaultValue;
	private final boolean isOptional;
	private final boolean isEditable;
	
	public SettingDescriptor( String name, String description, T defaultValue, boolean optional, boolean editable ) {
		
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name of SettingDescriptor must not be null or empty!");
		this.name = name;
		
		if( Strings.isNullOrEmpty(description)) {
			LOG.warn("Description of Setting " + name + " is null or empty!");
			this.description = DEFAULT_DESCRIPTION + " " + name;
		} else {
			this.description = description;
		}
		
		this.defaultValue = defaultValue;
		isOptional = optional;
		isEditable = editable;
	}
	
	public String getName() {
		return name;
	}
	
	public T getDefaultValue() {
		return defaultValue;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isOptional() {
		return isOptional;
	}
	
	public boolean isEditable() {
		return isEditable;
	}
}
