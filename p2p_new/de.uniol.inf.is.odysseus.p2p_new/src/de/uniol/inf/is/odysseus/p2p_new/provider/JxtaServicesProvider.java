package de.uniol.inf.is.odysseus.p2p_new.provider;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.endpoint.EndpointService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

public class JxtaServicesProvider implements IJxtaServicesProvider {

	private static JxtaServicesProvider instance;
	
	private ContentService contentService;
	private DiscoveryService discoveryService;
	private EndpointService endpointService;
	private PipeService pipeService;
	
	// called by OSGi
	public void activate() {
		Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				waitForStartedP2PNetwork();
				
				PeerGroup ownPeerGroup = P2PNetworkManager.getInstance().getLocalPeerGroup();
				
				contentService = ownPeerGroup.getContentService();
				discoveryService = ownPeerGroup.getDiscoveryService();
				endpointService = ownPeerGroup.getEndpointService();
				pipeService = ownPeerGroup.getPipeService();				
			}

			private void waitForStartedP2PNetwork() {
				while( !P2PNetworkManager.getInstance().isStarted() ) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
					}
				}
			}
		});
		
		thread.setName("Jxta Services providing thread");
		thread.setDaemon(true);
		thread.start();
		
		instance = this;
	}
	
	// called by OSGi
	public void deactivate() {
		contentService = null;
		discoveryService = null;
		endpointService = null;
		pipeService = null;
		
		instance = null;
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
	
	public static JxtaServicesProvider getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null && P2PNetworkManager.getInstance().isStarted();
	}
}
