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
package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Configuration {

	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
	
	private final Map<String, Setting<?>> settings;
	private final List<IConfigurationListener> listeners = Lists.newArrayList();
	
	public Configuration( Map<String, Setting<?>> settings) {
		this.settings = Preconditions.checkNotNull(settings, "Map of settings must not be null!");
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		return (T) settings.get(settingName).get();
	}
	
	public <T> void set( String settingName, T value ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		Setting<T> setting = getSettingImpl(settingName);
		T oldValue = setting.get();
		if( !Objects.equal(oldValue, value)) {
			setting.set(value);
			fireSettingChangeEvent(setting, oldValue);
		}
	}
	
	public void setAsString( String settingName, String value ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		Setting<?> setting = getSettingImpl(settingName);
		Object oldValue = setting.get();
		setting.setAsString(value);
		Object newValue = setting.get();
		if( !Objects.equal(oldValue, newValue)) {
			fireSettingChangeEvent(setting, oldValue);
		}
	}
	
	public boolean exists( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		return settings.containsKey(settingName);
	}
	
	public ImmutableList<String> getNames() {
		return ImmutableList.copyOf(settings.keySet());
	}
	
	public ImmutableList<Setting<?>> getSettings() {
		return ImmutableList.copyOf(settings.values());
	}
	
	public void reset( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		Setting<?> setting = getSettingImpl(settingName);
		Object oldValue = setting.get();
		setting.reset();
		Object newValue = setting.get();
		if( !Objects.equal(oldValue, newValue)) {
			fireSettingChangeEvent(setting, oldValue);
		}
	}
	
	public void resetAll() {
		for( String settingName : getNames() ) {
			reset(settingName);
		}
	}
	
	public Setting<?> getSetting( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		return getSettingImpl(settingName);
	}
	
	public void addListener( IConfigurationListener listener ) {
		Preconditions.checkNotNull(listener, "Listener must not be null!");
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IConfigurationListener listener ) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	
	private void fireSettingChangeEvent(Setting<?> setting, Object oldValue) {
		synchronized( listeners ) {
			for( IConfigurationListener listener : listeners ) {
				try {
					listener.settingChanged(setting.getSettingDescriptor().getName(), oldValue, setting.get());
				} catch( Throwable t ) {
					LOG.error("Exception during executing listener for configuration", t);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> Setting<T> getSettingImpl( String settingName ) {
		return (Setting<T>) settings.get(settingName);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Configuration{");
		for( String settingName : settings.keySet()) {
			sb.append(settingName).append("=").append(settings.get(settingName)).append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}
