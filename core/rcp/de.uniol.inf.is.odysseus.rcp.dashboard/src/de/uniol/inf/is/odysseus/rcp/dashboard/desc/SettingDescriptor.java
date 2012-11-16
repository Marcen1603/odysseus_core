/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.desc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;

public final class SettingDescriptor<T> {

	// lower case!
	public static final ImmutableList<String> SUPPORTED_TYPES = ImmutableList.<String>builder()
			.add("string") 
			.add("integer")
			.add("long")
			.add("double")
			.add("float")
			.add("boolean")
			.build();
	
	private static final Logger LOG = LoggerFactory.getLogger(SettingDescriptor.class);
	private static final String DEFAULT_DESCRIPTION = "Description of Setting";
	
	private final String name;
	private final String description;
	private final String type;
	
	private final T defaultValue;
	private final boolean isOptional;
	private final boolean isEditable;
	
	public SettingDescriptor( String name, String description, String type, T defaultValue, boolean optional, boolean editable ) {
		
		Preconditions.checkNotNull(defaultValue, "DefaultValue must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name of SettingDescriptor must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "Type of SettingDescriptor must not be null or empty!");
		Preconditions.checkArgument(SUPPORTED_TYPES.contains(type.toLowerCase()), "Type %s not supported!", type);
		Preconditions.checkArgument(checkDefaultValueWithType(defaultValue, type), "Default value %s is not from Type %s.", defaultValue, type);
		
		this.name = name;
		this.type = type;
		
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
	
	public Setting<T> createSetting() {
		return new Setting<T>(this);
	}
	
	public String getType() {
		return type;
	}
	
	private static boolean checkDefaultValueWithType(Object value, String type) {
		return value.getClass().getSimpleName().equalsIgnoreCase(type);		
	}
}
