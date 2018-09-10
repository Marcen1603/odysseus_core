package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptConfigSetting;

public final class OdysseusScriptConfigRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusScriptConfigRegistry.class);
	
	private static OdysseusScriptConfigRegistry instance;
	
	private final Map<String, IOdysseusScriptConfigSetting> registeredSettings = Maps.newHashMap();
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	boolean isActivated() {
		return instance != null;
	}
	
	static OdysseusScriptConfigRegistry getInstance() {
		return instance;
	}

	// called by OSGi-DS
	public void bindConfigSetting( IOdysseusScriptConfigSetting setting ) {
		if( !registeredSettings.containsKey(setting.getName())) {
			registeredSettings.put(setting.getName().toUpperCase(), setting);
		} else {
			LOG.warn("OdysseusScriptConfigSetting-name '{}' already registered", setting.getName());
		}
	}
	
	// called by OSGi-DS
	public void unbindConfigSetting( IOdysseusScriptConfigSetting setting ) {
		registeredSettings.remove(setting.getName());
	}
	
	public ImmutableList<String> getConfigSettingNames() {
		return ImmutableList.copyOf(registeredSettings.keySet());
	}
	
	public Optional<IOdysseusScriptConfigSetting> getConfigSetting( String name ) {
		return Optional.fromNullable(registeredSettings.get(name.toUpperCase()));
	}
}
