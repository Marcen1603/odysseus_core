package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.remotecontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.benchmarking.ILogLoadService;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.control.ILoadBalancingController;

public class LoadBalancingRemoteControlCommunicator implements IPeerCommunicatorListener {
	

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancingRemoteControlCommunicator.class);

	private IPeerCommunicator communicator;
	
	private ILoadBalancingController controller;
	
	private ILogLoadService loadLogger;
	
	private List<PeerID> peersToNotify = new ArrayList<PeerID>();
	
	private static LoadBalancingRemoteControlCommunicator instance;
	
	
	public static LoadBalancingRemoteControlCommunicator getInstance() {
		return instance;
	}
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
		serv.registerMessageType(StartLoadBalancingMessage.class);
		serv.addListener(this, StartLoadBalancingMessage.class);
		

		serv.registerMessageType(StartLoadBalancingAckMessage.class);
		serv.addListener(this, StartLoadBalancingAckMessage.class);
		
		instance = this;
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(communicator==serv) {
			communicator.removeListener(this, StartLoadBalancingMessage.class);
			communicator.unregisterMessageType(StartLoadBalancingMessage.class);
			

			communicator.removeListener(this, StartLoadBalancingAckMessage.class);
			communicator.unregisterMessageType(StartLoadBalancingAckMessage.class);
			communicator=null;
		}
	}
	
	
	
	public void bindLogLoadService(ILogLoadService serv) {
		loadLogger = serv;
	}
	

	public void unbindLogLoadService(ILogLoadService serv) {
		if(loadLogger == serv) {
			loadLogger = null;
		}
	}
	
	public void bindLoadBalancingController(ILoadBalancingController serv) {
		controller = serv;
	}
	
	public void unbindLoadBalancingController(ILoadBalancingController serv) {
		if(controller==serv) {
			controller = null;
		}
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		if(message instanceof StartLoadBalancingMessage) {
			StartLoadBalancingMessage msg = (StartLoadBalancingMessage)message;
			if(msg.isStartLogLoad()) {
				loadLogger.startLogging();
			}
			
			if(msg.getStrategyName()!="none") {
				controller.setLoadBalancingStrategy(msg.getStrategyName());
				controller.startLoadBalancing();
			}
			StartLoadBalancingAckMessage answer = new StartLoadBalancingAckMessage(msg.getStrategyName(),msg.isStartLogLoad());
			try {
				communicator.send(senderPeer, answer);
			} catch (PeerCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(message instanceof StartLoadBalancingAckMessage) {
			synchronized(peersToNotify) {
				if(peersToNotify!=null) {
					if(peersToNotify.contains(senderPeer)) {
						peersToNotify.remove(senderPeer);
					}
					if(peersToNotify.size()==0) {
						LOG.info("All known Peers reached!");
					}
				}
			}
		}
		
		
	}

	
	public void sendStartLoadBalancingMessage(PeerID destination, String strategyName, boolean startLogLoad) {
		StartLoadBalancingMessage msg = new StartLoadBalancingMessage(strategyName,startLogLoad);
		try {
			communicator.send(destination, msg);
		} catch (PeerCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notifyListOfPeers(Collection<PeerID> peers,String strategyName, boolean startLogLoad) {
		synchronized(peersToNotify) {
			peersToNotify = new ArrayList<PeerID>(peers);
		}
		
		for(PeerID peer : peers) {
			sendStartLoadBalancingMessage(peer, strategyName, startLogLoad);
		}
	}
	
}
