package de.uniol.inf.is.odysseus.p2p_new.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.endpoint.EndpointService;
import net.jxta.peer.PeerInfoService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

public class JxtaServicesProvider implements IJxtaServicesProvider {

	private static final int WAIT_INTERVAL_MILLIS = 500;
	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProvider.class);
	
	private static JxtaServicesProvider instance;
	
	private ContentService contentService;
	private DiscoveryService discoveryService;
	private EndpointService endpointService;
	private PeerInfoService informationService;
	private PipeService pipeService;
	
	// called by OSGi
	public void activate() {
		LOG.debug("Activating jxta services provider");
		
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				waitForP2PNetwork();
				
				PeerGroup ownPeerGroup = P2PNetworkManager.getInstance().getLocalPeerGroup();
				
				contentService = ownPeerGroup.getContentService();
				discoveryService = ownPeerGroup.getDiscoveryService();
				endpointService = ownPeerGroup.getEndpointService();
				pipeService = ownPeerGroup.getPipeService();	
				informationService = ownPeerGroup.getPeerInfoService();
				
				instance = JxtaServicesProvider.this;
			}
		});
		
		thread.setName("Jxta Services providing thread");
		thread.setDaemon(true);
		thread.start();
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
		contentService = null;
		discoveryService = null;
		endpointService = null;
		pipeService = null;
		
		instance = null;
		
		LOG.debug("Jxta services provider deactivated");
	}
	
	@Override
	public ContentService getContentService() {
		return contentService;
	}

	@Override
	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	@Override
	public EndpointService getEndpointService() {
		return endpointService;
	}

	@Override
	public PipeService getPipeService() {
		return pipeService;
	}
	
	@Override
	public PeerInfoService getPeerInfoService() {
		return informationService;
	}
	
	public static JxtaServicesProvider getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null && P2PNetworkManager.getInstance() != null && P2PNetworkManager.getInstance().isStarted();
	}
}
