package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.PriceCalculator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.MedusaPricingContract;

public class PeerContractCommunicator implements IPeerCommunicatorListener {
	

	private static final Logger LOG = LoggerFactory.getLogger(PeerContractCommunicator.class);
	
	private static IPeerCommunicator communicator;
	
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
		
		communicator.registerMessageType(PeerContractMessage.class);
		communicator.registerMessageType(RequestContractMessage.class);
				
		communicator.addListener(this, PeerContractMessage.class);
		communicator.addListener(this, RequestContractMessage.class);
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(communicator==serv) {
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
			MedusaPricingContract contract = new MedusaPricingContract(senderPeer, contractMessage.getMinPrice(), contractMessage.getMaxPrice());
			ContractRegistry.addContract(contract);
		}
		
		if(message instanceof RequestContractMessage) {
			PeerContractMessage contractMessage = new PeerContractMessage(PriceCalculator.getMinPrice(), PriceCalculator.getMaxPrice());
			try {
				communicator.send(senderPeer, contractMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Error while sending Contract Message to Peer ID {}",senderPeer);
				e.printStackTrace();
			}
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
	
	

}
