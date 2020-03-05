package de.uniol.inf.is.odysseus.script.parser.replacementprovider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.script.parser.IReplacementProvider;
import de.uniol.inf.is.odysseus.server.autostart.Autostart;

public class OdysseusHomeReplacementProvider implements IReplacementProvider {

	private Map<String, String> replacements;
	private static final String ODYSSEUS_HOME = "odysseusHome";
	private static final String ODYSSEUS_AUTOSTART_DIRECTORY = "odysseusAutostartDirectory";
	private static final String ODYSSEUS_AUTOSTART_FILE = "odysseusAutostartFile";

	public OdysseusHomeReplacementProvider() {
		replacements = new HashMap<String, String>(2);
		replacements.put(ODYSSEUS_HOME.toUpperCase(), OdysseusConfiguration.instance.getHomeDir());
		replacements.put(ODYSSEUS_AUTOSTART_DIRECTORY.toUpperCase(), Autostart.getAutostartDirectoryPath());
		replacements.put(ODYSSEUS_AUTOSTART_FILE.toUpperCase(), Autostart.getAutostartQueryFilePath());
	}

	@Override
	public Collection<String> getReplacementKeys() {
		return replacements.keySet();
	}

	@Override
	public String getReplacementValue(String replacementKey) {
		return replacements.get(replacementKey.toUpperCase());
	}

}
