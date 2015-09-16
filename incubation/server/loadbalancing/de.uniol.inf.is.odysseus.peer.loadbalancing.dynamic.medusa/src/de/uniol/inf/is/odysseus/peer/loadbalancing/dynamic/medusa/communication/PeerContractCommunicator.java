package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.PriceCalculator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.MedusaPricingContract;

public class PeerContractCommunicator implements IPeerCommunicatorListener {
	

	private static final Logger LOG = LoggerFactory.getLogger(PeerContractCommunicator.class);
	
	private static IPeerCommunicator communicator;
	private static IPeerDictionary peerDictionary;
	
	private static final int PRICE_UPDATE_INTERVAL = 5000;
	
	private PriceUpdaterThread updaterThread = null;
	
	public void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
		startUpdateThreadIfPossible();
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			stopUpdateThread();
			peerDictionary = null;
		}
		
	}
	
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
		
		communicator.registerMessageType(PeerContractMessage.class);
		communicator.registerMessageType(RequestContractMessage.class);
				
		communicator.addListener(this, PeerContractMessage.class);
		communicator.addListener(this, RequestContractMessage.class);
		startUpdateThreadIfPossible();
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(communicator==serv) {
			stopUpdateThread();
			communicator.removeListener(this, RequestContractMessage.class);
			communicator.removeListener(this, PeerContractMessage.class);
			
			communicator.unregisterMessageType(PeerContractMessage.class);
			communicator.unregisterMessageType(RequestContractMessage.class);
			
			communicator = null;
			
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		if(message instanceof PeerContractMessage) {
			PeerContractMessage contractMessage = (PeerContractMessage)message;
			MedusaPricingContract contract = new MedusaPricingContract(senderPeer,contractMessage.getPrice(),contractMessage.getValidUntil());
			ContractRegistry.addContract(contract);
		}
		
		if(message instanceof RequestContractMessage) {
			sendContractToPeer(senderPeer);
		}
		
	}
	
	public static void sendContractToPeer(PeerID peer) {
		PeerContractMessage contractMessage = new PeerContractMessage(PriceCalculator.getPrice(),(System.currentTimeMillis() + PRICE_UPDATE_INTERVAL));
		try {
			communicator.send(peer, contractMessage);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Contract Message to Peer ID {}",peer);
			e.printStackTrace();
		}
	}
	
	public static void requestContractFromPeer(PeerID peer) {
		RequestContractMessage msg = new RequestContractMessage();
		
		try {
			communicator.send(peer,msg);
		} catch (PeerCommunicationException e) {
			LOG.error("Error while sending Request Contract Message to Peer ID {}",peer);
			e.printStackTrace();
		}
	}
	
	private synchronized void  startUpdateThreadIfPossible() {
		if(communicator==null) {
			return;
		}
		if(peerDictionary==null) {
			return;
		}
		if(updaterThread==null) {
			updaterThread = new PriceUpdaterThread(peerDictionary, PRICE_UPDATE_INTERVAL);
			updaterThread.start();
		}
	}
	
	private synchronized void stopUpdateThread() {
		if(updaterThread!=null) {
			updaterThread.setInactive();
			updaterThread=null;
		}
	}
	
	

}
