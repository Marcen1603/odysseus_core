package de.uniol.inf.is.odysseus.net.startup;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;

public class OdysseusNetConfigStartup implements IOdysseusNetStartup {

	private static final String NODE_NAME_CONFIG_KEY = "net.node.name";
	private static final String NODE_DEFAULT_NAME = "OdysseusNode";
	
	private static final String PRESERVE_ID_CONFIG_KEY = "net.node.preserveid";
	private static final String PRESERVE_ID_DEFAULT_VALUE = "false";
	
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfigStartup.class);
	
	private OdysseusNodeID generatedID = null;
	
	@Override
	public IOdysseusNode start() throws OdysseusNetException {
		String randNumStr = generateRandomNumber();
		String nodeName = OdysseusNetConfiguration.get(NODE_NAME_CONFIG_KEY, NODE_DEFAULT_NAME);
		if( Strings.isNullOrEmpty(nodeName)) {
			LOG.error("Invalid node name. Using default " + NODE_DEFAULT_NAME + "_" + randNumStr);
			nodeName = NODE_DEFAULT_NAME + "_" + randNumStr;
		}
		
		if( NODE_DEFAULT_NAME.equals(nodeName)) {
			nodeName = nodeName + "_" + randNumStr;
		}
	
		String preserveID = OdysseusNetConfiguration.get(PRESERVE_ID_CONFIG_KEY, PRESERVE_ID_DEFAULT_VALUE);
		if( Strings.isNullOrEmpty(preserveID)) {
			LOG.error("Invalid value '{}' for " + PRESERVE_ID_CONFIG_KEY + ". Must be 'true' or 'false'. Using default " + PRESERVE_ID_DEFAULT_VALUE);
			preserveID = PRESERVE_ID_DEFAULT_VALUE;
		}
		if( generatedID == null || !Boolean.getBoolean(preserveID)) {
			generatedID = OdysseusNodeID.generateNew();
		}
		
		IOdysseusNode localNode = new OdysseusNode(generatedID, nodeName, true);
		return localNode;
	}

	// "1000" to "9999"
	private static String generateRandomNumber() {
		return String.valueOf(new Random().nextInt(8999) + 1000); 
	}

	@Override
	public void stop() {
		// do nothing
	}
}
