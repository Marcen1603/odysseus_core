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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;

public final class DashboardPartDescriptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartDescriptor.class);

	private static final String DEFAULT_DESCRIPTION = "DashboardPart description for";
	
	private final Map<String, SettingDescriptor<?>> settingDescriptors;
	private final String name;
	private final String description;
	
	public DashboardPartDescriptor( String name, String description) {
		this(name, description, Lists.<SettingDescriptor<?>>newArrayList());
	}
	
	public DashboardPartDescriptor( String name, String description, List<SettingDescriptor<?>> settingDescriptors) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name of DashboardPartDescriptor is null or empty!");
		this.name = name;
		
		if( Strings.isNullOrEmpty(description)) {
			LOG.warn("Description for DashboardPartDescriptor {} is null or empty.", name);
			description = DEFAULT_DESCRIPTION + " " + name;
		}
		this.description = description;
		
		if( settingDescriptors == null ) {
			LOG.warn("List of setting descriptors for DashboardPartDescriptor {} is empty.", name);
			settingDescriptors = Lists.newArrayList();
		}
		
		this.settingDescriptors = createSettingsMap( settingDescriptors );
	}
	
	public ImmutableList<String> getSettingDescriptorNames() {
		return ImmutableList.copyOf(settingDescriptors.keySet());
	}
	
	public Optional<SettingDescriptor<?>> getSettingDescriptor( String descName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(descName), "Name of SettingDescriptor is null or empty!");
		return Optional.<SettingDescriptor<?>>fromNullable(settingDescriptors.get(descName));
	}
	
	public boolean hasSettingDescriptor( String descName ) {
		return settingDescriptors.containsKey(descName);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Configuration createDefaultConfiguration() {
		Map<String, Setting<?>> settings = Maps.newHashMap();
		for( String settingDescriptor : settingDescriptors.keySet()) {
			settings.put(settingDescriptor, settingDescriptors.get(settingDescriptor).createSetting());
		}
		return new Configuration(settings);
	}

	private static Map<String, SettingDescriptor<?>> createSettingsMap(List<SettingDescriptor<?>> settingDescriptors) {
		Map<String, SettingDescriptor<?>> map = Maps.newHashMap();
		for( SettingDescriptor<?> settingDescriptor : settingDescriptors ) {
			map.put(settingDescriptor.getName(), settingDescriptor);
		}
		return map;
	}
}
