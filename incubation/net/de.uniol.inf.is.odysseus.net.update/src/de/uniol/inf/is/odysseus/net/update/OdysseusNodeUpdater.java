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
	
	private static IOdysseusNodeCommunicator peerCommunicator;
	private static ISession activeSession;
	
	// called by OSGi-DS
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(DoUpdateMessage.class);
		peerCommunicator.registerMessageType(DoRestartMessage.class);
		peerCommunicator.addListener(this, DoUpdateMessage.class);
		peerCommunicator.addListener(this, DoRestartMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, DoUpdateMessage.class);
			peerCommunicator.removeListener(this, DoRestartMessage.class);
			peerCommunicator.unregisterMessageType(DoUpdateMessage.class);
			peerCommunicator.unregisterMessageType(DoRestartMessage.class);

			peerCommunicator = null;
		}
	}
	
	public static void sendUpdateMessageToRemotePeers() {
		sendUpdateMessageToRemotePeers(peerCommunicator.getDestinationNodes());
	}
	
	public static void sendUpdateMessageToRemotePeers(Collection<IOdysseusNode> remotePeers) {
		Preconditions.checkNotNull(remotePeers, "List of remote peer ids must not be null!");
		LOG.info("Sending update message to {} remote peers", remotePeers.size());
		sendMessageToPeers(remotePeers, new DoUpdateMessage());
	}
	
	public static void sendRestartMessageToRemotePeers() {
		sendRestartMessageToRemotePeers(peerCommunicator.getDestinationNodes());
	}

	public static void sendRestartMessageToRemotePeers(Collection<IOdysseusNode> remotePeerIDs) {
		Preconditions.checkNotNull(remotePeerIDs, "List of remote peer ids must not be null!");
		LOG.info("Sending restart message to {} remote peers", remotePeerIDs.size());
		
		sendMessageToPeers(remotePeerIDs, new DoRestartMessage());
	}

	private static void sendMessageToPeers(Collection<IOdysseusNode> remotePeers, IMessage msg) {
		for( IOdysseusNode remotePeer : remotePeers ) {
			try {
				peerCommunicator.send(remotePeer, msg);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send message", e);
			}
		}
	}
	
	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderPeer, IMessage message) {
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
