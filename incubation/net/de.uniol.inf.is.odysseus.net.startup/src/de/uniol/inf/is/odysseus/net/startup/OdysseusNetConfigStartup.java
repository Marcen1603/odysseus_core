package de.uniol.inf.is.odysseus.net.startup;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;

public class OdysseusNetConfigStartup implements IOdysseusNetStartup {

	private static final String NODE_DEFAULT_NAME = "OdysseusNode";
	private static final String NODE_NAME_CONFIG_KEY = "net.node.name";
	private static final String NODE_COMMUNICATION_PORT_CONFIG_KEY = "net.node.port";
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfigStartup.class);
	
	@Override
	public OdysseusNetStartupData start() throws OdysseusNetException {
		String nodeName = OdysseusNetConfiguration.get(NODE_NAME_CONFIG_KEY, NODE_DEFAULT_NAME);
		if( Strings.isNullOrEmpty(nodeName)) {
			LOG.error("Invalid node name. Using default " + NODE_DEFAULT_NAME);
			nodeName = NODE_DEFAULT_NAME;
		}
		
		OdysseusNetStartupData data = new OdysseusNetStartupData();
		data.setNodeName(nodeName);
		data.setNodeID(OdysseusNodeID.generateNew());	
		data.setCommunicationPort(determineCommunicationPort());
		LOG.debug("Used port for communication is {}", data.getCommunicationPort());
		
		return data;
	}

	private static int determineCommunicationPort() throws OdysseusNetException {
		Optional<String> optPortStr = OdysseusNetConfiguration.get(NODE_COMMUNICATION_PORT_CONFIG_KEY);
		int port = -1;
		
		if( optPortStr.isPresent() ) {
			Optional<Integer> optPort = tryConvertToInt(optPortStr.get());
			if( optPort.isPresent() ) {
				port = optPort.get();
				if( port >= 1024 || port <= 65535 ) {
					LOG.info("Using specified port {}", port);
					return port;
				}
				
				LOG.error("Port number is out of valid range from 1024 to 65535: {}", port);
			} else {
				LOG.error("Could not convert to port number: {}", optPortStr.get());
			}
		} 
		
		LOG.info("Trying to use random free port for communication");
		return findFreePort();
	}

	private static Optional<Integer> tryConvertToInt(String text) {
		try {
			return Optional.of(Integer.parseInt(text));
		} catch( Throwable t ) {
			return Optional.absent();
		}
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
