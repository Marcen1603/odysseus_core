package de.uniol.inf.is.odysseus.peer.update;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class PeerUpdatePlugIn implements BundleActivator, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerUpdatePlugIn.class);
	
	private static IPeerCommunicator peerCommunicator;
	private static ISession activeSession;
	private static IPeerDictionary peerDictionary;
	
	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(DoUpdateMessage.class);
		peerCommunicator.registerMessageType(DoRestartMessage.class);
		peerCommunicator.addListener(this, DoUpdateMessage.class);
		peerCommunicator.addListener(this, DoRestartMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, DoUpdateMessage.class);
			peerCommunicator.removeListener(this, DoRestartMessage.class);
			peerCommunicator.unregisterMessageType(DoUpdateMessage.class);
			peerCommunicator.unregisterMessageType(DoRestartMessage.class);

			peerCommunicator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public static void sendUpdateMessageToRemotePeers() {
		sendUpdateMessageToRemotePeers(peerDictionary.getRemotePeerIDs());
	}
	
	public static void sendUpdateMessageToRemotePeers(Collection<PeerID> remotePeers) {
		Preconditions.checkNotNull(remotePeers, "List of remote peer ids must not be null!");
		LOG.info("Sending update message to {} remote peers", remotePeers.size());
		sendMessageToPeers(remotePeers, new DoUpdateMessage());
	}
	
	public static void sendRestartMessageToRemotePeers() {
		sendRestartMessageToRemotePeers(peerDictionary.getRemotePeerIDs());
	}

	public static void sendRestartMessageToRemotePeers(Collection<PeerID> remotePeerIDs) {
		Preconditions.checkNotNull(remotePeerIDs, "List of remote peer ids must not be null!");
		LOG.info("Sending restart message to {} remote peers", remotePeerIDs.size());
		
		sendMessageToPeers(remotePeerIDs, new DoRestartMessage());
	}

	private static void sendMessageToPeers(Collection<PeerID> remotePeers, IMessage msg) {
		for( PeerID remotePeer : remotePeers ) {
			try {
				peerCommunicator.send(remotePeer, msg);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send message", e);
			}
		}
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
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
