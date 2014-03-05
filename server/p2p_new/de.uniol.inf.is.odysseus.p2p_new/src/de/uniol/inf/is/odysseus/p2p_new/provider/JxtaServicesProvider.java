package de.uniol.inf.is.odysseus.p2p_new.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointService;
import net.jxta.endpoint.MessageTransport;
import net.jxta.endpoint.router.RouteController;
import net.jxta.impl.endpoint.router.EndpointRouter;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.protocol.RouteAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

public class JxtaServicesProvider implements IJxtaServicesProvider {

	private static final int WAIT_INTERVAL_MILLIS = 500;
	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProvider.class);
	
	private static JxtaServicesProvider instance;
	private static JxtaJobExecutor executor = new JxtaJobExecutor();
	
	private DiscoveryService discoveryService;
	private EndpointService endpointService;
	private PipeService pipeService;
	
	// called by OSGi
	public void activate() {
		LOG.debug("Activating jxta services provider");
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				waitForP2PNetwork();
				
				PeerGroup ownPeerGroup = P2PNetworkManager.getInstance().getLocalPeerGroup();
				
				discoveryService = ownPeerGroup.getDiscoveryService();
				endpointService = ownPeerGroup.getEndpointService();
				pipeService = ownPeerGroup.getPipeService();	
				
				instance = JxtaServicesProvider.this;
			}
		});
		
		thread.setName("Jxta Services providing thread");
		thread.setDaemon(true);
		thread.start();
		
		executor.start();
	}
	
	private static void waitForP2PNetwork() {
		LOG.debug("Waiting for started p2p network");
		while( P2PNetworkManager.getInstance() == null || !P2PNetworkManager.getInstance().isStarted() ) {
			try {
				Thread.sleep(WAIT_INTERVAL_MILLIS);
			} catch (InterruptedException ex) {
			}
		}
		LOG.debug("P2P network has started");
	}
	
	// called by OSGi
	public void deactivate() {
		executor.stop();
		
		discoveryService = null;
		endpointService = null;
		pipeService = null;
		
		instance = null;
		
		LOG.debug("Jxta services provider deactivated");
	}
		
	public static JxtaServicesProvider getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null && P2PNetworkManager.getInstance() != null && P2PNetworkManager.getInstance().isStarted();
	}
	
	public EndpointService getEndpointService() {
		return endpointService;
	}
	
	@Override
	public void publish(Advertisement adv) throws IOException {
		executor.addJob(new PublishJob(discoveryService, adv, DiscoveryService.DEFAULT_LIFETIME, DiscoveryService.DEFAULT_EXPIRATION));
	}

	@Override
	public void publish(Advertisement adv, long lifetime, long expirationTime) throws IOException {
		executor.addJob(new PublishJob(discoveryService, adv, lifetime, expirationTime));
	}

	@Override
	public void publishInfinite(Advertisement adv) throws IOException {
		executor.addJob(new PublishJob(discoveryService, adv, DiscoveryService.INFINITE_LIFETIME, DiscoveryService.NO_EXPIRATION));
	}

	@Override
	public void remotePublish(Advertisement adv) {
		executor.addJob(new RemotePublishJob(discoveryService, adv, DiscoveryService.DEFAULT_EXPIRATION));
	}

	@Override
	public void remotePublish(Advertisement adv, long expirationTime) {
		executor.addJob(new RemotePublishJob(discoveryService, adv, expirationTime));
	}
	
	@Override
	public void remotePublishToPeer(Advertisement adv, PeerID peerID, long expirationTime) {
		executor.addJob(new RemotePublishToPeerJob(discoveryService, adv, peerID, expirationTime));
	}

	@Override
	public void remotePublishInfinite(Advertisement adv) {
		executor.addJob(new RemotePublishJob(discoveryService, adv, DiscoveryService.NO_EXPIRATION));
	}

	@Override
	public void flushAdvertisement(Advertisement adv) throws IOException {
		discoveryService.flushAdvertisement(adv);
	}

	@Override
	public void getRemoteAdvertisements() {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99);
	}

	@Override
	public Enumeration<Advertisement> getLocalAdvertisements() throws IOException {
		return discoveryService.getLocalAdvertisements(DiscoveryService.ADV, null, null);
	}

	@Override
	public void getRemotePeerAdvertisements() {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0);
	}

	@Override
	public Enumeration<Advertisement> getPeerAdvertisements() throws IOException {
		return discoveryService.getLocalAdvertisements(DiscoveryService.PEER, null, null);
	}

	@Override
	public boolean isReachable(PeerID peerID) {
		return isReachable(peerID, false);
	}
	
	@Override
	public boolean isReachable(PeerID peerID, boolean tryToConnect) {
		return endpointService.isReachable(peerID, tryToConnect);
	}

	@Override
	public Optional<String> getRemotePeerAddress(PeerID peerID) {
		try {
			Iterator<MessageTransport> allMessageTransports = endpointService.getAllMessageTransports();
			while (allMessageTransports.hasNext()) {
				MessageTransport next = allMessageTransports.next();
				if (next instanceof EndpointRouter) {
					RouteController routeController = ((EndpointRouter) next).getRouteController();

					Collection<RouteAdvertisement> routes = routeController.getRoutes(peerID);
					for (RouteAdvertisement route : routes) {
						if (peerID.equals(route.getDestPeerID())) {
							List<EndpointAddress> destEndpointAddresses = route.getDestEndpointAddresses();
							for (EndpointAddress destEndpointAddress : destEndpointAddresses) {
								String address = destEndpointAddress.getProtocolAddress();
								return Optional.of(address);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOG.debug("Could not determine address of peerid {}", peerID, t);
		}
		return Optional.absent();
	}

	@Override
	public InputPipe createInputPipe(PipeAdvertisement pipeAdv, PipeMsgListener listener) throws IOException {
		return pipeService.createInputPipe(pipeAdv, listener);
	}

	@Override
	public OutputPipe createOutputPipe(PipeAdvertisement pipeAdv, long timeoutMillis) throws IOException {
		return pipeService.createOutputPipe(pipeAdv, timeoutMillis);
	}
}
