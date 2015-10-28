package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.discovery.AbstractOdysseusNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.request.BroadcastRequestReceiver;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.request.BroadcastRequestSender;

public class BroadcastOdysseusNodeDiscoverer extends AbstractOdysseusNodeDiscoverer {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastOdysseusNodeDiscoverer.class);
	
	private BroadcastRequestSender requestSender;
	private BroadcastRequestReceiver requestReceiver;
	
	@Override
	protected void startImpl(IOdysseusNodeManager manager) throws OdysseusNetDiscoveryException {
		requestSender = new BroadcastRequestSender(manager);		
		requestSender.start();
		
		requestReceiver = new BroadcastRequestReceiver();
		requestReceiver.start();
		
		LOG.info("Started broadcast node discoverer");
	}

	@Override
	protected void stopImpl() {
		requestReceiver.stop();
		requestSender.stop();
		
		LOG.info("Stopped broadcast node discoverer");
	}

}
