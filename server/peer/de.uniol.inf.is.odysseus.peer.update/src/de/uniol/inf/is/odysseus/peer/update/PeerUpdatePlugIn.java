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
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class PeerUpdatePlugIn implements BundleActivator, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerUpdatePlugIn.class);
	
	private static IPeerCommunicator peerCommunicator;
	private static ISession activeSession;
	private static IP2PDictionary p2pDictionary;
	
	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(DoUpdateMessage.class);
		peerCommunicator.addListener(this, DoUpdateMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, DoUpdateMessage.class);
			peerCommunicator.unregisterMessageType(DoUpdateMessage.class);

			peerCommunicator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public static void sendUpdateMessageToRemotePeers() {
		sendUpdateMessageToRemotePeers(p2pDictionary.getRemotePeerIDs());
	}
	
	public static void sendUpdateMessageToRemotePeers(Collection<PeerID> remotePeers) {
		Preconditions.checkNotNull(remotePeers, "List of remote peer ids must not be null!");
		
		LOG.debug("Sending update message to {} remote peers", remotePeers.size());
		DoUpdateMessage msg = new DoUpdateMessage();
		for( PeerID remotePeer : remotePeers ) {
			try {
				peerCommunicator.send(remotePeer, msg);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send update message", e);
			}
		}
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		DoUpdateMessage msg = (DoUpdateMessage)message;
		LOG.debug("Got message to update: {}", msg);
		
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

	private static ISession getActiveSession() {
		if( activeSession == null ) {
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}
}
