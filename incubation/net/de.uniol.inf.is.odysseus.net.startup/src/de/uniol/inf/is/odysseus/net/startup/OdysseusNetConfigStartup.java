package de.uniol.inf.is.odysseus.net.startup;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;

public class OdysseusNetConfigStartup implements IOdysseusNetStartup {

	private static final String NODE_NAME_CONFIG_KEY = "net.node.name";
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfigStartup.class);
	
	@Override
	public OdysseusNetStartupData start() throws OdysseusNetException {
		String nodeName = OdysseusNetConfiguration.get(NODE_NAME_CONFIG_KEY, "OdysseusNode");
		if( Strings.isNullOrEmpty(nodeName)) {
			LOG.error("Invalid node name. Using default");
			nodeName = "OdysseusNode";
		}
		
		OdysseusNetStartupData data = new OdysseusNetStartupData();
		data.setNodeName(nodeName);
		data.setNodeID(OdysseusNodeID.generateNew());
		data.setCommunicationPort(findFreePort());
		LOG.debug("Used port for communication is {}", data.getCommunicationPort());
		
		return data;
	}

	private static int findFreePort() throws OdysseusNetException {
		try {
			ServerSocket s = new ServerSocket(0);
			s.setReuseAddress(true);
			int port = s.getLocalPort();
			try {
				s.close();
			} catch( IOException ignore ) {
			}
			
			return port;
		} catch (IOException e) {
			throw new OdysseusNetException("Could not find a free port", e);
		}
	}

	@Override
	public void stop() {
		// do nothing
	}
}
