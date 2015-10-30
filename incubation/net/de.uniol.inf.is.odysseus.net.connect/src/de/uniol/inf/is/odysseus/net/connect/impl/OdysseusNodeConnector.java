package de.uniol.inf.is.odysseus.net.connect.impl;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelectorManager;

public class OdysseusNodeConnector implements IOdysseusNodeManagerListener {

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNodeConnectionSelectorManager connectionSelectorManager;

	// called by OSGi-DS
	public void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}

	// called by OSGi-DS
	public void bindOdysseusNodeConnectionSelectorManager(IOdysseusNodeConnectionSelectorManager serv) {
		connectionSelectorManager = serv;
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeConnectionSelectorManager(IOdysseusNodeConnectionSelectorManager serv) {
		if (connectionSelectorManager == serv) {
			connectionSelectorManager = null;
		}
	}
	
	// called by OSGi-DS
	public void activate() {
		nodeManager.addListener(this);
		
		// TODO: get selector
	}
	
	// called by OSGi-DS
	public void deactivate() {
		nodeManager.removeListener(this);
	}

	@Override
	public void nodeAdded(IOdysseusNodeManager sender, IOdysseusNode node) {
		// TODO: use selector to determine if a communication connection to this node is needed
	}

	@Override
	public void nodeRemoved(IOdysseusNodeManager sender, IOdysseusNode node) {
		
	}
	
}
