package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;

public class LBQueryManager implements IPeerCommunicatorListener, ILoadBalancingQueryManager {
	
	IPeerCommunicator communicator;
	
	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LBQueryManager.class);
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		QueryManagementMessage msg = (QueryManagementMessage)message;
		
		IQueryPartController controller = OsgiServiceManager.getQueryPartController();
		
		ID sharedQueryID = LoadBalancingHelper.toID(msg.getSharedQueryID());
		PeerID peerID = LoadBalancingHelper.toPeerID(msg.getPeerID());
		
		
		switch(msg.getMsgType()) {
		
			case QueryManagementMessage.CHANGE_MASTER:
				try {
					controller.registerAsSlave(controller.getLocalIds(sharedQueryID), sharedQueryID, peerID);
					//TODO Send Ack.
					
				}
				catch(Exception e) {
					LOG.error("Could not change master ",e);
				}
				break;
		
			case QueryManagementMessage.ACK_CHANGE_MASTER:
				break;
				
			case QueryManagementMessage.REGISTER_SLAVE:
				try {
					Collection<PeerID> otherPeers = controller.getOtherPeers(sharedQueryID);
					if(!otherPeers.contains(peerID)) {
						controller.addOtherPeer(sharedQueryID, peerID);
					}
					//TODO Send Ack.
				}
				catch (Exception e) {
					LOG.error("Could not register Slave: ",e);
				}
				break;
				
			case QueryManagementMessage.ACK_REGISTER_SLAVE:
				break;
				
			case QueryManagementMessage.ACK_UNREGISTER_SLAVE:
				break;
				
			case QueryManagementMessage.UNREGISTER_SLAVE:
				try {
					Collection<PeerID> otherPeers = controller.getOtherPeers(sharedQueryID);
					if(otherPeers.contains(peerID)) {
						controller.removeOtherPeer(sharedQueryID, peerID);
					}
					//TODO Send Ack.
				}
				catch (Exception e) {
					LOG.error("Could not unregister Slave: ",e);
				}
				
				break;
		}
		
	}
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		this.communicator = serv;
		communicator.registerMessageType(QueryManagementMessage.class);
		communicator.addListener(this, QueryManagementMessage.class);
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(communicator==serv) {
			communicator.removeListener(this,QueryManagementMessage.class);
			communicator.unregisterMessageType(QueryManagementMessage.class);
		}
	}
	
	
	public void sendRegisterAsSlave(PeerID masterPeer, ID sharedQueryID) {
		PeerID localPeerID = OsgiServiceManager.getP2pNetworkManager().getLocalPeerID();
		QueryManagementMessage message = QueryManagementMessage.createRegisterSlaveMsg(sharedQueryID.toString(), localPeerID.toString());
		try {
			communicator.send(masterPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message",e);
		}
	}
	
	public void sendUnregisterAsSlave(PeerID masterPeer, ID sharedQueryID) {
		PeerID localPeerID = OsgiServiceManager.getP2pNetworkManager().getLocalPeerID();
		QueryManagementMessage message = QueryManagementMessage.createUnregisterSlaveMsg(sharedQueryID.toString(), localPeerID.toString());
		try {
			communicator.send(masterPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message",e);
		}
	}
	
	public void sendChangeMaster(PeerID slavePeer, ID sharedQueryID) {
		PeerID localPeerID = OsgiServiceManager.getP2pNetworkManager().getLocalPeerID();
		QueryManagementMessage message = QueryManagementMessage.createUnregisterSlaveMsg(sharedQueryID.toString(), localPeerID.toString());
		try {
			communicator.send(slavePeer, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Message",e);
		}
	}
	
	
	
	

}
