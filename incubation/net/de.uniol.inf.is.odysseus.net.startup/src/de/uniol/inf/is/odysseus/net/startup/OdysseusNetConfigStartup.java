package de.uniol.inf.is.odysseus.net.startup;

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

	private static final String NODE_DEFAULT_NAME = "OdysseusNode";
	private static final String NODE_NAME_CONFIG_KEY = "net.node.name";
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfigStartup.class);
	
	@Override
	public IOdysseusNode start() throws OdysseusNetException {
		String nodeName = OdysseusNetConfiguration.get(NODE_NAME_CONFIG_KEY, NODE_DEFAULT_NAME);
		if( Strings.isNullOrEmpty(nodeName)) {
			LOG.error("Invalid node name. Using default " + NODE_DEFAULT_NAME);
			nodeName = NODE_DEFAULT_NAME;
		}
		
		IOdysseusNode localNode = new OdysseusNode(OdysseusNodeID.generateNew(), nodeName);
		return localNode;
	}
//
//
//	private static int findFreePort() throws OdysseusNetException {
//		try {
//			ServerSocket s = new ServerSocket(0);
//			s.setReuseAddress(true);
//			int port = s.getLocalPort();
//			try {
//				s.close();
//			} catch( IOException ignore ) {
//			}
//			
//			return port;
//		} catch (IOException e) {
//			throw new OdysseusNetException("Could not find a free port", e);
//		}
//	}

	@Override
	public void stop() {
		// do nothing
	}
}
