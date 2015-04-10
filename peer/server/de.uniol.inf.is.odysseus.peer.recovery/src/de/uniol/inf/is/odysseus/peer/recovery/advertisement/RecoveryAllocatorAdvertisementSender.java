package de.uniol.inf.is.odysseus.peer.recovery.advertisement;

import java.io.IOException;

import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class RecoveryAllocatorAdvertisementSender {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryAllocatorAdvertisementSender.class);

	private static IJxtaServicesProvider jxtaServicesProvider;

	private static IP2PNetworkManager p2pNetworkManager;

	public static void publish(String allocatorName) {
		RecoveryAllocatorAdvertisement adv = new RecoveryAllocatorAdvertisement();
		adv.setAllocatorName(allocatorName);
		adv.setPeerID(p2pNetworkManager.getLocalPeerID());
		adv.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
		try {
			waitForJxtaServicesProvider(jxtaServicesProvider);
			jxtaServicesProvider.publishInfinite(adv);
			jxtaServicesProvider.remotePublishInfinite(adv);
		} catch (IOException e) {
			LOG.error("Could not publish recovery allocator advertisement", e);
		}
	}

	private static void waitForJxtaServicesProvider(IJxtaServicesProvider provider) {
		while (!provider.isActive()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}
}
