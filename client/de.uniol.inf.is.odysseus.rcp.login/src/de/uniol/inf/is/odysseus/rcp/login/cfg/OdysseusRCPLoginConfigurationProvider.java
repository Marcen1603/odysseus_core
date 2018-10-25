package de.uniol.inf.is.odysseus.rcp.login.cfg;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguartionException;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;

public class OdysseusRCPLoginConfigurationProvider implements ILoginConfigurationProvider {

	private static final String LOGIN_SETTING_PREFIX = "__login__";

	@Override
	public Optional<String> get(String key) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Key of config setting must not be null or empty!");
		
		try {
			return Optional.of(OdysseusRCPConfiguration.get(LOGIN_SETTING_PREFIX + key));
		} catch (OdysseusRCPConfiguartionException ex) {
			return Optional.absent();
		}
	}

	@Override
	public void set(String key, String value) {
		OdysseusRCPConfiguration.set(LOGIN_SETTING_PREFIX + key, value);
		OdysseusRCPConfiguration.save();
	}

	@Override
	public Collection<String> getKeys() {
		Collection<String> allKeys = OdysseusRCPConfiguration.getKeys();
		Collection<String> relevantKeys = determineRelevantKeys(allKeys);
		
		return relevantKeys;
	}

	private static Collection<String> determineRelevantKeys(Collection<String> keys) {
		Collection<String> relevantKeys = Lists.newArrayList();
		for( String key : keys ) {
			if( key.startsWith(LOGIN_SETTING_PREFIX)) {
				relevantKeys.add(key.substring(LOGIN_SETTING_PREFIX.length()));
			}
		}
		return relevantKeys;
	}

}
