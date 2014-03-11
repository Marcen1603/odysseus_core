package de.uniol.inf.is.odysseus.peer.logging;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.apache.log4j.Logger;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkManagerListener implements IP2PNetworkListener {

	private final JXTAAppender jxtaAppender = new JXTAAppender();

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		Logger.getRootLogger().addAppender(jxtaAppender);
		
		if( JXTALogConfigProvider.isLogging() ) {
			publishLoggingAdvertisementAsync(sender);
		}
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
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
					JXTALoggingPlugIn.getJxtaServicesProvider().publish(adv);
					System.err.println("Published");
				} catch (IOException e) {
				}
			}

		});
		
		t.setDaemon(true);
		t.setName("LoggingAdvertisement publish thread");
		t.start();
	}

	private static void waitForJxtaServicesProvider() {
		while( !JXTALoggingPlugIn.getJxtaServicesProvider().isActive()) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
	}
}
