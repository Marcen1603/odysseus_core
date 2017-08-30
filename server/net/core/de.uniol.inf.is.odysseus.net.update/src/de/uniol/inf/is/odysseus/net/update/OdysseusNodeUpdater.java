package de.uniol.inf.is.odysseus.net.update;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.update.message.DoReinstallMessage;
import de.uniol.inf.is.odysseus.net.update.message.DoRestartMessage;
import de.uniol.inf.is.odysseus.net.update.message.DoUpdateMessage;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class OdysseusNodeUpdater implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeUpdater.class);

	private static final String REMOTE_UPDATE_CONFIG_KEY = "net.remoteUpdate";
	private static final boolean REMOTE_UPDATE_DEFAULT = false;
	
	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static ISession activeSession;
	
	// called by OSGi-DS
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;
		
		nodeCommunicator.registerMessageType(DoUpdateMessage.class);
		nodeCommunicator.registerMessageType(DoRestartMessage.class);
		nodeCommunicator.registerMessageType(DoReinstallMessage.class);
		nodeCommunicator.addListener(this, DoUpdateMessage.class);
		nodeCommunicator.addListener(this, DoRestartMessage.class);
		nodeCommunicator.addListener(this, DoReinstallMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.removeListener(this, DoUpdateMessage.class);
			nodeCommunicator.removeListener(this, DoRestartMessage.class);
			nodeCommunicator.removeListener(this, DoReinstallMessage.class);
			nodeCommunicator.unregisterMessageType(DoUpdateMessage.class);
			nodeCommunicator.unregisterMessageType(DoRestartMessage.class);
			nodeCommunicator.unregisterMessageType(DoReinstallMessage.class);

			nodeCommunicator = null;
		}
	}
	
	public static void sendUpdateMessageToRemoteNodes() {
		sendUpdateMessageToRemoteNodes(nodeCommunicator.getDestinationNodes());
	}
	
	public static void sendUpdateMessageToRemoteNodes(Collection<IOdysseusNode> remoteNodes) {
		Preconditions.checkNotNull(remoteNodes, "List of remote nodes must not be null!");
		LOG.info("Sending update message to {} remote nodes", remoteNodes.size());
		sendMessageToNodes(remoteNodes, new DoUpdateMessage());
	}
	
	public static void sendRestartMessageToRemoteNodes() {
		sendRestartMessageToRemoteNodes(nodeCommunicator.getDestinationNodes());
	}

	public static void sendRestartMessageToRemoteNodes(Collection<IOdysseusNode> remoteNodes) {
		Preconditions.checkNotNull(remoteNodes, "List of remote nodes must not be null!");
		LOG.info("Sending restart message to {} remote nodes", remoteNodes.size());
		
		sendMessageToNodes(remoteNodes, new DoRestartMessage());
	}
	
	public static void sendReinstallMessageToRemoteNodes() {
		sendReinstallMessageToRemoteNodes(nodeCommunicator.getDestinationNodes());
	}
	
	public static void sendReinstallMessageToRemoteNodes(Collection<IOdysseusNode> remoteNodes ) {
		Preconditions.checkNotNull(remoteNodes, "List of remote nodes must not be null!");
		LOG.info("Sending reinstall message to {} remote nodes", remoteNodes.size());
		
		sendMessageToNodes(remoteNodes, new DoReinstallMessage());
	}
	

	private static void sendMessageToNodes(Collection<IOdysseusNode> remoteNodes, IMessage msg) {
		for( IOdysseusNode remoteNode : remoteNodes ) {
			if( !remoteNode.isLocal() ) {
				try {
					nodeCommunicator.send(remoteNode, msg);
					LOG.error("Send signal to " + remoteNode.getName() + " OK");
				} catch (OdysseusNodeCommunicationException e) {
					LOG.error("Send signal to " + remoteNode.getName() + " FAILED");
					LOG.error("Exception:", e);
				}
			}
		}
	}
	
	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if( message instanceof DoReinstallMessage ) {
			LOG.info("Got message to reinstall");

			if( determineIfRemoteUpdateOrReInstallIsAllowed() ) {
				doReinstall();
			} else {
				LOG.info("But remote reinstall is not allowed");
			}
			
		} else if( message instanceof DoUpdateMessage ) {
			LOG.info("Got message to update");
			
			if( determineIfRemoteUpdateOrReInstallIsAllowed()) {
				doUpdate();
			} else {
				LOG.info("But remote update is not allowed");
			}
		} else if( message instanceof DoRestartMessage ) {
			LOG.info("Got message to restart");
			
			doRestart();
		}
	}

	public static void doRestart() {
		try {
			FeatureUpdateUtility.restart(getActiveSession());
		} catch( Throwable t ) {
			LOG.error("Could not restart", t);
		}
	}

	public static void doUpdate() {
		try {
			if( FeatureUpdateUtility.checkForUpdates(getActiveSession()) ) {
				FeatureUpdateUtility.checkForAndInstallUpdates(getActiveSession());
			} else {
				LOG.error("No updates available");
			}
		} catch( Throwable t ) {
			LOG.error("Cannot update", t);
		}
	}
	
	public static void doReinstall() {
		System.exit(1010); // signal OS to update here
	}
	
	private static ISession getActiveSession() {
		if( activeSession == null || !activeSession.isValid() ) {
			activeSession = SessionManagement.instance.loginSuperUser(null, UserManagementProvider.instance.getDefaultTenant().getName());
		}
		return activeSession;
	}
	
	private static boolean determineIfRemoteUpdateOrReInstallIsAllowed() {
		String updateStr = OdysseusNetConfiguration.get(REMOTE_UPDATE_CONFIG_KEY,REMOTE_UPDATE_DEFAULT + "");
		try {
			return Boolean.valueOf(updateStr);
		} catch( Throwable t ) {
			return REMOTE_UPDATE_DEFAULT;
		}
	}
}
