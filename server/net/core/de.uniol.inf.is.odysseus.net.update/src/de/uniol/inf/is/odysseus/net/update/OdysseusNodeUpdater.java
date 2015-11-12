package de.uniol.inf.is.odysseus.net.update;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.update.message.DoRestartMessage;
import de.uniol.inf.is.odysseus.net.update.message.DoUpdateMessage;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class OdysseusNodeUpdater implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeUpdater.class);
	
	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static ISession activeSession;
	
	// called by OSGi-DS
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;
		
		nodeCommunicator.registerMessageType(DoUpdateMessage.class);
		nodeCommunicator.registerMessageType(DoRestartMessage.class);
		nodeCommunicator.addListener(this, DoUpdateMessage.class);
		nodeCommunicator.addListener(this, DoRestartMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.removeListener(this, DoUpdateMessage.class);
			nodeCommunicator.removeListener(this, DoRestartMessage.class);
			nodeCommunicator.unregisterMessageType(DoUpdateMessage.class);
			nodeCommunicator.unregisterMessageType(DoRestartMessage.class);

			nodeCommunicator = null;
		}
	}
	
	public static void sendUpdateMessageToRemotePeers() {
		sendUpdateMessageToRemotePeers(nodeCommunicator.getDestinationNodes());
	}
	
	public static void sendUpdateMessageToRemotePeers(Collection<IOdysseusNode> remoteNodes) {
		Preconditions.checkNotNull(remoteNodes, "List of remote nodes must not be null!");
		LOG.info("Sending update message to {} remote nodes", remoteNodes.size());
		sendMessageToPeers(remoteNodes, new DoUpdateMessage());
	}
	
	public static void sendRestartMessageToRemotePeers() {
		sendRestartMessageToRemotePeers(nodeCommunicator.getDestinationNodes());
	}

	public static void sendRestartMessageToRemotePeers(Collection<IOdysseusNode> remoteNodes) {
		Preconditions.checkNotNull(remoteNodes, "List of remote nodes must not be null!");
		LOG.info("Sending restart message to {} remote nodes", remoteNodes.size());
		
		sendMessageToPeers(remoteNodes, new DoRestartMessage());
	}

	private static void sendMessageToPeers(Collection<IOdysseusNode> remoteNodes, IMessage msg) {
		for( IOdysseusNode remoteNode : remoteNodes ) {
			try {
				nodeCommunicator.send(remoteNode, msg);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send message", e);
			}
		}
	}
	
	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if( message instanceof DoUpdateMessage ) {
			LOG.info("Got message to update");
			
			try {
				if( FeatureUpdateUtility.checkForUpdates(getActiveSession()) ) {
					FeatureUpdateUtility.checkForAndInstallUpdates(getActiveSession());
				} else {
					LOG.error("No updates available");
				}
			} catch( Throwable t ) {
				LOG.error("Cannot update", t);
			}
		} else if( message instanceof DoRestartMessage ) {
			LOG.info("Got message to restart");
			
			try {
				FeatureUpdateUtility.restart(getActiveSession());
			} catch( Throwable t ) {
				LOG.error("Could not restart", t);
			}
		}
	}

	private static ISession getActiveSession() {
		if( activeSession == null || !activeSession.isValid() ) {
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}
}
