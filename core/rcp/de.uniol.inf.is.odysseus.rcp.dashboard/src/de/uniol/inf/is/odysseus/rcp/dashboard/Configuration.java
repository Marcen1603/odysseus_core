package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class Configuration {

	private final Map<String, Setting<?>> settings;
	
	public Configuration( Map<String, Setting<?>> settings) {
		this.settings = Preconditions.checkNotNull(settings, "Map of settings must not be null!");
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T> get( String settingName ) {
		Setting<?> setting = settings.get(settingName);
		if( setting == null ) {
			return Optional.<T>absent();
		} else {
			return (Optional<T>) Optional.of(setting.get());
		}
	}
}
