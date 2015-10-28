package de.uniol.inf.is.odysseus.net.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfigurationKeys;

public class OdysseusNetConfigStartup implements IOdysseusNetStartup {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfigStartup.class);
	
	@Override
	public OdysseusNetStartupData start() {
		String nodeName = OdysseusNetConfiguration.get(OdysseusNetConfigurationKeys.DISCOVERER_NAME_CONFIG_KEY, "OdysseusNode");
		if( Strings.isNullOrEmpty(nodeName)) {
			LOG.error("Invalid node name. Using default");
			nodeName = "OdysseusNode";
		}
		
		OdysseusNetStartupData data = new OdysseusNetStartupData();
		data.setNodeName(nodeName);
		data.setNodeID(OdysseusNodeID.generateNew());
		
		return data;
	}

	@Override
	public void stop() {
		// do nothing
	}
}
