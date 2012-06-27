package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class Configuration {

	private final Map<String, Setting<?>> settings;
	
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
		setting.set(value);
	}
	
	public boolean exists( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		return settings.containsKey(settingName);
	}
	
	public ImmutableList<String> getNames() {
		return ImmutableList.copyOf(settings.keySet());
	}
	
	public void reset( String settingName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(settingName), "Name of setting must not be null or empty!");
		Preconditions.checkArgument(exists(settingName), "Setting with name {} does not exist!", settingName);
		
		Setting<?> setting = getSettingImpl(settingName);
		setting.reset();
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
	
	@SuppressWarnings("unchecked")
	private <T> Setting<T> getSettingImpl( String settingName ) {
		return (Setting<T>) settings.get(settingName);
	}
}
