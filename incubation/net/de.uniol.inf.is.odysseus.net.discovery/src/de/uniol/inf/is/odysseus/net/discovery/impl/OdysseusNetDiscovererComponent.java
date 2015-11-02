package de.uniol.inf.is.odysseus.net.discovery.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.discovery.IOdysseusNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.IOdysseusNodeDiscovererManager;
import de.uniol.inf.is.odysseus.net.discovery.activator.OdysseusNetDiscoveryPlugIn;

public class OdysseusNetDiscovererComponent implements IOdysseusNetComponent {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetDiscovererComponent.class);

	private static final long WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS = 1000;
	private static final long MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS = 30 * 1000;

	private IOdysseusNodeManager nodeManager;
	private IOdysseusNodeDiscovererManager discovererManager;
	private IOdysseusNodeDiscoverer nodeDiscoverer;
	private IOdysseusNode localNode;

	// called by OSGi-DS
	public void bindNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
		LOG.info("Node manager bound");
	}

	// called by OSGi-DS
	public void unbindNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
			LOG.info("Node manager unbound");
		}
	}

	// called by OSGi-DS
	public void bindNodeDiscovererManager(IOdysseusNodeDiscovererManager serv) {
		discovererManager = serv;
		LOG.info("Node discoverer manager bound");
	}

	// called by OSGi-DS
	public void unbindNodeDiscovererManager(IOdysseusNodeDiscovererManager serv) {
		if (discovererManager == serv) {
			discovererManager = null;
			LOG.info("Node discoverer manager unbound");
		}
	}

	@Override
	public void init(IOdysseusNode localNode) {
		this.localNode = localNode;
	}

	@Override
	public void start() {
		LOG.info("Starting node discovery");

		waitForServices();

		long currentWaitingTime = 0;
		while (nodeDiscoverer == null) {
			Optional<IOdysseusNodeDiscoverer> optNodeDiscovererToUse = determineNodeDiscoverer();
			if (optNodeDiscovererToUse.isPresent()) {
				nodeDiscoverer = optNodeDiscovererToUse.get();
				LOG.info("Using node discoverer {}", nodeDiscoverer);

				try {
					nodeDiscoverer.start(nodeManager, localNode);
				} catch (Throwable t) {
					LOG.error("Could not start node discoverer", t);
					return;
				}

			} else {
				if (currentWaitingTime >= MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS) {
					LOG.error("Could not get a node discoverer after {} ms. Aborting.", MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS);
					return;
				}

				LOG.debug("No node discoverer available at the moment. Waiting {} ms. Total: {} ms", WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS, currentWaitingTime);
				waitSomeTime();
				currentWaitingTime += WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS;
			}
		}
	}

	@Override
	public void stop() {
		if (nodeDiscoverer != null) {
			nodeDiscoverer.stop();

			nodeDiscoverer = null;
		}

		if (nodeManager != null) {
			ImmutableCollection<IOdysseusNode> nodes = nodeManager.getNodes();
			for (IOdysseusNode node : nodes) {
				nodeManager.removeNode(node);
			}
		}
	}
	
	@Override
	public void terminate(IOdysseusNode localNode) {
		// do nothing
	}

	private void waitForServices() {
		while (nodeManager == null) {
			waitSomeTime();
		}

		while (discovererManager == null) {
			waitSomeTime();
		}
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS);
		} catch (InterruptedException e) {
		}
	}

	private Optional<IOdysseusNodeDiscoverer> determineNodeDiscoverer() {
		String discovererName = OdysseusNetConfiguration.get(OdysseusNetDiscoveryPlugIn.DISCOVERER_NAME_CONFIG_KEY, OdysseusNetDiscoveryPlugIn.DEFAULT_DISCOVERER_NAME);

		LOG.info("Selected node discoverer from config: {}", discovererName);
		Optional<IOdysseusNodeDiscoverer> optDiscoverer = discovererManager.get(discovererName);
		if (optDiscoverer.isPresent()) {
			return optDiscoverer;
		}

		LOG.error("Selected node discoverer name '{}' not found", optDiscoverer.get());

		return Optional.absent();
	}

}
