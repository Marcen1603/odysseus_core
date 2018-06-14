package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.discovery.AbstractOdysseusNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.request.BroadcastRequestSender;

public class BroadcastOdysseusNodeDiscoverer extends AbstractOdysseusNodeDiscoverer {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastOdysseusNodeDiscoverer.class);
	
	private BroadcastRequestSender requestSender;
	
	@Override
	protected void startImpl(IOdysseusNodeManager manager, IOdysseusNode localNode) throws OdysseusNetDiscoveryException {
		requestSender = new BroadcastRequestSender(localNode, manager);		
		requestSender.start();
		
		LOG.info("Started broadcast node discoverer");
	}

	@Override
	protected void stopImpl() {
		requestSender.stop();
		
		LOG.info("Stopped broadcast node discoverer");
	}

}
