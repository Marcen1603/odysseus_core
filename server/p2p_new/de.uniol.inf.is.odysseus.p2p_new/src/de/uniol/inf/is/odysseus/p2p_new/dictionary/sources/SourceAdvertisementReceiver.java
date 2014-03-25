package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.adv.AdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public class SourceAdvertisementReceiver extends P2PDictionaryAdapter implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisementReceiver.class);
	
	private final List<ID> removedSourceIDs = Lists.newArrayList();
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement ) {
			processSourceAdvertisement((SourceAdvertisement) adv);
			
		} else if( adv instanceof MultipleSourceAdvertisement ) {
			MultipleSourceAdvertisement multipleSrcAdvertisement = (MultipleSourceAdvertisement)adv;
			for( SourceAdvertisement srcAdvertisement : multipleSrcAdvertisement.getSourceAdvertisements() ) {
				processSourceAdvertisement(srcAdvertisement);
			}
			
		} else if( adv instanceof RemoveSourceAdvertisement ) {
			RemoveSourceAdvertisement removeSourceAdvertisement = (RemoveSourceAdvertisement)adv;
			
			LOG.debug("Got a remove source advertisement");
			removeSourceAdvertisement(removeSourceAdvertisement.getSourceAdvertisementID());
			
		} else if( adv instanceof RemoveMultipleSourceAdvertisement ) {
			RemoveMultipleSourceAdvertisement removeMultipleSrcAdvertisement = (RemoveMultipleSourceAdvertisement)adv;
			
			Collection<ID> idsToRemove = removeMultipleSrcAdvertisement.getSourceAdvertisementIDs();
			removedSourceIDs.addAll(idsToRemove);
			
			Collection<SourceAdvertisement> sourcesToRemove = Lists.newArrayList();
			for( SourceAdvertisement sourceAdv : P2PDictionary.getInstance().getSources() ) {
				if( idsToRemove.contains(sourceAdv.getID())) {
					sourcesToRemove.add(sourceAdv);
				}
			}
			
			for( SourceAdvertisement srcToRemove : sourcesToRemove ) {
				P2PDictionary.getInstance().removeSource(srcToRemove);
			}
		}
	}

	private void removeSourceAdvertisement(ID id) {
		removedSourceIDs.add(id);

		for( SourceAdvertisement sourceAdv : P2PDictionary.getInstance().getSources() ) {
			if( sourceAdv.getID().equals(id)) {
				LOG.debug("Removing source {}", sourceAdv.getName());
				P2PDictionary.getInstance().removeSource(sourceAdv);
				break; // for
			}
		}
	}

	private void processSourceAdvertisement(SourceAdvertisement srcAdvertisement) {
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
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof SourceAdvertisement) {
			processSourceAdvertisementRemove((SourceAdvertisement) adv);			
			
		}  else if( adv instanceof MultipleSourceAdvertisement ) {
			P2PDictionary p2pDictionary = P2PDictionary.getInstance();
			MultipleSourceAdvertisement multipleSrcAdvertisement = (MultipleSourceAdvertisement)adv;
			
			if( multipleSrcAdvertisement.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
				List<SourceAdvertisement> srcToRepublish = Lists.newArrayList();
				
				for( SourceAdvertisement srcAdvertisement : multipleSrcAdvertisement.getSourceAdvertisements() ) {
					if( p2pDictionary.isExported(srcAdvertisement.getName()) && !p2pDictionary.isImported(srcAdvertisement)) {
						srcToRepublish.add(srcAdvertisement);
					}
				}
				
				if( !srcToRepublish.isEmpty() ) {
					multipleSrcAdvertisement.setSourceAdvertisements(srcToRepublish);
					try {
						JxtaServicesProvider.getInstance().publish(multipleSrcAdvertisement);
					} catch (IOException e) {
						LOG.error("Could not republish multipleSrcAdvertisement", e);
					}
				}
			}
		} else if( adv instanceof RemoveSourceAdvertisement ) {
			removedSourceIDs.remove(((RemoveSourceAdvertisement)adv).getSourceAdvertisementID());
		} else if( adv instanceof RemoveMultipleSourceAdvertisement ) {
			RemoveMultipleSourceAdvertisement removeMultipleSrcAdvertisement = (RemoveMultipleSourceAdvertisement)adv;
			for( ID id : removeMultipleSrcAdvertisement.getSourceAdvertisementIDs() ) {
				removedSourceIDs.remove(id);
			}
		}
	}

	private void processSourceAdvertisementRemove(SourceAdvertisement srcAdvertisement) {
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
	}
	
	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		ImmutableCollection<SourceAdvertisement> sourceAdvertisements = AdvertisementManager.getInstance().getAdvertisements(SourceAdvertisement.class);
		for( SourceAdvertisement sourceAdvertisement : sourceAdvertisements ) {
			processSourceAdvertisement(sourceAdvertisement);
		}

		ImmutableCollection<MultipleSourceAdvertisement> mulSourceAdvertisements = AdvertisementManager.getInstance().getAdvertisements(MultipleSourceAdvertisement.class);
		for( MultipleSourceAdvertisement mulSrcAdv : mulSourceAdvertisements ) {
			for( SourceAdvertisement srcAdvertisement : mulSrcAdv.getSourceAdvertisements() ) {
				processSourceAdvertisement(srcAdvertisement);
			}
		}
	}
	
	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		ImmutableList<SourceAdvertisement> publishedSources = sender.getSources();
		for (SourceAdvertisement srcAdvertisement : publishedSources) {
			if (id.equals(srcAdvertisement.getPeerID()) && srcAdvertisement.isView()) {
				LOG.debug("Removing source {} due to peer loss", srcAdvertisement.getName());
				((P2PDictionary)sender).removeSource(srcAdvertisement);
			}
		}
	}
}
