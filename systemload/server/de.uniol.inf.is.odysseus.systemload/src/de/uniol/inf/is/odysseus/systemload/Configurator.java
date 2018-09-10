package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class Configurator {

	public void setConfig(OdysseusConfiguration config) {
		SystemLoadPlugIn.SIGAR_WRAPPER.setConfig(config);
	}

}
