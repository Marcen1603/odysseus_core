package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

public class SourceAdvertisementReceiver implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisementReceiver.class);
	
	private final List<ID> removedSourceIDs = Lists.newArrayList();
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement ) {
			SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			if( !P2PDictionary.getInstance().existsSource(srcAdvertisement) && !removedSourceIDs.contains(srcAdvertisement.getID())) {
				
				if( srcAdvertisement.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID()) && srcAdvertisement.isView() ) {
					return;
				}
				
				try {
					if( P2PDictionary.getInstance().checkSource(srcAdvertisement) ) {
						P2PDictionary.getInstance().addSource(srcAdvertisement, false);
					}
				} catch (InvalidP2PSource e) {
					LOG.error("Could not add source advertisement", e);
				}
			}
		} else if( adv instanceof RemoveSourceAdvertisement ) {
			RemoveSourceAdvertisement removeSourceAdvertisement = (RemoveSourceAdvertisement)adv;
			
			LOG.debug("Got a remove source advertisement");
			removedSourceIDs.add(removeSourceAdvertisement.getSourceAdvertisementID());
			
			for( SourceAdvertisement sourceAdv : P2PDictionary.getInstance().getSources() ) {
				if( sourceAdv.getID().equals(removeSourceAdvertisement.getSourceAdvertisementID())) {
					LOG.debug("Removing source {}", sourceAdv.getName());
					P2PDictionary.getInstance().removeSource(sourceAdv);
					break; // for
				}
			}
		}
	}


	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement) {
			SourceAdvertisement srcAdvertisement = (SourceAdvertisement) adv;
			P2PDictionary p2pDictionary = P2PDictionary.getInstance();

			if( srcAdvertisement.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
				if( p2pDictionary.isExported(srcAdvertisement.getName()) && !p2pDictionary.isImported(srcAdvertisement)) {
					try {
						// republish
						p2pDictionary.removeSource(srcAdvertisement);
						p2pDictionary.exportSource(srcAdvertisement.getName(), "Standard");
					} catch (PeerException e) {
						LOG.debug("Could not reexport source", e);
					}
				}
			}			
			
		}  else if( adv instanceof RemoveSourceAdvertisement ) {
			removedSourceIDs.remove(((RemoveSourceAdvertisement)adv).getSourceAdvertisementID());
		}
	}
}
