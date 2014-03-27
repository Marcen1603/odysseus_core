package de.uniol.inf.is.odysseus.peer.logging.impl;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisement;

public class P2PNetworkManagerListener implements IP2PNetworkListener {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(P2PNetworkManagerListener.class);
	
	private final JXTAAppender jxtaAppender = new JXTAAppender();

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		LOG.debug("P2PNetwork started");		

		if( JXTALoggingPlugIn.isLogging() ) {
			publishLoggingAdvertisementAsync(sender);
		} else {			
			LOG.debug("Adding JXTAAppender");
			Logger.getRootLogger().addAppender(jxtaAppender);
		}
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
		LOG.debug("P2PNetwork stopped. Removing JXTAAppender");
		
		Logger.getRootLogger().removeAppender(jxtaAppender);
	}

	private void publishLoggingAdvertisementAsync(final IP2PNetworkManager manager) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForJxtaServicesProvider();
				
				LoggingAdvertisement adv = (LoggingAdvertisement)AdvertisementFactory.newAdvertisement(LoggingAdvertisement.getAdvertisementType());
				
				adv.setID(IDFactory.newPipeID(manager.getLocalPeerGroupID()));
				adv.setPeerID(manager.getLocalPeerID());
				
				try {
					LOG.debug("Publishing LoggingAdvertisement");
					JXTALoggingPlugIn.getJxtaServicesProvider().publish(adv);
					JXTALoggingPlugIn.getJxtaServicesProvider().remotePublish(adv);
				} catch (IOException e) {
					LOG.error("Could not publish LoggingAdvertisement", e);
				}
			}

		});
		
		t.setDaemon(true);
		t.setName("LoggingAdvertisement publish thread");
		t.start();
	}

	private static void waitForJxtaServicesProvider() {
		LOG.debug("Waiting for JxtaServicesProvider to become active");
		while( !JXTALoggingPlugIn.getJxtaServicesProvider().isActive()) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
		LOG.debug("JxtaServicesProvider is active");
	}
}
