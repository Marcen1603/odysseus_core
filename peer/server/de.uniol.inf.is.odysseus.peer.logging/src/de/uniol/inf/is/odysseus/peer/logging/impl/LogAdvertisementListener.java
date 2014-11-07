package de.uniol.inf.is.odysseus.peer.logging.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisement;

public class LogAdvertisementListener implements IAdvertisementDiscovererListener {

	private static final Logger LOG = LoggerFactory.getLogger(LogAdvertisementListener.class);
	
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if( advertisement instanceof LoggingAdvertisement ) {
			LoggingAdvertisement logAdv = (LoggingAdvertisement)advertisement;
			
			PeerID pid = logAdv.getPeerID();
			if( !JXTALoggingPlugIn.getPeerDictionary().getRemotePeerIDs().contains(pid)) {
				try {
					JXTALoggingPlugIn.getJxtaServicesProvider().flushAdvertisement(logAdv);
				} catch (IOException e) {
					LOG.error("Exception during flushing invalid log advertisement");
				}
			}
		}
	}

	@Override
	public void updateAdvertisements() {
	}
}
