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

public class OdysseusNodeConnector implements IOdysseusNodeManagerListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeConnector.class);
	private static final String SELECTOR_CONFIG_KEY = "net.connect.selector.name";
	private static final String DEFAULT_SELECTOR_NAME = "ConnectAllSelector";
	
	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNodeConnectionSelectorManager connectionSelectorManager;
	
	private IOdysseusNodeConnectionSelector connectionSelector;

	// called by OSGi-DS
	public void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
		
		LOG.info("Bound odysseus node manager {}", serv);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
			
			LOG.info("Unbound odysseus node manager {}", serv);
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
			
			LOG.info("Unbound odysseus node connection selector manager {}", serv);
		}
	}
	
	// called by OSGi-DS
	public void activate() {
		nodeManager.addListener(this);
		
		String selectorName = OdysseusNetConfiguration.get(SELECTOR_CONFIG_KEY, DEFAULT_SELECTOR_NAME);
		Optional<IOdysseusNodeConnectionSelector> optSelector = connectionSelectorManager.getSelector(selectorName);
		if( optSelector.isPresent() ) {
			connectionSelector = optSelector.get();
		} else {
			LOG.error("Could not find connection selector called '{}'", selectorName);
		}
	}
	
	// called by OSGi-DS
	public void deactivate() {
		nodeManager.removeListener(this);
	}

	@Override
	public void nodeAdded(IOdysseusNodeManager sender, IOdysseusNode node) {
		if( connectionSelector != null ) {
			if( connectionSelector.doConnect(node) ) {
				
			}
		}
	}

	@Override
	public void nodeRemoved(IOdysseusNodeManager sender, IOdysseusNode node) {
		
	}
	
}
