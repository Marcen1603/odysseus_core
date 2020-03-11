package de.uniol.inf.is.odysseus.script.parser.replacementprovider;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.script.parser.IReplacementProvider;

public class SystemPropertyReplacementProvider implements IReplacementProvider {

	private static final String KEY_PREFIX = "_";
	
	private final Collection<String> keys; 
	
	public SystemPropertyReplacementProvider() {
		keys = determineSystemPropertyKeys();
	}
	
	private static Collection<String> determineSystemPropertyKeys() {
		Collection<String> keys = Lists.newArrayList();
		addSystemProperties(keys);
		addSystemEnvironment(keys);
		return keys;
	}

	private static void addSystemEnvironment(Collection<String> keys) {
		for (String environmentKey : System.getenv().keySet()) {
			keys.add(KEY_PREFIX + environmentKey);
		}
	}

	private static void addSystemProperties(Collection<String> keys) {
		for( Object key : System.getProperties().keySet()) {
			String keyStr = key.toString();
			keys.add(KEY_PREFIX + keyStr);
		}
	}

	@Override
	public Collection<String> getReplacementKeys() {
		return keys;
	}

	@Override
	public String getReplacementValue(String key ) {
		if( !key.startsWith(KEY_PREFIX)) {
			return "";
		}
		
		String realKey = key.substring(1);
		
		if ( System.getProperties().keySet().contains(realKey)) {
			return System.getProperty(realKey.toLowerCase());			
		} else if (System.getenv().containsKey(realKey)) {
			return System.getenv(realKey);
		}
		return "";
	}

}
