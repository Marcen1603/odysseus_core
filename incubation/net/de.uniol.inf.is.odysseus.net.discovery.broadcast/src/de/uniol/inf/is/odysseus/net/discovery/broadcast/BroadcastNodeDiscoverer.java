package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.INodeManager;
import de.uniol.inf.is.odysseus.net.discovery.AbstractNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.request.BroadcastRequestSender;

public class BroadcastNodeDiscoverer extends AbstractNodeDiscoverer {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastNodeDiscoverer.class);
	
	private BroadcastRequestSender requestSender;
	
	@Override
	protected void startImpl(INodeManager manager) throws OdysseusNetDiscoveryException {
		requestSender = new BroadcastRequestSender();		
		requestSender.start();
		
		LOG.info("Started broadcast node discoverer");
	}

	@Override
	protected void stopImpl() {
		requestSender.stop();
		
		LOG.info("Stopped broadcast node discoverer");
	}

}
