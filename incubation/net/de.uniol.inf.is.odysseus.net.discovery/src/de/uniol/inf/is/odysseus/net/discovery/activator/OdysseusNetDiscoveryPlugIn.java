package de.uniol.inf.is.odysseus.net.discovery.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfigurationKeys;
import de.uniol.inf.is.odysseus.net.discovery.IOdysseusNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.IOdysseusNodeDiscovererManager;

public class OdysseusNetDiscoveryPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetDiscoveryPlugIn.class);
	private static final long WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS = 1000;
	private static final long MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS = 30 * 1000;

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNodeDiscovererManager discovererManager;

	private static IOdysseusNodeDiscoverer nodeDiscoverer;

	private static Object syncObject = new Object();

	// called by OSGi-DS
	public static void bindNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
		LOG.info("Node manager bound");

		startNodeDiscovery();
	}

	// called by OSGi-DS
	public static void unbindNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
			LOG.info("Node manager unbound");

			stopNodeDiscovery();
		}
	}

	// called by OSGi-DS
	public static void bindNodeDiscovererManager(IOdysseusNodeDiscovererManager serv) {
		discovererManager = serv;
		LOG.info("Node discoverer manager bound");

		startNodeDiscovery();
	}

	// called by OSGi-DS
	public static void unbindNodeDiscovererManager(IOdysseusNodeDiscovererManager serv) {
		if (discovererManager == serv) {
			discovererManager = null;
			LOG.info("Node discoverer manager unbound");

			stopNodeDiscovery();
		}
	}

	private static void startNodeDiscovery() {
		synchronized (syncObject) {
			if (discovererManager != null && nodeManager != null) {
				
				Thread nodeDiscovererThread = new Thread( new Runnable() {
					@Override
					public void run() {
						LOG.info("Starting node discovery");
						
						long currentWaitingTime = 0;
						while( nodeDiscoverer == null ) {
							Optional<IOdysseusNodeDiscoverer> optNodeDiscovererToUse = determineNodeDiscoverer();
							if (optNodeDiscovererToUse.isPresent()) {
								nodeDiscoverer = optNodeDiscovererToUse.get();
								LOG.info("Using node discoverer {}", nodeDiscoverer);
								
								try {
									nodeDiscoverer.start(nodeManager);
								} catch( Throwable t ) {
									LOG.error("Could not start node discoverer", t);
									return;
								}
								
							} else {
								if( currentWaitingTime >= MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS ) {
									LOG.error("Could not get a node discoverer after {} ms. Aborting.", MAX_WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS);
									return;
								}
								
								LOG.debug("No node discoverer available at the moment. Waiting {} ms. Total: {} ms", WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS, currentWaitingTime);
								waitSomeTime();
								currentWaitingTime += WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS;
							}
						}
					}
				});
				
				nodeDiscovererThread.setName("Node discoverer");
				nodeDiscovererThread.setDaemon(true);
				nodeDiscovererThread.start();
			}
		}
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(WAIT_TIME_FOR_NODE_DISCOVERER_MILLIS);
		} catch (InterruptedException e) {
		}
	}

	private static Optional<IOdysseusNodeDiscoverer> determineNodeDiscoverer() {
		Optional<String> optDiscovererName = OdysseusNetConfiguration.get(OdysseusNetConfigurationKeys.DISCOVERER_NAME_CONFIG_KEY);
		if( optDiscovererName.isPresent() ) {
			LOG.info("Selected node discoverer from config: {}", optDiscovererName.get());
			Optional<IOdysseusNodeDiscoverer> optDiscoverer = discovererManager.get(optDiscovererName.get());
			if( optDiscoverer.isPresent() ) {
				return optDiscoverer;
			}
			
			LOG.error("Selected node discoverer name '{}' not found", optDiscoverer.get());
		}
		
		return Optional.absent();
	}

	private static void stopNodeDiscovery() {
		if (nodeDiscoverer != null && nodeDiscoverer.isStarted()) {
			nodeDiscoverer.stop();
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		// do nothing
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// do nothing
	}

}
