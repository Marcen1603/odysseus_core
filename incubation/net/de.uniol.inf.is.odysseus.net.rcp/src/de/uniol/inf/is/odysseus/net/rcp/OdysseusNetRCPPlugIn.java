package de.uniol.inf.is.odysseus.net.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.rcp.views.ChatMessage;

public class OdysseusNetRCPPlugIn extends AbstractUIPlugin {

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNetStartupManager startupManager;
	private static IOdysseusNodeConnectionManager connectionManager;
	private static IOdysseusNodeCommunicator nodeCommunicator;

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}

	public static IOdysseusNodeManager getOdysseusNodeManager() {
		return nodeManager;
	}

	// called by OSGi-DS
	public static void bindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		startupManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		if (startupManager == serv) {
			startupManager = null;
		}
	}

	public static IOdysseusNetStartupManager getOdysseusNetStartupManager() {
		return startupManager;
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		connectionManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		if (connectionManager == serv) {
			connectionManager = null;
		}
	}
	
	public static IOdysseusNodeConnectionManager getOdysseusNodeConnectionManager() {
		return connectionManager;
	}
	
	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;
		nodeCommunicator.registerMessageType(ChatMessage.class);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.unregisterMessageType(ChatMessage.class);
			nodeCommunicator = null;
		}
	}
	
	public static IOdysseusNodeCommunicator getOdysseusNodeCommunicator() {
		return nodeCommunicator;
	}

	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
