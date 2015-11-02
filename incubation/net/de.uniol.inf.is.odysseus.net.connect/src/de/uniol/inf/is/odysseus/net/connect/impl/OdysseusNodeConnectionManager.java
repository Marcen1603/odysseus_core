package de.uniol.inf.is.odysseus.net.connect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelector;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelectorManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnector;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeConnectionManager implements IOdysseusNodeManagerListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeConnectionManager.class);
	private static final String SELECTOR_CONFIG_KEY = "net.connect.selector.name";
	private static final String DEFAULT_SELECTOR_NAME = "ConnectAllSelector";

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNodeConnectionSelectorManager connectionSelectorManager;
	private static IOdysseusNodeConnector nodeConnector;

	private IOdysseusNodeConnectionSelector connectionSelector;

	// called by OSGi-DS
	public void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;

		LOG.debug("Bound odysseus node manager {}", serv);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;

			LOG.debug("Unbound odysseus node manager {}", serv);
		}
	}

	// called by OSGi-DS
	public void bindOdysseusNodeConnectionSelectorManager(IOdysseusNodeConnectionSelectorManager serv) {
		connectionSelectorManager = serv;

		LOG.debug("Bound odysseus node connection selector manager {}", serv);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeConnectionSelectorManager(IOdysseusNodeConnectionSelectorManager serv) {
		if (connectionSelectorManager == serv) {
			connectionSelectorManager = null;

			LOG.debug("Unbound odysseus node connection selector manager {}", serv);
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeConnector(IOdysseusNodeConnector serv) {
		nodeConnector = serv;
		
		LOG.debug("Bound odysseus node connector {}", serv);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnector(IOdysseusNodeConnector serv) {
		if (nodeConnector == serv) {
			nodeConnector = null;
			
			LOG.debug("Unbound odysseus node connector {}", serv);
		}
	}

	// called by OSGi-DS
	public void activate() {
		nodeManager.addListener(this);

		String selectorName = OdysseusNetConfiguration.get(SELECTOR_CONFIG_KEY, DEFAULT_SELECTOR_NAME);
		Optional<IOdysseusNodeConnectionSelector> optSelector = connectionSelectorManager.getSelector(selectorName);
		if (optSelector.isPresent()) {
			connectionSelector = optSelector.get();
			LOG.info("Selected node connection selector {}", selectorName);
		} else {
			LOG.error("Could not find connection selector called '{}'", selectorName);
		}
		
		LOG.debug("OdysseusNode connection manager activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		nodeManager.removeListener(this);
		
		LOG.debug("OdysseusNode connection manager deactivated");
	}

	@Override
	public void nodeAdded(IOdysseusNodeManager sender, IOdysseusNode node) {
		LOG.debug("OdysseusNode added: {}", node);
		
		if (connectionSelector != null && connectionSelector.doConnect(node)) {
			try {
				LOG.info("Trying to connect to node {}", node);
				nodeConnector.connect(node);
				
			} catch (OdysseusNetConnectionException e) {
				LOG.error("Could not connect to node {}", node, e);
			}
		} else {
			LOG.info("Did not connect to node {}", node);
		}
	}

	@Override
	public void nodeRemoved(IOdysseusNodeManager sender, IOdysseusNode node) {
		LOG.debug("OdysseusNode removed: {}", node);
		if( nodeConnector.isConnected(node)) {
			
			LOG.info("Trying to disconnect from node {}", node);
			nodeConnector.disconnect(node);
		}
	}
}
