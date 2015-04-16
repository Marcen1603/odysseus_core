package de.uniol.inf.is.odysseus.peer.jxta.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.jxta.discovery.DiscoveryListener;
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
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.protocol.RouteAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class JxtaServicesProvider implements IJxtaServicesProvider {

	private static final int WAIT_INTERVAL_MILLIS = 500;
	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProvider.class);

	private static JxtaServicesProvider instance;
	private static IP2PNetworkManager networkManager;
	private static JxtaJobExecutor executor = new JxtaJobExecutor();

	private DiscoveryService discoveryService;
	private EndpointService endpointService;
	private PipeService pipeService;


	// called by OSGi
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}

	// called by OSGi
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (serv == networkManager) {
			serv = null;
		}
	}
	// called by OSGi
	public void activate() {
		LOG.debug("Activating jxta services provider");

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForStartedP2PNetwork();

				PeerGroup ownPeerGroup = networkManager.getLocalPeerGroup();

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

	private static void waitForStartedP2PNetwork() {
		LOG.debug("Waiting for started p2p network");
		while (networkManager == null || !networkManager.isStarted()) {
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

		instance = null;

		LOG.debug("Jxta services provider deactivated");
	}

	public static void waitFor() {
		while (!isActivated()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	public static JxtaServicesProvider getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null && networkManager != null && networkManager.isStarted();
	}

	@Override
	public boolean isActive() {
		return isActivated();
	}

	@Override
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
	public void getRemoteAdvertisements(DiscoveryListener listener) {
		Preconditions.checkNotNull(listener, "DiscoveryListener must not be null!");

		discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99, listener);
	}

	@Override
	public Collection<Advertisement> getLocalAdvertisements() {
		try {
			if( discoveryService == null ) {
				return Lists.newArrayList();
			}
			
			return toCollection(discoveryService.getLocalAdvertisements(DiscoveryService.ADV, null, null));
		} catch (IOException e) {
			LOG.error("Could not get local advertisements", e);
			return Lists.newArrayList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Advertisement> Collection<T> getLocalAdvertisements(Class<T> advertisementClass) {
		Collection<Advertisement> advs = getLocalAdvertisements();
		Collection<T> result = Lists.newLinkedList();
		for (Advertisement adv : advs) {
			if (adv.getClass().equals(advertisementClass)) {
				result.add((T) adv);
			}
		}
		return result;
	}

	private static Collection<Advertisement> toCollection(Enumeration<Advertisement> advs) {
		List<Advertisement> list = Lists.newLinkedList();
		while (advs.hasMoreElements()) {
			list.add(advs.nextElement());
		}
		return list;
	}

	@Override
	public void getRemotePeerAdvertisements() {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0);
	}

	@Override
	public void getRemotePeerAdvertisements(DiscoveryListener listener) {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, listener);
	}

	@Override
	public Collection<PeerAdvertisement> getPeerAdvertisements() {
		try {
			if( discoveryService == null ) {
				return Lists.newArrayList();
			}
			
			return toPeerAdvCollection(discoveryService.getLocalAdvertisements(DiscoveryService.PEER, null, null));
		} catch (IOException e) {
			LOG.error("Could not get peer advertisements", e);
			return Lists.newArrayList();
		}
	}

	private static Collection<PeerAdvertisement> toPeerAdvCollection(Enumeration<Advertisement> advs) {
		List<PeerAdvertisement> list = Lists.newLinkedList();
		while (advs.hasMoreElements()) {
			list.add((PeerAdvertisement) advs.nextElement());
		}
		return list;
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
		Preconditions.checkNotNull(peerID, "PeerID to get address from must not be null!");
		
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
								String protocolName = destEndpointAddress.getProtocolName();
								if( protocolName.equals("tcp") || protocolName.equals("http")) {
									String address = destEndpointAddress.getProtocolAddress();
									return Optional.of(address);
								} 
								LOG.debug("Found destEndpoint not equal to tcp-format: protocol={}, address={} ", protocolName, destEndpointAddress.getProtocolAddress() );
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

	@Override
	public void addDiscoveryListener(DiscoveryListener listener) {
		discoveryService.addDiscoveryListener(listener);
	}

	@Override
	public void removeDiscoveryListener(DiscoveryListener listener) {
		discoveryService.removeDiscoveryListener(listener);
	}
}
