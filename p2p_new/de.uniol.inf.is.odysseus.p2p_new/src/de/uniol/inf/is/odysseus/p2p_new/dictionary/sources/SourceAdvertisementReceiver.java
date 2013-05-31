package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public class SourceAdvertisementReceiver implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisementReceiver.class);
	private static final int REACHABLE_TIMEOUT_MILLIS = 5000;
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement ) {
			final SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			if( !P2PDictionary.getInstance().existsSource(srcAdvertisement)) {
				
				if( srcAdvertisement.getPeerID().equals(P2PDictionary.getInstance().getLocalPeerID()) && srcAdvertisement.isView() ) {
					// überbleibsel aus alter veröffentlichung (Advertisement-Echo)
					// --> ignorieren
					return;
				}
				
				if( srcAdvertisement.isStream() ) {
					if( !checkStreamAdvertisement(srcAdvertisement) ) {
						// stream ist ungültig...
						// wird nicht eingetragen und gleich wieder aus dem
						// cache entfernt
						tryFlushAdvertisement(srcAdvertisement);
						return;
					}
				}
				
				P2PDictionary.getInstance().addSource(srcAdvertisement);
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement) {
			final SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			P2PDictionary.getInstance().removeSource(srcAdvertisement);
		}		
	}

	private static boolean checkStreamAdvertisement(SourceAdvertisement srcAdvertisement) {
		final AccessAO accessAO = srcAdvertisement.getAccessAO();
		final Map<String, String> optionsMap = accessAO.getOptionsMap();
		
		String host = optionsMap.get("host");
		if( Strings.isNullOrEmpty(host)) {
			LOG.error("Host not specified. Invalid sourceAdvertisement in p2p-context: {}", srcAdvertisement.getName());
			return false;
		}
		
		if( host.equalsIgnoreCase("localhost") && srcAdvertisement.getPeerID().equals(P2PDictionary.getInstance().getLocalPeerID())) {
			LOG.error("SourceAdvertisement has localhost as host (with non local origin peer): {}", srcAdvertisement.getName());
			return false;
		}
		
		try {
			// Nicht zuverlässig. Kann true zurückgeben, obwohl der host
			// nicht erreichbar ist
			InetAddress hostAddress = InetAddress.getByName(host);
			if( !hostAddress.isReachable(REACHABLE_TIMEOUT_MILLIS) ) {
				LOG.error("Host {} is not reachable");
				return false;
			}
		} catch (UnknownHostException e) {
			LOG.error("Unknown host {}", host, e );
			return false;
		} catch (IOException e) {
			LOG.error("Could not test for reachability host {}", host, e);
			return false;
		} 
		
		return true;
	}
	
	private static void tryFlushAdvertisement(final SourceAdvertisement srcAdvertisement) {
		try {
			JxtaServicesProvider.getInstance().getDiscoveryService().flushAdvertisement(srcAdvertisement);
		} catch (IOException e) {
			LOG.error("Could not flush unneeded source advertisement", e);
		}
	}
}
